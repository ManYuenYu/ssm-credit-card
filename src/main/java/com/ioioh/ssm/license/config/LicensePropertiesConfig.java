package com.ioioh.ssm.license;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author YuWenYuen
 * @version 1.0
 * @date 2020-02-24 6:10 PM
 */
@Data
@ConfigurationProperties(prefix = "license")
public class LicensePropertiesConfig {

    /**
     * 核心线程池大小
     */
    private int corePoolSize;

    /**
     * 最大线程池大小
     */
    private int maximumPoolSize;

    /**
     * 线程最大空闲时间 秒
     */
    private long keepAliveTime;
}
