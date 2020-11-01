package com.example.jadaa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentDetails extends AppCompatActivity {


    TextView txtId, txtAmount , txtStatus ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);


        txtId = (TextView)findViewById(R.id.txtId);
        txtAmount = (TextView)findViewById(R.id.txtAmount);
        txtStatus = (TextView)findViewById(R.id.txtStatus);


        // get Intent

        Intent intent = getIntent() ;


        try {

            // اسبقبل الاوبجكت الللي ارسلته في صفحة فيو بوست بعد الدفع
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("PaymentDetails"));
            showDetails(jsonObject.getJSONObject("response") ,intent.getStringExtra("PaymentAmount"));

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }// on craeete



    public void showDetails(JSONObject response, String paymentAmount){

        try {
            txtId.setText(response.getString("id"));
            txtStatus.setText(response.getString("state"));
            txtAmount.setText(response.getString(String.format("$%s",paymentAmount)));


        } catch (JSONException e) {

        }
    }








    }// class