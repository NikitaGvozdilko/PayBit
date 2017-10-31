package com.justimaginethat.gogodriver.APIModels.Payment;

/**
 * Created by Lion-1 on 3/30/2017.
 */

public class APIRequestPaymentRequestModel {
    public int idOrderMaster;
    public int idUser;
    public String number;
    public String expiryMonth;
    public String expiryYear;
    public String cvv;
    public String type;

    public APIRequestPaymentRequestModel() {
    }
}
