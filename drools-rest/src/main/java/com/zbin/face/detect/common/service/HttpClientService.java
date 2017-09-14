package com.zbin.face.detect.common.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface HttpClientService {

    String getRemoteResponse(String url,
                             String user,
                             String pass,
                             Map<String, String> params);
    
    String postRemoteResponse(String url,
    						  String user,
    						  String pass,
    						  Map<String, String> params,
    						  String requestStr);
    
    String postFileRemoteResponse(String url,
							  String user,
							  String pass,
							  Map<String, String> params,
							  Map<String, MultipartFile> fileParams);
}
