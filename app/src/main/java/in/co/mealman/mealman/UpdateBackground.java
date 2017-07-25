package in.co.mealman.mealman;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class UpdateBackground extends AsyncTask<String,Void,String> {
    Context context;
    AlertDialog alertDialog;
    UpdateBackground(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute()

    {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Login Information.....");


    }

    @Override
    protected String doInBackground(String... params) {
        String update_url = "http://mealman.roshi.in/api/login/updateUserAfterSignup?";
        String method = params[0];
        if (method.equals("update")) {
            String userID=params[1];
            String gender = params[2];
        String address = params[3];
        String alternate_no = params[4];
        String email = params[5];
        try {
            URL url = new URL(update_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream OS = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
            String data =URLEncoder.encode("userID", "UTF-8") + "=" +URLEncoder.encode(userID, "UTF-8") + "&" +
                    URLEncoder.encode("gender", "UTF-8") + "=" +URLEncoder.encode(gender, "UTF-8") + "&" +
                    URLEncoder.encode("address", "UTF-8") + "=" +URLEncoder.encode(address, "UTF-8") + "&" +
                    URLEncoder.encode("alternate_no", "UTF-8") + "=" +URLEncoder.encode(alternate_no, "UTF-8") + "&" +
                    URLEncoder.encode("email", "UTF-8") + "=" +URLEncoder.encode(email, "UTF-8");
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            OS.close();
            InputStream inputStream=httpURLConnection.getInputStream();
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
        if (result != null && result.equals(result)) {
            alertDialog.setMessage(result);
            alertDialog.show();


        } else {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }


}




