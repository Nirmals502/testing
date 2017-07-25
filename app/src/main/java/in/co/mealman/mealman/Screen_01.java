package in.co.mealman.mealman;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;

public class Screen_01 extends Activity {
    private static int SPLASH_TIME_OUT = 3000;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_01);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences shared = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                userID = (shared.getString("userid", "nodata"));
                if(!userID.contentEquals("nodata")) {
                    // do some thing
                    Intent i1 = new Intent(Screen_01.this, Navigationbar.class);
                    i1.putExtra("userid", userID);
                    startActivity(i1);

                    finish();

                }else{
                    Intent i = new Intent(Screen_01.this, Main.class);
                    startActivity(i);

                    finish();
                }

            }
        }, SPLASH_TIME_OUT);
    }

}