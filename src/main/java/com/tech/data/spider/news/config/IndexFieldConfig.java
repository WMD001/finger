package com.tech.data.spider.news.config;

import lombok.Data;

/**
 *
 * 采集字段配置
 *
 * @author Wang
 * 2023/6/19
 */
@Data
public class IndexFieldConfig {

    private String fieldName;
    private String alias;
    private String prefix = "";
    private String suffix = "";

    public String getAlias() {
        if (this.alias == null || this.alias.length() < 1) {
            this.alias = this.fieldName;
        }
        return this.alias;
    }

}
