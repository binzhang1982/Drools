package com.zbin.face.detect.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Constants {
	public static final String API_KEY_PARAM = "api_key";
	public static final String API_KEY_VALUE = "el06tEUdCG6k2FKw2sb6KdMlV9wfbPqt";
	public static final String API_SECRET_PARAM = "api_secret";
	public static final String API_SECRET_VALUE = "g7co7ivte5odRkC-FKgkFLDUbouJFi-R";
	public static final String IMAGE_FILE_PARAM = "image_file";
	public static final String IMAGE_FILE_TOKEN_1 = "face_token1";
	public static final String IMAGE_FILE_TOKEN_2 = "face_token2";
	
//	public static final String POST_FACEPP_DETECT = "https://api-cn.faceplusplus.com/facepp/v3/detect";
	public static final String POST_FACEPP_DETECT = "https://www.faceplusplus.com.cn/api/official/demo/facepp/v3/detect";
//	public static final String POST_FACEPP_COMPARE = "https://api-cn.faceplusplus.com/facepp/v3/compare";
	public static final String POST_FACEPP_COMPARE = "https://www.faceplusplus.com.cn/api/official/demo/facepp/v3/compare";
	
    private static final GsonBuilder GSON_BUILDER = new GsonBuilder();
    public static final Gson GSON = GSON_BUILDER.create();
}
