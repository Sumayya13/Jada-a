package com.example.jadaa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class myCustomersDetails extends AppCompatActivity {

    TextView Tvorder_ID ,TvBookTitle ,Tvauther_name ,Tvedition ,TvbookPrice ,TvpurchaserName ,TvpurchaserPhone , Tvorder_date ;
    String purchaserPhone ,purchaserName;
    ImageView IvbookImage;

    LinearLayout LLProcessing  , LLshipped ,LLinTransit, LLdelivered;
    TextView TvProcessing , Tvshipped , TvinTransit, Tvdelivered;
    ImageView IvProcessing , Ivshipped ,IvinTransit ,  Ivdelivered;

    String purchaseDate,purchaseTime, processing,shipped,inTransit,delivered,uri,BookPrice,BookTitle,BookAuthor,BookEdition;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_customers_details);


        /*---------------------delete app bar ------------------------*/
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // ------------------------tool bar ------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // move to previous page using toolbar
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Tvorder_ID = (TextView)findViewById(R.id.order_ID) ;
        IvbookImage = findViewById(R.id.bookImage);
        TvBookTitle = (TextView)findViewById(R.id.BookTitle) ;
        Tvauther_name = (TextView)findViewById(R.id.auther_name) ;
        Tvedition = (TextView)findViewById(R.id.edition) ;
        TvbookPrice = (TextView)findViewById(R.id.bookPrice) ;
        TvpurchaserName = (TextView)findViewById(R.id.purchaserName) ;
        TvpurchaserPhone = (TextView)findViewById(R.id.purchaserPhone) ;
        Tvorder_date = (TextView)findViewById(R.id.order_date);

        // ----------------------order status--------
        LLProcessing = findViewById(R.id.LLProcessing);
        LLshipped = findViewById(R.id.LLshipped);
        LLinTransit = findViewById(R.id.LLinTransit);
        LLdelivered = findViewById(R.id.LLdelivered);


        IvProcessing = findViewById(R.id.IvProcessing);
        Ivshipped = findViewById(R.id.Ivshipped);
        IvinTransit = findViewById(R.id.IvinTransit);
        Ivdelivered = findViewById(R.id.Ivdelivered);

        TvProcessing = (TextView)findViewById(R.id.TvProcessing) ;
        Tvshipped = (TextView) findViewById(R.id.Tvshipped);
        TvinTransit = (TextView)findViewById(R.id.TvinTransite) ;
        Tvdelivered = (TextView)findViewById(R.id.Tvdelivered) ;



        final String title,des,price,pAuth,uid,pStatus,pImage,pid;


        final Bundle extras = getIntent().getExtras();

        pid = extras.getString("pId");
        purchaseDate = extras.getString("purchaseDate");
        purchaseTime = extras.getString("purchaseTime");
        processing =extras.getString("processing");
        shipped =extras.getString("shipped");
        inTransit = extras.getString("inTransit");
        delivered = extras.getString("delivered");
        uri= extras.getString("uri");
        BookTitle =extras.getString("BookTitle");
        BookPrice =extras.getString("BookPrice");
        BookAuthor = extras.getString("BookAuthor");
        BookEdition =  extras.getString("BookEdition");
        purchaserPhone =  extras.getString("purchaserPhone");
        purchaserName =  extras.getString("purchaserName");



       // لو القيمة ١ تلقائيا لونها بيطلع لاني اضفته في ال xml
        if (processing.equals("0"))
            IvProcessing.setImageDrawable(getApplicationContext().getDrawable(R.drawable.gray));

        if (shipped.equals("0"))
        Ivshipped.setImageDrawable(getApplicationContext().getDrawable(R.drawable.gray));

        if (inTransit.equals("0"))
            IvinTransit.setImageDrawable(getApplicationContext().getDrawable(R.drawable.gray));

        if (delivered.equals("0"))
            Ivdelivered.setImageDrawable(getApplicationContext().getDrawable(R.drawable.gray));


        // أغير اللون من الرصاصي للأزرق
        IvProcessing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (processing.equals("0")) {
                    IvProcessing.setImageDrawable(getApplicationContext().getDrawable(R.drawable.processing_status));
                    processing = "1" ;
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("soldBooks");
                    ref.child(pid).child("processing").setValue(processing);
                }
                else {
                    IvProcessing.setImageDrawable(getApplicationContext().getDrawable(R.drawable.gray));
                    processing = "0" ;
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("soldBooks");
                    ref.child(pid).child("processing").setValue(processing);
                }

            }
        });



       // أغير اللون من الرصاصي للبرتقالي
        Ivshipped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (shipped.equals("0")) {
                    Ivshipped.setImageDrawable(getApplicationContext().getDrawable(R.drawable.shipped__status));
                    shipped = "1" ;
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("soldBooks");
                    ref.child(pid).child("shipped").setValue(shipped);
                }
                   else {
                    Ivshipped.setImageDrawable(getApplicationContext().getDrawable(R.drawable.gray));
                    shipped = "0" ;
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("soldBooks");
                    ref.child(pid).child("shipped").setValue(shipped);
                }

            }
        });


        // أغير اللون من الرصاصي الأصفر
        IvinTransit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (inTransit.equals("0")) {
                    IvinTransit.setImageDrawable(getApplicationContext().getDrawable(R.drawable.intransit_status));
                    inTransit = "1" ;
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("soldBooks");
                    ref.child(pid).child("inTransit").setValue(inTransit);
                }
                else {
                    IvinTransit.setImageDrawable(getApplicationContext().getDrawable(R.drawable.gray));
                    inTransit = "0" ;
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("soldBooks");
                    ref.child(pid).child("inTransit").setValue(inTransit);
                }

            }
        });

        // أغير اللون من الرصاصي الأخصر
        Ivdelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // -----------اشيك عالحالة اللي قبلها ما اقدر اسوي الحالة  الا اذا سويت الحالة اللي قبلها
                if (inTransit.equals("0") || shipped.equals("0")) {
                    Toast.makeText(myCustomersDetails.this,"Choose all status before it" , Toast.LENGTH_SHORT).show();}
else {
                    if (delivered.equals("0")) {
                        Ivdelivered.setImageDrawable(getApplicationContext().getDrawable(R.drawable.delivered__status));
                        delivered = "1";
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("soldBooks");
                        ref.child(pid).child("delivered").setValue(delivered);
                    } else {
                        Ivdelivered.setImageDrawable(getApplicationContext().getDrawable(R.drawable.gray));
                        delivered = "0";
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("soldBooks");
                        ref.child(pid).child("delivered").setValue(delivered);
                    }
                }// else



            }
        });

/*
        // احط الصورة المناسبةلليوزر بناء على حاله الطلب
        if (delivered.equals("")) {
            track.setImageDrawable(getApplicationContext().getDrawable(R.drawable.alldelivered));
        } else {
            if (inTransit.equals("1"))
                track.setImageDrawable(getResources().getDrawable(R.drawable.allintransit));
            else if (shipped.equals("1"))
                track.setImageDrawable(getResources().getDrawable(R.drawable.allshipped));
            else
                track.setImageDrawable(getResources().getDrawable(R.drawable.allprocessing));

        }


 */




        //_____________________ set data
        Tvorder_ID.setText("#"+pid);
        TvBookTitle.setText(BookTitle);
        Tvedition.setText("Edition "+BookEdition);
        Tvauther_name.setText(BookAuthor);

        try{
            Picasso.get().load(uri).into(IvbookImage);
        }
        catch (Exception e){
        }

        if (BookPrice.equals("0"))
            TvbookPrice.setText("Book is free");
        else
            TvbookPrice.setText(BookPrice + " SAR");

        TvpurchaserName.setText(purchaserName);
        TvpurchaserPhone.setText(purchaserPhone);
        Tvorder_date.setText(purchaseDate+" "+purchaseTime);


        //TvpurchaserName.setText(pur);

    }// on create
}