package pl.mikolajkarebski.tomoto.activities;
 
import pl.mikolajkarebski.tomoto.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
 
public class SplashScreenActivity extends Activity {
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        getActionBar().hide();
         
        Thread background = new Thread() {
            public void run() {
                try {
                    sleep(2*1000);
                    Intent i = new Intent(getBaseContext(),LoginActivity.class);
                    startActivity(i);
                    finish();
                } catch (Exception e) {
                	Log.d("splash", "Gdzies tu jest blad");
                }
            }
        };
        background.start();
    }
     
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}