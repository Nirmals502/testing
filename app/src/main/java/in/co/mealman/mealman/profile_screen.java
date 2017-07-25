package in.co.mealman.mealman;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
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
 * Activities that contain this fragment must implement the
 * {@link profile_screen.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link profile_screen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profile_screen extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ProgressDialog pDialog;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2,STR_FNAME,str_lastname,Str_phno,Stradress,Str_Email_id,Pic;
    String userid;
    private OnFragmentInteractionListener mListener;
    TextView Txt_fname,Txt_lastname,Txt_ph,Txt_adress,Txt_emailid,Txt_edit_profile;
    ImageView img_profile_pic,rbackbutton;

    public profile_screen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profile_screen.
     */
    // TODO: Rename and change types and number of parameters
    public static profile_screen newInstance(String param1, String param2) {
        profile_screen fragment = new profile_screen();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);


        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.user_profile, container, false);



    }




    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences shared = getActivity().getSharedPreferences("MyPrefs", getActivity().MODE_PRIVATE);
        userid = (shared.getString("userid", ""));
        Txt_fname = (TextView) view.findViewById(R.id.Txt_name);
        Txt_ph = (TextView) view.findViewById(R.id.textView3);
        Txt_emailid = (TextView) view.findViewById(R.id.textView10);
        Txt_adress = (TextView) view.findViewById(R.id.textView9);
        Txt_edit_profile = (TextView)view.findViewById(R.id.textView11);
        img_profile_pic = (ImageView)view.findViewById(R.id.profile_image);
        rbackbutton=(ImageView)view.findViewById(R.id.rbackbutton);
        rbackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(),Navigationbar.class);
                startActivity(i);
                getActivity().finish();
            }
        });
        if(isNetworkAvailable()) {
            new User_profile().execute();
        }
        else
        {
            Toast.makeText(getActivity(),"Internet Not Available",Toast.LENGTH_LONG).show();
        }
        Txt_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(getActivity(),User_profile_edit_screen.class);
                getActivity().startActivity(i1);
                getActivity().finish();
            }
        });

    }



    @Override
    public void onClick(View v) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private class User_profile extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {


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
            nameValuePairs.add(new BasicNameValuePair("userID", userid));

            String jsonStr = sh.makeServiceCall(Constant.USER_PROFILE,
                    ServiceHandler.GET, nameValuePairs);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(jsonStr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Getting JSON Array node
                    // JSONArray array1 = null;
                    STR_FNAME = jsonObj.getString("fName");
                    str_lastname = jsonObj.getString("lName");
                    Str_phno = jsonObj.getString("mPhone");
                    Stradress = jsonObj.getString("address");
                    Str_Email_id = jsonObj.getString("emailID");
                    Pic =  jsonObj.getString("pic");
//                    if (str.contentEquals("true")) {
//                        jsonnode = jsonObj.getJSONObject("data");
//                        json_User = jsonnode.getJSONObject("user");
//
//                        // looping through All data
//                    }
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
            Txt_fname.setText(STR_FNAME+" "+str_lastname);
            Txt_ph.setText(Str_phno);
            Txt_emailid.setText(Str_Email_id);
            Txt_adress.setText(Stradress);
            Picasso.with(getActivity())
                    .load(Pic)
                    .placeholder(R.drawable.profile_pic)   // optional
                    // optional
                    .resize(400, 400).centerCrop().skipMemoryCache()                        // optional
                    .into(img_profile_pic);



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
