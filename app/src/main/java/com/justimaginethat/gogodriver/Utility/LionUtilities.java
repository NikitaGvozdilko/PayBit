package com.justimaginethat.gogodriver.Utility;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.Gson;
import com.justimaginethat.gogodriver.StartUp.FixLabels;

import org.apache.http.HttpStatus;
import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by Lion-1 on 1/30/2017.
 */

@SuppressLint("DefaultLocale")
public class LionUtilities implements Serializable{
    int connectionTimeOut = 250000;
    private static final long serialVersionUID = 1L;
    public static boolean hasConnection(Context context) {
        ConnectivityManager cManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo network = cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (network != null && network.isConnected()) {
            return true;
        }

        network = cManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (network != null && network.isConnected()) {
            return true;
        }

        NetworkInfo activeNetwork = cManager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }
        return false;
    }
    public static void makeToast(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }
    public boolean isConnected(Activity curr) {

        ConnectivityManager connectivity = (ConnectivityManager) curr
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }
    //    MMMM/dd/yyyy
    public Date getDateFromString(String dateString, String format) throws ParseException {
        Date d = new SimpleDateFormat(format, Locale.ENGLISH).parse(dateString);
        return d;
    }
    public static String getStringFromDate(Date date, String format) throws ParseException {
        Date d = date;
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String dateString = new SimpleDateFormat(format).format(cal.getTime());
        return dateString;
    }
    public Object setSharedPreferences(Object object, Context context)
            throws OptionalDataException, ClassNotFoundException, IOException {

        SharedPreferences settings = context.getSharedPreferences(
                FixLabels.SharedPrefrenceDATAVariable, 0);
        SharedPreferences.Editor editor = settings.edit();

        Object obj = object;
        obj = object;

        settings = context.getSharedPreferences(
                FixLabels.SharedPrefrenceDATAVariable, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putString(FixLabels.SharedPrefrenceDATAGlobalObject,
                ConvertObjectToString(obj));
        editor.commit();

        return obj;

    }

    public Object getSharedPreferences(Context context)
            throws OptionalDataException, ClassNotFoundException, IOException {

        SharedPreferences settings = context.getSharedPreferences(
                FixLabels.SharedPrefrenceDATAVariable, 0);
        Object obj=null;
        String str =settings.getString(FixLabels.SharedPrefrenceDATAGlobalObject, "");
        if(!str.equals("") )
        {
            obj = ConvertStringToObjectInputStream(
                    settings.getString(FixLabels.SharedPrefrenceDATAGlobalObject, ""))
                    .readObject();
        }

        return obj;

    }

    public String ConvertObjectToString(Object object) {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutput;
        try {
            objectOutput = new ObjectOutputStream(arrayOutputStream);
            objectOutput.writeObject(object);
            byte[] data = arrayOutputStream.toByteArray();
            objectOutput.close();
            arrayOutputStream.close();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Base64OutputStream b64 = new Base64OutputStream(out, Base64.DEFAULT);
            b64.write(data);
            b64.close();
            out.close();
            return new String(out.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
//
//	Intent mServiceIntent ;
//	public void StartDownloadManagerService(Context DriverHeadingToPickup) {
//
//		if(!isMyServiceRunning(DownloadManageService.class,DriverHeadingToPickup)) {
//
//			// Registers the DownloadStateReceiver and its intent filters
//			mServiceIntent = new Intent(DriverHeadingToPickup, DownloadManageService.class);
//			DriverHeadingToPickup.startService(mServiceIntent);
//		}
//	}
//	public void StopDownloadManagerService(Context DriverHeadingToPickup) {
//		DownloadManageService.stopServiceProcess = true;
//		if(isMyServiceRunning(DownloadManageService.class,DriverHeadingToPickup)) {
//
//			DriverHeadingToPickup.stopService(mServiceIntent);
//		}
//	}

    private boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    public ObjectInputStream ConvertStringToObjectInputStream(String str)
            throws ClassNotFoundException {

        try {
            byte[] bytes = str.getBytes();
            if (bytes.length == 0) {
                return null;
            }
            ByteArrayInputStream byteArray = new ByteArrayInputStream(bytes);
            Base64InputStream base64InputStream = new Base64InputStream(
                    byteArray, Base64.DEFAULT);

            // use .readObject();
            return new ObjectInputStream(base64InputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String objectconvertparse(Object object) {
        Gson gson = new Gson();
        String createparsestring = gson.toJson(object);
        return createparsestring;
    }

    public Bitmap convertToCornerRadius(Bitmap mbitmap)
    {
        //                Bitmap mbitmap = compressor.compressImage(Integer.parseInt(cat.get(position).storyCategoryImageUrl),mContext,200,150);
        if(mbitmap!=null) {
            Bitmap imageRounded = Bitmap.createBitmap(mbitmap.getWidth(), mbitmap.getHeight(), mbitmap.getConfig());
            Canvas canvas = new Canvas(imageRounded);
            Paint mpaint = new Paint();
            mpaint.setAntiAlias(true);
            mpaint.setShader(new BitmapShader(mbitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
            canvas.drawRoundRect((new RectF(0, 0, mbitmap.getWidth(), mbitmap.getHeight())), 10, 10, mpaint);// Round Image Corner 100 100 100 100

            return imageRounded;
        }
        return null;
    }
    public String StoreFromURL(String URL, String FileName, Context context)
    {
        try {
            java.net.URL url = new URL(URL); //you can write here any link
            File file = new File(context.getCacheDir().getAbsoluteFile(),FileName);

            long startTime = System.currentTimeMillis();
            Log.d("ImageManager", "download begining");
            Log.d("ImageManager", "download url:" + url);
            Log.d("ImageManager", "downloaded file name:" + FileName);
                        /* Open a connection to that URL. */
            URLConnection ucon = url.openConnection();

                        /*
                         * Define InputStreams to read from the URLConnection.
                         */
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);

                        /*
                         * Read bytes to the Buffer until there is nothing more to read(-1).
                         */
            ByteArrayBuffer baf = new ByteArrayBuffer(50);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }

                        /* Convert the Bytes read to a String. */
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baf.toByteArray());
            fos.close();
            Log.d("ImageManager", "download ready in"
                    + ((System.currentTimeMillis() - startTime) / 1000)
                    + " sec");

        } catch (IOException e) {
            Log.d("ImageManager", "Error: " + e);
        }
        return context.getCacheDir().getAbsoluteFile()+FileName;

    }

    public String requestGETHTTPSResponse(String URL, HashMap<String,String> postDataParams)
    {
        String response="";
        java.net.URL url;
        HttpURLConnection conn = null;
        try {

            String toSendParam = "";
            if(postDataParams!= null) {
                toSendParam = getPostDataString(postDataParams, true);
            }
            url = new URL(URL+"?"+toSendParam);

            conn = (HttpsURLConnection) url
                    .openConnection();
            conn.setInstanceFollowRedirects(true);
            conn.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
            conn.setRequestProperty("Accept","*/*");
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String requestHTTPSResponse(String URL, HashMap<String,String> postDataParams, String Method, boolean encodeInUrl)
    {
        String response="";
        try{
            java.net.URL url = new URL(URL);
            HttpsURLConnection conn = null;

            conn = (HttpsURLConnection) url.openConnection();
            conn.setReadTimeout(connectionTimeOut);
            conn.setConnectTimeout(connectionTimeOut);
            conn.setRequestMethod(Method);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String toSendParam = "";
            if(postDataParams!= null) {
                toSendParam = getPostDataString(postDataParams, encodeInUrl);
            }
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            if(postDataParams!= null) {
                writer.write(toSendParam);
            }
            else
            {
                writer.write("");
            }
            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();


            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String requestHTTPResponse(String URL, HashMap<String,String> postDataParams, String Method, boolean encodeInUrl)
    {
        String response="";
        try{
            java.net.URL url = new URL(URL);
            HttpURLConnection conn = null;

            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(connectionTimeOut);
            conn.setConnectTimeout(connectionTimeOut);
            conn.setRequestMethod(Method);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String toSendParam = "";
            if(postDataParams!= null) {
                toSendParam = getPostDataString(postDataParams, encodeInUrl);
            }
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            if(postDataParams!= null) {
                writer.write(toSendParam);
            }
            else
            {
                writer.write("");
            }
            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();


            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }










    public String requestHTTPResponseWithFile(String URL, HashMap<String,String> postDataParams, HashMap<String,String> filesData, String Method, boolean encodeInUrl) throws IOException {

        MultipartUtility multipart = new MultipartUtility(URL,Method, "UTF-8");

        for(Map.Entry<String, String> entry : postDataParams.entrySet()){
            if(encodeInUrl==true)
            {

                multipart.addFormField(URLEncoder.encode(entry.getKey(), "UTF-8"), URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
            else
            {
                multipart.addFormField(entry.getKey(),entry.getValue());
            }

        }
        for(Map.Entry<String, String> entry : filesData.entrySet()){
            if(new File(entry.getValue()).exists()) {
                multipart.addFilePart(entry.getKey(), new File(entry.getValue()));
            }
        }

        return multipart.finish();

    } // End else block





    private Bitmap downloadBitmap(String url) {
        HttpURLConnection urlConnection = null;
        try {
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (Exception e) {
            urlConnection.disconnect();
            Log.w("ImageDownloader", "Error downloading image from " + url);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }


    public static String AssetJSONFile(String filename, Context context) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream file = manager.open(filename);
        byte[] formArray = new byte[file.available()];
        file.read(formArray);
        file.close();

        return new String(formArray);
    }

    public int isGooglePlayServicesAvailable(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if(status != ConnectionResult.SUCCESS) {
            if(googleApiAvailability.isUserResolvableError(status)) {
                //googleApiAvailability.getErrorDialog(activity, status, 2404).show();
                return 1;
            }
            else
            {
                return 0;
            }

        }
        return 1;
    }


    public String getPostDataString(HashMap<String, String> params, boolean encodeURL) throws UnsupportedEncodingException {

//		HashMap<String, String>  map =new HashMap<String, String>();
//		map.put("FirstKey","FirstValue");
//		map.put("SecondKey","SecondValue");

        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            if(encodeURL==true)
            {
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
            else
            {
                result.append(entry.getKey());
                result.append("=");
                result.append(entry.getValue());
            }

        }

        return result.toString();
    }








    /**
     * Created by Jay Bhavsar on 09/08/2016.
     */
    public class MultipartUtility {
        private final String boundary;
        private static final String LINE_FEED = "\r\n";
        private HttpURLConnection httpConn;

        private OutputStream outputStream;
        private PrintWriter writer;
        String charset = "UTF-8";
        String requestURL = "YOUR_URL";

        /**
         * This constructor initializes a new HTTP POST request with content type
         * is set to multipart/form-data
         *
         * @param requestURL
         * @param charset
         * @throws IOException
         */
        public MultipartUtility(String requestURL, String Method, String charset)
                throws IOException {
            this.charset = charset;

            // creates a unique boundary based on time stamp
            boundary =  "--" + System.currentTimeMillis() + "--";

            URL url = new URL(requestURL);
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setUseCaches(false);
            httpConn.setDoOutput(true); // indicates POST method
            httpConn.setDoInput(true);
            httpConn.setRequestMethod(Method);
            httpConn.setReadTimeout(25000);
            httpConn.setConnectTimeout(25000);
            httpConn.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + boundary);
//			httpConn.setRequestProperty("User-Agent", "CodeJava Agent");
//        httpConn.setRequestProperty("Test", "Bonjour");
            outputStream = httpConn.getOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(outputStream, charset),
                    true);
        }

        /**
         * Adds a form field to the request
         *
         * @param name  field name
         * @param value field value
         */
        public void addFormField(String name, String value) {
            writer.append("--" + boundary).append(LINE_FEED);
            writer.append("Content-Disposition: form-data; name=\"" + name + "\"")
                    .append(LINE_FEED);
            writer.append("Content-Type: application/x-www-form-urlencoded;").append(
                    LINE_FEED);
            writer.append(LINE_FEED);
            writer.append(value).append(LINE_FEED);

            writer.flush();
        }

        /**
         * Adds a upload file section to the request
         *
         * @param fieldName  name attribute in <input type="file" name="..." />
         * @param uploadFile a File to be uploaded
         * @throws IOException
         */
        public void addFilePart(String fieldName, File uploadFile)
                throws IOException {
            String fileName = uploadFile.getName();
            writer.append("--" + boundary).append(LINE_FEED);
            writer.append(
                    "Content-Disposition: form-data; name=\"" + fieldName
                            + "\"; filename=\"" + fileName + "\"")
                    .append(LINE_FEED);
            writer.append(
                    "Content-Type: "
                            + URLConnection.guessContentTypeFromName(fileName))
                    .append(LINE_FEED);
            writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
            writer.append(LINE_FEED);
            writer.flush();

            FileInputStream inputStream = new FileInputStream(uploadFile);
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            inputStream.close();

            writer.append(LINE_FEED);
            writer.flush();
        }

        /**
         * Adds a header field to the request.
         *
         * @param name  - name of the header field
         * @param value - value of the header field
         */
        public void addHeaderField(String name, String value) {
            writer.append(name + ": " + value).append(LINE_FEED);
            writer.flush();
        }

        /**
         * Completes the request and receives response from the server.
         *
         * @return a list of Strings as response in case the server returned
         * status OK, otherwise an exception is thrown.
         * @throws IOException
         */
        public String finish() throws IOException {
            String response = "";

            writer.append(LINE_FEED).flush();
            writer.append("--" + boundary + "--").append(LINE_FEED);
            writer.close();

            // checks server's status code first
            int status = httpConn.getResponseCode();
//			if (status == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpConn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                response+=(line);
            }
            reader.close();
            httpConn.disconnect();
//			} else {
//				throw new IOException("Server returned non-OK status: " + status);
//			}

            return response;
        }
    }



    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
                int count=is.read(bytes, 0, buffer_size);
                if(count==-1)
                    break;
                os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }

  public   long getDateDiffInDays(Date startDate,Date endDate)
    {
        //1 minute = 60 seconds
        //1 hour = 60 x 60 = 3600
        //1 day = 3600 x 24 = 8640
    //milliseconds
        long different = startDate.getTime() - endDate.getTime() ;

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays,
                elapsedHours, elapsedMinutes, elapsedSeconds);
        return elapsedDays;

    }





}







//private String RequestHTTPResponse(String apiKey, Object content){
//{
//	try{
//
//		// 1. URL
//		URL url = new URL("https://android.googleapis.com/gcm/send");
//
//		// 2. Open connection
//		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//		// 3. Specify POST method
//		conn.setRequestMethod("POST");
//
//		// 4. Set the headers
//		conn.setRequestProperty("Content-Type", "application/json");
//		conn.setRequestProperty("Authorization", "key="+apiKey);
//
//		conn.setDoOutput(true);
//
//		// 5. Add JSON data into POST request body
//
//		//`5.1 Use Jackson object mapper to convert Contnet object into JSON
//		ObjectMapper mapper = new ObjectMapper();
//
//		// 5.2 Get connection output stream
//		DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
//
//		// 5.3 Copy Content "JSON" into
//		mapper.writeValue(wr, content);
//
//		// 5.4 Send the request
//		wr.flush();
//
//		// 5.5 close
//		wr.close();
//
//		// 6. Get the response
//		int responseCode = conn.getResponseCode();
//		System.out.println("\nSending 'POST' request to URL : " + url);
//		System.out.println("Response Code : " + responseCode);
//
//		BufferedReader in = new BufferedReader(
//				new InputStreamReader(conn.getInputStream()));
//		String inputLine;
//		StringBuffer response = new StringBuffer();
//
//		while ((inputLine = in.readLine()) != null) {
//			response.append(inputLine);
//		}
//		in.close();
//
//		// 7. Print result
//		System.out.println(response.toString());
//
//	} catch (MalformedURLException e) {
//		e.printStackTrace();
//	} catch (IOException e) {
//		e.printStackTrace();
//	}
//}



//Old HTTP Methods Librery
// compile 'org.jbundle.util.osgi.wrapped:org.jbundle.util.osgi.wrapped.org.apache.http.client:4.1.2'

//	public static JSONObject getLogin(String url, String userid,
//			String isstudent, String ipaddress) {
//		String resultJson = null;
//		Log.e("Login ipaddress====>", ipaddress);
//		try {
//
//			DefaultHttpClient httpClient = new DefaultHttpClient();
//			HttpPost request = new HttpPost(url);
//			List<NameValuePair> postAuthData = new ArrayList<NameValuePair>();
//			postAuthData.add(new BasicNameValuePair("pinnumber", userid
//					.toLowerCase()));
//			postAuthData.add(new BasicNameValuePair("isStudent", isstudent
//					.toLowerCase()));
//			postAuthData.add(new BasicNameValuePair("ipaddress", ipaddress
//					.toLowerCase()));
//
//			request.setEntity(new UrlEncodedFormEntity(postAuthData));
//
//			HttpResponse response = httpClient.execute(request);
//			StatusLine statusLine = response.getStatusLine();
//			int statusCode = statusLine.getStatusCode();
//			resultJson = EntityUtils.toString(response.getEntity());
//			Log.d("TAG", "Status Code : " + statusCode + "Login Detail : "
//					+ resultJson);
//			return new JSONObject(resultJson);
//		} catch (Exception e) {
//			Log.d("Error Sendloginid===>", e.toString());
//		}
//		return null;
//	}