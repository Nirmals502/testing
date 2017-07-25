package in.co.mealman.mealman;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
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
import in.co.mealman.mealman.Service_handler.Constant;
import in.co.mealman.mealman.Service_handler.ServiceHandler;


/**
 * A simple {@link Fragment} subclass.
 */
public class subscription_pack extends Fragment implements View.OnClickListener {
    private ProgressDialog pDialog;
    ImageView rbackbutton;
    ListView list;
    ArrayList<HashMap<String, String>> List_Subscription = new ArrayList<HashMap<String, String>>();
    String userID, SUBSCRIPTION_PAKAGEID;
    String amountGlobal, String_static_amount;
    String Tittle;
    String Description;
    String typee;
    TextView date;
    int int_amountd=7;
    String dayss="1";
    String formattedDate="no_date";
    Dialog dialog = null;
    String status, error_code;
    int int_amount = 1;
    String quantiity = "1";


    public subscription_pack() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.subscription_package, container, false);
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
        new GET_SUBSRIPTION_DATA().execute();
    }

    private void setListner() {
        rbackbutton.setOnClickListener(this);
        {

        }
    }

    private void initialize(View view) {

        rbackbutton = (ImageView) view.findViewById(R.id.rbackbutton);
        list = (ListView) view.findViewById(R.id.mylist);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Description=List_Subscription.get(position).get("description");
                amountGlobal = List_Subscription.get(position).get("amount");
                typee = List_Subscription.get(position).get("type");
                String_static_amount = List_Subscription.get(position).get("amount");
                Tittle = List_Subscription.get(position).get("str_tittle");
                SUBSCRIPTION_PAKAGEID = List_Subscription.get(position).get("subscriptionPackageID");
                Dialog(amountGlobal, Tittle, Description);

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rbackbutton:
                Intent i = new Intent(getActivity(), Navigationbar.class);
                startActivity(i);
                getActivity().finish();
                break;

        }
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
            nameValuePairs.add(new BasicNameValuePair("type", "subscription"));
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
            Subscription_adapter adapter = new Subscription_adapter(getActivity(),
                    List_Subscription
            );
            list.setAdapter(adapter);


        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }
    private void Dialog(final String amount, String tittle, final String Description) {
        String str=Description;

        String result = str.replaceAll("save ","");
        String Discount = result.replaceAll("%","");
        final int intDicount = Integer.parseInt(Discount);

       // System.out.println("....."+result2);



        System.out.println(result);
        dialog = new Dialog(getActivity(), R.style.DialoueBox);
        dialog.setContentView(R.layout.subscribe_addto_cart);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        Button otpconfirm = (Button) dialog.findViewById(R.id.btn_confirm);
        ImageView img_close = (ImageView) dialog.findViewById(R.id.imgcross);
        TextView Txt_tittle = (TextView) dialog.findViewById(R.id.txtvw_tittle);
        final TextView Txt_vw_amount = (TextView) dialog.findViewById(R.id.textView4);
        ImageView increment = (ImageView) dialog.findViewById(R.id.nbinc1);
        ImageView decrement = (ImageView) dialog.findViewById(R.id.nbdec1);
        final TextView txt_amount = (TextView) dialog.findViewById(R.id.nbincdec);
        final TextView txt_amount1=(TextView)dialog.findViewById(R.id.nbincdec1);
        ImageView incre=(ImageView)dialog.findViewById(R.id.nbinc2);
        ImageView decre=(ImageView)dialog.findViewById(R.id.nbdec2);
        date=(TextView)dialog.findViewById(R.id.date);
        ImageView calender=(ImageView)dialog.findViewById(R.id.calender);
        Txt_tittle.setText(tittle);
        Double value = 0.0;
        int integer_amount=0;
        try {
             integer_amount = Integer.parseInt(String_static_amount);
        } catch (java.lang.NumberFormatException e) {
            e.printStackTrace();
            value =  Double.parseDouble(String_static_amount);
            integer_amount = value.intValue();
        }

        int global_amount = integer_amount*1*7;
        int integer_discount=(global_amount * intDicount)/100;
        int integer_total=global_amount - integer_discount;
        String int_to_string = String.valueOf(integer_total);

        Txt_vw_amount.setText("Rs." + int_to_string);
        amountGlobal = int_to_string;

        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (int_amount != 9) {
                    int_amount++;
                    int integer_amount = 0;
                    int integer_discount=intDicount;
                    Double value = 0.0;
                    try {
                        integer_amount = Integer.parseInt(String_static_amount);
                    } catch (java.lang.NumberFormatException e) {
                        e.printStackTrace();
                        value =  Double.parseDouble(String_static_amount);
                        integer_amount = value.intValue();
                    }
                    integer_amount = integer_amount * int_amount * int_amountd;
                    integer_discount=(integer_amount * integer_discount)/100;
                    int integer_total=integer_amount - integer_discount;
                    String value_after_multiply = String.valueOf(integer_total);
                    String int_to_string = String.valueOf(int_amount);
                    txt_amount.setText(int_to_string);
                    Txt_vw_amount.setText("Rs." + value_after_multiply);
                    amountGlobal = value_after_multiply;
                    quantiity = int_to_string;

                }

            }
        });
        incre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (int_amountd != 60) {
                    int_amountd++;
                    int integer_amount = 0;
                    int integer_discount=intDicount;
                    int integer_total=0;
                    Double value = 0.0;

                    try {
                       // integer_discount=Integer.parseInt(Description);
                        integer_amount = Integer.parseInt(String_static_amount);
                    } catch (java.lang.NumberFormatException e) {
                        e.printStackTrace();
                       value =  Double.parseDouble(String_static_amount);
                        integer_amount = value.intValue();

                    }

                    integer_amount = integer_amount * int_amount * int_amountd;
                    integer_discount=(integer_amount * integer_discount)/100;
                    integer_total=integer_amount - integer_discount;
                    String value_after_multiply = String.valueOf(integer_total);
                    String int_to_string = String.valueOf(int_amountd);
                    txt_amount1.setText(int_to_string);
                    Txt_vw_amount.setText("Rs." + value_after_multiply);
                    amountGlobal = value_after_multiply;
                    dayss=int_to_string;
                }
            }
        });
        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (int_amount != 1) {
                    int_amount--;
                    int integer_amount = 0;
                    int integer_discount=intDicount;
                    Double value = 0.0;
                    try {
                        integer_amount = Integer.parseInt(String_static_amount);
                    } catch (java.lang.NumberFormatException e) {
                        e.printStackTrace();
                        value =  Double.parseDouble(String_static_amount);
                        integer_amount = value.intValue();
                    }
                    integer_amount = integer_amount * int_amount * int_amountd;
                    integer_discount=(integer_amount * integer_discount)/100;
                    int integer_total=integer_amount - integer_discount;
                    String value_after_multiply = String.valueOf(integer_total);
                    String int_to_string = String.valueOf(int_amount);
                    txt_amount.setText(int_to_string);
                    Txt_vw_amount.setText("Rs." + value_after_multiply);
                    amountGlobal = value_after_multiply;
                    quantiity = int_to_string;

                }
            }
        });
        decre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (int_amountd != 7) {
                    int_amountd--;
                    int integer_amount = 0;
                    int integer_discount=intDicount;
                    Double value = 0.0;
                    try {
                        integer_amount = Integer.parseInt(String_static_amount);
                    } catch (java.lang.NumberFormatException e) {
                        e.printStackTrace();
                        value =  Double.parseDouble(String_static_amount);
                        integer_amount = value.intValue();
                    }
                    integer_amount = integer_amount * int_amount * int_amountd;
                    integer_discount=(integer_amount * integer_discount)/100;
                    int integer_total=integer_amount - integer_discount;
                    String value_after_multiply = String.valueOf(integer_total);
                    String int_to_string = String.valueOf(int_amountd);
                    txt_amount1.setText(int_to_string);
                    Txt_vw_amount.setText("Rs." + value_after_multiply);
                    amountGlobal = value_after_multiply;
                    dayss=int_to_string;
                }
            }
        });
      //  Txt_vw_amount.setText("Rs." + amount);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int_amount = 1;
                int_amountd=7;
                dialog.cancel();
            }
        });
        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });
        otpconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(formattedDate.contentEquals("no_date")){
                    Toast.makeText(getActivity(),"Select date to start package",Toast.LENGTH_LONG).show();
                }else{
                    new Sbscribe_data().execute();
                }
                // onStartTransaction();

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

           /* Calendar c = Calendar.getInstance();
            System.out.println("Current time => " + c.getTime());

            //SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MMM-dd");
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            int year = c.get(Calendar.YEAR);
            String strmonth = String.valueOf(month + 1);
            String strday = String.valueOf(day);
            String stryear = String.valueOf(year);
            String formattedDate = stryear + "-" + strmonth + "-" + strday;*/
//            try {
//                long longuserid = Long.parseLong(userID);
//                long longsubscriptionid = Long.parseLong(SUBSCRIPTION_PAKAGEID);
//                long longquantity= Long.parseLong("1");
//
//            } catch (NumberFormatException nfe) {
//                System.out.println("NumberFormatException: " + nfe.getMessage());
//            }
            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
            //nameValuePairs.add(new BasicNameValuePair("email", email_fb));
            nameValuePairs.add(new BasicNameValuePair("userID", userID));
            nameValuePairs.add(new BasicNameValuePair("subscriptionPackageID", SUBSCRIPTION_PAKAGEID));
            nameValuePairs.add(new BasicNameValuePair("startDate", formattedDate));
            nameValuePairs.add(new BasicNameValuePair("quantity", quantiity));
            nameValuePairs.add(new BasicNameValuePair("type", typee));
            nameValuePairs.add(new BasicNameValuePair("days",dayss));
            nameValuePairs.add(new BasicNameValuePair("amount",amountGlobal));
            //nameValuePairs.add(new BasicNameValuePair("paymentID", amountGlobal));

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
            dialog.cancel();
            if (status.contentEquals("success")) {
                //dialog.cancel();
                Toast.makeText(getActivity(), "Your Subscription Package is Add to Cart", Toast.LENGTH_LONG).show();

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
                + r.nextInt(10000) + Sttr_milisec;

        paramMap.put("ORDER_ID", orderId);
        paramMap.put("MID", "WorldP64425807474247");
        paramMap.put("CUST_ID", "CUST828509098");
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
        paramMap.put("WEBSITE", "worldpressplg");
        paramMap.put("TXN_AMOUNT", amountGlobal);
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
    public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        //TextView mngdate;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            // calendar.setTimeInMillis(System.currentTimeMillis() - 1000);
           System.out.println("Current time => " + calendar.getTime());
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dpd=new DatePickerDialog(getActivity(),this,yy,mm,dd);
            calendar.add(calendar.DATE,7);
            dpd.getDatePicker().setMaxDate(calendar.getTimeInMillis());
            calendar.add(calendar.DATE,-7);
            dpd.getDatePicker().setMinDate(calendar.getTimeInMillis());
            //return new DatePickerDialog(getActivity(), this, yy, mm, dd);
            return dpd;
        }
        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            showDate(yy,mm+1,dd);
        }
        private void showDate(int yy, int mm, int dd) {
            //((TextView) getActivity().findViewById(R.id.mngdate)).setText(dd+ "/" + mm + "/" + yy);
            //((TextView) getActivity().findViewById(R.id.date)).setText(dd+ "/" + mm + "/" + yy);
            String Str_year = String.valueOf(yy);
            String Str_month = String.valueOf(mm);
            String Str_day = String.valueOf(dd);
            date.setText(Str_day+ "/" + Str_month + "/" +Str_year);
            formattedDate = Str_year + "-" + Str_month + "-" + Str_day;
        }
    }
}


