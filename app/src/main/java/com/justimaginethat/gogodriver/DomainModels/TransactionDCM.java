package com.justimaginethat.gogodriver.DomainModels;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;
import com.justimaginethat.gogodriver.ORMLite.DbStructureConfig.Database;

import java.io.Serializable;

/**
 * Created by Lion-1 on 3/25/2017.
 */

@DatabaseTable(tableName = Database.TABLE.Transaction.TableName)
public class TransactionDCM extends BaseAPIErrorModel implements Serializable {
    @DatabaseField(columnName = Database.TABLE.Transaction.idTransactionMaster,id=true, generatedId = false )
    public int idTransactionMaster=0;
    @DatabaseField(columnName = Database.TABLE.Transaction.idOrderMaster)
    public int idOrderMaster=0;
    @DatabaseField(columnName = Database.TABLE.Transaction.utf8)
    public String utf8="";
    @DatabaseField(columnName = Database.TABLE.Transaction.invoiceNumber)
    public String invoiceNumber = "";
    @DatabaseField(columnName = Database.TABLE.Transaction.req_card_number)
    public String req_card_number = "";
    @DatabaseField(columnName = Database.TABLE.Transaction.req_locale)
    public String req_locale = "";
    @DatabaseField(columnName = Database.TABLE.Transaction.signature)
    public String signature = "";
    @DatabaseField(columnName = Database.TABLE.Transaction.auth_trans_ref_no)
    public String auth_trans_ref_no = "";
    @DatabaseField(columnName = Database.TABLE.Transaction.req_bill_to_surname)
    public String req_bill_to_surname = "";
    @DatabaseField(columnName = Database.TABLE.Transaction.req_bill_to_address_city)
    public String req_bill_to_address_city = "";
    @DatabaseField(columnName = Database.TABLE.Transaction.req_card_expiry_date)
    public String req_card_expiry_date = "";
    @DatabaseField(columnName = Database.TABLE.Transaction.req_bill_to_address_postal_code)
    public String req_bill_to_address_postal_code = "";
    @DatabaseField(columnName = Database.TABLE.Transaction.req_bill_to_phone)
    public String req_bill_to_phone = "";
    @DatabaseField(columnName = Database.TABLE.Transaction.reason_code)
    public String reason_code = "";
    @DatabaseField(columnName = Database.TABLE.Transaction.auth_amount)
    public String auth_amount = "";
    @DatabaseField(columnName = Database.TABLE.Transaction.auth_response)
    public String auth_response = "";
    @DatabaseField(columnName = Database.TABLE.Transaction.req_bill_to_forename)
    public String req_bill_to_forename = "";
    @DatabaseField(columnName = Database.TABLE.Transaction.req_payment_method)
    public String req_payment_method = "";
    @DatabaseField(columnName = Database.TABLE.Transaction.request_token)
    public String request_token = "";
    @DatabaseField(columnName = Database.TABLE.Transaction.req_device_fingerprint_id)
    public String req_device_fingerprint_id = "";
    @DatabaseField(columnName = Database.TABLE.Transaction.auth_time)
    public String auth_time = "";
    @DatabaseField(columnName = Database.TABLE.Transaction.req_amount)
    public String req_amount = "";
    @DatabaseField(columnName = Database.TABLE.Transaction.req_bill_to_email)
    public String req_bill_to_email = "";
    @DatabaseField(columnName = Database.TABLE.Transaction.transaction_id)
    public String transaction_id = "";
    @DatabaseField(columnName = Database.TABLE.Transaction.auth_avs_code_raw)
    public String auth_avs_code_raw = "";
    @DatabaseField(columnName = Database.TABLE.Transaction.req_currency)
    public String req_currency = "";
    @DatabaseField(columnName = Database.TABLE.Transaction.req_card_type)
    public String req_card_type = "";
    @DatabaseField(columnName = Database.TABLE.Transaction.decision)
    public String decision = "";
    @DatabaseField(columnName = Database.TABLE.Transaction.message)
    public String message = "";
    @DatabaseField(columnName = Database.TABLE.Transaction.signed_field_names)
    public String signed_field_names = "";
    @DatabaseField(columnName = Database.TABLE.Transaction.req_transaction_uuid)
    public String req_transaction_uuid = "";
    @DatabaseField(columnName = Database.TABLE.Transaction.auth_avs_code)
    public String auth_avs_code = "";
    @DatabaseField(columnName = Database.TABLE.Transaction.auth_code)
    public String auth_code = "";
    @DatabaseField(columnName = Database.TABLE.Transaction.req_bill_to_address_country)
    public String req_bill_to_address_country = "";
    @DatabaseField(columnName = Database.TABLE.Transaction.req_transaction_type)
    public String req_transaction_type = "";
    @DatabaseField(columnName = Database.TABLE.Transaction.req_access_key)
    public String req_access_key = "";
    @DatabaseField(columnName = Database.TABLE.Transaction.req_profile_id)
    public String req_profile_id = "";
    @DatabaseField(columnName = Database.TABLE.Transaction.req_reference_number)
    public String req_reference_number = "";
    @DatabaseField(columnName = Database.TABLE.Transaction.req_bill_to_address_state)
    public String req_bill_to_address_state = "";
    @DatabaseField(columnName = Database.TABLE.Transaction.signed_date_time)
    public String signed_date_time = "";
    @DatabaseField(columnName = Database.TABLE.Transaction.req_bill_to_address_line1)
    public String req_bill_to_address_line1 = "";



    public TransactionDCM() {
    }



    public TransactionDCM(
            int idTransactionMaster,
            int idOrderMaster,
            String utf8,
            String invoiceNumber,
            String req_card_number,
            String req_locale,
            String signature,
            String auth_trans_ref_no,
            String req_bill_to_surname,
            String req_bill_to_address_city,
            String req_card_expiry_date,
            String req_bill_to_address_postal_code,
            String req_bill_to_phone, String reason_code,
            String auth_amount,
            String auth_response,
            String req_bill_to_forename,
            String req_payment_method,
            String request_token,
            String req_device_fingerprint_id,
            String auth_time, String req_amount,
            String req_bill_to_email,
            String transaction_id,
            String auth_avs_code_raw,
            String req_currency,
            String req_card_type,
            String decision,
            String message,
            String signed_field_names,
            String req_transaction_uuid,
            String auth_avs_code,
            String auth_code,
            String req_bill_to_address_country,
            String req_transaction_type,
            String req_access_key,
            String req_profile_id,
            String req_reference_number,
            String req_bill_to_address_state,
            String signed_date_time,
            String req_bill_to_address_line1
    ) {
        this.idTransactionMaster = idTransactionMaster;
        this.idOrderMaster = idOrderMaster;
        this.utf8 = utf8;
        this.invoiceNumber = invoiceNumber;
        this.req_card_number = req_card_number;
        this.req_locale = req_locale;
        this.signature = signature;
        this.auth_trans_ref_no = auth_trans_ref_no;
        this.req_bill_to_surname = req_bill_to_surname;
        this.req_bill_to_address_city = req_bill_to_address_city;
        this.req_card_expiry_date = req_card_expiry_date;
        this.req_bill_to_address_postal_code = req_bill_to_address_postal_code;
        this.req_bill_to_phone = req_bill_to_phone;
        this.reason_code = reason_code;
        this.auth_amount = auth_amount;
        this.auth_response = auth_response;
        this.req_bill_to_forename = req_bill_to_forename;
        this.req_payment_method = req_payment_method;
        this.request_token = request_token;
        this.req_device_fingerprint_id = req_device_fingerprint_id;
        this.auth_time = auth_time;
        this.req_amount = req_amount;
        this.req_bill_to_email = req_bill_to_email;
        this.transaction_id = transaction_id;
        this.auth_avs_code_raw = auth_avs_code_raw;
        this.req_currency = req_currency;
        this.req_card_type = req_card_type;
        this.decision = decision;
        this.message = message;
        this.signed_field_names = signed_field_names;
        this.req_transaction_uuid = req_transaction_uuid;
        this.auth_avs_code = auth_avs_code;
        this.auth_code = auth_code;
        this.req_bill_to_address_country = req_bill_to_address_country;
        this.req_transaction_type = req_transaction_type;
        this.req_access_key = req_access_key;
        this.req_profile_id = req_profile_id;
        this.req_reference_number = req_reference_number;
        this.req_bill_to_address_state = req_bill_to_address_state;
        this.signed_date_time = signed_date_time;
        this.req_bill_to_address_line1 = req_bill_to_address_line1;
    }
}
