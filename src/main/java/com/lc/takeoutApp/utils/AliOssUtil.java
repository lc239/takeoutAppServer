package com.lc.takeoutApp.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

@Component
public class AliOssUtil {

    private final String bucketName = "takeoutapp";

    @Autowired
    private OSS ossClient;

    public boolean upload(String objectName, InputStream inputStream){
        try {
            ObjectMetadata meta = new ObjectMetadata();
            meta.setCacheControl("private"); //设置私有缓存
            ossClient.putObject(bucketName, objectName, inputStream);
            ossClient.setObjectAcl(bucketName, objectName, CannedAccessControlList.PublicRead); //设置公共读
        } catch (OSSException oe){
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
            return false;
        } catch (ClientException ce){
            System.out.println("Error Message:" + ce.getMessage());
            return false;
        }
        return true;
    }

    public boolean delete(String objectName){
        try {
            ossClient.deleteObject(bucketName, objectName);
        } catch (OSSException oe){
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
            return false;
        } catch (ClientException ce){
            System.out.println("Error Message:" + ce.getMessage());
            return false;
        }
        return true;
    }

    public boolean objectExist(String objectName){
        try {
            return ossClient.doesObjectExist(bucketName, objectName);
        } catch (OSSException oe) {
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Error Message:" + ce.getMessage());
        }
        return false;
    }

    public URL getUrl(String keyname){

        // 设置URL过期时间为1小时
        Date expiration = new Date(new Date().getTime() + 3600 * 10000);

        // 生成URL
        URL url = ossClient.generatePresignedUrl(bucketName, keyname, expiration);
        return url;
    }
}
