package com.zbin.face.detect.common.service.impl;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.mime.MIME;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.zbin.face.detect.common.service.HttpClientService;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("httpClientService")
public class HttpClientServiceImpl implements HttpClientService{
    private static Logger logger = Logger.getLogger(HttpClientServiceImpl.class);

    public String getRemoteResponse(String url,
                                    String user,
                                    String pass,
                                    Map<String, String> params) {
        String content = "";
        HttpClient httpClient = new HttpClient();
        // 读取超时：
        HttpConnectionManager httpConManager = httpClient.getHttpConnectionManager();
        httpConManager.getParams().setConnectionTimeout(120 * 1000);
        httpConManager.getParams().setSoTimeout(120 * 1000);

        GetMethod getMethod = new GetMethod(url);
//		getMethod.setRequestHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);

        httpClient.getParams().setAuthenticationPreemptive(true);
        if (params != null) {
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            for (String key : params.keySet()) {
                String value = params.get(key);
                NameValuePair nameValuePair = new NameValuePair(key, value);
                nameValuePairs.add(nameValuePair);
            }
            getMethod.setQueryString(nameValuePairs.toArray(new NameValuePair[0]));
        }

        // httpClient.getHostConfiguration().setProxy( ip,
        // port);
        if (StringUtils.isNotBlank(user)) {
            httpClient.getState().setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(user,
                            pass));
        } else {
//			httpClient.getState().setCredentials(	AuthScope.ANY,
//																						new UsernamePasswordCredentials(user,
//																																						pass));
        }

        getMethod.setDoAuthentication(true);

        // get请求获取json
//		GetMethod getMethod = null;
        try {
            logger.info("执行远程请求......" + url);

            // get 方法content_type无效，只能用accept
//			getMethod.setRequestHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
            getMethod.setRequestHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON);

            int returnStatus = httpClient.executeMethod(getMethod);
            // 请求成功
            if (returnStatus == HttpStatus.SC_OK) {
                InputStream resStream = getMethod.getResponseBodyAsStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(resStream,
                        "UTF-8"));
                StringBuffer resBuffer = new StringBuffer();
                String resTemp = "";
                while ((resTemp = br.readLine()) != null) {
                    resBuffer.append(resTemp);
                }
                content = resBuffer.toString();
            } else {
                logger.info("远程请求失败 !! code=" + returnStatus);
                return null;
            }
        } catch (Exception e) {
            logger.error("从服务器获取数据失败",
                    e);
            throw new RuntimeException("远程请求失败 !!错误信息: " + e.getMessage());
        } finally {
            if (null != getMethod) {
                getMethod.releaseConnection();
            }
        }
        return content;
    }

	@Override
	public String postRemoteResponse(String url, String user, String pass, 
			Map<String, String> params, String requestStr) {
        String content = "";
        HttpClient httpClient = new HttpClient();
        // 读取超时：
        HttpConnectionManager httpConManager = httpClient.getHttpConnectionManager();
        httpConManager.getParams().setConnectionTimeout(120 * 1000);
        httpConManager.getParams().setSoTimeout(120 * 1000);

        PostMethod postMethod = new PostMethod(url);

        try {
	        httpClient.getParams().setAuthenticationPreemptive(true);
	        if (params != null) {
	            List<NameValuePair> nameValuePairs = new ArrayList<>();
	            for (String key : params.keySet()) {
	                String value = params.get(key);
	                NameValuePair nameValuePair = new NameValuePair(key, value);
	                nameValuePairs.add(nameValuePair);
	            }
	            postMethod.setQueryString(nameValuePairs.toArray(new NameValuePair[0]));
	        }
	
	        if (requestStr != null) {
	            RequestEntity entity = new StringRequestEntity(requestStr,"application/json","UTF-8");
	            postMethod.setRequestEntity(entity);
	        }
	        
	        // httpClient.getHostConfiguration().setProxy( ip,
	        // port);
	        if (StringUtils.isNotBlank(user)) {
	            httpClient.getState().setCredentials(AuthScope.ANY,
	                    new UsernamePasswordCredentials(user,
	                            pass));
	        } else {
	//			httpClient.getState().setCredentials(	AuthScope.ANY,
	//																						new UsernamePasswordCredentials(user,
	//																																						pass));
	        }

	        postMethod.setDoAuthentication(true);
            logger.info("执行远程请求......" + url);

            // post 方法content_type无效，只能用accept
            postMethod.setRequestHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON);

            int returnStatus = httpClient.executeMethod(postMethod);
            // 请求成功
            if (returnStatus == HttpStatus.SC_OK) {
                InputStream resStream = postMethod.getResponseBodyAsStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(resStream,
                        "UTF-8"));
                StringBuffer resBuffer = new StringBuffer();
                String resTemp = "";
                while ((resTemp = br.readLine()) != null) {
                    resBuffer.append(resTemp);
                }
                content = resBuffer.toString();
            } else {
                logger.info("远程请求失败 !! code=" + returnStatus);
                return null;
            }
        }catch (UnsupportedEncodingException ee) {
        	logger.error("从服务器获取数据失败", ee);
            throw new RuntimeException("远程请求失败 !!错误信息: " + ee.getMessage());
		}catch (Exception e) {
            logger.error("从服务器获取数据失败",
                    e);
            throw new RuntimeException("远程请求失败 !!错误信息: " + e.getMessage());
        } finally {
            if (null != postMethod) {
                postMethod.releaseConnection();
            }
        }
        return content;
	}

	@Override
	public String postFileRemoteResponse(String url, String user, String pass, Map<String, String> params,
			Map<String, MultipartFile> fileParams) {
        String content = "";
        HttpClient httpClient = new HttpClient();
        // 读取超时：
        HttpConnectionManager httpConManager = httpClient.getHttpConnectionManager();
        httpConManager.getParams().setConnectionTimeout(120 * 1000);
        httpConManager.getParams().setSoTimeout(120 * 1000);

        PostMethod postMethod = new PostMethod(url);

        try {
	        httpClient.getParams().setAuthenticationPreemptive(true);
	        List<Part> parts = new ArrayList<Part>();
	        if (params != null) {
	            for (String key : params.keySet()) {
	                String value = params.get(key);
	                StringPart strPart = new StringPart(key, value);
	                parts.add(strPart);
	            }
	        }
	        if (fileParams != null) {
	            for (String key : fileParams.keySet()) {
	            	MultipartFile value = fileParams.get(key);
			        CommonsMultipartFile cf= (CommonsMultipartFile)value;
			        DiskFileItem fi = (DiskFileItem)cf.getFileItem();
			        File f = fi.getStoreLocation();
			        FilePart fp = new FilePart(key, f);
//			        fp.setContentType(MIME.getMIME(f.getName().substring(f.getName().lastIndexOf(".")+1)));
			        parts.add(fp);
	            }
	        }

	        // httpClient.getHostConfiguration().setProxy( ip,
	        // port);
	        if (StringUtils.isNotBlank(user)) {
	            httpClient.getState().setCredentials(AuthScope.ANY,
	                    new UsernamePasswordCredentials(user,
	                            pass));
	        } else {
	//			httpClient.getState().setCredentials(	AuthScope.ANY,
	//																						new UsernamePasswordCredentials(user,
	//																																						pass));
	        }
            
            MultipartRequestEntity mre = new MultipartRequestEntity(parts.toArray(new Part[parts.size()]), postMethod.getParams());
            postMethod.setRequestEntity(mre);
            

	        postMethod.setDoAuthentication(true);
            logger.info("执行远程请求......" + url);

            // post 方法content_type无效，只能用accept
            postMethod.setRequestHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON);

            int returnStatus = httpClient.executeMethod(postMethod);
            // 请求成功
            if (returnStatus == HttpStatus.SC_OK) {
                InputStream resStream = postMethod.getResponseBodyAsStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(resStream,
                        "UTF-8"));
                StringBuffer resBuffer = new StringBuffer();
                String resTemp = "";
                while ((resTemp = br.readLine()) != null) {
                    resBuffer.append(resTemp);
                }
                content = resBuffer.toString();
            } else {
                logger.info("远程请求失败 !! code=" + returnStatus);
                return null;
            }
        }catch (UnsupportedEncodingException ee) {
        	logger.error("从服务器获取数据失败", ee);
            throw new RuntimeException("远程请求失败 !!错误信息: " + ee.getMessage());
		}catch (Exception e) {
            logger.error("从服务器获取数据失败",
                    e);
            throw new RuntimeException("远程请求失败 !!错误信息: " + e.getMessage());
        } finally {
            if (null != postMethod) {
                postMethod.releaseConnection();
            }
        }
        return content;
	}
}
