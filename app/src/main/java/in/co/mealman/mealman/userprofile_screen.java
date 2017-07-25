package in.co.mealman.mealman;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.Subscription_adapter;
import in.co.mealman.mealman.Service_handler.Constant;
import in.co.mealman.mealman.Service_handler.ServiceHandler;


/**
 * A simple {@link Fragment} subclass.
 */
public class userprofile_screen extends Fragment implements View.OnClickListener{


    private ProgressDialog pDialog;
    ImageView reviewicon,rbackbutton;
    ListView list;
    ArrayList<HashMap<String, String>> List_Subscription = new ArrayList<HashMap<String, String>>();
    public userprofile_screen() {
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
        setdata();

    }

    private void setdata() {
        new GET_SUBSRIPTION_DATA().execute();
    }

    private void setListner()
    {
        //  reviewicon.setOnClickListener(this);

        rbackbutton.setOnClickListener(this);

    }

    private void initialize(View view) {
        // reviewicon=(ImageView) view.findViewById(R.id.reviewicon);
        list = (ListView) view.findViewById(R.id.weeklylist);
        rbackbutton=(ImageView)view.findViewById(R.id.rbackbutton);

    }

    @Override
    public void onClick(View v) {


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
            nameValuePairs.add(new BasicNameValuePair("type", "daily"));

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

}
