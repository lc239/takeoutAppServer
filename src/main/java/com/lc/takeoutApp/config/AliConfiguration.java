package com.lc.takeoutApp.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyuncs.exceptions.ClientException;
import com.lc.takeoutApp.utils.AliOssUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AliConfiguration {

    @Bean
    public OSS ossClient() throws ClientException {
        String endpoint = "https://oss-cn-beijing.aliyuncs.com";
        //设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET
        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
        return new OSSClientBuilder().build(endpoint, credentialsProvider);
    }
}
