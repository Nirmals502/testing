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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.History_adapter;
import adapter.Subscription_adapter;
import in.co.mealman.mealman.Service_handler.Constant;
import in.co.mealman.mealman.Service_handler.ServiceHandler;


/**
 * A simple {@link Fragment} subclass.
 */
public class Order_history extends Fragment implements View.OnClickListener{
    ImageView rbackbutton;
    TextView nmanageweekly;
    int mininc = 0;
    private ProgressDialog pDialog;
    String userID;
    ListView list;
    ArrayList<HashMap<String, String>> List_Subscription = new ArrayList<HashMap<String, String>>();

    public Order_history() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the git push -u origin masterlayout for this fragment
        return inflater.inflate(R.layout.order_history, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences shared = getActivity().getSharedPreferences("MyPrefs", getActivity().MODE_PRIVATE);
        userID = (shared.getString("userid", ""));

        initialize(view);
        listner();
        if(isNetworkAvailable()) {
            new ORDER_HISTORY().execute();
        }
        else
        {
            Toast.makeText(getActivity(),"No Network Available",Toast.LENGTH_LONG).show();
        }
    }

    private void listner() {
        rbackbutton.setOnClickListener(this);
    }

    private void setdata() {

    }


    private void initialize(View view) {
        list = (ListView) view.findViewById(R.id.mylist);
        rbackbutton=(ImageView)view.findViewById(R.id.rbackbutton);


    }

    @Override
    public void onClick(View v) {
        Intent i=new Intent(getActivity(),Navigationbar.class);
        startActivity(i);
        getActivity().finish();
    }

    private class ORDER_HISTORY extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {
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


            String jsonStr = sh.makeServiceCall(Constant.ORDER_HISTORY,
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
                        String quantity = jsonObj.getString("quantity");
                        String start_date = jsonObj.getString("startDate");
                        JSONObject pakage = jsonObj.getJSONObject("subscriptionPackage");
                        String str_tittle = pakage.getString("title");
                        String subscriptionPackageID = pakage.getString("subscriptionPackageID");
                        String description = pakage.getString("description");
                        String amount = pakage.getString("amount");
                        String type = pakage.getString("type");
                        String position = pakage.getString("position");
                        HashMap<String, String> Explorer = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        Explorer.put("quantity",quantity);
                        Explorer.put("str_tittle", str_tittle);
                        Explorer.put("subscriptionPackageID", subscriptionPackageID);
                        Explorer.put("description", start_date);
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
            History_adapter adapter = new History_adapter(getActivity(),
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

}

