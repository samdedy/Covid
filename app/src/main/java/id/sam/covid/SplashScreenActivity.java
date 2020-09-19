package id.sam.covid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.location.aravind.getlocation.GeoLocator;

import id.sam.covid.utility.SharedPrefUtil;

public class SplashScreenActivity extends AppCompatActivity {

    Double lat = 0.0, lon = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        GeoLocator geoLocator = new GeoLocator(getApplicationContext(),this);
        lat = geoLocator.getLattitude();
        lon = geoLocator.getLongitude();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String dataJson = SharedPrefUtil.getInstance(SplashScreenActivity.this).getString("data_input");
                if (!TextUtils.isEmpty(dataJson)){
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SplashScreenActivity.this, RegistrasiActivity.class);
                    intent.putExtra("lat", lat);
                    intent.putExtra("lon", lon);
                    startActivity(intent);
                }
                finish();
            }
        },5000);


    }
}