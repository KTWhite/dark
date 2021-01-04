package com.github.dark.app;

import com.github.dark.commom.BaseResp;
import com.github.dark.constants.CommonMessage;
import com.github.dark.biz.PhotoGalleryBiz;
import com.github.dark.commom.ResultData;
import com.github.dark.config.BaseContextHandler;
import com.github.dark.entity.PhotoCommentEntity;
import com.github.dark.entity.PhotoGalleryEntity;
import com.github.dark.vo.request.CommentReq;
import com.github.dark.vo.request.PhotoReq;
import com.github.dark.vo.request.SaveCommentReq;
import com.github.dark.vo.response.EditCommentResp;
import com.github.dark.vo.response.EditPhotosResp;
import com.github.dark.vo.request.SavePhotoReq;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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
    public ResultData<BaseResp> getPhotos(@RequestBody PhotoReq photoReq){
        ResultData<BaseResp> resultData = new ResultData<>();
        String userId = BaseContextHandler.getUserID();
        resultData.setData(photoGalleryBiz.getPhotos(photoReq,userId));
        return resultData;
    }

    @PostMapping("/savePhotos")
    public ResultData<Boolean> savePhotos(@RequestBody SavePhotoReq savePhotoReq){
        ResultData<Boolean> resultData = new ResultData<>();
        String userId = BaseContextHandler.getUserID();
        try {
            resultData.setData(photoGalleryBiz.savePhotos(savePhotoReq,userId) > 0 ? true : false);
        }catch (Exception e){
            e.printStackTrace();
            resultData.setCode(CommonMessage.ERROR);
            resultData.setMsg("添加行为异常");
        }
        return resultData;
    }
    @PostMapping("/editPhotos")
    public ResultData<Boolean> editPhotos(@RequestBody EditPhotosResp editPhotosResp){
        ResultData<Boolean> resultData = new ResultData<>();
        String userID = BaseContextHandler.getUserID();
        try {
            resultData.setData(photoGalleryBiz.editPhotos(editPhotosResp, userID) > 0 ? true : false);
        }catch (Exception e){
            e.printStackTrace();
            resultData.setCode(CommonMessage.ERROR);
            resultData.setMsg("修改行为异常");
        }
        return resultData;
    }
    @DeleteMapping("/deletePhotos")
    public ResultData<Boolean> deletePhotos(@RequestParam("id") Integer id){
        ResultData<Boolean> resultData = new ResultData<>();
        try {
            resultData.setData(photoGalleryBiz.deletePhotos(id) > 0 ? true : false);
        }catch (Exception e){
            e.printStackTrace();
            resultData.setMsg("删除行为异常");
            resultData.setCode(CommonMessage.ERROR);
        }
        return resultData;
    }

    @PostMapping("/getComments")
    public ResultData<BaseResp> getComments(@RequestBody CommentReq commentReq){
        ResultData<BaseResp> resultData = new ResultData<>();
        BaseResp<PhotoCommentEntity> baseResp = new BaseResp<>();
        String userID = BaseContextHandler.getUserID();
        Page<Object> page = PageHelper.startPage(commentReq.getPageNo(), commentReq.getPageSize());
        List<PhotoCommentEntity> list = photoGalleryBiz.getComments(commentReq, userID);
        baseResp.setList(list);
        baseResp.setTotal(page.getTotal());
        resultData.setData(baseResp);
        return resultData;
    }
    @PostMapping("/saveComments")
    public ResultData<Boolean> saveComments(@RequestBody SaveCommentReq saveCommentReq){
        ResultData<Boolean> resultData = new ResultData<>();
        String userId = BaseContextHandler.getUserID();
        try {
            resultData.setData(photoGalleryBiz.saveComments(saveCommentReq, userId) > 0 ? true : false);
        }catch (Exception e){
            e.printStackTrace();
            resultData.setCode(CommonMessage.ERROR);
            resultData.setMsg("保存评论异常");
        }
        return resultData;
    }

    @PostMapping("editComments")
    public ResultData<Boolean> editComments(@RequestBody EditCommentResp editCommentResp){
        ResultData<Boolean> resultData = new ResultData<>();
        String userID = BaseContextHandler.getUserID();
        try {
            resultData.setData(photoGalleryBiz.editComments(editCommentResp, userID) > 0 ? true : false);
        }catch (Exception e){
            e.printStackTrace();
            resultData.setCode(CommonMessage.ERROR);
            resultData.setMsg("修改评论异常");
        }
        return resultData;
    }


    @DeleteMapping("/deleteComments")
    public ResultData<Boolean> deleteComments(@RequestParam("id") Integer id){
        ResultData<Boolean> resultData = new ResultData<>();
        try {
            resultData.setData(photoGalleryBiz.deleteComments(id) > 0 ? true : false);
        }catch (Exception e){
            e.printStackTrace();
            resultData.setCode(CommonMessage.ERROR);
            resultData.setMsg("删除评论异常");
        }
        return resultData;
    }


}
