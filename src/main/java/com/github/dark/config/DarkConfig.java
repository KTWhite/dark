package com.github.dark.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取项目相关配置信息
 */
@Component
@ConfigurationProperties(prefix = "dark")
public  class DarkConfig {
    private String name;
    private String version;
    private static String profile;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public static String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    /**
     * 获取文件上传路径
     * @return
     */
    public static String getUploadPath(){
        return getProfile()+"/upload";
    }
}
