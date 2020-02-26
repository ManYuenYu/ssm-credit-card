package com.ioioh.ssm.license;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author YuWenYuen
 * @version 1.0
 * @date 2020-02-24 5:05 PM
 */
@Component
@Slf4j
public class LicenseTimer implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("应用启动了~~~~~~~~~~~~~~~~~~~~~~~~~~");

    }
}
