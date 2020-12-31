package com.github.dark.biz;

import com.github.dark.entity.PhotoGalleryEntity;
import com.github.dark.mapper.PhotoGalleryMapper;
import com.github.dark.vo.request.PhotoResp;
import com.github.dark.vo.response.PhotoListResponse;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PhotoGalleryBiz {

    @Resource
    private PhotoGalleryMapper photoGalleryMapper;


    public List<PhotoGalleryEntity> getPhotos(PhotoResp photoResp,String userId){
        PageHelper.startPage(photoResp.getPageNo(),photoResp.getPageSize());
        Example example = new Example(PhotoGalleryEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        example.selectProperties("id","imgName","imgUrl","imgType","imgParent");
        List<PhotoGalleryEntity> photoGalleryEntities = photoGalleryMapper.selectByExample(example);
        return photoGalleryEntities;
    }
}
