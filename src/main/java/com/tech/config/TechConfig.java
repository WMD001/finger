package com.tech.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Wang
 * 2023/6/14
 */
@ConfigurationProperties(prefix = "tech")
@Component
@Data
public class TechConfig {

    /**
     * 新闻索引表
     */
    private String newsIndex;
    /**
     * 图片索引表
     */
    private String picIndex;
    /**
     * 文件索引表
     */
    private String fileIndex;

    /**
     * es 地址
     */
    private String esHost;
    /**
     * 预览服务地址
     */
    private String previewServiceHost;
    /**
     * 文件下载前缀
     */
    private String downloadUrlPrefix;

}
