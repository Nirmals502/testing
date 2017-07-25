package in.co.mealman.mealman;

import android.app.Activity;
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
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Multipart_enttity.AndroidMultiPartEntity;
import in.co.mealman.mealman.Service_handler.Constant;
import in.co.mealman.mealman.Service_handler.ServiceHandler;

import static in.co.mealman.mealman.R.id.imageView;

public class User_profile_edit_screen extends Activity {
    private ProgressDialog pDialog;
    String userid;
    private String mParam2, STR_FNAME, str_lastname, Str_phno, Stradress, Str_Email_id, Str_pic;
    EditText Txt_fname, Txt_lastname, Txt_ph, Txt_adress, Txt_emailid, Txt_edit_profile;
    TextView Txt_changepic, Txt_change_password;
    Button btn_update;
    String selectedImagePath = "";
    ImageView img_profile_pic,rbackbutton;
    long totalSize = 0;
    String address, alternate_no, email, Updated_password, Phone_no, Name;
    String mobilepattern = "[0-9]{10}";
    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String status,error;
    Dialog dialog = null;
    ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edit_profile);
        SharedPreferences shared = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        userid = (shared.getString("userid", ""));
        Txt_fname = (EditText) findViewById(R.id.edttxt_name);
        Txt_ph = (EditText) findViewById(R.id.editText3);
        Txt_emailid = (EditText) findViewById(R.id.editText6);
        Txt_adress = (EditText) findViewById(R.id.editText5);
        btn_update = (Button) findViewById(R.id.button2);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Txt_changepic = (TextView) findViewById(R.id.Txt_changepic);
        Txt_change_password = (TextView) findViewById(R.id.Txt_changepassword);
        img_profile_pic = (ImageView) findViewById(R.id.profile_image);
        rbackbutton=(ImageView)findViewById(R.id.rbackbutton);
        rbackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(User_profile_edit_screen.this,Navigationbar.class);
                startActivity(i);
                finish();

            }
        });
        //progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
        if(isNetworkAvailable()) {
            new User_profile_new().execute();
        }
        else
        {
            Toast.makeText(User_profile_edit_screen.this,"No Network Available",Toast.LENGTH_LONG).show();
        }
        Txt_changepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = Utility.checkPermission(User_profile_edit_screen.this);

                if (result) {
                    selectImage_new();

                    //galleryIntent();


                }
            }
        });
        Txt_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_Changepassword();
            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Txt_fname.getText().toString().trim().equals("")) {
                    Txt_fname.setError("Enter name");
                } else if (Txt_ph.getText().toString().trim().equals("")) {
                    Txt_ph.setError(("Enter Mobile No"));
                } else if (!Txt_ph.getText().toString().trim().matches(mobilepattern)) {
                    Txt_ph.setError(getString(R.string.error_invalid_mobileno));

                } else if (Txt_emailid.getText().toString().trim().equals("")) {
                    Txt_emailid.setError("Email Required");
                } else if (Txt_adress.getText().toString().trim().equals("")) {
                    Txt_adress.setError("Enter address");
                } else if (!Txt_emailid.getText().toString().trim().matches(emailpattern)) {
                    Txt_emailid.setError(getString(R.string.error_invalid_email));
                } else {
                    address = Txt_adress.getText().toString();
                    alternate_no = "";
                    email = Txt_emailid.getText().toString();
                    Phone_no = Txt_ph.getText().toString();
                    Name = Txt_fname.getText().toString();
                    if(isNetworkAvailable()) {
                        new Update_info().execute();
                    }
                    else
                    {
                        Toast.makeText(User_profile_edit_screen.this,"Internet Not Available",Toast.LENGTH_LONG).show();

                    }
                }

            }
        });
    }

    private class User_profile_new extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // mProgressHUD = ProgressHUD.show(Login_Screen.this, "Connecting", true, true, this);
            // Showing progress dialog
            pDialog = new ProgressDialog(User_profile_edit_screen.this);
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
                    Str_pic = jsonObj.getString("pic");
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
            Txt_fname.setText(STR_FNAME + " " + str_lastname);
            Txt_ph.setText(Str_phno);
            Txt_emailid.setText(Str_Email_id);
            Txt_adress.setText(Stradress);
            Picasso.with(User_profile_edit_screen.this)
                    .load(Str_pic)
                    .placeholder(R.drawable.profile_pic)   // optional
                    // optional
                    .resize(400, 400).centerCrop().skipMemoryCache()                        // optional
                    .into(img_profile_pic);

        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectImage_new();
                }
            case Utility.MY_PERMISSIONS_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectImage_new();
                }

//                    if (userChoosenTask.equals("Choose from Library"))
//                        galleryIntent();
//                } else {
//                    //code for deny
//                }
                break;
        }
    }

    private void selectImage_new() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(User_profile_edit_screen.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                // onCaptureImageResult(data);
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                    // selectedImagePath = selectedImageUri.getPath();
                    try {
                        bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                                bitmapOptions);

                        //img_profile_pic.setImageBitmap(bitmap);
                        Uri tempUri = getImageUri(User_profile_edit_screen.this, bitmap);
                        File finalFile = new File(getRealPathFromURI(tempUri));

                        selectedImagePath = finalFile.getAbsolutePath();
                        ExifInterface exif = null;
                        try {
                            exif = new ExifInterface(f.getAbsolutePath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                ExifInterface.ORIENTATION_UNDEFINED);

                        Bitmap bmRotated = rotateBitmap(bitmap, orientation);
                        //BITMAP_RESIZER
                        //   Bitmap bit_big_image_crop = cropToSquare(bit_big_image);
                        // Bitmap bit_big_image_crop = BITMAP_RESIZER(bit_big_image,400,400);
                        selectedImagePath = saveimage(bmRotated, "Mealman");
                        //  arrPath[position] = squareimagepath;


                        //   Log.w("path of image from gallery......******************.........", picturePath+"");
                        img_profile_pic.setImageBitmap(bmRotated);
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }
                    if(isNetworkAvailable()) {
                        new Upload_profile_pic().execute();
                    }
                    else
                    {
                        Toast.makeText(User_profile_edit_screen.this,"Internet Not Available",Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


                // new UploadFileToServer().execute();
            } else if (requestCode == 2) {
                try {
                    Uri selectedImage = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String picturePath = c.getString(columnIndex);

                    // selectedImagePath = picturePath;
                    ExifInterface exif = null;
                    try {
                        exif = new ExifInterface(picturePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);
                    Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                    Bitmap bmRotated = rotateBitmap(thumbnail, orientation);
                    //BITMAP_RESIZER
                    //   Bitmap bit_big_image_crop = cropToSquare(bit_big_image);
                    // Bitmap bit_big_image_crop = BITMAP_RESIZER(bit_big_image,400,400);
                    selectedImagePath = saveimage(bmRotated, "Mealman");
                    //  arrPath[position] = squareimagepath;


                    c.close();

                    //   Log.w("path of image from gallery......******************.........", picturePath+"");
                    img_profile_pic.setImageBitmap(bmRotated);
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }
                if(isNetworkAvailable()) {
                    new Upload_profile_pic().execute();
                }
                else
                {
                    Toast.makeText(User_profile_edit_screen.this,"No Network Available",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private class Upload_profile_pic extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            pDialog = new ProgressDialog(User_profile_edit_screen.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.show();
//            progressBar.setVisibility(View.VISIBLE);
//            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible


            // updating progress bar value
            pDialog.setProgress(progress[0]);

            // updating percentage value
            //txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }
        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Constant.Update_profile_pic);


            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });


                File sourceFile = new File(selectedImagePath);

                // Adding file data to http body
                entity.addPart("userID", new StringBody(userid));
                //entity.addPart("gender", new StringBody(gender));

                entity.addPart("file", new FileBody(sourceFile));


                // Extra parameters if you want to pass to server

                // totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }
            if (responseString != null) {
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(responseString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Getting JSON Array node
                // JSONArray array1 = null;
                try {
                    status = jsonObj.getString("status");
                    error = jsonObj.getString("errorCode");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            //Log.e(TAG, "Response from server: " + result);

            // showing the server response in an alert dialog
            //  showAlert(result);

            // new User_profile().execute();
            if (status.contentEquals("sucess")) {
                Intent i1 = new Intent(User_profile_edit_screen.this, Navigationbar.class);

                startActivity(i1);

                finish();

                Toast.makeText(User_profile_edit_screen.this, "Profile updated Successfully", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(User_profile_edit_screen.this, error, Toast.LENGTH_LONG).show();
            }
            pDialog.dismiss();
            //progressBar.setVisibility(View.GONE);
            super.onPostExecute(result);
        }

    }

    private class Update_info extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {


        JSONObject jsonnode, json_User;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // mProgressHUD = ProgressHUD.show(Login_Screen.this, "Connecting", true, true, this);
            // Showing progress dialog
            pDialog = new ProgressDialog(User_profile_edit_screen.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            publishProgress("Please wait...");

            // System.out.println("Current time => " + c.getTime());


            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
            //nameValuePairs.add(new BasicNameValuePair("email", email_fb));
            nameValuePairs.add(new BasicNameValuePair("userID", userid));
            nameValuePairs.add(new BasicNameValuePair("name", Name));
            nameValuePairs.add(new BasicNameValuePair("address", address));
            nameValuePairs.add(new BasicNameValuePair("alternate_no", "7567456543"));
            nameValuePairs.add(new BasicNameValuePair("email", email));
            nameValuePairs.add(new BasicNameValuePair("mphone", Phone_no));
            nameValuePairs.add(new BasicNameValuePair("gender", "male"));


            String jsonStr = sh.makeServiceCall(Constant.UPDATE_USER_PROFILE,
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
                Intent i1 = new Intent(User_profile_edit_screen.this, Navigationbar.class);

                startActivity(i1);

                finish();
                //dialog.cancel();
                Toast.makeText(User_profile_edit_screen.this, "Updated successfully", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(User_profile_edit_screen.this, error, Toast.LENGTH_LONG).show();
            }


        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }

    private void Dialog_Changepassword() {
        // reviewicon=(ImageView) view.findViewById(R.id.reviewicon);
        dialog = new Dialog(User_profile_edit_screen.this, R.style.DialoueBox);

        dialog.setContentView(R.layout.confirm_password);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        Button otpconfirm = (Button) dialog.findViewById(R.id.btn_confirm);
        final EditText Edtxt_oldpassword = (EditText) dialog.findViewById(R.id.editText);
        final EditText Edtxt_new_password = (EditText) dialog.findViewById(R.id.editText2);
        final EditText Edtxt_Confirm_password = (EditText) dialog.findViewById(R.id.editText4);
        ImageView img_close = (ImageView) dialog.findViewById(R.id.imageView6);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        otpconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Edtxt_oldpassword.getText().toString().trim().equals("")) {
                    Edtxt_oldpassword.setError("Enter old password");
                } else if (Edtxt_new_password.getText().toString().trim().equals("")) {
                    Edtxt_new_password.setError(("Enter New password"));
                }
                else if(Edtxt_oldpassword.getText().toString().trim().length()<6)
                {
                    Edtxt_oldpassword.setError(("Password too short"));
                }else if (Edtxt_Confirm_password.getText().toString().trim().equals("")) {
                    Edtxt_Confirm_password.setError("Enter confirm password");
                } else if (!Edtxt_new_password.getText().toString().trim().matches(Edtxt_Confirm_password.getText().toString())) {
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
                        Toast.makeText(User_profile_edit_screen.this,"Internet Not Available",Toast.LENGTH_LONG).show();
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
            pDialog = new ProgressDialog(User_profile_edit_screen.this);
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
                    error = jsonObj.getString("errorCode");
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
                dialog.cancel();
                Toast.makeText(User_profile_edit_screen.this, "Updated successfully", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(User_profile_edit_screen.this, error, Toast.LENGTH_LONG).show();
            }


        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {

            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

//            try {
//                bitmap.recycle();
//            }catch (java.lang.RuntimeException e){
//                e.printStackTrace();
//            }
            return bmRotated;

        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }

    }

    public String saveimage(Bitmap bmp, String file_name) {
// Find the SD Card path
        String path = null;
        File filepath = Environment.getExternalStorageDirectory();
        OutputStream output;
        // Create a new folder in SD Card

        File dir = new File(filepath.getAbsolutePath()
                + "/Mealman/");
        if (dir.exists() && dir.isDirectory()) {
            // do something here


            // Create a name for the saved image
            String timeStamp = "meal_profile_pic";
            File file = new File(dir, timeStamp + ".png");

            // Show a toast message on successful save

            try {

                output = new FileOutputStream(file);
                path = file.getAbsolutePath();

                // Compress into png format image from 0% - 100%
                bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
                output.flush();
                output.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            dir.mkdirs();
            String timeStamp = "meal_profile_pic";
            File file = new File(dir, timeStamp + ".png");

            // Show a toast message on successful save

            try {

                output = new FileOutputStream(file);
                path = file.getAbsolutePath();

                // Compress into png format image from 0% - 100%
                bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
                output.flush();
                output.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return path;
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
