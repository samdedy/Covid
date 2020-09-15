package id.sam.covid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import id.sam.covid.model.DataSPUCovidModel;
import id.sam.covid.model.update.UpdateModel;
import id.sam.covid.service.APIClient;
import id.sam.covid.service.APIInterfacesRest;
import id.sam.covid.utility.SharedPrefUtil;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateStatusActivity extends AppCompatActivity {

    DataSPUCovidModel dataSPUCovidModel;
    LinearLayout linlayKembali;
    CheckBox cb1,cb2,cb3,cb4,cb5;
    Button btnUpdate;
    String scb1 = "0", scb2 = "0", scb3 = "0", scb4 = "0", scb5 = "0";
    public static final int RESULT_CODE = 110;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_status);

        linlayKembali = findViewById(R.id.linlayKembali);
        cb1 = findViewById(R.id.cb1);
        cb2 = findViewById(R.id.cb2);
        cb3 = findViewById(R.id.cb3);
        cb4 = findViewById(R.id.cb4);
        cb5 = findViewById(R.id.cb5);
        btnUpdate = findViewById(R.id.btnUpdate);

        String json = SharedPrefUtil.getInstance(UpdateStatusActivity.this).getString("data_input");
        dataSPUCovidModel = new Gson().fromJson(json, DataSPUCovidModel.class);

        // Status
        String mystring = dataSPUCovidModel.getStatus();
        String arr[] = mystring.split(" ", 5);
        String firstWord = arr[0];

        if (arr[0].equals("1")){
            cb1.setChecked(true);
        }
        if (arr[1].equals("1")){
            cb2.setChecked(true);
        }
        if (arr[2].equals("1")){
            cb3.setChecked(true);
        }
        if (arr[3].equals("1")){
            cb4.setChecked(true);
        }
        if (arr[4].equals("1")){
            cb5.setChecked(true);
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });
        linlayKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    APIInterfacesRest apiInterface;
    ProgressDialog progressDialog;
    public void updateData(){
        apiInterface = APIClient.getClient().create(APIInterfacesRest.class);
        progressDialog = new ProgressDialog(UpdateStatusActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.show();

        if (cb1.isChecked()){
            scb1 = "1";
        }
        if (cb2.isChecked()){
            scb2 = "1";
        }
        if (cb3.isChecked()){
            scb3 = "1";
        }
        if (cb4.isChecked()){
            scb4 = "1";
        }
        if (cb5.isChecked()){
            scb5 = "1";
        }
        String status = scb1+" "+scb2+" "+scb3+" "+scb4+" "+scb5;

        //Kondisi
        String kondisi;
        if (status.equals("0 0 0 0 0")){
            kondisi = "sehat";
        } else if (status.equals("1 1 0 1 0")){
            kondisi = "sakit";
        } else if (status.equals("0 1 0 1 1") || status.equals("0 1 1 1 1") || status.equals("1 1 1 1 1")){
            kondisi = "covid";
        } else {
            kondisi = "sakit";
        }

        //TimeStamp
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();

        Call<UpdateModel> absentAdd = apiInterface.updateData(
                toRequestBody(dataSPUCovidModel.getId()),
                toRequestBody(dataSPUCovidModel.getUsername()),
                toRequestBody(kondisi),
                toRequestBody(dataSPUCovidModel.getLat()),
                toRequestBody(dataSPUCovidModel.getLon()),
                toRequestBody(ts),
                toRequestBody(status),
                toRequestBody(dataSPUCovidModel.getNama_lengkap()),
                toRequestBody(dataSPUCovidModel.getUmur()),
                toRequestBody(dataSPUCovidModel.getJenis_kelamin()),
                toRequestBody(dataSPUCovidModel.getKota_domisili()),
                toRequestBody(dataSPUCovidModel.getNo_telepon()),
                toRequestBody("")
        );

        absentAdd.enqueue(new Callback<UpdateModel>() {
            @Override
            public void onResponse(Call<UpdateModel> call, Response<UpdateModel> response) {
                progressDialog.dismiss();
                UpdateModel status = response.body();
                //Toast.makeText(LoginActivity.this,userList.getToken().toString(),Toast.LENGTH_LONG).show();
                if (status != null) {

                    if (status.getStatus()) {
                        Toast.makeText(UpdateStatusActivity.this, "Update status Berhasil", Toast.LENGTH_LONG).show();

                        Gson gson = new Gson();
                        String json = gson.toJson(status.getData());
                        SharedPrefUtil.getInstance(UpdateStatusActivity.this).put("data_input", json);

                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("kondisi", status.getData().getKondisi());
                        resultIntent.putExtra("timeStamp", status.getData().getTimestamp());
                        setResult(RESULT_CODE, resultIntent);
                        finish();
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.body().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        //     Toast.makeText(ShoppingProductGrid.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                        String error = jObjError.get("message").toString();
                        Toast.makeText(UpdateStatusActivity.this, error, Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        try {
                            Toast.makeText(UpdateStatusActivity.this, "Send Failed, " + response.errorBody().string(), Toast.LENGTH_LONG).show();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        //    Toast.makeText(ShoppingProductGrid.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdateModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Maaf koneksi bermasalah",Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public RequestBody toRequestBody(String value) {
        if (value == null) {
            value = "";
        }
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body;
    }
}