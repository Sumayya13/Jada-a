package com.example.jadaa;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jadaa.Config.Config;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.HashMap;

public class PaypalActivity extends AppCompatActivity {

// payment system ________________________________    PAYMENT System

    private static final int PAYPAL_REQUEST_CODE = 7171; // 2
    private static PayPalConfiguration config = new PayPalConfiguration() // 3
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX) // use sandbox becuese we on test
            .clientId(Config.PAYPAL_CLIENT_ID);


    Button btnPayNow ;
    TextView edtAmount ;
    String amount ="";

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal);

        // Start payPal Services
        Intent intent = new Intent(this,PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);




        btnPayNow = (Button) findViewById(R.id.btnPayNow);
        edtAmount = (TextView) findViewById(R.id.editAmount);

        // اعرض السعر لليوزر
        final Bundle extras = getIntent().getExtras();
        String amountToShow = "" ;
        amount = extras.getString("pPrice");
        amount = String.valueOf(Float.valueOf(amount)/3.75)  ;  // بارسلها للبيمنت سستم
        amountToShow = amount +" $"  ;// باعرضها لليوزر
        edtAmount.setText(amountToShow);

        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPayment();
            }
        });
    }

    private void processPayment() {


        //amount=edtAmount.getText().toString();
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)),"USD",        ////////// change

                "pay for book",PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);

    }// End processPayment()


    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == PAYPAL_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK) {

                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try { // اودي مشتري كتاب لصفحة تعرض النتيجة
                        String paymentDetails = confirmation.toJSONObject().toString(4);


                        // اغير حالة البوست في الداتابيس من متاح إلى تم بيعه
                        final Bundle extras = getIntent().getExtras();
                        final String pid = extras.getString("pId");
                        final String buyer = extras.getString("uid");
                        final FirebaseUser thisUser = FirebaseAuth.getInstance().getCurrentUser();

                        // حدثت حالة الكتاب
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
                        ref.child(pid).child("BookStatus").setValue("SOLD");

                        // خزنت آي دي البائع
                        ref.child(pid).child("BuyerID").setValue(buyer);
                        //خزنت آي دي المشتري
                        ref.child(pid).child("PurchaserID").setValue(thisUser.getUid());




                        // اخزن في الداتابيس آي دي الكتاب و البائع والمشتري





                        startActivity(new Intent(this, PaymentDetails.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", amount)
                        );

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }// confirmation != null
                }// RESULT_OK
            else if (resultCode == Activity.RESULT_CANCELED)
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();

        }// PAYPAL_REQUEST_CODE
        else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();

    }
}