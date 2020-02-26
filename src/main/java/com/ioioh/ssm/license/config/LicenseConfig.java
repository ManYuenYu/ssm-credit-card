package com.ioioh.ssm.license.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @author YuWenYuen
 * @version 1.0
 * @date 2020-02-24 4:47 PM
 */
@Configuration
public class LicenseConfig {

    /**
     * 线程名称
     */
    private static final String THREAD_FACTORY_LICENSE_NAME = "thread-license-%d";

    @Bean
    public LicensePropertiesConfig licenseExcutorConfig(){
        return new LicensePropertiesConfig();
    }

    @Bean(name = "licenseExecutorService")
    public ExecutorService createLicenseExecutorService(){
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat(THREAD_FACTORY_LICENSE_NAME)
                .build();

        LicensePropertiesConfig config = licenseExcutorConfig();

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(config.getCorePoolSize(),
                                                    config.getMaximumPoolSize(),
                                                    config.getKeepAliveTime(),
                                                    TimeUnit.SECONDS, new LinkedBlockingQueue<>(), threadFactory);

        return threadPoolExecutor;
    }

}
