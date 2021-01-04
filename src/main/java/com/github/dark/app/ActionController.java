package com.github.dark.app;

import com.github.dark.biz.PhotoGalleryBiz;
import com.github.dark.commom.ResultData;
import com.github.dark.constants.CommonMessage;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Api("用户行为接口")
@RequestMapping("/action")
@Slf4j
public class ActionController {
    @Resource
    private PhotoGalleryBiz photoGalleryBiz;

    @GetMapping("/collect")
    public ResultData<Boolean> Collection(@RequestParam(name = "id")Integer id,
                                          @RequestParam(name = "status")Boolean status){
        ResultData<Boolean> resultData = new ResultData<>();
        try {
            resultData.setData(photoGalleryBiz.doCollect(id, status) > 0 ? true : false);
        }catch (Exception e){
            e.printStackTrace();
            resultData.setData(false);
            resultData.setMsg("收藏行为异常");
            resultData.setCode(CommonMessage.ERROR);
        }
        return resultData;
    }
    @GetMapping("/monitor")
    public ResultData<Boolean> Monitor(@RequestParam(name = "id")Integer id,@RequestParam(name = "monitor")Boolean status){
        ResultData<Boolean> resultData = new ResultData<>();
        try {
            resultData.setData(photoGalleryBiz.doMonitor(id, status) > 0 ? true : false);
        }catch (Exception e){
            e.printStackTrace();
            resultData.setData(false);
            resultData.setMsg("监控行为异常");
            resultData.setCode(CommonMessage.ERROR);
        }
        return resultData;
    }
}
