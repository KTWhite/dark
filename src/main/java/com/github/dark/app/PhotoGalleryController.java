package com.github.dark.app;

import com.github.dark.Constants.CommonMessage;
import com.github.dark.biz.PhotoGalleryBiz;
import com.github.dark.commom.ResultData;
import com.github.dark.config.BaseContextHandler;
import com.github.dark.entity.PhotoGalleryEntity;
import com.github.dark.vo.response.EditPhotosResp;
import com.github.dark.vo.response.PhotoResp;
import com.github.dark.vo.request.SavePhotoReq;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api("图片集接口")
@RequestMapping("/photo")
@Slf4j
public class PhotoGalleryController {
    private static final String TAG="PhotoGalleryController";

    @Resource
    private PhotoGalleryBiz photoGalleryBiz;

    @PostMapping("/getPhotos")
    public ResultData<List<PhotoGalleryEntity>> getPhotos(@RequestBody PhotoResp photoResp){
        ResultData<List<PhotoGalleryEntity>> list = new ResultData<>();
        String userId = BaseContextHandler.getUserID();
        List<PhotoGalleryEntity> photos = photoGalleryBiz.getPhotos(photoResp,userId);
        list.setData(photos);
        return list;
    }

    @PostMapping("/savePhotos")
    public ResultData<Boolean> savePhotos(@RequestBody SavePhotoReq savePhotoReq){
        ResultData<Boolean> resultData = new ResultData<>();
        String userId = BaseContextHandler.getUserID();
        boolean saved = photoGalleryBiz.savePhotos(savePhotoReq,userId) > 0 ? true : false;
        if (saved){
            resultData.setData(true);
            resultData.setCode(CommonMessage.SUCCESS);
        }else{
            resultData.setData(false);
            resultData.setCode(CommonMessage.ERROR);
            resultData.setMsg("添加异常");
        }
        return resultData;
    }
    @PostMapping("/editPhotos")
    public ResultData<Boolean> editPhotos(@RequestBody EditPhotosResp editPhotosResp){
        ResultData<Boolean> resultData = new ResultData<>();
        String userID = BaseContextHandler.getUserID();
        boolean editor = photoGalleryBiz.editPhotos(editPhotosResp, userID) > 0 ? true : false;
        if (editor){
            resultData.setData(true);
        }else {
            resultData.setData(false);
            resultData.setCode(CommonMessage.ERROR);
            resultData.setMsg("修改异常");
        }
        return resultData;
    }
    @DeleteMapping("/deletePhotos")
    public ResultData<Boolean> deletePhotos(@RequestParam("id") Integer id){
        ResultData<Boolean> resultData = new ResultData<>();
        boolean delete = photoGalleryBiz.deletePhotos(id) > 0 ? true : false;
        if (delete){
            resultData.setData(true);
        }else{
            resultData.setData(false);
            resultData.setCode(CommonMessage.ERROR);
            resultData.setMsg("删除异常");
        }
        return resultData;
    }


}
