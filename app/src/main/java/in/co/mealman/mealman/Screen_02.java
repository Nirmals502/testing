package in.co.mealman.mealman;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.android.photoutil.MainActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import in.co.mealman.mealman.Service_handler.Constant;
import in.co.mealman.mealman.Service_handler.ServiceHandler;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A simple {@link Fragment} subclass.
 */
public class Screen_02 extends Fragment implements View.OnClickListener {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    private ProgressDialog pDialog;
    EditText blankno;
    TextView txt_timer;
    TextView txt_resend_otp;
    String userid,OTP;
     Dialog dialog_forgot,dialog_update_password;
    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */


    // UI references.
    AutoCompleteTextView mobileno;
    TextView fpass, newusertext;
    EditText mpassword;
    Button login_button;
    String mobilepattern = "[0-9]{10}";
    String mphone, password;
    private String passwordpattern = "jgsfsdg";
    private View mProgressView;
    private View mLoginFormView;
    String status, Sring_phone_number, error,Updated_password;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.screen_02, container, false);

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize(view);
        setListner();
        setdata();

    }


    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("any1", "any1");
    }


    private void initialize(View view) {

        mobileno = (AutoCompleteTextView) view.findViewById(R.id.mobileno);
        login_button = (Button) view.findViewById(R.id.login_button);
        fpass = (TextView) view.findViewById(R.id.fpass);
        mpassword = (EditText) view.findViewById(R.id.password);
        newusertext = (TextView) view.findViewById(R.id.newusertext);

        mpassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }

            private void attemptLogin() {
            }
        });
    }

    private void setListner() {
        login_button.setOnClickListener(this);
        fpass.setOnClickListener(this);
        newusertext.setOnClickListener(this);
    }


    private void setdata() {


    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newusertext:
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Screen_03 fragment = new Screen_03();
                fragmentTransaction.replace(R.id.MainAct, fragment, "B");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                break;
            case R.id.fpass:
                dialog_forgot = new Dialog(getActivity(), R.style.DialoueBox);
                dialog_forgot.setContentView(R.layout.forgotpassword);
                dialog_forgot.show();
                dialog_forgot.setCanceledOnTouchOutside(false);
                TextView cross = (TextView) dialog_forgot.findViewById(R.id.canceltext);
                cross.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dialog_forgot.dismiss();
                    }
                });
                Button confirmbutton = (Button) dialog_forgot.findViewById(R.id.confirmbutton);
                confirmbutton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        EditText blankno = (EditText) dialog_forgot.findViewById(R.id.forgotpasswordedittext);
                        if (blankno.getText().toString().trim().equals("")) {
                            blankno.setError(getString(R.string.error_mobileno_required));
                        } else if (!blankno.getText().toString().trim().matches(mobilepattern)) {
                            blankno.setError(getString(R.string.error_invalid_mobileno));
                        } else {

                            Sring_phone_number = blankno.getText().toString();
                            if(isNetworkAvailable()) {
                                new Forgot_password().execute();
                            }
                            else
                            {
                                Toast.makeText(getActivity(),"Network Not Available",Toast.LENGTH_LONG).show();
                            }
//                            Toast.makeText(getActivity(), "You will receive an OTP Shortly", Toast.LENGTH_SHORT).show();
//                            dialog.dismiss();
                        }

                    }
                });
                break;
            case R.id.login_button:
                 if (mobileno.getText().toString().trim().equals("")) {
                 mobileno.setError(getString(R.string.error_mobileno_required));
                } //else if (!mobileno.getText().toString().trim().matches(mobilepattern)) {
                //mobileno.setError(getString(R.string.error_invalid_mobileno));
                else if (mpassword.getText().toString().trim().equals("")) {
                mpassword.setError(getString(R.string.error_password_required));
                } //else if (mpassword.getText().toString().trim().length() < 1) {
                //mpassword.setError(getString(R.string.error_invalid_password));

                //else if (!mpassword.getText().toString().trim().matches(passwordpattern))
                // {
                //mpassword.setError(getString(R.string.error_incorrect_password));
                else
            {
                    mphone = mobileno.getText().toString();
                    password = mpassword.getText().toString();
                    String method = "Login";
                    BackgroundTask backgroundTask = new BackgroundTask(this.getActivity());
                if(isNetworkAvailable()) {
                    backgroundTask.execute(method, mphone, password);
                }
                else
                {
                    Toast.makeText(getActivity(),"No Internet Available",Toast.LENGTH_LONG).show();
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

    private class Forgot_password extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {


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


            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            //nameValuePairs.add(new BasicNameValuePair("email", email_fb));
            nameValuePairs.add(new BasicNameValuePair("mPhone", Sring_phone_number));


            // password - character


            String jsonStr = sh.makeServiceCall(Constant.FORGOT_PASSWORD,
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
                    userid =  jsonObj.getString("userID");
                    error = jsonObj.getString("errorCode");
                    OTP=  jsonObj.getString("otp");
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
                dialog_forgot.dismiss();
                Toast.makeText(getActivity(), status, Toast.LENGTH_LONG).show();
                dialog_for_otp();


            } else {
                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
            }


        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }

    private void dialog_for_otp() {
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
                            Toast.makeText(getActivity(),"Internet Not Available",Toast.LENGTH_LONG).show();
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
                    Dialog_Changepassword();
                } else {
                    Toast.makeText(getActivity(), "Invalid otp", Toast.LENGTH_LONG).show();
                }

            }
        });


    }
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
                    error = jsonObj.getString("errorCode");
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
            new CountDownTimer(10000, 1000) {

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
                                Toast.makeText(getActivity(),"No Internet Available",Toast.LENGTH_LONG).show();
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
    private void Dialog_Changepassword() {
        // reviewicon=(ImageView) view.findViewById(R.id.reviewicon);
        dialog_update_password = new Dialog(getActivity(), R.style.DialoueBox);
        dialog_update_password.setContentView(R.layout.new_confirm_password);
        dialog_update_password.show();
        dialog_update_password.setCanceledOnTouchOutside(false);
        Button otpconfirm = (Button) dialog_update_password.findViewById(R.id.btn_confirm);
        final EditText Edtxt_oldpassword = (EditText) dialog_update_password.findViewById(R.id.editText);
        final EditText Edtxt_new_password = (EditText) dialog_update_password.findViewById(R.id.editText2);
        final EditText Edtxt_Confirm_password = (EditText) dialog_update_password.findViewById(R.id.editText4);
        ImageView img_close = (ImageView) dialog_update_password.findViewById(R.id.imageView6);
        //Edtxt_oldpassword.setVisibility(View.GONE);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_update_password.dismiss();
            }
        });
        otpconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Edtxt_new_password.getText().toString().trim().equals("")) {
                    Edtxt_new_password.setError(("Enter New password"));
                }
                else if(Edtxt_new_password.getText().toString().trim().length()<6)
                {
                    Edtxt_new_password.setError(("Password too short"));
                } else if (Edtxt_Confirm_password.getText().toString().trim().equals("")) {
                    Edtxt_Confirm_password.setError("Enter confirm password");
                }
                else if (!Edtxt_new_password.getText().toString().trim().matches(Edtxt_Confirm_password.getText().toString())) {
                    Edtxt_Confirm_password.setError("Password do not matches");


                } else {
//                    address = Txt_adress.getText().toString();
//                    alternate_no = "";
//                    email = Txt_emailid.getText().toString();
                    Updated_password = Edtxt_Confirm_password.getText().toString();
                    if(isNetworkAvailable()) {
                        new Update_Password().execute();
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Internet Not Available",Toast.LENGTH_LONG).show();
                    }
                }

                //new subscription_pack.Sbscribe_data().execute();
            }
        });
    }
    private class Update_Password extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {


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


            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            //nameValuePairs.add(new BasicNameValuePair("email", email_fb));
            nameValuePairs.add(new BasicNameValuePair("userID", userid));

            nameValuePairs.add(new BasicNameValuePair("password", Updated_password));
            // password - character


            String jsonStr = sh.makeServiceCall(Constant.UPDATE_PASSWORD,
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
                dialog_update_password.cancel();
                Toast.makeText(getActivity(), "password updated successfuly now you can login with new password", Toast.LENGTH_LONG).show();

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

