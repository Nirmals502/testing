package adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.HashMap;

import in.co.mealman.mealman.R;


/**
 * Created by Nirmal on 6/24/2016.
 */
public class Trials_adapter extends BaseAdapter {

    ArrayList<HashMap<String, String>> subscriptionarray = new ArrayList<HashMap<String, String>>();
    LayoutInflater inflater;
    Context context;


    public Trials_adapter(Context context, ArrayList<HashMap<String, String>> Array_subscription) {
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
            convertView = inflater.inflate(R.layout.wingle_row, parent, false);
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

        mViewHolder.Tittle.setText((subscriptionarray.get(position).get("str_tittle")));
        mViewHolder.Amount.setText((subscriptionarray.get(position).get("amount")));
        mViewHolder.Description.setText((subscriptionarray.get(position).get("description")));


        return convertView;
    }

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
}