package in.co.mealman.mealman;


import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import adapter.Subscription_adapter;
import adapter.Trials_adapter;
import in.co.mealman.mealman.Service_handler.Constant;
import in.co.mealman.mealman.Service_handler.ServiceHandler;


/**
 * A simple {@link Fragment} subclass.
 */
public class weekly_packages extends Fragment implements View.OnClickListener {
    private ProgressDialog pDialog;
    ImageView reviewicon,rbackbutton;
    ListView list;
    ArrayList<HashMap<String, String>> List_Subscription = new ArrayList<HashMap<String, String>>();
    String userID, SUBSCRIPTION_PAKAGEID;
    String amount;
    String typee;
    String Tittle;
    Dialog dialog = null;
    String status,error_code;
    String dayss="1";
    public weekly_packages() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.wtrials, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize(view);
         setListner();
        SharedPreferences shared = getActivity().getSharedPreferences("MyPrefs", getActivity().MODE_PRIVATE);
        userID = (shared.getString("userid", ""));

        setdata();

    }

    private void setdata() {
        if(isNetworkAvailable()) {
            new GET_SUBSRIPTION_DATA().execute();
        }
        else
        {
            Toast.makeText(getActivity(),"No Internet Available",Toast.LENGTH_LONG).show();
        }
    }

    private void setListner() {
        //  reviewicon.setOnClickListener(this);
        rbackbutton.setOnClickListener(this);

    }

    private void initialize(View view) {
        // reviewicon=(ImageView) view.findViewById(R.id.reviewicon);
        list = (ListView) view.findViewById(R.id.weeklylist);
        rbackbutton=(ImageView)view.findViewById(R.id.rbackbutton);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                typee=List_Subscription.get(position).get("type");
                amount = List_Subscription.get(position).get("amount");
                Tittle = List_Subscription.get(position).get("str_tittle");
                SUBSCRIPTION_PAKAGEID = List_Subscription.get(position).get("subscriptionPackageID");
                Dialog(amount, Tittle);
            }
        });

    }

    @Override
    public void onClick(View v) {
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        Saysomeabtus sayus = new Saysomeabtus();
//        fragmentTransaction.replace(R.id.Mainslider, sayus, "A");
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
        Intent i=new Intent(getActivity(),Navigationbar.class);
        startActivity(i);
        getActivity().finish();
    }

    private class GET_SUBSRIPTION_DATA extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {
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


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            publishProgress("Please wait...");
            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            //nameValuePairs.add(new BasicNameValuePair("email", email_fb));
            nameValuePairs.add(new BasicNameValuePair("userID", userID));
            nameValuePairs.add(new BasicNameValuePair("type", "trial"));
            String jsonStr = sh.makeServiceCall(Constant.Get_Subscription,
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

                        String str_tittle = jsonObj.getString("title");
                        String subscriptionPackageID = jsonObj.getString("subscriptionPackageID");
                        String description = jsonObj.getString("description");
                        String amount = jsonObj.getString("amount");
                        String type = jsonObj.getString("type");
                        String position = jsonObj.getString("position");
                        HashMap<String, String> Explorer = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        Explorer.put("str_tittle", str_tittle);
                        Explorer.put("subscriptionPackageID", subscriptionPackageID);
                        Explorer.put("description", description);
                        Explorer.put("amount", amount);
                        Explorer.put("type", type);
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
            Trials_adapter adapter = new Trials_adapter(getActivity(),
                    List_Subscription
            );
            list.setAdapter(adapter);


        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }

    private void Dialog(String amount, String tittle) {
        // reviewicon=(ImageView) view.findViewById(R.id.reviewicon);
        dialog = new Dialog(getActivity(), R.style.DialoueBox);
        dialog.setContentView(R.layout.trial_addto_cart);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        Button otpconfirm = (Button) dialog.findViewById(R.id.btn_confirm);
        ImageView img_close = (ImageView) dialog.findViewById(R.id.imgcross);
        TextView Txt_tittle = (TextView) dialog.findViewById(R.id.txtvw_tittle);
        TextView Txt_vw_amount = (TextView) dialog.findViewById(R.id.textView4);
        Txt_tittle.setText(tittle);
        Txt_vw_amount.setText("Rs." + amount);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        otpconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onStartTransaction();
                new Sbscribe_data().execute();
            }
        });
    }

    private class Sbscribe_data extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {


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

            Calendar c = Calendar.getInstance();
            System.out.println("Current time => " + c.getTime());

            //SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MMM-dd");
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            int year = c.get(Calendar.YEAR);
            String strmonth = String.valueOf(month + 1);
            String strday = String.valueOf(day);
            String stryear = String.valueOf(year);
            String formattedDate = stryear + "-" + strmonth + "-" + strday;
//            try {
//                long longuserid = Long.parseLong(userID);
//                long longsubscriptionid = Long.parseLong(SUBSCRIPTION_PAKAGEID);
//                long longquantity= Long.parseLong("1");
//
//            } catch (NumberFormatException nfe) {
//                System.out.println("NumberFormatException: " + nfe.getMessage());
//            }
            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
            //nameValuePairs.add(new BasicNameValuePair("email", email_fb));
            nameValuePairs.add(new BasicNameValuePair("userID", userID));

            nameValuePairs.add(new BasicNameValuePair("subscriptionPackageID", SUBSCRIPTION_PAKAGEID));
            nameValuePairs.add(new BasicNameValuePair("startDate", formattedDate));
            nameValuePairs.add(new BasicNameValuePair("quantity", "1"));
            nameValuePairs.add(new BasicNameValuePair("type", typee));
            nameValuePairs.add(new BasicNameValuePair("days",dayss));
            nameValuePairs.add(new BasicNameValuePair("amount", amount));
            //nameValuePairs.add(new BasicNameValuePair("paymentID", amount));

            String jsonStr = sh.makeServiceCall(Constant.Add_Order,
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
                    error_code =jsonObj.getString("errorCode");

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
            dialog.cancel();
            if (status.contentEquals("success")) {
                //dialog.cancel();
                Toast.makeText(getActivity(), "Your Trial Pack is Add to Cart", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(getActivity(), error_code, Toast.LENGTH_LONG).show();
            }


        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }

    public String date_calculator(String month_name) {
        int month = 0;
        if (month_name.contentEquals("Jan")) {
            month = 1;
        } else if (month_name.contentEquals("Feb")) {
            month = 2;
        } else if (month_name.contentEquals("Mar")) {
            month = 3;
        } else if (month_name.contentEquals("Apr")) {
            month = 4;
        } else if (month_name.contentEquals("May")) {
            month = 5;
        } else if (month_name.contentEquals("Jun")) {
            month = 6;
        } else if (month_name.contentEquals("Jul")) {
            month = 7;
        } else if (month_name.contentEquals("Aug")) {
            month = 8;
        } else if (month_name.contentEquals("Sep")) {
            month = 9;
        } else if (month_name.contentEquals("Oct")) {
            month = 10;
        } else if (month_name.contentEquals("Nov")) {
            month = 11;
        } else if (month_name.contentEquals("Dec")) {
            month = 12;
        }
        String int_to_string = String.valueOf(month);
        return int_to_string;
    }

    public void onStartTransaction() {
        PaytmPGService Service = PaytmPGService.getStagingService();
        Map<String, String> paramMap = new HashMap<String, String>();

        // these are mandatory parameters
        Random r = new Random(System.currentTimeMillis());
        int milisec = Calendar.MILLISECOND;
        String Sttr_milisec = String.valueOf(milisec);
        String orderId = "ORDER" + (1 + r.nextInt(2)) * 10000
                + r.nextInt(10000)+Sttr_milisec;

        paramMap.put("ORDER_ID", orderId);
        paramMap.put("MID", "WorldP64425807474247");
        paramMap.put("CUST_ID", "CUST828509098");
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
        paramMap.put("WEBSITE", "worldpressplg");
        paramMap.put("TXN_AMOUNT", amount);
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
                        Log.d("LOG", "Payment Transaction is successful " + inResponse);
                        Toast.makeText(getActivity(), "Payment Transaction is successful ", Toast.LENGTH_LONG).show();
                        new Sbscribe_data().execute();
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
                    }

                    @Override
                    public void networkNotAvailable() { // If network is not
                        // available, then this
                        // method gets called.
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
                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode,
                                                      String inErrorMessage, String inFailingUrl) {

                    }

                    // had to be added: NOTE
                    @Override
                    public void onBackPressedCancelTransaction() {
                        // TODO Auto-generated method stub
                    }

                });
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
