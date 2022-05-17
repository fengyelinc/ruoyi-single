package com.iuiga.qa.enums;

/**
 * 问答类型
 * 
 * @author guiguzi
 */
public enum SubjectType
{
    OK("1", "正误型"), DISABLE("2", "选择型");

    private final String code;
    private final String info;

    SubjectType(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public String getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}
