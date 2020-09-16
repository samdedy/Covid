package id.sam.covid;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import id.sam.covid.model.DataSPUCovidModel;
import id.sam.covid.model.detail.DetailModel;
import id.sam.covid.model.register.Data;
import id.sam.covid.service.APIClient;
import id.sam.covid.service.APIInterfacesRest;
import id.sam.covid.utility.SharedPrefUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    CardView cvStatus,cvLihatKondisi,cvUpdateStatus;
    TextView txtKondisi,txtKet;
    ImageView imgKondisi;
    private int REQUEST_CODE = 100;
    String time = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cvStatus =findViewById(R.id.cvStatus);
        cvLihatKondisi =findViewById(R.id.cvLihatKondisi);
        cvUpdateStatus =findViewById(R.id.cvUpdateStatus);
        txtKondisi =findViewById(R.id.txtKondisi);
        txtKet =findViewById(R.id.txtKet);
        imgKondisi = findViewById(R.id.imgKondisi);

        String json = SharedPrefUtil.getInstance(MainActivity.this).getString("data_input");
        DataSPUCovidModel dataSPUCovidModel = new Gson().fromJson(json, DataSPUCovidModel.class);

        //TimeStamp
        time = getTimeStampRemains(dataSPUCovidModel.getTimestamp());

        changeStatus(dataSPUCovidModel.getKondisi());

        cvUpdateStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UpdateStatusActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        cvLihatKondisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }

    private String getTimeStampRemains(String timestamp) {
//        Long tsLong = System.currentTimeMillis()/1000;
        Long ts = Long.parseLong(timestamp);
//        Long mTimeStamp = tsLong - ts;
//        time = getDate(mTimeStamp);
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(ts * 1000L);
        String date = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();

//        Date dNow = new Date( );
//        SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

        return date;
    }

    private String getDate(long time) {
        String date = DateFormat.format("dd-MM-yyyy hh:mm:ss", time).toString();
        return date;
    }

    public void changeStatus(String mKondisi) {
        switch (mKondisi) {
            case "sehat":
                cvStatus.setCardBackgroundColor(Color.parseColor("#FFC1F486"));
                txtKondisi.setText("SEHAT");
                imgKondisi.setImageResource(R.drawable.ic_done_24);
                txtKet.setText("Terakhir update "+time);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent sehat = new Intent(MainActivity.this, KetActivity.class);
                        startActivity(sehat);
                    }
                },1000);

                break;
            case "sakit":
                cvStatus.setCardBackgroundColor(Color.parseColor("#FFFFF6A4"));
                txtKondisi.setText("SAKIT");
                imgKondisi.setImageResource(R.drawable.ic_warning_24);
                txtKet.setText("Terakhir update "+time);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent sakit = new Intent(MainActivity.this, KetActivity.class);
                        startActivity(sakit);
                    }
                },1000);
                break;
            case "covid":
                cvStatus.setCardBackgroundColor(Color.parseColor("#FFFF9A9A"));
                txtKondisi.setText("COVID");
                imgKondisi.setImageResource(R.drawable.ic_report_24);
                txtKet.setText("Terakhir update "+time);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent covid = new Intent(MainActivity.this, DaruratActivity.class);
                        startActivity(covid);
                    }
                },1000);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == UpdateStatusActivity.RESULT_CODE){
                time = getTimeStampRemains(data.getStringExtra("timeStamp"));
                changeStatus(data.getStringExtra("kondisi"));
            }
        }
    }
}