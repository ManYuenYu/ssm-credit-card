package com.ioioh.ssm.license.domain;

import lombok.*;

import java.util.Date;

/**
 * @author YuWenYuen
 * @version 1.0
 * @date 2020-02-25 7:27 PM
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class License {

    private Long id;

    private String account;

    private String productCode;

    private String version;

    private String expireDate;


}
