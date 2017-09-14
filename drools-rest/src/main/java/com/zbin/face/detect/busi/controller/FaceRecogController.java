package com.zbin.face.detect.busi.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zbin.face.detect.busi.dto.MsgDTO;
import com.zbin.face.detect.busi.dto.facepp.Detect;
import com.zbin.face.detect.busi.dto.facepp.Face;
import com.zbin.face.detect.busi.dto.facepp.FaceRectangle;
import com.zbin.face.detect.busi.service.FaceRecognitionService;
import com.zbin.face.detect.busi.vo.FaceResult;
import com.zbin.face.detect.busi.vo.RecogResult;

@CrossOrigin(maxAge = 3600)
@Controller
@RequestMapping("api/face")
public class FaceRecogController {

    @Resource(name = "faceRecognitionService")
    private FaceRecognitionService faceRecognitionService;
    
    @RequestMapping(value = "recog", method = {RequestMethod.POST})
    public @ResponseBody MsgDTO getRecognition(String userName, Float conf,
    		MultipartFile videoFace, MultipartFile userFace, HttpServletRequest request) {
        MsgDTO msgDTO = new MsgDTO();
        try {
	        msgDTO.setStatus(MsgDTO.STATUS_OK);
	        if (conf == null) {
	        	conf = 0f;
	        }
	        
	        RecogResult res = new RecogResult();
	        res.setUserName(userName);
	        res.setConf(conf);
	        FaceResult left = new FaceResult();
	        left.setPath(saveFile(videoFace, request));
	        res.setLeft(left);
	        FaceResult right = new FaceResult();
	        right.setPath(saveFile(userFace, request));
	        res.setRight(right);
	        
	        Detect detVideo = faceRecognitionService.faceDetect(videoFace);
	        Detect detUser = faceRecognitionService.faceDetect(userFace);
	        
	        Detect detCompare = faceRecognitionService.faceCompare(detVideo, detUser, conf);
	        
	        setRecognizedFace(detCompare, res);
	        
	        msgDTO.setData(res);
	    } catch (Exception e) {
	    	msgDTO.setStatus(MsgDTO.STATUS_FAIL);
	    	msgDTO.setMessage(e.getMessage());
	    }
        return msgDTO;
    }
    
    private void setRecognizedFace(Detect detCompare, RecogResult res) {
    	if (detCompare.getBiggest_face_token() != null) {
    		for (Face face : detCompare.getFaces()) {
    			if (detCompare.getBiggest_face_token().equals(face.getFace_token())) {
    				res.getLeft().setFace_rectangle(new FaceRectangle());
    				res.getLeft().getFace_rectangle().setHeight(face.getFace_rectangle().getHeight());
    				res.getLeft().getFace_rectangle().setLeft(face.getFace_rectangle().getLeft());
    				res.getLeft().getFace_rectangle().setTop(face.getFace_rectangle().getTop());
    				res.getLeft().getFace_rectangle().setWidth(face.getFace_rectangle().getWidth());
    				res.getLeft().setConfidence(0f);
    				for (Face comp : face.getCompare_faces()) {
    					if (res.getLeft().getConfidence().compareTo(comp.getConfidence()) < 0 ) {
    						res.getLeft().setConfidence(comp.getConfidence());
    					}
    				}
    			}
    		}
    	}
    }
    
    private String saveFile(MultipartFile file, HttpServletRequest request) {
    	String urlPath = "";
        if (!file.isEmpty()) {
            String type = file.getOriginalFilename().substring(
                    file.getOriginalFilename().indexOf("."));// 取文件格式后缀名
            String filename = System.currentTimeMillis() + type;// 取当前时间戳作为文件名
            String path = request.getSession().getServletContext()
                    .getRealPath("/upload/" + filename);// 存放位置
            urlPath = "/upload/" + filename;
            File destFile = new File(path);
            try {
                // FileUtils.copyInputStreamToFile()这个方法里对IO进行了自动操作，不需要额外的再去关闭IO流
                FileUtils
                        .copyInputStreamToFile(file.getInputStream(), destFile);// 复制临时文件到指定目录下
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return urlPath;
    }
}
