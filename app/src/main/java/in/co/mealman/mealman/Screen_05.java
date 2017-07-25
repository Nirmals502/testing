package in.co.mealman.mealman;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import Multipart_enttity.AndroidMultiPartEntity;
import de.hdodenhof.circleimageview.CircleImageView;
import in.co.mealman.mealman.Service_handler.Constant;

import static android.R.attr.checked;
import static android.R.attr.name;
import static android.R.attr.start;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.CAMERA_SERVICE;
import static android.content.Context.CAPTIONING_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class Screen_05 extends Fragment implements View.OnClickListener {
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView camera;
    ImageView ubackbutton;
    RadioGroup radioSexGroup;
    RadioButton radioSexButton;
    EditText addresstext, altertext, emailtext;
    CircleImageView profile_image;
    Button updatebutton;
    private Bitmap bitmap;
    String mobilepattern = "[0-9]{10}";
    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private String userChoosenTask;
    String address, alternate_no, email;
    String gender = "Male";
    String userID = " ";
    String selectedImagePath = "";
    private ProgressDialog pDialog;
    private ProgressBar progressBar;
    String status, Error;

    long totalSize = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.screen_05, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize(view);
        setListner();
        setdata();
        SharedPreferences shared = getActivity().getSharedPreferences("MyPrefss", getActivity().MODE_PRIVATE);
        userID = (shared.getString("useridd", ""));

    }

    private void setdata() {
    }

    private void setListner() {
        radioSexGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioFemale:
                        gender = "Female";
                        break;
                    case R.id.radioMale:
                        gender = "Male";
                        break;
                }
            }
        });
        profile_image.setOnClickListener(this);
        updatebutton.setOnClickListener(this);
        ubackbutton.setOnClickListener(this);
        camera.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                //selectImage();

                //requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);

                boolean result = Utility.checkPermission(getActivity());

                if (result) {

                    selectImage_new();


                    //galleryIntent();


                }
                //   else {
//                    Utility.
//                    requestPermissions(new String[]{Manifest.permission.CAMERA},
//                            1);
//                }

//
            }
        });
        {

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

    private void selectImage() {
        final CharSequence[] items = {"Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getActivity());
                if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result) {
                        //selectImage_new();
                    }
                    //galleryIntent();


                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }


    //    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null && data.getData() != null)
//                onSelectFromGalleryResult(data);
//        Uri filePath = data.getData();
//        try {
//            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
//            profile_image.setImageBitmap(bitmap);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        }
//    @SuppressWarnings("deprecation")
//    private void onSelectFromGalleryResult(Intent data) {
//
//        Bitmap bm=null;
//        if (data != null) {
//            try {
//                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        profile_image.setImageBitmap(bm);
//
//    }
    private void initialize(View view) {

        camera = (ImageView) view.findViewById(R.id.camera);
        addresstext = (EditText) view.findViewById(R.id.addresstext);
        altertext = (EditText) view.findViewById(R.id.altertext);
        emailtext = (EditText) view.findViewById(R.id.emailtext);
        updatebutton = (Button) view.findViewById(R.id.updatebutton);
        profile_image = (CircleImageView) view.findViewById(R.id.profile_image);
        radioSexGroup = (RadioGroup) view.findViewById(R.id.radioSex);
        ubackbutton = (ImageView) view.findViewById(R.id.ubackbutton);


    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.ubackbutton:
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Screen_03 regtr = new Screen_03();
                fragmentTransaction.replace(R.id.MainAct, regtr, "r");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            case R.id.updatebutton:

                if (addresstext.getText().toString().trim().equals("")) {
                    addresstext.setError(getString(R.string.error_Address_required));
                } else if (addresstext.getText().toString().trim().length() < 20) {
                    addresstext.setError(getString(R.string.error_Addess_minimum));
                } else if (!altertext.getText().toString().trim().matches(mobilepattern)) {
                    altertext.setError(getString(R.string.error_invalid_mobileno));
                } else if (altertext.getText().toString().trim().equals("")) {
                    altertext.setError(getString(R.string.error_mobileno_required));
                } else if (emailtext.getText().toString().trim().equals("")) {
                    emailtext.setError(getString(R.string.error_email_required));
                } else if (!emailtext.getText().toString().trim().matches(emailpattern)) {
                    emailtext.setError(getString(R.string.error_invalid_email));
                } else {
                    address = addresstext.getText().toString();
                    alternate_no = altertext.getText().toString();
                    email = emailtext.getText().toString();
//                    String method = "update";
//                    UpdateBackground updateBackground = new UpdateBackground(this.getActivity());
//                    updateBackground.execute(method, userID, gender, address, alternate_no, email);
//                    getActivity().getFragmentManager().beginTransaction().remove(this).commit();
//                    Intent i = new Intent(getActivity(), Navigationbar.class);
//                    i.putExtra("userid", userID);
//                    startActivity(i);
                    if (selectedImagePath.contentEquals("")) {
                        Toast.makeText(getActivity(), "Select image", Toast.LENGTH_LONG).show();
                    } else {
                        if (isNetworkAvailable()) {
                            new UploadFileToServer().execute();
                        } else {
                            Toast.makeText(getActivity(), "Internet Not Available", Toast.LENGTH_LONG).show();
                        }
                    }

                }
                break;
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

    private void selectImage_new() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);

                    profile_image.setImageBitmap(bitmap);
                    Uri tempUri = getImageUri(getActivity(), bitmap);
                    File finalFile = new File(getRealPathFromURI(tempUri));

                    selectedImagePath = finalFile.getAbsolutePath();

//                    String path = android.os.Environment
//                            .getExternalStorageDirectory()
//                            + File.separator
//                            + "Phoenix" + File.separator + "default";
//                    f.delete();
//                    OutputStream outFile = null;
//                    //selectedImagePath = path;
//                     File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
//
//                    try {
//                        outFile = new FileOutputStream(file);
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
//                        outFile.flush();
//                        outFile.close();
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                // new UploadFileToServer().execute();
            } else if (requestCode == 2) {

                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                selectedImagePath = picturePath;
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                //   Log.w("path of image from gallery......******************.........", picturePath+"");
                profile_image.setImageBitmap(thumbnail);
                //  new UploadFileToServer().execute();
            }
        }
    }

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Constant.UPDATE_PROFILE);


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
                entity.addPart("userID", new StringBody(userID));
                entity.addPart("gender", new StringBody(gender));
                entity.addPart("address", new StringBody(address));
                entity.addPart("alternate_no", new StringBody(alternate_no));
                entity.addPart("email", new StringBody(email));
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
                    Error = jsonObj.getString("errorCode");
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
            if (status.contentEquals("success")) {
//                SharedPreferences sharedpreferences = getActivity().getSharedPreferences("MyPrefs", getActivity().MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedpreferences.edit();
//
//                editor.putString("userid", userID);
//                editor.commit();
//                //dialog.cancel();
//                Toast.makeText(getActivity(), "Updated Successfully", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getActivity(), Main.class);
                // i.putExtra("userid", userID);
                startActivity(i);
                getActivity().finish();

            } else {
                Toast.makeText(getActivity(), Error, Toast.LENGTH_LONG).show();
            }
            pDialog.dismiss();
            super.onPostExecute(result);
        }

    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        selectedImagePath = destination.getAbsolutePath();
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        profile_image.setImageBitmap(thumbnail);

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        String path = "";
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        }

        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}



