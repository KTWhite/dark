package com.github.dark.app;

import com.github.dark.biz.ParkStatisticBiz;
import com.github.dark.commom.ResultData;
import com.github.dark.constants.CommonMessage;
import com.github.dark.entity.ParkEntEntity;
import com.github.dark.entity.ParkIndustryEntity;
import com.github.dark.vo.request.BaseRequest;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@Api("通用接口")
@RequestMapping("/statistic")
@Slf4j
public class ParkStatisticController {

    @Resource
    private ParkStatisticBiz parkStatisticBiz;

    @RequestMapping(value = "/ign/getEntInfo",method = RequestMethod.POST)
    @ApiOperation("企业信息")
    public ResultData<Boolean> getEntInfo(@RequestBody BaseRequest baseRequest) {
        ResultData<Boolean> resultData = new ResultData<>();
        try {
            List<ParkEntEntity> entInfo = parkStatisticBiz.getEntInfo();
            entInfo.stream().forEach(x->{
                String industrycoName = x.getIndustrycoName();
                if (industrycoName!=null){
                    String[] split = industrycoName.split("/");
                    int length = split.length;
                    String result = split[length - 1];
                    ParkIndustryEntity parkIndustryEntity = parkStatisticBiz.selectByIndustry(result);
                    if (parkIndustryEntity!=null){
                        System.out.println("根据子行业查询行业信息："+parkIndustryEntity);
                        x.setSubIndustry(parkIndustryEntity.getIndustryCode());
                        x.setSubIndustryName(parkIndustryEntity.getIndustryName());
                        parkStatisticBiz.updateEntInfo(x);
                        System.out.println("当前企业行业比对成功："+x.getId()+":"+x.getEntName());
                    }else{
                        System.out.println("当前企业信息的子行业信息比对不存在："+x.getId()+":"+x.getEntName());
                    }
                }
            });
            System.out.println("结束所有比对数据");
            resultData.setData(true);
        }catch (Exception e){
            e.printStackTrace();
            resultData.setData(false);
            resultData.setCode(CommonMessage.ERROR);
            resultData.setMsg("异常异常！");
        }
        return resultData;
    }

    @RequestMapping(value = "/ign/getParkEnt",method = RequestMethod.POST)
    @ApiOperation("企业信息")
    public ResultData<Boolean> getParkEnt(@RequestBody BaseRequest baseRequest){
        ResultData<Boolean> resultData = new ResultData<>();
        List<ParkEntEntity> entInfo = parkStatisticBiz.getEntInfoByLng();
        entInfo.stream().forEach(x->{
            String lng = x.getLng();
            String lat = x.getLat();
            System.out.println("获取到的经度:"+lng+",获取到的纬度："+lat);
            if (lng!=null&&lng.length()>0&&lat!=null&&lat.length()>0){
                parkStatisticBiz.getDistance(lng, lat,x);
            }
        });
        resultData.setData(true);
        return resultData;
    }
    @RequestMapping(value = "/ign/updatePlate",method = RequestMethod.GET)
    @ApiOperation("更新板块")
    public void updatePlate(@RequestParam("plate")String plate,
                            @RequestParam("code")String code){
        parkStatisticBiz.updatePlate(plate,code);
    }





}
