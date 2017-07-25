package in.co.mealman.mealman;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.DatePicker;
import android.widget.TextView;

/**
 * Created by ace on 26/12/16.
 */

public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    TextView mngdate;
   TextView date;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis() - 1000);
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
     // date.setText(dd+ "/" + mm + "/" +yy);
    }
}