package adapter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.co.mealman.mealman.R;
import in.co.mealman.mealman.Service_handler.Constant;
import in.co.mealman.mealman.Service_handler.ServiceHandler;
import in.co.mealman.mealman.subscription_pack;


/**
 * Created by Nirmal on 6/24/2016.
 */
public class Subscription_adapter extends BaseAdapter{
Dialog subscribe_dialog;
    private ProgressDialog pDialog;
    String status, error_code;
    String amountGlobal, String_static_amount;
    String Tittle;
    String typee;
    String discount;
    TextView txt_amount;
    int integer_amount=0;
            TextView Txt_vw_amount;
    TextView date;
    Dialog dialog = null;
    int int_amount = 1;
    int int_amountd=1;
    String userID, SUBSCRIPTION_PAKAGEID;
   String quantiity = "1";
    ArrayList<HashMap<String, String>> subscriptionarray = new ArrayList<HashMap<String, String>>();
    LayoutInflater inflater;
    Context context;

    public Subscription_adapter(Context context, ArrayList<HashMap<String, String>> Array_subscription) {
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
            convertView = inflater.inflate(R.layout.single_row, parent, false);
//            convertView.findViewById(R.id.subscribed).setOnClickListener(new View.OnClickListener() {
   //             @Override
     //           public void onClick(View v) {
       //             typee=subscriptionarray.get(position).get("type");
         //           discount=subscriptionarray.get(position).get("description");
           //         amountGlobal = subscriptionarray.get(position).get("amount");
             //       String_static_amount = subscriptionarray.get(position).get("amount");
               //     Tittle = subscriptionarray.get(position).get("str_tittle");
                 //   SUBSCRIPTION_PAKAGEID = subscriptionarray.get(position).get("subscriptionPackageID");
                   // Dialog(amountGlobal, Tittle);
                //}
            //});
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else  {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }
        try {
            //if(Str_profile_image!=null) {
            //img_loader.DisplayImage(fl,vh.Img_profilepic);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        mViewHolder.Tittle.setText((subscriptionarray.get(position).get("str_tittle")));
        mViewHolder.Amount.setText((subscriptionarray.get(position).get("amount")));
        mViewHolder.Description.setText((subscriptionarray.get(position).get("description")));
        return convertView;
    }
    //public void Dialog(final String amount,String tittle){

      //  subscribe_dialog=new Dialog(context,R.style.DialoueBox);
       // subscribe_dialog.setContentView(R.layout.subscribe_addto_cart);
       // subscribe_dialog.show();
        //subscribe_dialog.setCanceledOnTouchOutside(false);
        //ImageView imgcross=(ImageView)subscribe_dialog.findViewById(R.id.imgcross);
        //imgcross.setOnClickListener(new View.OnClickListener() {
          //  @Override
            //public void onClick(View v) {
              //  subscribe_dialog.cancel();
                //int_amount=1;
               // int_amountd=1;
            //}
        //});
        //TextView Txt_tittle = (TextView) subscribe_dialog.findViewById(R.id.txtvw_tittle);
        //final TextView Txt_vw_amount = (TextView) subscribe_dialog.findViewById(R.id.textView4);
        //ImageView increment = (ImageView) subscribe_dialog.findViewById(R.id.nbinc1);
        //ImageView decrement = (ImageView) subscribe_dialog.findViewById(R.id.nbdec1);
        //final TextView txt_amount = (TextView) subscribe_dialog.findViewById(R.id.nbincdec);
        //ImageView incre=(ImageView)subscribe_dialog.findViewById(R.id.nbinc2);
        //ImageView decre=(ImageView)subscribe_dialog.findViewById(R.id.nbdec2);
        //final TextView txt_amount1=(TextView)subscribe_dialog.findViewById(R.id.nbincdec1);
        //date=(TextView)subscribe_dialog.findViewById(R.id.date);
        //ImageView calender=(ImageView) subscribe_dialog.findViewById(R.id.calender);
        //Button subscribe=(Button)subscribe_dialog.findViewById(R.id.btn_subscribeconfirm);
        //Txt_tittle.setText(tittle);
        /*increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (int_amount != 9) {
                    int_amount++;
                    int integer_amount = 0;
                    Double value = 0.0;
                    try {
                        integer_amount = Integer.parseInt(String_static_amount);
                    } catch (java.lang.NumberFormatException e) {
                        e.printStackTrace();
                        value =  Double.parseDouble(String_static_amount);
                        integer_amount = value.intValue();
                    }
                    integer_amount = integer_amount * int_amount * int_amountd;
                    String value_after_multiply = String.valueOf(integer_amount);
                    String int_to_string = String.valueOf(int_amount);
                    txt_amount.setText(int_to_string);
                    Txt_vw_amount.setText("Rs." + value_after_multiply);
                    amountGlobal = value_after_multiply;
                    quantiity = int_to_string;
                }

            }
        });
        incre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (int_amountd != 60) {
                    int_amountd++;
                    int integer_amount = 0;
                    int integer_discount=0;
                    int integer_total=0;
                    Double value = 0.0;
                    try {
                        integer_discount=Integer.parseInt(discount);
                        integer_amount = Integer.parseInt(String_static_amount);
                    } catch (java.lang.NumberFormatException e) {
                        e.printStackTrace();
                        value =  Double.parseDouble(String_static_amount);
                        integer_amount = value.intValue();
                        integer_discount=value.intValue();
                        integer_total=value.intValue();
                    }
                    integer_amount = integer_amount * int_amount * int_amountd;
                    integer_discount=(integer_amount * integer_discount)/100;
                    integer_total=integer_amount - integer_discount;
                    String value_after_multiply = String.valueOf(integer_total);
                    String int_to_string = String.valueOf(int_amountd);
                    txt_amount1.setText(int_to_string);
                    Txt_vw_amount.setText("Rs." + value_after_multiply);
                    amountGlobal = value_after_multiply;
                    quantiity = int_to_string;
                }
            }
        });
        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (int_amount != 1) {
                    int_amount--;
                    int integer_amount = 0;
                    Double value = 0.0;
                    try {
                        integer_amount = Integer.parseInt(String_static_amount);
                    } catch (java.lang.NumberFormatException e) {
                        e.printStackTrace();
                        value =  Double.parseDouble(String_static_amount);
                        integer_amount = value.intValue();
                    }
                    integer_amount = integer_amount * int_amount * int_amountd;
                    String value_after_multiply = String.valueOf(integer_amount);
                    String int_to_string = String.valueOf(int_amount);
                    txt_amount.setText(int_to_string);
                    Txt_vw_amount.setText("Rs." + value_after_multiply);
                    amountGlobal = value_after_multiply;
                    quantiity = int_to_string;
                }
            }
        });
        decre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (int_amountd != 1) {
                    int_amountd--;
                    int integer_amount = 0;
                    int integer_discount=0;
                    int integer_total=0;
                    Double value = 0.0;
                    try {
                        integer_discount=Integer.parseInt(discount);
                        integer_amount = Integer.parseInt(String_static_amount);
                    } catch (java.lang.NumberFormatException e) {
                        e.printStackTrace();
                        value =  Double.parseDouble(String_static_amount);
                        integer_amount = value.intValue();
                        integer_discount=value.intValue();
                        integer_total=value.intValue();
                    }
                    integer_amount = integer_amount * int_amount * int_amountd;
                    integer_discount=(integer_amount * integer_discount)/100;
                    integer_total=integer_amount - integer_discount;
                    String value_after_multiply = String.valueOf(integer_total);
                    String int_to_string = String.valueOf(int_amountd);
                    txt_amount1.setText(int_to_string);
                    Txt_vw_amount.setText("Rs." + value_after_multiply);
                    amountGlobal = value_after_multiply;
                    quantiity = int_to_string;
                }
            }
        });
        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(((Activity) context).getFragmentManager(), "DatePicker");
            }
        });
        Txt_vw_amount.setText("Rs." + amount);
    }*/

    private class MyViewHolder {
        TextView Tittle,Amount,Description;
        ImageView Img_profilepic;

        public MyViewHolder(View item) {
            Tittle = (TextView) item.findViewById(R.id.title);
            Amount = (TextView) item.findViewById(R.id.price);
            Description= (TextView) item.findViewById(R.id.description);

//            tvDesc = (TextView) item.findViewById(R.id.tvDesc);

        }

    }
    /*public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        //TextView mngdate;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
           // calendar.setTimeInMillis(System.currentTimeMillis() - 1000);
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dpd=new DatePickerDialog(getContext(),this,yy,mm,dd);
            calendar.add(calendar.DATE,7);
            dpd.getDatePicker().setMaxDate(calendar.getTimeInMillis());
            calendar.add(calendar.DATE,-7);
            dpd.getDatePicker().setMinDate(calendar.getTimeInMillis());
            //return new DatePickerDialog(getActivity(), this, yy, mm, dd);
            return dpd;
        }
        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            showDate(yy,mm+1,dd);
        }
        private void showDate(int yy, int mm, int dd) {
            //((TextView) getActivity().findViewById(R.id.mngdate)).setText(dd+ "/" + mm + "/" + yy);
            //((TextView) getActivity().findViewById(R.id.date)).setText(dd+ "/" + mm + "/" + yy);
            String Str_year = String.valueOf(yy);
            String Str_month = String.valueOf(mm);
            String Str_day = String.valueOf(dd);
            date.setText(Str_day+ "/" + Str_month + "/" +Str_year);
        }
    }*/
}