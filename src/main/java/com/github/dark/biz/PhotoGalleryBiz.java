package com.github.dark.biz;

import com.github.dark.commom.BaseResp;
import com.github.dark.entity.PhotoCommentEntity;
import com.github.dark.entity.PhotoGalleryEntity;
import com.github.dark.mapper.PhotoCommentMapper;
import com.github.dark.mapper.PhotoGalleryMapper;
import com.github.dark.vo.request.CommentReq;
import com.github.dark.vo.request.PhotoReq;
import com.github.dark.vo.request.SaveCommentReq;
import com.github.dark.vo.request.SavePhotoReq;
import com.github.dark.vo.response.EditCommentResp;
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
        PhotoGalleryEntity photoGalleryEntity = new PhotoGalleryEntity();
        photoGalleryEntity.setUserId(Integer.valueOf(userId));
        photoGalleryEntity.setImgName(savePhotoReq.getImgName());
        photoGalleryEntity.setImgUrl(savePhotoReq.getImgUrl());
        photoGalleryEntity.setImgParent(savePhotoReq.getImgParent());
        photoGalleryEntity.setImgType(savePhotoReq.getImgType());
        photoGalleryEntity.setCreateTime(new Date());
        photoGalleryEntity.setUpdateTime(new Date());
        return photoGalleryMapper.insert(photoGalleryEntity);
    }

    public Integer editPhotos(EditPhotosResp editPhotosResp,String userId){
        PhotoGalleryEntity photoGalleryEntity = new PhotoGalleryEntity();
        photoGalleryEntity.setImgType(editPhotosResp.getImgType());
        photoGalleryEntity.setImgParent(editPhotosResp.getImgParent());
        photoGalleryEntity.setImgUrl(editPhotosResp.getImgUrl());
        photoGalleryEntity.setImgName(editPhotosResp.getImgName());
        photoGalleryEntity.setUpdateTime(new Date());
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
    public Integer saveComments(SaveCommentReq saveCommentReq,String userId){
        PhotoCommentEntity photoCommentEntity = new PhotoCommentEntity();
        photoCommentEntity.setComment(saveCommentReq.getComment());
        photoCommentEntity.setCreateTime(saveCommentReq.getCreateTime());
        photoCommentEntity.setUserId(Integer.valueOf(userId));
        return photoCommentMapper.insert(photoCommentEntity);
    }

    public Integer editComments(EditCommentResp commentResp,String userId){
        PhotoCommentEntity photoCommentEntity = new PhotoCommentEntity();
        photoCommentEntity.setComment(commentResp.getComment());
        Example example = new Example(PhotoCommentEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        return photoCommentMapper.updateByExampleSelective(photoCommentEntity,example);
    }
    public Integer deleteComments(Integer id){
        return photoCommentMapper.deleteByPrimaryKey(id);
    }

    //收藏项目
    public Integer doCollect(Integer id,Boolean status){
        Example example = new Example(PhotoGalleryEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",id);
        PhotoGalleryEntity photoGalleryEntity = new PhotoGalleryEntity();
        if (status){
            //收藏
            photoGalleryEntity.setCollect(1);
        }else{
            //取消收藏
            photoGalleryEntity.setCollect(0);
        }
        return photoGalleryMapper.updateByExampleSelective(photoGalleryEntity,example);
    }
    //监控项目
    public Integer doMonitor(Integer id,Boolean status){
        Example example = new Example(PhotoGalleryEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",id);
        PhotoGalleryEntity photoGalleryEntity = new PhotoGalleryEntity();
        if (status){
            //监控
            photoGalleryEntity.setMonitor(1);
        }else{
            //取消监控
            photoGalleryEntity.setMonitor(0);
        }
        return photoGalleryMapper.updateByExampleSelective(photoGalleryEntity,example);
    }
}
