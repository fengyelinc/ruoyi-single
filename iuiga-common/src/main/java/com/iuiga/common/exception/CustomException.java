package com.iuiga.common.exception;

import com.iuiga.common.utils.CustomExceptionUtils;
import com.iuiga.common.utils.MessageUtils;
import com.iuiga.common.utils.StringUtils;

/**
 * 自定义异常
 */
public final class CustomException extends ServiceException{
    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误提示
     */
    private String message;

    /**
     * 错误明细，内部调试错误
     *
     * 和 {@link CommonResult#getDetailMessage()} 一致的设计
     */
    private String detailMessage;

    /**
     * 空构造方法，避免反序列化问题
     */
    public CustomException()
    {
    }

    public CustomException(String message)
    {
        this.message = message;
    }

    public CustomException(String message, Integer code)
    {
        this.message = message;
        this.code = code;
    }

    public CustomException(Integer code, Object... args) {
        this.code = code;
        // 优先读取缓存中的消息模板（即优先数据库中的模板）
        String message = MessageUtils.getMessageCache(code.toString());
        if(StringUtils.isNotBlank(message)) {
            this.message = MessageUtils.formatMessage(message, args);
        } else {
            this.message = CustomExceptionUtils.getErrorMsg(code, args);
        }
    }

    public String getDetailMessage()
    {
        return detailMessage;
    }

    public String getMessage()
    {
        return message;
    }

    public Integer getCode()
    {
        return code;
    }

    public CustomException setMessage(String message)
    {
        this.message = message;
        return this;
    }

    public CustomException setDetailMessage(String detailMessage)
    {
        this.detailMessage = detailMessage;
        return this;
    }

    public static void main(String[] args) {
        throw new CustomException(100001);
    }
}
