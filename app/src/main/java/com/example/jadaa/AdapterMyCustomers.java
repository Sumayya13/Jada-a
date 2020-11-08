package com.example.jadaa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jadaa.R;
import com.example.jadaa.models.soldBooks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterMyCustomers extends RecyclerView.Adapter<AdapterMyCustomers.MyHolder>{

   // String uid,pTitle,pDescription,pAuthor,pEdition,pImage,pPrice,pStatus,pCollege,pDate,pTime,pPublisher;
    Context context;
    List<soldBooks> postList;


    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    String customerName , customerPhone ;

    public AdapterMyCustomers(Context context, List<soldBooks> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        //inflate layout row_posts.xml
        View view = LayoutInflater.from(context).inflate(R.layout.row_my_customers,viewGroup,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, int i) {
        //get data

        final String BookPrice = postList.get(i).getBookPrice();
        final String BookTitle = postList.get(i).getBookTitle();
        final String delivered = postList.get(i).getDelivered();
        final String inTransit = postList.get(i).getInTransit();
        final String pId = postList.get(i).getpId();
        final String processing = postList.get(i).getProcessing();
        final String purchaseDate = postList.get(i).getPurchaseDate();
        final String purchaseTime = postList.get(i).getPurchaseTime();
        final String purchaserID = postList.get(i).getPurchaserID();
        final String sellerID = postList.get(i).getSellerID();
        final String shipped = postList.get(i).getShipped();
        final String uri = postList.get(i).getUri();
        final String orderConfirmation = postList.get(i).getOrderConfirmation();



        //set data
        myHolder.TBookTitle.setText(BookTitle);
        myHolder.Torder_ID.setText("#"+pId);

        if (BookPrice.equals("0"))
            myHolder.TbookPrice.setText("Book is free");
        else
            myHolder.TbookPrice.setText(BookPrice + " SAR");

        try {
            Picasso.get().load(uri).into(myHolder.TbookImage);
        } catch (Exception e) {
        }


        //--------------view Customer's information-----------------
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");



/*
     Query query = databaseReference.child(purchaserID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //get Data
                    customerName = "" + ds.child("fullName").getValue();
                    customerPhone = "" + ds.child("phone").getValue();


                    myHolder.purchaserName.setText(customerName);
                    myHolder.purchaserPhone.setText(customerPhone);
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
 */

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }


    //view holder class
    class MyHolder extends RecyclerView.ViewHolder{

        //views from row_my_customers.xml

        TextView Torder_ID,TBookTitle,TbookPrice,TpurchaserName,TpurchaserPhone,Torder_detail,Torder_detail2,Torder_date;
        ImageView TbookImage;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //init views
            Torder_ID = itemView.findViewById(R.id.order_ID);
            TbookImage= itemView.findViewById(R.id.bookImage);
            TBookTitle = itemView.findViewById(R.id.BookTitle);
            TbookPrice = itemView.findViewById(R.id.bookPrice);
            TpurchaserName = itemView.findViewById(R.id.purchaserName);
            TpurchaserPhone = itemView.findViewById(R.id.purchaserPhone);
            Torder_detail2 = itemView.findViewById(R.id.order_detail2);
            Torder_detail = itemView.findViewById(R.id.order_detail);
            Torder_date  = itemView.findViewById(R.id.order_date);

        }
    }
}//adapter class
