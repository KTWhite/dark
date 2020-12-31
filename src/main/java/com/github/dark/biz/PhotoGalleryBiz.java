package com.github.dark.biz;

import com.github.dark.entity.PhotoGalleryEntity;
import com.github.dark.mapper.PhotoGalleryMapper;
import com.github.dark.vo.request.PhotoResp;
import com.github.dark.vo.response.PhotoListResponse;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PhotoGalleryBiz {

    @Resource
    private PhotoGalleryMapper photoGalleryMapper;


    public List<PhotoGalleryEntity> getPhotos(PhotoResp photoResp){
        List<PhotoGalleryEntity> photoGalleryEntities = photoGalleryMapper.selectAll();
        return photoGalleryEntities;
    }
}
