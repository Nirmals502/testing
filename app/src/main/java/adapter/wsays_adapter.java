package adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import in.co.mealman.mealman.R;
import in.co.mealman.mealman.User_profile_edit_screen;


/**
 * Created by Nirmal on 6/24/2016.
 */
public class wsays_adapter extends BaseAdapter {

    ArrayList<HashMap<String, String>> subscriptionarray = new ArrayList<HashMap<String, String>>();
    LayoutInflater inflater;
    Context context;


    public wsays_adapter(Context context, ArrayList<HashMap<String, String>> Array_subscription) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.wsays, parent, false);
            mViewHolder = new MyViewHolder(convertView);
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
        mViewHolder.userName.setText((subscriptionarray.get(position).get("userName")));
        mViewHolder.Tittle.setText((subscriptionarray.get(position).get("comment")));
        Picasso.with(context)
                .load(subscriptionarray.get(position).get("userImage"))
                .placeholder(R.drawable.profile_pic)   // optional
                // optional
                .resize(400, 400)                        // optional
                .into(mViewHolder.Img_profilepic);


        return convertView;
    }

    private class MyViewHolder {
        TextView Tittle, Amount, Description,userName;
        ImageView Img_profilepic;

        public MyViewHolder(View item) {
            userName=(TextView)item.findViewById(R.id.cusname);
            Tittle = (TextView) item.findViewById(R.id.istetxt);
            Img_profilepic = (ImageView) item.findViewById(R.id.istcus_image);
//            tvDesc = (TextView) item.findViewById(R.id.tvDesc);

        }
    }
}