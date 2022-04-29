package com.iuiga.common.utils;

import com.iuiga.common.constant.Constants;
import com.iuiga.common.core.redis.RedisCache;
import com.iuiga.common.core.text.StrFormatter;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import com.iuiga.common.utils.spring.SpringUtils;

import java.util.Collection;

/**
 * 获取i18n资源文件
 * 
 * @author guiguzi
 */
public class MessageUtils
{
    /**
     * 根据消息键和参数 获取消息 委托给spring messageSource
     *
     * @param code 消息键
     * @param args 参数
     * @return 获取国际化翻译值
     */
    public static String message(String code, Object... args)
    {
        MessageSource messageSource = SpringUtils.getBean(MessageSource.class);
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    /**
     * 根据消息模板生成消息
     * @param template
     * @param args
     * @return
     */
    public static String formatMessage(String template, Object... args) {
        String result = "";
        try {
            result = StrFormatter.format(template, args);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 设置参数缓存
     *
     * @param key 参数键
     * @param message 参数数据列表
     */
    public static void setMessageCache(String key, String message)
    {
        SpringUtils.getBean(RedisCache.class).setCacheObject(getCacheKey(key), message);
    }

    /**
     * 获取参数缓存
     *
     * @param key 参数键
     * @return messages 参数数据列表
     */
    public static String getMessageCache(String key)
    {
        Object cacheObj = SpringUtils.getBean(RedisCache.class).getCacheObject(getCacheKey(key));
        if (StringUtils.isNotNull(cacheObj))
        {
            return StringUtils.cast(cacheObj);
        }
        return null;
    }

    /**
     * 删除指定参数缓存
     *
     * @param key 参数键
     */
    public static void removeMessageCache(String key)
    {
        SpringUtils.getBean(RedisCache.class).deleteObject(getCacheKey(key));
    }

    /**
     * 清空参数缓存
     */
    public static void clearMessageCache()
    {
        Collection<String> keys = SpringUtils.getBean(RedisCache.class).keys(Constants.SYS_MESSAGE_KEY + "*");
        SpringUtils.getBean(RedisCache.class).deleteObject(keys);
    }

    /**
     * 设置cache key
     *
     * @param messageKey 参数键
     * @return 缓存键key
     */
    public static String getCacheKey(String messageKey)
    {
        return Constants.SYS_MESSAGE_KEY + messageKey;
    }
}
