package com.zbin.face.detect.busi.vo;

public class RecogResult {
	private String userName;
	
	private Float conf;
	
	private FaceResult left;
	
	private FaceResult right;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Float getConf() {
		return conf;
	}

	public void setConf(Float conf) {
		this.conf = conf;
	}

	public FaceResult getLeft() {
		return left;
	}

	public void setLeft(FaceResult left) {
		this.left = left;
	}

	public FaceResult getRight() {
		return right;
	}

	public void setRight(FaceResult right) {
		this.right = right;
	}
}
