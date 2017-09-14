package com.zbin.face.detect.busi.dto.facepp;

import java.util.List;

public class Detect {
	private String request_id;
	
	private List<Face> faces;
	
	private String image_id;
	
	private Integer time_used;
	
	private String error_message;
	
	private String biggest_face_token;

	public String getRequest_id() {
		return request_id;
	}

	public void setRequest_id(String request_id) {
		this.request_id = request_id;
	}

	public List<Face> getFaces() {
		return faces;
	}

	public void setFaces(List<Face> faces) {
		this.faces = faces;
	}

	public String getImage_id() {
		return image_id;
	}

	public void setImage_id(String image_id) {
		this.image_id = image_id;
	}

	public Integer getTime_used() {
		return time_used;
	}

	public void setTime_used(Integer time_used) {
		this.time_used = time_used;
	}

	public String getError_message() {
		return error_message;
	}

	public void setError_message(String error_message) {
		this.error_message = error_message;
	}

	public String getBiggest_face_token() {
		return biggest_face_token;
	}

	public void setBiggest_face_token(String biggest_face_token) {
		this.biggest_face_token = biggest_face_token;
	}

}
