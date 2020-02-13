package com.ioioh.ssm.domain;

import lombok.Getter;

/**
 * @author YuWenYuen
 * @version 1.0
 * @date 2020-02-08 7:34 PM
 */

@Getter
public enum PaymentEvent {

    /**
     * 预授权
     */
    PRE_AUTHORIZE,

    /**
     * 预授权批准
     */
    PRE_AUTH_APPROVED,

    /**
     * 预授权被拒
     */
    PRE_AUTH_DECLINED,

    /**
     * 授权
     */
    AUTHORIZE,

    /**
     * 授权批准
     */
    AUTH_APPROVED,

    /**
     * 授权被拒
     */
    AUTH_DELIENED;


//        /**
//         * 判断
//         * @param eventName
//         * @return
//         */
//        public static PaymentEvent getEvent(String eventName) {
//            if (StringUtils.isBlank(eventName)) {
//                return null;
//            }
//
//            Optional<PaymentEvent> resultOptional = Arrays.asList(PaymentEvent.values()).parallelStream().filter(eventEnum ->
//                    StringUtils.equals(eventName, eventEnum.getEvent())).findAny();
//
//            if (resultOptional.isPresent()) {
//                return resultOptional.get();
//            }
//            return null;
//        }
}
