package com.justimaginethat.gogodriver.ORMLite.DbStructureConfig;


/**
 * Created by LION1 on 03-03-2017.
 */

//Step one.....

public class Database {
    public static final String DatabaseName = "paybitdb";

    public static class TABLE {
        public static class User {
            public static final String TableName = "userDB";
            //                public static final String idRegistration = "idRegistration";
            public static final String idUser = "idUser";
            public static final String isCustomer = "isCustomer";
            public static final String isDriver = "isDriver";
            public static final String isAdmin = "isAdmin";
            public static final String workStatus = "workStatus";
            public static final String recordBySystem = "recordBySystem";
            public static final String isActive = "isActive";
            public static final String isDeleted = "isDeleted";
            public static final String attachment = "attachment";
            public static final String type = "type";
            public static final String address1 = "address1";
            public static final String countryCode = "countryCode";
            public static final String emailAddress = "emailAddress";
            public static final String firstName = "firstName";
            public static final String gcmId = "gcmId";
            public static final String mobileNumber = "mobileNumber";
            public static final String password = "password";
            public static final String profilePicture = "profilePicture";
            public static final String udId = "udId";
            public static final String userName = "userName";
            public static final String lastName = "lastName";
            public static final String passwordEncryptionKey = "passwordEncryptionKey";
            public static final String lastPasswordResetDate = "lastPasswordResetDate";
            public static final String otp = "otp";
            public static final String apnID = "apnID";
            public static final String address2 = "address2";
            public static final String longitude1 = "longitude1";
            public static final String latitude1 = "latitude1";
            public static final String longitude2 = "longitude2";
            public static final String latitude2 = "latitude2";
            public static final String streetName1 = "streetName1";
            public static final String streetName2 = "streetName2";
            public static final String sessionID = "sessionID";
            public static final String sessionValue = "sessionValue";
            public static final String sessionExpireyDateTime = "sessionExpireyDateTime";
            public static final String sessionCreateTime = "sessionCreateTime";
            public static final String blockExpiry = "blockExpiry";
            public static final String userAccountStatus = "userAccountStatus";
            public static final String lastChangeDate = "lastChangeDate";
            public static final String entryByUserName = "entryByUserName";
            public static final String entryDate = "entryDate";
            public static final String changeByUserName = "changeByUserName";
            public static final String insertRoutePoint = "insertRoutePoint";
            public static final String updateRoutePoint = "updateRoutePoint";
            public static final String syncGUID = "syncGUID";
            public static final String buildingName1 = "buildingName1";
            public static final String floorNumber1 = "floorNumber1";
            public static final String buildingName2 = "buildingName2";
            public static final String floorNumber2 = "floorNumber2";
            public static final String defaultAddress = "defaultAddress";
            public static final String defaultBuildingName = "defaultBuildingName";
            public static final String defaultFloorNumber = "defaultFloorNumber";
            public static final String defaultLongitude = "defaultLongitude";
            public static final String defaultLatitude = "defaultLatitude";
            public static final String heading = "heading";


//                    public static final String emailAddress = "emailAddress";
//                    public static final String userName = "userName";
//                    public static final String firstName = "firstName";
//                    public static final String lastName = "lastName";
//                    public static final String mobileNumber = "mobileNumber";
//                    public static final String countryCode = "countryCode";
//                    public static final String entryDate = "entryDate";
//                    public static final String password = "password";
        }

        public static class Order {
            public static final String TableName = "OrderDB";
            public static final String idOrderMaster = "idOrderMaster";
            public static final String idUserCustomer = "idUserCustomer";
            public static final String idDeliveryPickupPoints = "idDeliveryPickupPoints";
            public static final String idUserDriver = "idUserDriver";
            public static final String productQuantity = "productQuantity";
            public static final String fixedRateAmount = "fixedRateAmount";
            public static final String orderAmount = "orderAmount";
            public static final String isCompleted = "isCompleted";
            public static final String paid = "paid";
            public static final String isActive = "isActive";
            public static final String isDeleted = "isDeleted";
            public static final String recordBySystem = "recordBySystem";
            public static final String orderTitle = "orderTitle";
            public static final String productName = "productName";
            public static final String quantityType = "quantityType";
            public static final String orderDate = "orderDate";
            public static final String orderTime = "orderTime";
            public static final String entryDate = "entryDate";
            public static final String lastChangeDate = "lastChangeDate";
            public static final String entryByUserName = "entryByUserName";
            public static final String changeByUserName = "changeByUserName";
            public static final String insertRoutePoint = "insertRoutePoint";
            public static final String updateRoutePoint = "updateRoutePoint";
            public static final String syncGUID = "syncGUID";
            public static final String orderStatus = "orderStatus";
            public static final String deliveryAddress = "deliveryAddress";
            public static final String deliveryLongitude = "deliveryLongitude";
            public static final String deliveryLatitude = "deliveryLatitude";
            public static final String deliveryBuildingName = "deliveryBuildingName";
            public static final String deliveryFloorNumber = "deliveryFloorNumber";
        }

        public static class DeliveryPickupPoint {
            public static final String TableName = "DeliveryPickupPoint";
            public static final String idDeliveryPickupPoints = "idDeliveryPickupPoints";
            public static final String entryByUserName = "entryByUserName";
            public static final String isActive = "isActive";
            public static final String title = "title";
            public static final String pickupAddress = "pickupAddress";
            public static final String pickupLongitude = "pickupLongitude";
            public static final String pickupLatitude = "pickuplatitude";
            public static final String entryDate = "entryDate";
            public static final String lastChangeDate = "lastChangeDate";
            public static final String changeByUserName = "changeByUserName";
            public static final String updateRoutePoint = "updateRoutePoint";
            public static final String syncGUID = "syncGUID";
            public static final String insertRoutePoint = "insertRoutePoint";
        }

        public static class Session {
            public static final String TableName = "session";
            //                public static final String idRegistration = "idRegistration";
            public static final String idUser = "idUser";
            public static final String isCustomer = "isCustomer";
            public static final String isDriver = "isDriver";
            public static final String isAdmin = "isAdmin";
            public static final String workStatus = "workStatus";
            public static final String recordBySystem = "recordBySystem";
            public static final String isActive = "isActive";
            public static final String isDeleted = "isDeleted";
            public static final String attachment = "attachment";
            public static final String type = "type";
            public static final String address1 = "address1";
            public static final String countryCode = "countryCode";
            public static final String emailAddress = "emailAddress";
            public static final String firstName = "firstName";
            public static final String gcmId = "gcmId";
            public static final String mobileNumber = "mobileNumber";
            public static final String password = "password";
            public static final String profilePicture = "profilePicture";
            public static final String udId = "udId";
            public static final String userName = "userName";
            public static final String lastName = "lastName";
            public static final String passwordEncryptionKey = "passwordEncryptionKey";
            public static final String lastPasswordResetDate = "lastPasswordResetDate";
            public static final String otp = "otp";
            public static final String apnID = "apnID";
            public static final String address2 = "address2";
            public static final String longitude1 = "longitude1";
            public static final String latitude1 = "latitude1";
            public static final String longitude2 = "longitude2";
            public static final String latitude2 = "latitude2";
            public static final String streetName1 = "streetName1";
            public static final String streetName2 = "streetName2";
            public static final String sessionID = "sessionID";
            public static final String sessionValue = "sessionValue";
            public static final String sessionExpireyDateTime = "sessionExpireyDateTime";
            public static final String sessionCreateTime = "sessionCreateTime";
            public static final String blockExpiry = "blockExpiry";
            public static final String userAccountStatus = "userAccountStatus";
            public static final String lastChangeDate = "lastChangeDate";
            public static final String entryByUserName = "entryByUserName";
            public static final String entryDate = "entryDate";
            public static final String changeByUserName = "changeByUserName";
            public static final String insertRoutePoint = "insertRoutePoint";
            public static final String updateRoutePoint = "updateRoutePoint";
            public static final String syncGUID = "syncGUID";
            public static final String buildingName1 = "buildingName1";
            public static final String floorNumber1 = "floorNumber1";
            public static final String buildingName2 = "buildingName2";
            public static final String floorNumber2 = "floorNumber2";
            public static final String defaultAddress = "defaultAddress";
            public static final String defaultBuildingName = "defaultBuildingName";
            public static final String defaultFloorNumber = "defaultFloorNumber";
            public static final String defaultLongitude = "defaultLongitude";
            public static final String defaultLatitude = "defaultLatitude";
            public static final String heading = "heading";


//                    public static final String emailAddress = "emailAddress";
//                    public static final String userName = "userName";
//                    public static final String firstName = "firstName";
//                    public static final String lastName = "lastName";
//                    public static final String mobileNumber = "mobileNumber";
//                    public static final String countryCode = "countryCode";
//                    public static final String entryDate = "entryDate";
//                    public static final String password = "password";
        }

        public static class Driver {
            public static final String TableName = "Driver";
            //                public static final String idRegistration = "idRegistration";
            public static final String idUser = "idUser";
            public static final String isCustomer = "isCustomer";
            public static final String isDriver = "isDriver";
            public static final String isAdmin = "isAdmin";
            public static final String workStatus = "workStatus";
            public static final String recordBySystem = "recordBySystem";
            public static final String isActive = "isActive";
            public static final String isDeleted = "isDeleted";
            public static final String attachment = "attachment";
            public static final String type = "type";
            public static final String address1 = "address1";
            public static final String countryCode = "countryCode";
            public static final String emailAddress = "emailAddress";
            public static final String firstName = "firstName";
            public static final String gcmId = "gcmId";
            public static final String mobileNumber = "mobileNumber";
            public static final String password = "password";
            public static final String profilePicture = "profilePicture";
            public static final String udId = "udId";
            public static final String userName = "userName";
            public static final String lastName = "lastName";
            public static final String passwordEncryptionKey = "passwordEncryptionKey";
            public static final String lastPasswordResetDate = "lastPasswordResetDate";
            public static final String otp = "otp";
            public static final String apnID = "apnID";
            public static final String address2 = "address2";
            public static final String longitude1 = "longitude1";
            public static final String latitude1 = "latitude1";
            public static final String longitude2 = "longitude2";
            public static final String latitude2 = "latitude2";
            public static final String streetName1 = "streetName1";
            public static final String streetName2 = "streetName2";
            public static final String sessionID = "sessionID";
            public static final String sessionValue = "sessionValue";
            public static final String sessionExpireyDateTime = "sessionExpireyDateTime";
            public static final String sessionCreateTime = "sessionCreateTime";
            public static final String blockExpiry = "blockExpiry";
            public static final String userAccountStatus = "userAccountStatus";
            public static final String lastChangeDate = "lastChangeDate";
            public static final String entryByUserName = "entryByUserName";
            public static final String entryDate = "entryDate";
            public static final String changeByUserName = "changeByUserName";
            public static final String insertRoutePoint = "insertRoutePoint";
            public static final String updateRoutePoint = "updateRoutePoint";
            public static final String syncGUID = "syncGUID";
            public static final String buildingName1 = "buildingName1";
            public static final String floorNumber1 = "floorNumber1";
            public static final String buildingName2 = "buildingName2";
            public static final String floorNumber2 = "floorNumber2";
            public static final String defaultAddress = "defaultAddress";
            public static final String defaultBuildingName = "defaultBuildingName";
            public static final String defaultFloorNumber = "defaultFloorNumber";
            public static final String defaultLongitude = "defaultLongitude";
            public static final String defaultLatitude = "defaultLatitude";
            public static final String heading = "heading";

//                    public static final String emailAddress = "emailAddress";
//                    public static final String userName = "userName";
//                    public static final String firstName = "firstName";
//                    public static final String lastName = "lastName";
//                    public static final String mobileNumber = "mobileNumber";
//                    public static final String countryCode = "countryCode";
//                    public static final String entryDate = "entryDate";
//                    public static final String password = "password";
        }

        public static class Cards {
            public static final String TableName = "Cards";
            public static final String id = "id";
            public static final String  isDefault = "isDefault";
            public static final String  number = "number";
            public static final String  expiryMonth = "expiryMonth";
            public static final String  expiryYear = "expiryYear";
            public static final String  cvvNumber = "cvvNumber";
            public static final String  type = "type";
            public static final String  paymentToken = "paymentToken";
        }


        public static class Transaction {
            public static final String TableName = "Transaction";
            public static final String idTransactionMaster="idTransactionMaster";
            public static final String idOrderMaster="idOrderMaster";
            public static final String utf8="utf8";
            public static final String invoiceNumber ="invoiceNumber";
            public static final String req_card_number = "req_card_number";
            public static final String req_locale = "req_locale";
            public static final String signature = "signature";
            public static final String auth_trans_ref_no = "auth_trans_ref_no";
            public static final String req_bill_to_surname = "req_bill_to_surname";
            public static final String req_bill_to_address_city = "req_bill_to_address_city";
            public static final String req_card_expiry_date = "req_card_expiry_date";
            public static final String req_bill_to_address_postal_code = "req_bill_to_address_postal_code";
            public static final String req_bill_to_phone = "req_bill_to_phone";
            public static final String reason_code = "reason_code";
            public static final String auth_amount = "auth_amount";
            public static final String auth_response = "auth_response";
            public static final String req_bill_to_forename = "req_bill_to_forename";
            public static final String req_payment_method = "req_payment_method";
            public static final String request_token = "request_token";
            public static final String req_device_fingerprint_id = "req_device_fingerprint_id";
            public static final String auth_time = "auth_time";
            public static final String req_amount = "req_amount";
            public static final String req_bill_to_email = "req_bill_to_email";
            public static final String transaction_id = "transaction_id";
            public static final String auth_avs_code_raw = "auth_avs_code_raw";
            public static final String req_currency = "req_currency";
            public static final String req_card_type = "req_card_type";
            public static final String decision = "decision";
            public static final String message = "message";
            public static final String signed_field_names = "signed_field_names";
            public static final String req_transaction_uuid = "req_transaction_uuid";
            public static final String auth_avs_code = "auth_avs_code";
            public static final String auth_code = "auth_code";
            public static final String req_bill_to_address_country = "req_bill_to_address_country";
            public static final String req_transaction_type = "req_transaction_type";
            public static final String req_access_key = "req_access_key";
            public static final String req_profile_id = "req_profile_id";
            public static final String req_reference_number = "req_reference_number";
            public static final String req_bill_to_address_state = "req_bill_to_address_state";
            public static final String signed_date_time = "signed_date_time";
            public static final String req_bill_to_address_line1 = "req_bill_to_address_line1";


        }




    }


}
