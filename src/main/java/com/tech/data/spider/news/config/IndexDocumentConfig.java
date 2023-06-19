package com.tech.data.spider.news.config;

import lombok.Data;

/**
 * <p>
 * 网站内容采集配置 rest接口采集需要从接口返回的json中获取去各个字段的值
 * 从中拼接出详情地址字符串，详情地址中的内容再使用html解析
 * </p>
 *
 * @author Wang
 * 2023/6/19
 */
@Data
public class IndexDocumentConfig {

    private String pageUrl;
    private String jsonArrayPath;
    private IndexFieldConfig[] fieldConfigs;
    private IndexDocContentConfig docContentConfig;

}
