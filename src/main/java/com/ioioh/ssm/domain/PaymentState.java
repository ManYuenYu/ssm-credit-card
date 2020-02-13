package com.ioioh.ssm.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author YuWenYuen
 * @version 1.0
 * @date 2020-02-08 6:48 PM
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum PaymentState {

    /**
     * 新支付
     */
    NEW,

    /**
     * 预授权
     */
    PRE_AUTH,

    /**
     * 预授权错误
     */
    PRE_AUTH_ERROR,

    /**
     * 授权
     */
    AUTH,

    /**
     * 授权错误
     */
    AUTH_ERROR;


    private String status;

    private String desc;

    /**
     * status是否合法
     *
     * @param status
     * @return
     */
    public static boolean isIn(String status) {
        return Arrays.asList(PaymentState.values()).parallelStream().
                anyMatch(value -> StringUtils.equals(value.getStatus(), status));

    }

    /**
     * 判断status是否相等
     *
     * @param status
     * @param statusEnum
     * @return
     */
    public static boolean equals(String status, PaymentState statusEnum) {
        return StringUtils.equalsIgnoreCase(status, statusEnum.getStatus());

    }

    /**
     * status-->statusEnum
     *
     * @param status
     * @return
     */
    public static PaymentState getByStatus(String status) {
        Optional<PaymentState> statusEnumOptional = Arrays.asList(PaymentState.values()).parallelStream()
                .filter(statusEnum -> StringUtils.equalsIgnoreCase(status, statusEnum.getStatus())).findAny();

        if (statusEnumOptional.isPresent()) {
            return statusEnumOptional.get();
        }

        return null;

    }

    /**
     * 判断status是否合法
     *
     * @param status
     * @param statusEnums
     * @return
     */
    public static boolean isIn(String status, PaymentState... statusEnums) {
        return Arrays.asList(statusEnums).parallelStream().
                anyMatch(value -> StringUtils.equals(value.getStatus(), status));

    }

//    /**
//     * 判断是否订单已终结，取消、关闭、成功、拒绝都属于终结状态
//     *
//     * @param status
//     * @return
//     */
//    public static boolean isFinish(String status) {
//        return isIn(status, , CANCEL, CLOSE);
//    }
//
//    /**
//     * 判断订单是否是初始创建状态
//     * 对于： WAIT_REAL_NAME_AUTH, WAIT_BORROW 都可能是初始状态
//     * 对于其他：暂时为CREATE状态
//     *
//     * @param status
//     * @return
//     */
//    public static boolean isInitialStatus(String status) {
//        return isIn(status, CREATE, WAIT_REAL_NAME_AUTH, WAIT_BORROW);
//    }
}
