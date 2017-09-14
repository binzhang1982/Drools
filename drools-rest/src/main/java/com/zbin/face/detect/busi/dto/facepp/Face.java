package com.zbin.face.detect.busi.dto.facepp;

import java.util.List;

public class Face {
	private String face_token;
	
	private FaceRectangle face_rectangle;
	
	private List<Face> compare_faces;
	
	private Float confidence;

	public String getFace_token() {
		return face_token;
	}

	public void setFace_token(String face_token) {
		this.face_token = face_token;
	}

	public FaceRectangle getFace_rectangle() {
		return face_rectangle;
	}

	public void setFace_rectangle(FaceRectangle face_rectangle) {
		this.face_rectangle = face_rectangle;
	}

	public List<Face> getCompare_faces() {
		return compare_faces;
	}

	public void setCompare_faces(List<Face> compare_faces) {
		this.compare_faces = compare_faces;
	}

	public Float getConfidence() {
		return confidence;
	}

	public void setConfidence(Float confidence) {
		this.confidence = confidence;
	}
}
