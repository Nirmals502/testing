package in.co.mealman.mealman;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.paytm.pgsdk.PaytmMerchant;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import adapter.Cart_adapter;
import adapter.History_adapter;
import adapter.Subscription_adapter;
import in.co.mealman.mealman.Service_handler.Constant;
import in.co.mealman.mealman.Service_handler.ServiceHandler;


/**
 * A simple {@link Fragment} subclass.
 */
public class Cart extends Fragment implements View.OnClickListener {
    ImageView rbackbutton;
    TextView nmanageweekly;
    int mininc = 0;
    private ProgressDialog pDialog;
    String userID, Strng_total_amount, status, error_code;
    ListView list;
    Button Btn_Checkout;
    int sum = 0;
    ArrayList<HashMap<String, String>> List_Subscription = new ArrayList<HashMap<String, String>>();

    public Cart() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.cart, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences shared = getActivity().getSharedPreferences("MyPrefs", getActivity().MODE_PRIVATE);
        userID = (shared.getString("userid", ""));

        initialize(view);
        listner();
        if (isNetworkAvailable()) {
            new Cart_Packages().execute();
        } else {
            Toast.makeText(getActivity(), "No Network Available", Toast.LENGTH_LONG).show();
        }
    }

    private void listner() {
        rbackbutton.setOnClickListener(this);
    }

    private void setdata() {

    }


    private void initialize(View view) {
        list = (ListView) view.findViewById(R.id.mylist);
        rbackbutton = (ImageView) view.findViewById(R.id.rbackbutton);
        Btn_Checkout = (Button) view.findViewById(R.id.checkout);
        Btn_Checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int count = 0; count < List_Subscription.size(); count++) {
                    String price = List_Subscription.get(count).get("amount");

                    Double value = 0.0;
                    int integer_amount = 0;
                    try {
                        integer_amount = Integer.parseInt(price);
                    } catch (java.lang.NumberFormatException e) {
                        e.printStackTrace();
                        value = Double.parseDouble(price);
                        integer_amount = value.intValue();
                    }

                    sum += integer_amount;


                }
                Strng_total_amount = String.valueOf(sum);
                onStartTransaction();
            }
        });

    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(getActivity(), Navigationbar.class);
        startActivity(i);
        getActivity().finish();
    }

    private class Cart_Packages extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {
        String android_id;

        JSONObject jsonnode, json_User;

        String str = "nostatus";
        String Name, access_tocken, Ostype;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // mProgressHUD = ProgressHUD.show(Login_Screen.this, "Connecting", true, true, this);
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
            List_Subscription.clear();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            publishProgress("Please wait...");


            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            //nameValuePairs.add(new BasicNameValuePair("email", email_fb));
            nameValuePairs.add(new BasicNameValuePair("userID", userID));


            String jsonStr = sh.makeServiceCall(Constant.ORDER_CART,
                    ServiceHandler.GET, nameValuePairs);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                //                    JSONObject jsonObj = null;
//                    try {
//                        jsonObj = new JSONObject(jsonStr);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }

                // Getting JSON Array node
                // JSONArray array1 = null;
                JSONArray jArr = null;
                try {
                    jArr = new JSONArray(jsonStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for (int count = 0; count < jArr.length(); count++) {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = jArr.getJSONObject(count);
                        // String cartID=jsonObj.getString("cartID");
                        //String userID=jsonObj.getString("userID");
                        //String days=jsonObj.getString("days");
                        String quantity = jsonObj.getString("quantity");
                        String cart_id = jsonObj.getString("cartID");
                        Strng_total_amount = jsonObj.getString("amount");
                        //String startDate = jsonObj.getString("startDate");
                        //String creationTime=jsonObj.getString("creationTime");
                        JSONObject pakage = jsonObj.getJSONObject("subscriptionPackage");
                        String str_tittle = pakage.getString("title");
                        String subscriptionPackageID = pakage.getString("subscriptionPackageID");

                        //String start_date = pakage.getString("startDate");
                        String amount = pakage.getString("amount");
                        //String type = jsonObj.getString("type");
                        String position = pakage.getString("position");

                        HashMap<String, String> Explorer = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        //Explorer.put("cartID",cartID);
                        //Explorer.put("userID",userID);
                        //Explorer.put("days",days);
                        Explorer.put("quantity", quantity);
                        Explorer.put("str_tittle", str_tittle);
                        Explorer.put("subscriptionPackageID", subscriptionPackageID);
                        Explorer.put("Cart_id", cart_id);
                        //Explorer.put("startDate", start_date);
                        //Explorer.put("creationTime",creationTime);
                        Explorer.put("amount", Strng_total_amount);
                        //Explorer.put("type", type);
                        Explorer.put("position", position);


                        // adding contact to contact list
                        List_Subscription.add(Explorer);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            pDialog.dismiss();
            Cart_adapter adapter = new Cart_adapter(getActivity(),
                    List_Subscription
            );
            list.setAdapter(adapter);


        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public void onStartTransaction() {
        PaytmPGService Service = PaytmPGService.getStagingService();
        Map<String, String> paramMap = new HashMap<String, String>();

        // these are mandatory parameters
        Random r = new Random(System.currentTimeMillis());
        int milisec = Calendar.MILLISECOND;
        String Sttr_milisec = String.valueOf(milisec);
        String orderId = "ORDER" + (1 + r.nextInt(2)) * 10000
                + r.nextInt(10000) + Sttr_milisec;

        paramMap.put("ORDER_ID", orderId);
        paramMap.put("MID", "WorldP64425807474247");
        paramMap.put("CUST_ID", "CUST828509098");
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
        paramMap.put("WEBSITE", "worldpressplg");
        paramMap.put("TXN_AMOUNT", Strng_total_amount);
        paramMap.put("THEME", "merchant");
        paramMap.put("EMAIL", "abc@gmail.com");
        paramMap.put("MOBILE_NO", "123");
        PaytmOrder Order = new PaytmOrder(paramMap);
        //PaytmMerchant Merchant = new PaytmMerchant( “http://cos.panchawaticredit.com/ChecksumVB/GenerateChecksum.aspx”, ” http://cos.panchawaticredit.com/ChecksumVB/VerifyChecksum.aspx“);
        PaytmMerchant Merchant = new PaytmMerchant(
                "https://pguat.paytm.com/paytmchecksum/paytmCheckSumGenerator.jsp",
                "https://pguat.paytm.com/paytmchecksum/paytmCheckSumVerify.jsp");

        Service.initialize(Order, Merchant, null);

        Service.startPaymentTransaction(getActivity(), true, true,
                new PaytmPaymentTransactionCallback() {
                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {
                        // Some UI Error Occurred in Payment Gateway Activity.
                        // // This may be due to initialization of views in
                        // Payment Gateway Activity or may be due to //
                        // initialization of webview. // Error Message details
                        // the error occurred.
                    }

                    @Override
                    public void onTransactionSuccess(Bundle inResponse) {
                        // After successful transaction this method gets called.
                        // // Response bundle contains the merchant response
                        // parameters.
                        //    inResponse.
                        Log.d("LOG", "Payment Transaction is successful " + inResponse);
                        Toast.makeText(getActivity(), "Payment Transaction is successful ", Toast.LENGTH_LONG).show();
                        new UPDATE_ORDER().execute();
                        //  new subscription_pack.Sbscribe_data().execute();
                    }

                    @Override
                    public void onTransactionFailure(String inErrorMessage,
                                                     Bundle inResponse) {
                        // This method gets called if transaction failed. //
                        // Here in this case transaction is completed, but with
                        // a failure. // Error Message describes the reason for
                        // failure. // Response bundle contains the merchant
                        // response parameters.
                        Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
                        Toast.makeText(getActivity(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
                        sum = 0;
                    }

                    @Override
                    public void networkNotAvailable() { // If network is not
                        // available, then this
                        // method gets called.
                        sum = 0;
                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {
                        // This method gets called if client authentication
                        // failed. // Failure may be due to following reasons //
                        // 1. Server error or downtime. // 2. Server unable to
                        // generate checksum or checksum response is not in
                        // proper format. // 3. Server failed to authenticate
                        // that client. That is value of payt_STATUS is 2. //
                        // Error Message describes the reason for failure.
                        sum = 0;
                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode,
                                                      String inErrorMessage, String inFailingUrl) {

                    }

                    // had to be added: NOTE
                    @Override
                    public void onBackPressedCancelTransaction() {
                        // TODO Auto-generated method stub
                        sum = 0;
                    }

                });
    }

    private class UPDATE_ORDER extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {


        JSONObject jsonnode, json_User;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // mProgressHUD = ProgressHUD.show(Login_Screen.this, "Connecting", true, true, this);
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            publishProgress("Please wait...");
//totalAmount - double
            //paymentID - character

            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
            //nameValuePairs.add(new BasicNameValuePair("email", email_fb));
            nameValuePairs.add(new BasicNameValuePair("userID", userID));
            nameValuePairs.add(new BasicNameValuePair("totalAmount", Strng_total_amount));
            nameValuePairs.add(new BasicNameValuePair("paymentID", "65675776fgfg"));

            //nameValuePairs.add(new BasicNameValuePair("paymentID", amountGlobal));

            String jsonStr = sh.makeServiceCall(Constant.UPDATE_ORDER,
                    ServiceHandler.POST, nameValuePairs);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(jsonStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Getting JSON Array node
                // JSONArray array1 = null;
                try {
                    status = jsonObj.getString("status");
                    error_code = jsonObj.getString("errorCode");

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            pDialog.dismiss();

            if (status.contentEquals("success")) {
                //dialog.cancel();
                Toast.makeText(getActivity(), "We recieved your order", Toast.LENGTH_LONG).show();
                //sum = 0;
                Intent i1 = new Intent(getActivity(), Navigationbar.class);
                getActivity().startActivity(i1);

            } else {
                Toast.makeText(getActivity(), error_code, Toast.LENGTH_LONG).show();
            }


        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }
}

