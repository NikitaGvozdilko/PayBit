package com.justimaginethat.gogodriver.BaseModel;

import java.io.Serializable;

public class BaseAPIErrorModel implements Serializable {
    public int errorLogId;
    public String error;
    public String errorDetails;
    public String errorURL;
}
