package com.github.dark.biz;

import com.github.dark.entity.ParkDataSynEntity;
import com.github.dark.entity.ParkEntEntity;
import com.github.dark.entity.ParkIndustryEntity;
import com.github.dark.entity.ParkLngLatEntity;
import com.github.dark.mapper.ParkDataSynMapper;
import com.github.dark.mapper.ParkEntMapper;
import com.github.dark.mapper.ParkIndustryMapper;
import com.github.dark.mapper.ParkLngLatMapper;
import com.github.dark.utils.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ParkStatisticBiz {

    @Resource
    private ParkEntMapper parkEntMapper;

    @Resource
    private ParkIndustryMapper parkIndustryMapper;

    @Resource
    private ParkLngLatMapper parkLngLatMapper;

    @Resource
    private ParkDataSynMapper parkDataSynMapper;

    public List<ParkEntEntity> getEntInfo(){
        List<ParkEntEntity> parkEntEntities = parkEntMapper.selectAll();
        return parkEntEntities;
    }

    public List<ParkEntEntity> getEntInfoByLng(){
        Example example = new Example(ParkEntEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIsNotNull("lng");
        List<ParkEntEntity> parkEntEntities = parkEntMapper.selectByExample(example);
        return parkEntEntities;
    }

    public ParkIndustryEntity selectByIndustry(String result){
        Example example = new Example(ParkIndustryEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("industryName",result);
        List<ParkIndustryEntity> parkIndustryEntities = parkIndustryMapper.selectByExample(example);
        if (parkIndustryEntities.size()>0){
            return parkIndustryEntities.get(0);
        }else{
            return null;
        }
    }

    public Integer updateEntInfo(ParkEntEntity parkEntEntity){
        Example example = new Example(ParkEntEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",parkEntEntity.getId());
        return parkEntMapper.updateByExampleSelective(parkEntEntity, example);
    }


    public void getDistance(String lng,String lat,ParkEntEntity parkEntEntity){
        List<ParkLngLatEntity> parkLngLatEntities = parkLngLatMapper.selectAll();
        parkLngLatEntities.stream().forEach(y->{
            String lng1 = y.getLng();
            String lat1 = y.getLat();
            //这个是企业的经纬度
            Double alng = parseDouble(lng);
            Double alat = parseDouble(lat);
            //这个是园区的经纬度
            Double alng1 = parseDouble(lng1);
            Double alat1 = parseDouble(lat1);
            Double distance = Distance(alng, alat, alng1, alat1);
            System.out.println("算出两点之间经纬度的距离："+distance);
            if (distance!=null){
                if (y.getRange()!=null&&y.getRange().length()>0){
                    Double range = parseDouble(y.getRange());
                    if (distance<range){
                        System.out.println("该企业{"+parkEntEntity.getEntName()+"}在园区内：");
                    }else{
                        System.out.println("该企业{"+parkEntEntity.getEntName()+"}不在园区内：");
                    }
                }
            }else{
                System.out.println("该企业{"+parkEntEntity.getEntName()+"}不在园区内：");
            }

        });
    }

    /**
     *
     * @param lng1 企业纬度
     * @param lat1 企业境地
     * @param lng2 园区纬度
     * @param lat2 园区经度
     * @return
     */
    public Double Distance(Double lng1,Double lat1,Double lng2,Double lat2){
        if (lng1!=null&&lat1!=null&&lng2!=null&&lat2!=null){
            double radLat1  = lat1 * Math.PI / 180.0;
            double radLat2  = lat2 * Math.PI / 180.0;
            double a = radLat2-radLat1;
            double radLng1 = lng1 * Math.PI / 180.0;
            double radLng2 = lng2 * Math.PI / 180.0;
            double b = radLng2 - radLng1;
            double s =2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                    Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
            s = s * 6378.137;
            s = Math.round(s*10000)/10000;
            double result = s = s / 1000;
            return result;
        }else{
            return null;
        }

    }
    public Double parseDouble(String s){
        if (!StringUtils.isEmpty(s)){
            String m = s.replace("m", "");
            return Double.parseDouble(m);
        }else{
            return null;
        }
    }


    public void updatePlate(String plate,String code){
        Example example = new Example(ParkDataSynEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.orLike("entCode",code+"%");
        criteria.orLike("entSocialNo","%"+code+"%");
        criteria.orLike("entCity",code+"%");
        List<ParkDataSynEntity> parkDataSynEntities = parkDataSynMapper.selectByExample(example);
        parkDataSynEntities.stream().forEach(x->{
            x.setPlate(plate);
            Example example1 = new Example(ParkDataSynEntity.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("id",x.getId());
            parkDataSynMapper.updateByExampleSelective(x,example1);
            System.out.println("更新企业板块："+x.getEntName());
        });
        System.out.println("更新结束");
    }
}
