package com.github.dark.app;

import com.github.dark.biz.PhotoGalleryBiz;
import com.github.dark.commom.ResultData;
import com.github.dark.config.BaseContextHandler;
import com.github.dark.entity.PhotoGalleryEntity;
import com.github.dark.vo.request.PhotoResp;
import com.github.dark.vo.response.PhotoListResponse;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
