package com.justimaginethat.gogodriver.BaseModel;

import java.io.Serializable;

public class BaseAPIResponseModel<T> implements Serializable {
    public String ContentEncoding;
    public String ContentType;
    public int JsonRequestBehavior;
    public int MaxJsonLength;
    public int RecursionLimit;
    public T Data;
    public T data;
}
