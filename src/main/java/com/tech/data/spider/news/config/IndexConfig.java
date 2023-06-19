package com.tech.data.spider.news.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Wang
 * 2023/6/19
 */
@ConfigurationProperties(prefix = "web-index")
@Component
@Data
public class IndexConfig {

    IndexDocumentConfig[] indexDocumentConfigs;

}
