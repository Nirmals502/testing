package in.co.mealman.mealman;


import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import in.co.mealman.mealman.Service_handler.Constant;
import in.co.mealman.mealman.Service_handler.ServiceHandler;

/**
 * A simple {@link Fragment} subclass.
 */
public class Screen_03 extends Fragment implements View.OnClickListener {

    Context mContext;
    EditText rname, rmobileno, rpassword;
    Button joinbutton;
    ImageView backico;
    String mobilepattern = "[0-9]{10}";
    String otppattern = "[0-9]{10}";
    private String passwordpattern = "jgsfsdg";
    String name, mphone, password;
    long totalSize = 0;
    private ProgressBar progressBar;
    private ProgressDialog pDialog;
    String status, Error, userid, OTP;
    EditText blankno;
     TextView txt_timer;
     TextView txt_resend_otp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.screen_03, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize(view);
        setListner();
        setdata();
        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                Log.d("Text", messageText);
                //  Toast.makeText(getActivity(),"Messagemealman: "+messageText,Toast.LENGTH_LONG).show();
            }
        });

    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("any2", "any2");
    }


    private void initialize(View view) {

        rname = (EditText) view.findViewById(R.id.rname);
        rmobileno = (EditText) view.findViewById(R.id.rmobileno);
        rpassword = (EditText) view.findViewById(R.id.rpassword);
        joinbutton = (Button) view.findViewById(R.id.joinbutton);
        backico = (ImageView) view.findViewById(R.id.rbackbutton);
        // progressBar = (ProgressBar) view.findViewById(R.id.progressBar);


    }

    private void setListner() {

        joinbutton.setOnClickListener(this);
        backico.setOnClickListener(this);
    }

    private void setdata() {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.rbackbutton:
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Screen_02 login = new Screen_02();
                fragmentTransaction.replace(R.id.MainAct, login, "l");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            case R.id.joinbutton:
                if (rname.getText().toString().trim().equals("")) {
                    rname.setError(getString(R.string.error_name_required));
                } else if (rmobileno.getText().toString().trim().equals("")) {
                    rmobileno.setError(getString(R.string.error_mobileno_required));
                } else if (!rmobileno.getText().toString().trim().matches(mobilepattern)) {
                    rmobileno.setError(getString(R.string.error_invalid_mobileno));
                } else if (rpassword.getText().toString().trim().equals("")) {
                    rpassword.setError(getString(R.string.error_password_required));
                } else {
                    if (rpassword.getText().toString().trim().length() < 6) {
                        rpassword.setError(getString(R.string.error_invalid_password));
                    } else {
                        name = rname.getText().toString();
                        mphone = rmobileno.getText().toString();
                        password = rpassword.getText().toString();
//                        String method = "join";
//                        BackgroundTask backgroundTask = new BackgroundTask(this.getActivity());
//                        backgroundTask.execute(method, name, mphone, password);

                        if(isNetworkAvailable()) {
                            new Join().execute();
                        }
                        else
                        {
                            Toast.makeText(getActivity(),"Internet Not Available",Toast.LENGTH_LONG).show();
                        }
                        // getActivity().getFragmentManager().beginTransaction().remove(this).commit();


                        break;
                    }
                }
        }


    }


    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void onDestroyView()


    {
        super.onDestroyView();

    }

    private class Join extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {


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
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            //nameValuePairs.add(new BasicNameValuePair("email", email_fb));
            nameValuePairs.add(new BasicNameValuePair("name", name));

            nameValuePairs.add(new BasicNameValuePair("mphone", mphone));
            nameValuePairs.add(new BasicNameValuePair("password", password));

            String jsonStr = sh.makeServiceCall(Constant.join,
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
                    Error = jsonObj.getString("errorCode");
                    userid = jsonObj.getString("userID");
                    OTP = jsonObj.getString("otp");
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

                final Dialog dialog = new Dialog(getActivity(), R.style.DialoueBox);
                dialog.setContentView(R.layout.dialogue_for_otp);
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
                Button otpconfirm = (Button) dialog.findViewById(R.id.confirmbutton);
                txt_timer = (TextView) dialog.findViewById(R.id.validotptext);
                txt_resend_otp = (TextView) dialog.findViewById(R.id.resendotptext);
                blankno = (EditText) dialog.findViewById(R.id.otpedittext);
                txt_resend_otp.setVisibility(View.GONE);
                new CountDownTimer(240000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        //long seconds = millisUntilFinished / 1000;

                        //long minutes = seconds / 60;
                        // txt_timer.setText("Time remaining: " + minutes+":"+seconds);
                        txt_timer.setText(("Time remaining: " + millisUntilFinished / 60000) + ":" + (millisUntilFinished % 60000 / 1000));
                        //here you can have your logic to set text to edittext
                    }

                    public void onFinish() {
                        txt_timer.setText("Your otp has expired!");
                        txt_resend_otp.setVisibility(View.VISIBLE);
                        Animation anm = Shake_Animation();
//                        Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
//                        // Vibrate for 500 milliseconds
//                        v.vibrate(500);
                        txt_resend_otp.startAnimation(anm);
                        //blankno.d
                        blankno.setEnabled(false);
//                        blankno.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Animation anm = Shake_Animation();
//
//                                txt_resend_otp.startAnimation(anm);
//                            }
//                        });
                        txt_resend_otp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(isNetworkAvailable()) {
                                    new resend_otp().execute();
                                }
                                else
                                {
                                    Toast.makeText(getActivity(),"Internet Not Availbale",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        //dialog.cancel();
                    }

                }.start();

                otpconfirm.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        if (blankno.getText().toString().trim().equals("")) {
                            blankno.setError(getString(R.string.error_otp_required2));
                        } else if (blankno.getText().toString().trim().equals(OTP)) {
                            dialog.dismiss();
                            SharedPreferences sharedpreferences = getActivity().getSharedPreferences("MyPrefss", getActivity().MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();

                            editor.putString("useridd", userid);
                            editor.commit();
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            Screen_05 upload = new Screen_05();
                            fragmentTransaction.replace(R.id.MainAct, upload, "C");
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        } else {
                            Toast.makeText(getActivity(), "Invalid otp", Toast.LENGTH_LONG).show();
                        }

                    }
                });


            } else {
                Toast.makeText(getActivity(), Error, Toast.LENGTH_LONG).show();
            }


        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }

    //    public void recivedSms(String message)
//    {
//        try
//        {
//            blankno.setText(message);
//           // dialog.dismiss();
//            FragmentManager fragmentManager = getFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            Screen_05 upload = new Screen_05();
//            fragmentTransaction.replace(R.id.MainAct, upload, "C");
//            fragmentTransaction.addToBackStack(null);
//            fragmentTransaction.commit();
//        }
//        catch (Exception e) {}
//
//    }
    public Animation Shake_Animation() {
        Animation shake = new TranslateAnimation(0, 5, 0, 0);
        shake.setInterpolator(new CycleInterpolator(5));
        shake.setDuration(300);


        return shake;
    }
    private class resend_otp extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {


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
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            //nameValuePairs.add(new BasicNameValuePair("email", email_fb));
            nameValuePairs.add(new BasicNameValuePair("userID", userid));

            String jsonStr = sh.makeServiceCall(Constant.RESEND_OTP,
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
                    Error = jsonObj.getString("errorCode");
                    userid = jsonObj.getString("userID");
                    OTP = jsonObj.getString("otp");
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
            blankno.setEnabled(true);
            txt_resend_otp.setVisibility(View.GONE);
            new CountDownTimer(240000, 1000) {

                public void onTick(long millisUntilFinished) {
                    //long seconds = millisUntilFinished / 1000;

                    //long minutes = seconds / 60;
                    // txt_timer.setText("Time remaining: " + minutes+":"+seconds);
                    txt_timer.setText(("Time remaining: " + millisUntilFinished / 60000) + ":" + (millisUntilFinished % 60000 / 1000));
                    //here you can have your logic to set text to edittext
                }

                public void onFinish() {
                    txt_timer.setText("Your otp has expired!");
                    txt_resend_otp.setVisibility(View.VISIBLE);
                    Animation anm = Shake_Animation();
//                        Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
//                        // Vibrate for 500 milliseconds
//                        v.vibrate(500);
                    txt_resend_otp.startAnimation(anm);
                    //blankno.d
                    blankno.setEnabled(false);
//                        blankno.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Animation anm = Shake_Animation();
//
//                                txt_resend_otp.startAnimation(anm);
//                            }
//                        });
                    txt_resend_otp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(isNetworkAvailable()) {
                                new resend_otp().execute();
                            }
                            else
                            {
                                Toast.makeText(getActivity(),"Internet Not Available",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    //dialog.cancel();
                }

            }.start();
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




