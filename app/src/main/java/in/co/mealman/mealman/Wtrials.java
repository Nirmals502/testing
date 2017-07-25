package in.co.mealman.mealman;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Wtrials extends Activity {
ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wtrials);
        list=(ListView) findViewById(R.id.weeklylist);
        list.setAdapter(new NiksAdapter(this));
    }
}
class WingleRow
{
    String wtitles;
    String days;
    String wprice;
    WingleRow(String wtitles,String days,String wprice)
    {
        this.wtitles=wtitles;
        this.days=days;
        this.wprice=wprice;
    }
}
class NiksAdapter extends BaseAdapter {


    ArrayList<WingleRow> list;
    Context context;
    NiksAdapter(Context c)
    {
        context=c;
        list=new ArrayList<WingleRow>();
        Resources res=c.getResources();
        String[] wtitles=res.getStringArray(R.array.wtitles);
        String[] days=res.getStringArray(R.array.days);
        String[] wprice=res.getStringArray(R.array.wprice);
        for(int i=0;i<3;i++)
        {
            list.add(new WingleRow(wtitles[i],days[i],wprice[i]));
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row=inflater.inflate(R.layout.wingle_row,viewGroup,false);
        TextView wtitles=(TextView) row.findViewById(R.id.title);
        TextView days=(TextView)row.findViewById(R.id.description);
        TextView wprice=(TextView) row.findViewById(R.id.price);
        WingleRow temp=list.get(i);
        wtitles.setText(temp.wtitles);
        days.setText(temp.days);
        wprice.setText(temp.wprice);

        return row;
    }
}



