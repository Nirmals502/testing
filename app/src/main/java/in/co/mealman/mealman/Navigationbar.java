package in.co.mealman.mealman;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.Regular_adapter;
import in.co.mealman.mealman.Service_handler.Constant;
import in.co.mealman.mealman.Service_handler.ServiceHandler;

public class Navigationbar extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ProgressDialog pDialog;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    TextView text;
    ImageView img_profile_pic;
    String userid;
    String Profile_image;

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    Button wsaystext, subpackbutton, wtrialbutton, regularbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigationbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        wsaystext = (Button) findViewById(R.id.wsaystext);
        subpackbutton = (Button) findViewById(R.id.subpackbutton);
        wtrialbutton = (Button) findViewById(R.id.wtrialbutton);
        regularbutton = (Button) findViewById(R.id.regularbutton);
        setSupportActionBar(toolbar);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Slider slide = new Slider();
        fragmentTransaction.add(R.id.slider, slide, "S");
        fragmentTransaction.commit();
        // toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        }
        {

        }
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            String id = "";
            id = iin.getStringExtra("fragment_value_cart");
            try {
                if (id.contentEquals("fragment_cart")) {
                    FragmentManager fragmentManagerr = getFragmentManager();
                    FragmentTransaction fragmentTransactionn = fragmentManagerr.beginTransaction();
                    Cart cart = new Cart();
                    fragmentTransactionn.replace(R.id.Mainslider, cart, "c");
                    fragmentTransactionn.addToBackStack(null);
                    fragmentTransactionn.commit();
                }
            }catch (java.lang.NullPointerException e){
                e.printStackTrace();
            }
            // and get whatever type user account id is
        }


        SharedPreferences shared = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        userid = (shared.getString("userid", ""));
        if (isNetworkAvailable()) {
            new User_profile().execute();
        } else {
            Toast.makeText(Navigationbar.this, "No Internet Available", Toast.LENGTH_LONG).show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        text = (TextView) header.findViewById(R.id.text_headr);
        img_profile_pic = (ImageView) header.findViewById(R.id.profile_image);
        //  text.setText("nirmal");

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void wsaysopen(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Wsays csays = new Wsays();
        fragmentTransaction.replace(R.id.Mainslider, csays, "c");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    public void opensubpack(View view) {
//        Intent i = new Intent(Navigationbar.this, Subscription_package.class);
//        startActivity(i);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction1 = fragmentManager.beginTransaction();
        subscription_pack sub_pack = new subscription_pack();
        fragmentTransaction1.replace(R.id.Mainslider, sub_pack, "c");
        fragmentTransaction1.addToBackStack(null);
        fragmentTransaction1.commit();
        //weekly_packages

    }

    public void openwtrial(View view) {
//        Intent i = new Intent(Navigationbar.this, Wtrials.class);
//        startActivity(i);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction1 = fragmentManager.beginTransaction();
        weekly_packages weeklypackage = new weekly_packages();
        fragmentTransaction1.replace(R.id.Mainslider, weeklypackage, "c");
        fragmentTransaction1.addToBackStack(null);
        fragmentTransaction1.commit();
    }

    public void regular(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction1 = fragmentManager.beginTransaction();
        Regular_package regular = new Regular_package();
        fragmentTransaction1.replace(R.id.Mainslider, regular, "r");
        fragmentTransaction1.addToBackStack(null);
        fragmentTransaction1.commit();
    }


    @Override
    public void onBackPressed() {
        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //if (drawer.isDrawerOpen(GravityCompat.START)) {
        //  drawer.closeDrawer(GravityCompat.START);
        //} else {
        //  super.onBackPressed();
        // }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_notification) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Notification notify = new Notification();
            fragmentTransaction.replace(R.id.Mainslider, notify, "n");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (id == R.id.action_cart) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Cart cart = new Cart();
            fragmentTransaction.replace(R.id.Mainslider, cart, "c");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        return super.onOptionsItemSelected(item);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent i = new Intent(Navigationbar.this, Navigationbar.class);
            startActivity(i);
            // Handle the camera action
        } else if (id == R.id.nav_profile) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            profile_screen Profile_screen = new profile_screen();
            fragmentTransaction.replace(R.id.Mainslider, Profile_screen, "U");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (id == R.id.history) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Order_history order = new Order_history();
            fragmentTransaction.replace(R.id.Mainslider, order, "U");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_aboutus) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Aboutus aboutus = new Aboutus();
            fragmentTransaction.replace(R.id.Mainslider, aboutus, "U");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();


        } else if (id == R.id.nav_contactus) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Contactus cus = new Contactus();
            fragmentTransaction.replace(R.id.Mainslider, cus, "s");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_tc) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Termsconditions tcondition = new Termsconditions();
            fragmentTransaction.replace(R.id.Mainslider, tcondition, "p");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_ppolicy) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Privacypolicy ppolicy = new Privacypolicy();
            fragmentTransaction.replace(R.id.Mainslider, ppolicy, "p");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (id == R.id.navi_logout) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(Navigationbar.this);
            builder.setMessage("Are you sure you want to logout?");
            builder.setCancelable(true);
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();


                }
            });
            builder.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    SharedPreferences shared = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.clear();
                    editor.commit();
                    Intent i = new Intent(Navigationbar.this, Main.class);
                    startActivity(i);
                    finish();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            alertDialog.setCanceledOnTouchOutside(false);
        } else if (id == R.id.navi_exit) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(Navigationbar.this);
            builder.setMessage("Wanna Exit?");
            builder.setCancelable(true);
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.setPositiveButton("Exit!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Navigationbar Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private class User_profile extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {
        String android_id;

        JSONObject jsonnode, json_User;
        String str = "nostatus";
        String Name, access_tocken, Ostype;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // mProgressHUD = ProgressHUD.show(Login_Screen.this, "Connecting", true, true, this);
            // Showing progress dialog
            pDialog = new ProgressDialog(Navigationbar.this);
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
                    str = jsonObj.getString("fName");
                    Profile_image = jsonObj.getString("pic");
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
            text.setText(str);
            Picasso.with(Navigationbar.this)
                    .load(Profile_image)
                    .placeholder(R.drawable.profile_pic).resize(400, 400)   // optional
                    // optional
                    .centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)             // optional
                    .into(img_profile_pic);

//            ImageLoader imgloader = new ImageLoader(Navigationbar.this);
//            imgloader.DisplayImage(str,img_profile_pic);

        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

}

