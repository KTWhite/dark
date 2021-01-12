package com.github.dark.biz;

import com.github.dark.entity.ParkDataSynEntity;
import com.github.dark.entity.ParkEntEntity;
import com.github.dark.entity.ParkIndustryEntity;
import com.github.dark.entity.ParkLngLatEntity;
import com.github.dark.mapper.*;
import com.github.dark.utils.StringUtils;
import com.github.dark.vo.response.FileExcelColumn;
import com.github.dark.vo.response.getFileEntity;
import io.netty.util.internal.MathUtil;
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

    @Resource
    private SysUserMapper sysUserMapper;

    public List<ParkEntEntity> getEntInfo(){
        List<ParkEntEntity> parkEntEntities = parkEntMapper.selectAll();
        return parkEntEntities;
    }

    public List<ParkDataSynEntity> getEntInfoByLng(){
        Example example = new Example(ParkEntEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIsNotNull("lng");
        criteria.andIsNull("yuanqu");
        List<ParkDataSynEntity> parkDataSynEntities = parkDataSynMapper.selectByExample(example);
        return parkDataSynEntities;
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


    public void getDistance(String lng,String lat,ParkDataSynEntity parkEntEntity){
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
                        parkEntEntity.setYuanqu(y.getParkName());
                        Example example = new Example(ParkEntEntity.class);
                        Example.Criteria criteria = example.createCriteria();
                        criteria.andEqualTo("id",parkEntEntity.getId());
                        parkDataSynMapper.updateByExampleSelective(parkEntEntity,example);
                        System.out.println("更新的企业ID：{ "+parkEntEntity.getId()+"：企业名称："+parkEntEntity.getEntName()+" }的园区");
                    }else{
                        System.out.println("该企业{"+parkEntEntity.getEntName()+"}不在园区内：");
                    }
                }
            }else{
                System.out.println("该企业{"+parkEntEntity.getEntName()+"}不在园区内：");
            }
        });
        System.out.println("结束园区经纬度更新");
    }


    public void parkByAddress(ParkDataSynEntity parkDataSynEntity){
        List<ParkLngLatEntity> parkLngLatEntities = parkLngLatMapper.selectAll();
        parkLngLatEntities.stream().forEach(y->{
            if (parkDataSynEntity.getEntAddress().contains(y.getParkName())){
                parkDataSynEntity.setYuanqu(y.getParkName());
                Example example = new Example(parkDataSynEntity.getClass());
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("id",parkDataSynEntity.getId());
                System.out.println("更新的当前企业是："+parkDataSynEntity.getEntName());
                parkDataSynMapper.updateByExampleSelective(parkDataSynEntity,example);
            }
        });
    }



    /**
     *
     * @param lng1 企业纬度
     * @param lat1 企业境地
     *             企业起点 园区终点
     * @param lng2 园区纬度
     * @param lat2 园区经度
     * @return
     */
    public Double Distance(Double lng1,Double lat1,Double lng2,Double lat2){
        Integer radius = 6371; //km
        if (lng1!=null&&lat1!=null&&lng2!=null&&lat2!=null){
            double dLat = Math.toRadians(lat2 - lat1);
            double dLng = Math.toRadians(lng2 - lng1);
            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            return radius * c*1000;
        }else{
            return null;
        }

    }

    public FileExcelColumn setProp(getFileEntity x){
        FileExcelColumn fileExcelColumn = new FileExcelColumn();
        fileExcelColumn.setRegDate(x.getRegDate());
        fileExcelColumn.setTotal(x.getTotal());
        fileExcelColumn.setYuanqu(x.getYuanqu());
        return fileExcelColumn;
    }

    public Integer getParkCount(){
        Example example = new Example(ParkEntEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIsNotNull("yuanqu");
        return parkDataSynMapper.selectCountByExample(example);
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
        criteria.andIsNull("plate");
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

    public List<getFileEntity> getFile(){
        return sysUserMapper.getFile();
    }
    public List<getFileEntity> getFile2(){
        return sysUserMapper.getFiles();
    }
}
