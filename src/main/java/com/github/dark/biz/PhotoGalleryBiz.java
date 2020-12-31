package com.github.dark.biz;

import com.github.dark.commom.BaseResp;
import com.github.dark.entity.PhotoCommentEntity;
import com.github.dark.entity.PhotoGalleryEntity;
import com.github.dark.mapper.PhotoCommentMapper;
import com.github.dark.mapper.PhotoGalleryMapper;
import com.github.dark.vo.request.CommentReq;
import com.github.dark.vo.request.PhotoReq;
import com.github.dark.vo.request.SavePhotoReq;
import com.github.dark.vo.response.EditPhotosResp;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class PhotoGalleryBiz {

    @Resource
    private PhotoGalleryMapper photoGalleryMapper;

    @Resource
    private PhotoCommentMapper photoCommentMapper;


    public BaseResp<PhotoGalleryEntity> getPhotos(PhotoReq photoResp, String userId){
        BaseResp<PhotoGalleryEntity> baseResp = new BaseResp<>();
        Example example = new Example(PhotoGalleryEntity.class);
        example.selectProperties("id","imgName","imgUrl","imgType","imgParent");
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        Page<Object> page = PageHelper.startPage(photoResp.getPageNo(), photoResp.getPageSize());
        List<PhotoGalleryEntity> photoGalleryEntities = photoGalleryMapper.selectByExample(example);
        baseResp.setList(photoGalleryEntities);
        baseResp.setTotal(page.getTotal());
        return baseResp;
    }

    public Integer savePhotos(SavePhotoReq savePhotoReq,String userId){
        PhotoGalleryEntity photoGalleryEntity = new PhotoGalleryEntity(null,savePhotoReq.getImgName(),savePhotoReq.getImgUrl(),savePhotoReq.getImgType(),
                savePhotoReq.getImgParent(),userId,new Date(),new Date());
        return photoGalleryMapper.insert(photoGalleryEntity);
    }

    public Integer editPhotos(EditPhotosResp editPhotosResp,String userId){
        PhotoGalleryEntity photoGalleryEntity = new PhotoGalleryEntity(editPhotosResp.getId(), editPhotosResp.getImgName(), editPhotosResp.getImgUrl(),
                editPhotosResp.getImgType(), editPhotosResp.getImgParent(), null,null,new Date());
        Example example = new Example(PhotoGalleryEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        return photoGalleryMapper.updateByExampleSelective(photoGalleryEntity,example);
    }
    public Integer deletePhotos(Integer id){
        return photoGalleryMapper.deleteByPrimaryKey(id);
    }

    public  List<PhotoCommentEntity> getComments(CommentReq commentReq,String userId){
        Example example = new Example(PhotoCommentEntity.class);
        example.selectProperties("id","comment","createTime");
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        return photoCommentMapper.selectByExample(example);
    }
}
