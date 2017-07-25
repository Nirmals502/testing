package adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import in.co.mealman.mealman.Cart;
import in.co.mealman.mealman.Main;
import in.co.mealman.mealman.Navigationbar;
import in.co.mealman.mealman.R;
import in.co.mealman.mealman.Service_handler.Constant;
import in.co.mealman.mealman.Service_handler.ServiceHandler;


/**
 * Created by Nirmal on 6/24/2016.
 */
public class Cart_adapter extends BaseAdapter {
    private ProgressDialog pDialog;
    String status, error_code;
    String cartID;
    String userID;
    ArrayList<HashMap<String, String>> subscriptionarray = new ArrayList<HashMap<String, String>>();
    LayoutInflater inflater;
    Context context;


    public Cart_adapter(Context context, ArrayList<HashMap<String, String>> Array_subscription) {
        this.subscriptionarray = Array_subscription;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return subscriptionarray.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.cart_row, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            SharedPreferences shared = context.getSharedPreferences("MyPrefs", context.MODE_PRIVATE);
            userID = (shared.getString("userid", ""));
            convertView.findViewById(R.id.crosspackage).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartID = subscriptionarray.get(position).get("Cart_id");
                    new Delete_cart().execute();
                }
            });
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }
        try {
            //if(Str_profile_image!=null) {
            //img_loader.DisplayImage(fl,vh.Img_profilepic);


        } catch (IllegalArgumentException e) {
            e.printStackTrace();

        }

        mViewHolder.Tittle.setText((subscriptionarray.get(position).get("str_tittle")));
        mViewHolder.Quantity.setText((subscriptionarray.get(position).get("quantity")));
//        mViewHolder.Type.setText((subscriptionarray.get(position).get("quantity")));
        //  mViewHolder.StartDate.setText((subscriptionarray.get(position).get("startDate")));
        mViewHolder.Amount.setText((subscriptionarray.get(position).get("amount")));
        //mViewHolder.Days.setText((subscriptionarray.get(position).get("days")));


        return convertView;
    }

    private class MyViewHolder {
        TextView Tittle, Quantity, StartDate, Type, Description, Amount, Days;
        ImageView Img_profilepic;

        public MyViewHolder(View item) {
            Tittle = (TextView) item.findViewById(R.id.title);
            Quantity = (TextView) item.findViewById(R.id.quantity);
            //Type=(TextView) item.findViewById(R.id.type);
            Amount = (TextView) item.findViewById(R.id.price);
            //Days= (TextView) item.findViewById(R.id.date);
//            tvDesc = (TextView) item.findViewById(R.id.tvDesc);

        }
    }

    private class Delete_cart extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {


        JSONObject jsonnode, json_User;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // mProgressHUD = ProgressHUD.show(Login_Screen.this, "Connecting", true, true, this);
            // Showing progress dialog
            pDialog = new ProgressDialog(context);
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
            nameValuePairs.add(new BasicNameValuePair("cartID", cartID));
            String jsonStr = sh.makeServiceCall(Constant.DELETE_CART,
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
                Toast.makeText(context, "Your Package is Deleted from Cart", Toast.LENGTH_LONG).show();
                Intent i1 = new Intent(context, Navigationbar.class);
                i1.putExtra("fragment_value_cart", "fragment_cart");
                context.startActivity(i1);
                //notifyDataSetChanged();

            } else {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
            }


        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }
}
