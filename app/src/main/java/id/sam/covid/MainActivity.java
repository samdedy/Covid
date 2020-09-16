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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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
    String ket = "Anda belum melakukan update status kesehatan";

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
        time = dataSPUCovidModel.getTimestamp();
        Date d1 = new Date(Long.valueOf(time) * 1000);
        Date d2 = new Date(System.currentTimeMillis());

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date d3 = null;
        try {
            d3 = sdf.parse("20-09-2020 12:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        findDifference(d2, d3);

        findDifference(d1, d2);

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

    // Function to print difference in
    // time start_date and end_date
    public  void findDifference(Date d1,
                                Date d2) {
        // SimpleDateFormat converts the
        // string format to date object

        // parse method is used to parse
        // the text from a string to
        // produce the date

        // Calucalte time difference
        // in milliseconds
        long difference_In_Time
                = d2.getTime() - d1.getTime();

        // Calucalte time difference in seconds,
        // minutes, hours, years, and days
        long difference_In_Seconds
                = TimeUnit.MILLISECONDS
                .toSeconds(difference_In_Time)
                % 60;

        long difference_In_Minutes
                = TimeUnit
                .MILLISECONDS
                .toMinutes(difference_In_Time)
                % 60;

        long difference_In_Hours
                = TimeUnit
                .MILLISECONDS
                .toHours(difference_In_Time)
                % 24;

        long difference_In_Days
                = TimeUnit
                .MILLISECONDS
                .toDays(difference_In_Time)
                % 365;

        long difference_In_Years
                = TimeUnit
                .MILLISECONDS
                .toDays(difference_In_Time)
                / 365l;

        // Print the date difference in
        // years, in days, in hours, in
        // minutes, and in seconds
        Log.d( "Days" , String.valueOf(difference_In_Days));
        Log.d( "hour" , String.valueOf(difference_In_Hours));
        Log.d( "minutes" , String.valueOf(difference_In_Minutes));
        Log.d( "seconds" , String.valueOf(difference_In_Seconds));

        if (difference_In_Days != 0){
            ket = "Terakhir update "+String.valueOf(difference_In_Days)+" hari yang lalu";
        } else if (difference_In_Hours != 0){
            ket = "Terakhir update "+String.valueOf(difference_In_Hours)+" jam yang lalu";
        } else if (difference_In_Minutes != 0){
            ket = "Terakhir update "+String.valueOf(difference_In_Minutes)+" menit yang lalu";
        } else {
            ket = "Baru saja terupdate";
        }
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

    public void changeStatus(String mKondisi) {
        switch (mKondisi) {
            case "sehat":
                cvStatus.setCardBackgroundColor(Color.parseColor("#FFC1F486"));
                txtKondisi.setText("SEHAT");
                imgKondisi.setImageResource(R.drawable.ic_done_24);
                txtKet.setText(ket);
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
                txtKet.setText(ket);
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
                txtKet.setText(ket);
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
                time = data.getStringExtra("timeStamp");
                Date d1 = new Date(Long.valueOf(time) * 1000);
                Date d2 = new Date(System.currentTimeMillis());
                findDifference(d1, d2);
                changeStatus(data.getStringExtra("kondisi"));
            }
        }
    }
}