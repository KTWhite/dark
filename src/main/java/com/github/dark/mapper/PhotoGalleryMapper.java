package com.github.dark.mapper;

import com.github.dark.entity.PhotoGalleryEntity;
import com.github.dark.vo.response.PhotoListResponse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PhotoGalleryMapper extends Mapper<PhotoGalleryEntity> {
}
