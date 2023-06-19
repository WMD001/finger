package com.tech.data.spider.news.config;

import lombok.Data;

/**
 * <p>
 *     解析html网页内容，存储到对应字段中，path应为select path 或者正则规则
 * </p>
 *
 * @author Wang
 * 2023/6/19
 */
@Data
public class DocContentFieldConfig {

    private String fieldName;
    private String path;

}
