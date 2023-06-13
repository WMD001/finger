package com.tech.util;


import org.springframework.context.ApplicationContext;

/**
 * @author Wang
 * 2023/6/8
 */
public class SpringBeanUtils {

    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        SpringBeanUtils.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> tClass) {
        return applicationContext.getBean(tClass);
    }




}
