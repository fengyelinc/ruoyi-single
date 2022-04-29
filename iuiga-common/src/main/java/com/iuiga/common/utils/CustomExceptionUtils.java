package com.iuiga.common.utils;

import com.iuiga.common.constant.ErrMsgConstants;
import com.iuiga.common.core.text.StrFormatter;

import java.lang.reflect.Field;

/**
 * 自定义异常工具类
 */
public class CustomExceptionUtils {

    /**
     * 获取错误信息
     * @param errCode
     * @param args
     * @return
     */
    public static String getErrorMsg(Integer errCode, Object... args) {
        Object result = null;
        try {
            Class clazz = Class.forName("com.iuiga.common.constant.ErrMsgConstants");
            Object obj = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for(Field field: fields) {
                field.setAccessible(true);
                String name = field.getName();
                if(name.equals(ErrMsgConstants.ERR_MSG_PREFIX+errCode)) {
                    result = field.get(obj);
                    break;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return result!=null? StrFormatter.format(result.toString(), args): "系统异常";
    }
}
