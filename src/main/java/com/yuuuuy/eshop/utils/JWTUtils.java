package com.yuuuuy.eshop.utils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;

import java.util.HashMap;
import java.util.Map;

public class JWTUtils {
    private static final String KEY = "yuuuuy";
    private static final Integer TIME_OUT = 24;
    public static String createToken(Integer id){
        DateTime now = DateTime.now();
        //过期时间
        DateTime expTime = now.offsetNew(DateField.HOUR, TIME_OUT);
        HashMap<String, Object> payload = new HashMap<>();
        //签发时间
        payload.put(JWTPayload.ISSUED_AT, now);
        // 过期时间
        payload.put(JWTPayload.EXPIRES_AT, expTime);
        // 生效时间
        payload.put(JWTPayload.NOT_BEFORE, now);
        payload.put("id", id);
        String token = JWTUtil.createToken(payload, KEY.getBytes());
        return token;
    }

    public static String createToken(Map map){
        return null;
    }

    public static Boolean validate(String token){
        //TODO 处理验证错误
        JWT jwt = JWTUtil.parseToken(token).setKey(KEY.getBytes());
        boolean validate = jwt.validate(0);
        return validate;
    }

    public static String getID(String token) {
        JWT jwt = JWTUtil.parseToken(token).setKey(KEY.getBytes());
        JSONObject payloads = jwt.getPayloads();
        Object id = payloads.get("id");
        return id.toString();
    }
}
