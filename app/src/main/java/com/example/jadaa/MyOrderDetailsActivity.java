package com.example.jadaa;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jadaa.adapters.AdapterPosts;
import com.example.jadaa.models.ModelPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MyOrderDetailsActivity extends AppCompatActivity {


    ImageView TbookImage ,order_detail2 ,track;
    TextView TvpurchaserName ,TvpurchaserPhone ,order_status ,order_dateT ,TBookTitle ,TbookPrice,confirm_order,order_detail,Tedition,Tauther_name,Tcollege,TorderID,orderStatus,total;

    ImageView uPictureIv;
String sellerID;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_details);

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


        TbookImage = findViewById(R.id.bookImage);
        TBookTitle =(TextView) findViewById(R.id.BookTitle);
        Tedition =(TextView) findViewById(R.id.edition);
        Tauther_name =(TextView) findViewById(R.id.auther_name);
        TbookPrice=(TextView) findViewById(R.id.bookPrice);
        Tcollege=(TextView) findViewById(R.id.college);
        TorderID = (TextView) findViewById(R.id.orderID);
        order_dateT= (TextView) findViewById(R.id.order_date);
        track =  findViewById(R.id.track);
        orderStatus =findViewById(R.id.status);
        total=findViewById(R.id.total);


        // seller not purchaserName ,purchaserPhone
        TvpurchaserName = (TextView) findViewById(R.id.purchaserName);
        TvpurchaserPhone = (TextView) findViewById(R.id.purchaserPhone);
        uPictureIv = findViewById(R.id.uPictureIv);

        String title,des,price,pAuth,uid,pStatus,pImage,pid,TotalPayment;
        String purchaseDate,purchaseTime, processing,shipped,inTransit,delivered,uri,BookPrice,BookTitle,BookAuthor,BookEdition;

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
        TotalPayment=  extras.getString("TotalPayment");
        sellerID = extras.getString("sellerID");

        TorderID.setText("#"+pid);  // يشتغل
        order_dateT.setText( purchaseDate+" "+purchaseTime); // يشتغل
        TBookTitle.setText(BookTitle);
        Tedition.setText("Edition "+BookEdition);
        Tauther_name.setText(BookAuthor);
        total.setText("$ "+TotalPayment);

        try{
            Picasso.get().load(uri).into(TbookImage);
        }
        catch (Exception e){
        }

        if (BookPrice.equals("0"))
            TbookPrice.setText("Book is free");
        else
            TbookPrice.setText(BookPrice + " SAR");

        //orderIDT.setText("#"+pid);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users").child(sellerID);

        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    User user = dataSnapshot.getValue(User.class);

                    String name =user.getFullName() ;
                    String phone1 = user.getPhone() ;
                    String image = user.getImage();

                    try{
                        if (image!= null || image!= ""||image!= " " )
                            Picasso.get().load(user.getImage()).into(uPictureIv);
                    }
                    catch (Exception e){
                        uPictureIv.setImageDrawable(getDrawable(R.drawable.user1));
                    }


                    TvpurchaserName.setText(user.getFullName());
                    TvpurchaserPhone.setText(user.getPhone());
                    // System.out.println(post);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });



        // بأعرض اللون للحالة الطلب

        if (delivered.equals("1"))// not delivered yet
        {
            track.setImageDrawable(getResources().getDrawable(R.drawable.allstat2_removebg_preview));
            orderStatus.setText("delivered");
        }else
        if (inTransit.equals("1"))  // not in Transit yet
        {
            track.setImageDrawable(getResources().getDrawable(R.drawable.totransit2_removebg_preview));
            orderStatus.setText("inTransit");
        }else
        if (shipped.equals("1"))  // not shipped yet
        {
            track.setImageDrawable(getResources().getDrawable(R.drawable.ship2));
            orderStatus.setText("shipped");
        }else

        if (processing.equals("1"))  // not shipped yet
        {
            track.setImageDrawable(getResources().getDrawable(R.drawable.proc2));
            orderStatus.setText("processing");
        } else {
            track.setImageDrawable(getResources().getDrawable(R.drawable.order2));
            orderStatus.setText("Ordered");
        }

/*

        // احط الصورة المناسبةلليوزر بناء على حاله الطلب
        if (delivered.equals("1")) {
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


       //orderIDT.setText("#"+pid);













    }
}