package in.co.mealman.mealman;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Subscription_package extends Activity {
    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscription_package);
        list=(ListView) findViewById(R.id.mylist);
        list.setAdapter(new VivzAdapter(this));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Toast.makeText(getApplicationContext()," "+i,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
class SingleRow
{
    String titles;
    String description;
    String price;
    SingleRow(String titles,String description,String price)
    {
        this.titles=titles;
        this.description=description;
        this.price=price;
    }
}
class VivzAdapter extends BaseAdapter {


    ArrayList<SingleRow> list;
    Context context;
    VivzAdapter(Context c)
    {
        context=c;
        list=new ArrayList<SingleRow>();
        Resources res=c.getResources();
        String[] titles=res.getStringArray(R.array.titles);
        String[] description=res.getStringArray(R.array.description);
        String[] price=res.getStringArray(R.array.price);
        for(int i=0;i<6;i++)
        {
            list.add(new SingleRow(titles[i],description[i],price[i]));
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i)
    {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row=inflater.inflate(R.layout.single_row,viewGroup,false);
        TextView titles=(TextView) row.findViewById(R.id.title);
        TextView description=(TextView)row.findViewById(R.id.description);
        TextView price=(TextView) row.findViewById(R.id.price);
        SingleRow temp=list.get(i);
        titles.setText(temp.titles);
        description.setText(temp.description);
        price.setText(temp.price);


        return row;
    }
}


