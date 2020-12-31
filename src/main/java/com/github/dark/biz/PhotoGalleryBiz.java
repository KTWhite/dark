package com.github.dark.biz;

import com.github.dark.entity.PhotoGalleryEntity;
import com.github.dark.mapper.PhotoGalleryMapper;
import com.github.dark.vo.request.SavePhotoReq;
import com.github.dark.vo.response.EditPhotosResp;
import com.github.dark.vo.response.PhotoResp;
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

    public Integer savePhotos(SavePhotoReq savePhotoReq,String userId){
        PhotoGalleryEntity photoGalleryEntity = new PhotoGalleryEntity(null,savePhotoReq.getImgName(),savePhotoReq.getImgUrl(),savePhotoReq.getImgType(),savePhotoReq.getImgParent(),userId);
        return photoGalleryMapper.insert(photoGalleryEntity);
    }

    public Integer editPhotos(EditPhotosResp editPhotosResp,String userId){
        PhotoGalleryEntity photoGalleryEntity = new PhotoGalleryEntity(editPhotosResp.getId(), editPhotosResp.getImgName(), editPhotosResp.getImgUrl(), editPhotosResp.getImgType(), editPhotosResp.getImgParent(), null);
        Example example = new Example(PhotoGalleryEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        return photoGalleryMapper.updateByExampleSelective(photoGalleryEntity,example);
    }
    public Integer deletePhotos(Integer id){
        return photoGalleryMapper.deleteByPrimaryKey(id);
    }
}
