package in.co.mealman.mealman;


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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import in.co.mealman.mealman.Service_handler.Constant;
import in.co.mealman.mealman.Service_handler.ServiceHandler;


/**
 * A simple {@link Fragment} subclass.
 */
public class Saysomeabtus extends Fragment {

EditText edttxt_comment;
    Button Btn_submit;
    ImageView rbackbutton;
    private ProgressDialog pDialog;
    String userID,Comment,status;
    public Saysomeabtus() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edttxt_comment = (EditText)view.findViewById(R.id.cussayabt);
        Btn_submit=(Button)view.findViewById(R.id.custoer_say_button);
        SharedPreferences shared = getActivity().getSharedPreferences("MyPrefs", getActivity().MODE_PRIVATE);
        userID = (shared.getString("userid", ""));
        rbackbutton=(ImageView)view.findViewById(R.id.rbackbutton);
        rbackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Wsays say = new Wsays();
                fragmentTransaction.replace(R.id.Mainslider, say, "l");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        Btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edttxt_comment.getText().toString().trim().equals("")) {
                    edttxt_comment.setError("Say something..");
                }else if(edttxt_comment.getText().toString().trim().length()<25) {
                        edttxt_comment.setError("Minimum 25 words required");
                }else{
                    Comment = edttxt_comment.getText().toString();
                    if(isNetworkAvailable()) {
                        new Add_testmonial().execute();
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"No Network Available",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.saysomeabtus, container, false);
    }
    private class Add_testmonial extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {


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


            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            //nameValuePairs.add(new BasicNameValuePair("email", email_fb));
            nameValuePairs.add(new BasicNameValuePair("userID", userID));


            nameValuePairs.add(new BasicNameValuePair("comment", Comment));

            String jsonStr = sh.makeServiceCall(Constant.ADD_TESTIMONIAL,
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
                Toast.makeText(getActivity(), "Post successfully", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getActivity(), Navigationbar.class);
                i.putExtra("userid", userID);
                startActivity(i);

            } else {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
            }


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
