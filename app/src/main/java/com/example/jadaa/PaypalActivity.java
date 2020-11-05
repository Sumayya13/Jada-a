package com.example.jadaa;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    double total =0 , tax =0 ,amountDouble = 0;
    ImageView pImageIv;
    TextView pTitle ,bookPrice, subtotal, Tax ,Total, auther_name ,edition ;


    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal);


        /*---------------------delete app bar ------------------------*/
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Start payPal Services
        Intent intent = new Intent(this,PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // move to previous page using toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        //init views
        pImageIv = findViewById(R.id.bookImage);
        pTitle =(TextView) findViewById(R.id.BookTitle);
        subtotal =(TextView) findViewById(R.id.editAmount);
        Total =(TextView) findViewById(R.id.total);
        Tax=(TextView) findViewById(R.id.tax);
        bookPrice=(TextView) findViewById(R.id.bookPrice);
        btnPayNow = (Button) findViewById(R.id.btnPayNow);
        auther_name =(TextView) findViewById(R.id.auther_name);
        //edition =(TextView) findViewById(R.id.edition);






        // اعرض السعر لليوزر
        final Bundle extras = getIntent().getExtras();
        String uri =extras.getString("pImage");
        String title = extras.getString("pTitle");
        String price =extras.getString("pPrice");
        String edition = extras.getString("pEdition");
        String pAuth =extras.getString("pAuthor");



        if ( extras.getString("pPrice").equals("0"))
            bookPrice.setText("Book is free");
        else
            bookPrice.setText(" "+extras.getString("pPrice")+" SAR");



        pTitle.setText( " "+extras.getString("pTitle"));
        try{
            Picasso.get().load(uri).into(pImageIv);
        }
        catch (Exception e){
        }

        auther_name.setText(  extras.getString("pAuthor"));
      //  edition.setText(" Edition "+extras.getString("pEdition"));


        String amountToShow = "" ;

        amount = extras.getString("pPrice");
        amount = String.valueOf(Float.valueOf(amount))  ;  // بارسلها للبيمنت سستم
       // amountToShow = amount +" $"  ;// باعرضها لليوزر
       // subtotal.setText(amountToShow);


        // calculate subtotal
        amountDouble = (double) Math.round( Double.valueOf(amount)/3.75 *100 ) / 100;
        subtotal.setText("$ "+amountDouble);


        // calculate tax 5%
        tax = (double) Math.round(amountDouble *0.05 *100 ) / 100;
        Tax.setText("$ "+tax);


        // calculate total
        total = (double) Math.round((amountDouble+tax) *100 ) / 100;
        Total.setText("$ "+total);

       // amountDouble = (double) Math.round(amount * 100) / 100;


        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPayment();
            }
        });
    }

    private void processPayment() {


        //amount=edtAmount.getText().toString();
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(total)),"USD",        ////////// change

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

                        //Date & Time
                        Calendar calFordDate = Calendar.getInstance();
                        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                        String purchaseDate = currentDate.format(calFordDate.getTime());

                        Calendar calFordTime = Calendar.getInstance();
                        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
                        String purchaseTime  = currentTime.format(calFordDate.getTime());


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