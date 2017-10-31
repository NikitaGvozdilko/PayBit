package com.justimaginethat.gogodriver.StartUp;

import com.justimaginethat.gogodriver.DomainModels.PickupPointDCM;
import com.justimaginethat.gogodriver.DomainModels.DriverDCM;
import com.justimaginethat.gogodriver.DomainModels.OrderDCM;
import com.justimaginethat.gogodriver.DomainModels.SessionDCM;
import com.justimaginethat.gogodriver.DomainModels.UserDCM;

import java.util.ArrayList;
import java.util.List;

public class FixLabels {

    public static String FILE_PATH="";
    public  static double pendingAmount = 0.0;
    public static int connectionTimeOut = 250000;
    public static String Server = "http://gogodriverapp.com/admin";

    //        public static String Server = "http://paybit.lionvisionits.com";
//    public static String Server = "http://192.168.0.107:1044";
//    public static String Server = "http://192.168.1.126:1044";
//    public static String Server = "http://192.168.0.103:1044";
    public static String udId ="TESTDevice";
    public static String gcmid ="TESTDevice";
    public static String alertDefaultTitle="GoGoDriver";


    public static boolean WaitingPaymentConfrimationFromDriver = false;

    public static  String SharedPrefrenceDATAVariable = "PayBitSharedPrefrenceDATAVariable";
    public static  String SharedPrefrenceDATAGlobalObject = "PayBitSharedPrefrenceDATAGlobalObject";
    public static String ServerGetAllDateAPI = Server + "/api/GetAllDataApi";
    public static  String TWITTER_KEY = "h9k289teBLKevKFWakn3vMpIe";
    public static  String TWITTER_SECRET = "6v2c1vU0Oz7A9i9GF5Jf3SrzE3EfYH9RWyaPo3tXadlXHfWQg9";





    //    public static String Server = "http://admin.cattleguru.org";
    //    public static  String Server = "http://127.0.0.1:2913";


    //    public static  String Server = "http://192.168.0.102:5326";
    //    https://quickstats.nass.usda.gov/api/get_counts
    public static String nassAPI ="0FF9AC17-1DB5-3655-BB02-8AC9AC3EA659";
//    public static String SharedPrefrenceDATAVariable = "ChikaraksSharedPrefrenceDATAVariable";
//    public static String SharedPrefrenceDATAGlobalObject = "ChikaraksSharedPrefrenceDATAGlobalObject";
public  static String cacheLocation ="/data/data/com.lionvisionits.chikaraks/cache";
    // Defines a custom Intent action
    public static final String BROADCAST_ACTION ="com.lionvisionits.chikaraks.BROADCAST";
    // Defines the key for the status "extra" in an Intent
    public static final String DOWNLOAD_BROADCAST_STATUS ="com.lionvisionits.chikaraks.STATUS";
    public static final String DOWNLOAD_BROADCAST_SECOND_STATUS ="com.lionvisionits.chikaraks.SECOND_STATUS";
    public static final String DOWNLOAD_BROADCAST_SIGNALTYPE ="com.lionvisionits.chikaraks.SIGNALTYPE";
    public static final String DOWNLOAD_BROADCAST_LISTPOSITION ="com.lionvisionits.chikaraks.LISTPOSITION";
    public static final String DOWNLOAD_BROADCAST_ID ="com.lionvisionits.chikaraks.ID";
    public static final String DOWNLOAD_BROADCAST_ACTIVITYNAME="com.lionvisionits.chikaraks.ACTIVITYNAME";
    public static final String DOWNLOAD_BROADCAST_CONTROLNAME ="com.lionvisionits.chikaraks.CONTROLNAME";



    public static String dateFormat ="MMMM/dd/yyyy";
    public static String dateFormatLong ="MMMM/dd/yyyy hh:mm aa";
    public static String serverDateFormat ="yyyy-MM-dd'T'HH:mm:ss";
    public static String timeFormatLong ="hh:mm aa";
    public static String timeFormatLongAM ="hh:mm a";

    public static final String keySeparator =",";
    public static int AddCattleActivityResult = 10;

    public static int ScheduleManageActivityResult = 11;
    public static int AddScheduleActivityResult = 12;

    public static class TRANSACTIONMODE{
        public static final String INSERT ="insert";
        public static final String UPDATE ="update";
        public static final String DELETE ="delete";
        public static final String COMPLETE_CLOSE ="COMPLETE_CLOSE";
    }

    public static class DOWNLOAD_SIGNAL{
        public static final String UPDATE ="UPDATE";
        public static final String COMPLETE ="COMPLETE";
        public static final String START ="START";
    }

    public static class activity_schedule_manage{
        public static final String cattletag ="cattletag";

    }
    public static class activity_add_cattle_putextra{
        public static final String openMode ="openMode";
        public static final String cattletag ="cattletag";

    }
    public static class activity_add_calves_putextra{
        public static final String tag ="tag";

    }

    public static class UserAddressAdapter {
        public static  String defaultAddress ="defaultAddress";
        public static  Double defaultLatitude =0.00;
        public static  Double defaultLongitude =0.00;
        public static  String defaultTitle ="defaultTitle";
    }



   /* public static class StorePickupListAdapter{
        public static  String pickupAddress ="pickupAddress";
        public static  String pickupLatitude ="pickuplatitude";
        public static  String pickupLongitude ="pickupLongitude";
        public static  String pickupTitle ="pickupTitle";
    }
*/
    public static class OrderStatus {
        public static final String NoActiveOrder= "No Active Order";
        public static final String PrepareNewOrder= "Prepare New Order";
        public static final String WaitingForDriver= "Waiting For Driver";
        public static final String DriverHeadingToPickUpPoint= "Driver Heading To Pickup Point";
        public static final String DriverArrivedAtPickUpPoint= "Driver Arrived At Pickup Point";
        public static final String PleaseConfirmPayment= "Please Confirm Payment";
        public static final String ConfirmingPayment = "Confirming Payment";
        public static final String PaymentComplete = "Payment Complete";
        public static final String DriverHeadingToYourLocation= "Driver Heading To Your Location";
        public static final String DriverReachedYourLocation= "Driver Reached Your Location";
        public static final String DeliveryComplete = "Delivery Complete";
        public static final String ReviewPending = "Review Pending";
        public static final String OrderComplete = "Order Complete";
        public static final String OrderCanceled= "Order Canceled";
       public static final String OrderAcceptOrReject= "Order Accept Or Reject";
       public static final String Payment= "Payment";
    }



    public static final String map ="AIzaSyDI_hibK27ZS5CUnpOYwKG9KwQljKCvbeU";
// new    public static final String map ="AIzaSyB4Xxo4ex0f2eesntSi9DtPe4ltgVgWFzM";

    String MapIndiaSecreateKey="AIzaSyCOELg30kEG5-HzQYuTApm3gTVY0h54w5Y";
    String MapBeairuteSecreateKey="AIzaSyAocxTn-t7riWSxQGs_hC3IqRjji46XRQA";

    public static String MAP_LAT_STR = "", MAP_LON_STR = "", MAP_ADDRESS = "";
    public static double MAP_LAT = 0.0, MAP_LON = 0.0;





    public static class sessionDatabase{

//        public static UserDCM user=new UserDCM();
//        public static SessionDCM session=new SessionDCM();
//        public static DriverDCM driver=new DriverDCM();
//        public static OrderDCM order=new OrderDCM();
        public static PickupPointDCM deliveryPickupPoints=new PickupPointDCM();
        public static List<PickupPointDCM>  deliveryPickupPointsList=new ArrayList<PickupPointDCM>();

        public static int pickupPointId=0;
        public static  String pickupAddress ="";
        public static  String pickupLatitude ="";
        public static  String pickupLongitude ="";
        public static  String pickupTitle ="";

    }




}




