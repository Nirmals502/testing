package in.co.mealman.mealman;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.DialogFragment;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.co.mealman.mealman.Service_handler.Constant;
import in.co.mealman.mealman.Service_handler.ServiceHandler;

/**
 * A simple {@link Fragment} subclass.
 */
public class Notification extends Fragment implements View.OnClickListener {
ImageView rbackbutton;

    public Notification() {
        // Required empty public constructor
    }

    TextView txtDateEntered;
    private ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.notification, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize(view);
        setListner();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mngdate:
            {
                //DialogFragment newFragment = new SelectDateFragment();
                //newFragment.show(getFragmentManager(), "DatePicker");
                break;
            }
            case R.id.rbackbutton:
            {
                Intent i=new Intent(getActivity(),Navigationbar.class);
                startActivity(i);
                getActivity().finish();
            }
        }
    }


    private void setListner() {
        txtDateEntered.setOnClickListener(this);
        rbackbutton.setOnClickListener(this);
    }

    private void initialize(View view) {
        txtDateEntered = (TextView) view.findViewById(R.id.mngdate);
        rbackbutton=(ImageView)view.findViewById(R.id.rbackbutton);


    }


    private class GET_ORDERLIST extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {
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

            String jsonStr = sh.makeServiceCall(Constant.GET_ORDERLIST,
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
                        //List_Subscription.add(Explorer);
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
//            Subscription_adapter adapter = new Subscription_adapter(getActivity(),
//                    List_Subscription);
//            list.setAdapter(adapter);
        }
        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }
}
