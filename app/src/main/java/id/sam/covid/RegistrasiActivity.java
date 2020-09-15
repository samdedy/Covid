package id.sam.covid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.location.aravind.getlocation.GeoLocator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import id.sam.covid.model.register.RegisterModel;
import id.sam.covid.service.APIClient;
import id.sam.covid.service.APIInterfacesRest;
import id.sam.covid.utility.SharedPrefUtil;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrasiActivity extends AppCompatActivity {

    TextView txtNamaLengkap, txtUmur, txtKotaDomisili, txtNoTelepon;
    Spinner spnJenisKelamin;
    Button btnRegistrasi;
    Double lat = 0.0, lon = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);

        txtNamaLengkap = findViewById(R.id.txtNamaLengkap);
        txtUmur = findViewById(R.id.txtUmur);
        txtKotaDomisili = findViewById(R.id.txtKotaDomisili);
        txtNoTelepon = findViewById(R.id.txtNoTelepon);
        spnJenisKelamin = findViewById(R.id.spnJenisKelamin);
        btnRegistrasi = findViewById(R.id.btnRegistrasi);

        lat = getIntent().getDoubleExtra("lat",0);
        lon = getIntent().getDoubleExtra("lon",0);

        btnRegistrasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkMandatory()) {
                    addData();
                } else {
                    showErrorDialog();
                }
            }
        });
    }

    APIInterfacesRest apiInterface;
    ProgressDialog progressDialog;
    public void addData(){
        apiInterface = APIClient.getClient().create(APIInterfacesRest.class);
        progressDialog = new ProgressDialog(RegistrasiActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.show();

        //First name
        String mystring = txtNamaLengkap.getText().toString();
        String arr[] = mystring.split(" ", 2);
        String firstWord = arr[0];

        //TimeStamp
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();

        Call<RegisterModel> absentAdd = apiInterface.addData(
                toRequestBody(firstWord),
                toRequestBody("sehat"),
                toRequestBody(String.valueOf(lat)),
                toRequestBody(String.valueOf(lon)),
                toRequestBody(ts),
                toRequestBody("0 0 0 0 0"),
                toRequestBody(txtNamaLengkap.getText().toString()),
                toRequestBody(txtUmur.getText().toString()),
                toRequestBody(spnJenisKelamin.getSelectedItem().toString()),
                toRequestBody(txtKotaDomisili.getText().toString()),
                toRequestBody(txtNoTelepon.getText().toString()),
                toRequestBody("")
        );

        absentAdd.enqueue(new Callback<RegisterModel>() {
            @Override
            public void onResponse(Call<RegisterModel> call, Response<RegisterModel> response) {
                progressDialog.dismiss();
                RegisterModel status = response.body();
                //Toast.makeText(LoginActivity.this,userList.getToken().toString(),Toast.LENGTH_LONG).show();
                if (status != null) {

                    if (status.getStatus()) {
                        Toast.makeText(RegistrasiActivity.this, "Registrasi Berhasil", Toast.LENGTH_LONG).show();

                        Gson gson = new Gson();
                        String json = gson.toJson(status.getData());
                        SharedPrefUtil.getInstance(RegistrasiActivity.this).put("data_input", json);

                        Intent intent = new Intent(RegistrasiActivity.this, MainActivity.class);
                        startActivity(intent);
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
                        String error = jObjError.get("status_detail").toString();
                        Toast.makeText(RegistrasiActivity.this, error, Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        try {
                            Toast.makeText(RegistrasiActivity.this, "Send Failed, " + response.errorBody().string(), Toast.LENGTH_LONG).show();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        //    Toast.makeText(ShoppingProductGrid.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterModel> call, Throwable t) {
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

    public boolean checkMandatory(){
        boolean pass = true;
        if (TextUtils.isEmpty(txtNamaLengkap.getText().toString())){
            pass = false;
            txtNamaLengkap.setError("Masukkan Nama Lengkap, mandatory");
        }

        if (TextUtils.isEmpty(txtUmur.getText().toString())){
            pass = false;
            txtUmur.setError("Masukkan Umur, mandatory");
        }

        if (TextUtils.isEmpty(txtKotaDomisili.getText().toString())){
            pass = false;
            txtKotaDomisili.setError("Masukkan Kota Domisili, mandatory");
        }

        if (TextUtils.isEmpty(txtNoTelepon.getText().toString()) || !Patterns.PHONE.matcher(txtNoTelepon.getText().toString()).matches()){
            pass = false;
            txtNoTelepon.setError("Masukkan No Telepon dengan format yang benar");
        }

        return pass;
    }

    public void showErrorDialog(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegistrasiActivity.this);
        alertDialog.setTitle("Peringatan");
        alertDialog.setMessage("Mohon isi field yang mandatory")
                .setIcon(R.drawable.ic_close)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(RegistrasiActivity.this, "Cancel ditekan", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }
}