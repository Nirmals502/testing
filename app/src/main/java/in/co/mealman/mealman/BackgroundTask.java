package in.co.mealman.mealman;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.sax.StartElementListener;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import in.co.mealman.mealman.Service_handler.Get_Set_value;

public class BackgroundTask extends AsyncTask<String, Void, String> {
    SharedPreferences sharedpreferences;
    ProgressDialog alertDialog;
    Context ctx;
    String userid;
    String CHECK_METHOD;
    public String str = null;
    public String Error = null;

    BackgroundTask(Context ctx) {
        this.ctx = ctx;


    }

    @Override
    protected void onPreExecute()

    {
        alertDialog = new ProgressDialog(ctx);
        alertDialog.setTitle("please wait...");
        alertDialog.show();

    }

    @Override
    protected String doInBackground(String... params) {
        String login_url = "http://34.197.228.70/api/login/checkUserCredentials?";
        String join_url = "http://34.197.228.70/api/login/createNewUser?";
        String method = params[0];
        CHECK_METHOD = params[0];
        if (method.equals("join")) {
            String name = params[1];
            String mphone = params[2];
            String password = params[3];
            try {
                URL url = new URL(join_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                        URLEncoder.encode("mphone", "UTF-8") + "=" + URLEncoder.encode(mphone, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS, "iso-8859-1"));
                String response = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    response += line;
                }
                bufferedReader.close();
                IS.close();
                httpURLConnection.disconnect();
                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (method.equals("Login")) {
            String mphone = params[1];
            String password = params[2];
            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTf-8"));
                String data = URLEncoder.encode("mphone", "UTF-8") + "=" + URLEncoder.encode(mphone, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String response = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    response += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        alertDialog.dismiss();
        if (result != null && result.equals(result)) {
//            alertDialog.setMessage(result);
//            alertDialog.show();

            JSONObject jsonObj = null;
            try {
                jsonObj = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
             str = null;
             Error = null;
            try {
                str = jsonObj.getString("status");
                Error = jsonObj.getString("errorCode");

                if (CHECK_METHOD.contentEquals("join")) {
                    if (str.contentEquals("success")) {
                        Get_Set_value GSETVALUE = new Get_Set_value();
                        GSETVALUE.setName(str);
                        userid = jsonObj.getString("userID");
                        SharedPreferences sharedpreferences = ctx.getSharedPreferences("MyPrefs", ctx.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();

                        editor.putString("userid", userid);
                        editor.commit();

                    } else {
                        Toast.makeText(ctx,Error,Toast.LENGTH_LONG).show();
                    }
                } else {
                    userid = jsonObj.getString("user_id");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (CHECK_METHOD.contentEquals("Login")) {
                if (str.contentEquals("success")) {
                    SharedPreferences sharedpreferences = ctx.getSharedPreferences("MyPrefs", ctx.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();

                    editor.putString("userid", userid);
                    editor.commit();
                    Intent i1 = new Intent(ctx, Navigationbar.class);
                    i1.putExtra("userid", userid);
                    ctx.startActivity(i1);
                }else {
//                    alertDialog.setMessage(Error);
//                    alertDialog.show();
                     Toast.makeText(ctx,Error,Toast.LENGTH_LONG).show();
                }
            } else {
                alertDialog.setMessage(Error);
                alertDialog.show();
               // Toast.makeText(ctx,Error,Toast.LENGTH_LONG).show();
            }

        } else {
            alertDialog.setMessage(result);
            alertDialog.show();
        }

    }
}
