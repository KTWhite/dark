package com.github.dark.app;

import com.github.dark.biz.ParkStatisticBiz;
import com.github.dark.commom.ResultData;
import com.github.dark.constants.CommonMessage;
import com.github.dark.entity.ParkDataSynEntity;
import com.github.dark.entity.ParkEntEntity;
import com.github.dark.entity.ParkIndustryEntity;
import com.github.dark.utils.ExcelUtils;
import com.github.dark.vo.request.BaseRequest;
import com.github.dark.vo.response.FileExcelColumn;
import com.github.dark.vo.response.getFileEntity;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@Api(tags = "园区企业统计")
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
    @ApiOperation("园区统计信息")
    public ResultData<Integer> getParkEnt(@RequestBody BaseRequest baseRequest){
        ResultData<Integer> resultData = new ResultData<>();
        Integer init = parkStatisticBiz.getParkCount();
        List<ParkDataSynEntity> entInfo = parkStatisticBiz.getEntInfoByLng();
        entInfo.stream().forEach(x->{
            String lng = x.getLng();
            String lat = x.getLat();
            System.out.println(x.getEntName()+"-> 获取到的经度:"+lng+",获取到的纬度："+lat);
            if (lng!=null&&lng.length()>0&&lat!=null&&lat.length()>0){
                parkStatisticBiz.getDistance(lng, lat,x);
            }
        });
        System.out.println("更新企业园区名称结束");
        Integer parkCount = parkStatisticBiz.getParkCount();
        resultData.setData(parkCount);
        resultData.setMsg("当前已更新的企业的园区数量为："+(parkCount-init));
        return resultData;
    }

    @RequestMapping(value = "/ign/parkByAddress",method = RequestMethod.POST)
    @ApiOperation("根据企业地址配置园区")
    public ResultData<Integer> parkByAddress(){
        ResultData<Integer> resultData= new ResultData<>();
        Integer init = parkStatisticBiz.getParkCount();
        List<ParkDataSynEntity> entInfo = parkStatisticBiz.getEntInfoByLng();
        entInfo.stream().forEach(x->{
            System.out.println("当前正在匹配的企业："+x.getEntName());
            parkStatisticBiz.parkByAddress(x);
        });
        Integer parkCount = parkStatisticBiz.getParkCount();
        System.out.println("当前已更新的企业的园区数量为:"+(parkCount-init));
        resultData.setData(parkCount-init);
        resultData.setMsg("当前已更新的企业的园区数量为："+(parkCount-init));
        return resultData;
    }
    @RequestMapping(value = "/ign/updatePlate",method = RequestMethod.GET)
    @ApiOperation("更新板块")
    public void updatePlate(@RequestParam("plate")String plate,
                            @RequestParam("code")String code){
        parkStatisticBiz.updatePlate(plate,code);
    }

    @RequestMapping(value = "/ign/distance",method = RequestMethod.GET)
    @ApiOperation("经纬度距离计算")
    public Double Distance(@RequestParam("lng")Double lng,
                         @RequestParam("lat")Double lat,
                         @RequestParam("lng2") Double lng2,
                         @RequestParam("lat2")Double lat2){
        return parkStatisticBiz.Distance(lng, lat, lng2, lat2);
    }

    @RequestMapping(value = "/ign/exportExcel",method = RequestMethod.GET)
    @ApiOperation("导出园区存续企业数据")
//    @PreAuthorize("@ss.hasPermi('system:projectAdmin:export')")
    public void exportExcel(HttpServletResponse response,
                            @RequestParam(value = "pageNum",required = false,defaultValue = "1")Integer pageNum,
                            @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize){
        List<getFileEntity> file = parkStatisticBiz.getFile();
        ArrayList<FileExcelColumn> list = new ArrayList<>();
        file.stream().forEach(x->{
            FileExcelColumn fileExcelColumn = parkStatisticBiz.setProp(x);
            list.add(fileExcelColumn);
        });
        ExcelUtils.writeExcel(response,list,FileExcelColumn.class);
    }

    @RequestMapping(value = "/ign/exportExcel2",method = RequestMethod.GET)
    @ApiOperation("导出园区注销企业数据")
//    @PreAuthorize("@ss.hasPermi('system:projectAdmin:export')")
    public void exportExcel2(HttpServletResponse response,
                            @RequestParam(value = "pageNum",required = false,defaultValue = "1")Integer pageNum,
                            @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize){
        List<getFileEntity> file = parkStatisticBiz.getFile2();
        ArrayList<FileExcelColumn> list = new ArrayList<>();
        file.stream().forEach(x->{
            FileExcelColumn fileExcelColumn = parkStatisticBiz.setProp(x);
            list.add(fileExcelColumn);
        });
        ExcelUtils.writeExcel(response,list,FileExcelColumn.class);
    }




}
