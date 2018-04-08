package com.ade.open.api.server.context;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by yangshuxi on 2017/6/30.
 */
public enum ReturnCode {

    SUCCESS("SUCCESS","成功"),
    ILLEGAL_MESSAGE("ILLEGAL_MESSAGE","消息格式错误"),
    SECRET_DISABLE("SECRET_DISABLE","秘钥已禁用"),
    METHOD_ONLY_POST( "METHOD_ONLY_POST", "只支持POST请求"),
    MESSAGE_INCOMPLETE("MESSAGE_INCOMPLETE","缺少参数"),

    CUSTOMER_NOT_EXIST("CUSTOMER_NOT_EXIST","用户不存在"),
    CUSTOMER_DATA_EXCEPTION("CUSTOMER_DATA_EXCEPTION","用户数据异常，请联系管理员"),
    CUSTOMER_NO_AUTHORITY("CUSTOMER_NO_AUTHORITY","用户没有权限"),

    INVALID_SIGN("INVALID_SIGN","签名错误"),

    SYSTEM_ERROR("SYSTEM_ERROR","系统内部错误"),
    SYSTEM_TIMEOUT("SYSTEM_TIMEOUT","响应超时"),
    ;

    private String returnCode;
    private String returnDesc;

    ReturnCode(String returnCode, String returnDesc) {
        this.returnCode = returnCode;
        this.returnDesc = returnDesc;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public String getReturnDesc() {
        return returnDesc;
    }

    public String toJson() {
        return String.format("{\"return_code\":\"%s\",\"return_desc\":\"%s\" }", returnCode, returnDesc);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("returnCode", returnCode);
        map.put("returnDesc", returnDesc);
        return map;
    }

    public static boolean success(ReturnCode returnCode) {
        return ReturnCode.SUCCESS.equals(returnCode);
    }

}
