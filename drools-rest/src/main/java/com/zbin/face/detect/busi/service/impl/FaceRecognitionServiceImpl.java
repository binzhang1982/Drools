package com.zbin.face.detect.busi.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zbin.face.detect.busi.dto.facepp.Compare;
import com.zbin.face.detect.busi.dto.facepp.Detect;
import com.zbin.face.detect.busi.dto.facepp.Face;
import com.zbin.face.detect.busi.service.FaceRecognitionService;
import com.zbin.face.detect.common.Constants;
import com.zbin.face.detect.common.exception.ApplicationException;
import com.zbin.face.detect.common.service.HttpClientService;

@Service("faceRecognitionService")
public class FaceRecognitionServiceImpl implements FaceRecognitionService {

    @Resource(name = "httpClientService")
    private HttpClientService httpClientService;

	@Override
	public Detect faceDetect(MultipartFile face) {
		Map<String, String> paras = new HashMap<>();
//		paras.put(Constants.API_KEY_PARAM, Constants.API_KEY_VALUE);
//		paras.put(Constants.API_SECRET_PARAM, Constants.API_SECRET_VALUE);
		Map<String, MultipartFile> paraFiles = new HashMap<>();
		paraFiles.put(Constants.IMAGE_FILE_PARAM, face);
		String resString = httpClientService.postFileRemoteResponse(
				Constants.POST_FACEPP_DETECT, null, null, paras, paraFiles);
		
        Gson gson = new Gson();
        Detect ret = gson.fromJson(resString, 
        		new TypeToken<Detect>(){}.getType());
		if (ret.getError_message() != null) {
        	throw new ApplicationException(ret.getError_message());
		}
		return ret;
	}

	@Override
	public Detect faceCompare(Detect detVideo, Detect detUser, Float conf) {
		Map<String, String> paras = new HashMap<>();
//		paras.put(Constants.API_KEY_PARAM, Constants.API_KEY_VALUE);
//		paras.put(Constants.API_SECRET_PARAM, Constants.API_SECRET_VALUE);
		Float biggestConfidence = conf;
		for (Face fVideo : detVideo.getFaces()) {
			fVideo.setCompare_faces(new ArrayList<Face>());
			for (Face fUser : detUser.getFaces()) {
				paras.put(Constants.IMAGE_FILE_TOKEN_1, fVideo.getFace_token());
				paras.put(Constants.IMAGE_FILE_TOKEN_2, fUser.getFace_token());
				String resString = httpClientService.postFileRemoteResponse(
						Constants.POST_FACEPP_COMPARE, null, null, paras, null);

				if (resString != null) {
					Face compare_face = new Face();
			        Gson gson = new Gson();
			        Compare comp = gson.fromJson(resString, 
			        		new TypeToken<Compare>(){}.getType());
					if (comp.getError_message() == null) {
						compare_face.setConfidence(comp.getConfidence());
						if (biggestConfidence.compareTo(comp.getConfidence()) < 0) {
							biggestConfidence = comp.getConfidence();
							detVideo.setBiggest_face_token(fVideo.getFace_token());
						}
					}
					compare_face.setFace_token(fUser.getFace_token());
					compare_face.setFace_rectangle(fUser.getFace_rectangle());
					fVideo.getCompare_faces().add(compare_face);
				}
			}
		}
		return detVideo;
	}
}
