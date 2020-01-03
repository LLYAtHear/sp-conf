package com.jt.service;

import org.springframework.web.multipart.MultipartFile;

import com.jt.vo.ImageVo;

public interface FileService {

	ImageVo upload(MultipartFile uploadFile);

}
