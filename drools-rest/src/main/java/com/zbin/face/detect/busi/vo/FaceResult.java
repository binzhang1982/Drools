package com.zbin.face.detect.busi.vo;

import com.zbin.face.detect.busi.dto.facepp.FaceRectangle;

public class FaceResult {
	private FaceRectangle face_rectangle;
	
	private Float confidence;
	
	private String path;

	public FaceRectangle getFace_rectangle() {
		return face_rectangle;
	}

	public void setFace_rectangle(FaceRectangle face_rectangle) {
		this.face_rectangle = face_rectangle;
	}

	public Float getConfidence() {
		return confidence;
	}

	public void setConfidence(Float confidence) {
		this.confidence = confidence;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
