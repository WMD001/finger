package com.tech.data.spider.news.config;

import lombok.Data;

/**
 * <p>
 *     网页采集详情页面配置
 * </p>
 *
 * @author Wang
 * 2023/6/19
 */
@Data
public class IndexDocContentConfig {

    private String defaultContentField = "_content";

    private DocContentFieldConfig[] docContentFieldConfigs;

}
