package com.zbin.face.detect.busi.service;

import org.springframework.web.multipart.MultipartFile;

import com.zbin.face.detect.busi.dto.facepp.Detect;

public interface FaceRecognitionService {

	Detect faceDetect(MultipartFile videoFace);
	
	Detect faceCompare(Detect detVideo, Detect detUser, Float conf);
}
