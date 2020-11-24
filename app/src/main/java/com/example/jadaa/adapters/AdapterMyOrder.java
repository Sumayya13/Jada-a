package com.example.jadaa.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jadaa.HomeActivity;
import com.example.jadaa.MainActivity;
import com.example.jadaa.MyOrderActivity;
import com.example.jadaa.MyOrderDetailsActivity;
import com.example.jadaa.R;
import com.example.jadaa.User;
import com.example.jadaa.ViewPostActivity;
import com.example.jadaa.models.ModelPost;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AdapterMyOrder extends RecyclerView.Adapter<AdapterMyOrder.MyHolder>{

   // String uid,pTitle,pDescription,pAuthor,pEdition,pImage,pPrice,pStatus,pCollege,pDate,pTime,pPublisher;
    Context context;
    List<soldBooks> postList;

    //Posting progress bar
    ProgressDialog pd;

     String name ="" ;
     String email="";
     String phone="";

    String description_val="",selectedItem="";

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public AdapterMyOrder(Context context, List<soldBooks> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        //inflate layout row_posts.xml
        View view = LayoutInflater.from(context).inflate(R.layout.row_myorder,viewGroup,false);
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
        final String BookAuthor = postList.get(i).getBookAuthor();
        final String BookEdition= postList.get(i).getBookEdition();
        final String TotalPayment= postList.get(i).getTotalPayment();
        final String release1= postList.get(i).getResale();

        // بأعرض آخر حالة للطلب
        if (delivered.equals("1")) {
            myHolder.orderstatusT.setText("Delivered");

        }
        else if (inTransit.equals("1")) {
            myHolder.orderstatusT.setText("In Transit");
        }
        else if (shipped.equals("1")) {
            myHolder.orderstatusT.setText("Shipped");
        }
        else
        if (processing.equals("1")) {
            myHolder.orderstatusT.setText("Processing");
        }else  myHolder.orderstatusT.setText("ordered");




        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("soldBooks").child(pId);

        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    soldBooks user = dataSnapshot.getValue(soldBooks.class);

                    String confirmation =user.getOrderConfirmation() ;

                    if (orderConfirmation.equals("1"))
                    {

                        myHolder.confirm_order.setClickable(false);
                        //myHolder.confirm_order.setBackgroundColor(FFD4D9DD);
                        myHolder.confirm_order.setBackgroundColor(Color.parseColor("#E6E6E6"));
                        myHolder.confirm_order.setTextColor(Color.parseColor("#888888"));


                    }


                    if (orderConfirmation.equals("0")) {

                        myHolder.confirm_order.setVisibility(View.VISIBLE);
                        myHolder.confirm_order.setClickable(true);
                        myHolder.confirm_order.setBackgroundColor(Color.parseColor("#1473BD"));
                        myHolder.confirm_order.setTextColor(Color.parseColor("#F8F5F5"));

                    }





                    if (release1.equals("1"))
                    {
                        myHolder.resale.setClickable(false);
                        //myHolder.confirm_order.setBackgroundColor(FFD4D9DD);
                        myHolder.resale.setBackgroundColor(Color.parseColor("#E6E6E6"));
                        myHolder.resale.setTextColor(Color.parseColor("#888888"));


                    }


                    if (release1.equals("0")) {
                        myHolder.resale.setVisibility(View.VISIBLE);
                        myHolder.resale.setClickable(true);
                        myHolder.resale.setBackgroundColor(Color.parseColor("#1473BD"));
                        myHolder.resale.setTextColor(Color.parseColor("#F8F5F5"));

                    }





                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


        myHolder.confirm_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (orderConfirmation.equals("0")) { // صفر يعني لم يتم تأكيد الطلب بعد

                    android.app.AlertDialog.Builder alertDialogBilder = new AlertDialog.Builder(context);
                   // alertDialogBilder.setTitle("Log out");
                    alertDialogBilder.setMessage("Are you sure you want to confirm the order?")
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // close the dialog
                                }
                            })
                            .setPositiveButton("yes", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialogInterface, int id) {

                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("soldBooks");
                                    ref.child(pId).child("orderConfirmation").setValue("1");
                                    ref.child(pId).child("processing").setValue("1");
                                    ref.child(pId).child("shipped").setValue("1");
                                    ref.child(pId).child("inTransit").setValue("1");
                                    ref.child(pId).child("delivered").setValue("1");
                                    Toast.makeText(context,"The order has been confirmed",Toast.LENGTH_SHORT).show();                                }
                            });


                    AlertDialog alertDialog = alertDialogBilder.create();
                    alertDialog.show();



                }


            }
        });



        pd = new ProgressDialog(context);

        myHolder.resale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (release1.equals("0")) { // صفر يعني لم يتم تأكيد الطلب بعد

                    android.app.AlertDialog.Builder alertDialogBilder = new AlertDialog.Builder(context);
                    // alertDialogBilder.setTitle("Log out");
                    alertDialogBilder.setMessage("Are you sure you want to resell the book?")
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // close the dialog
                                }
                            })
                            .setPositiveButton("yes", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialogInterface, int id) {

                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("soldBooks");
                                    ref.child(pId).child("Resale").setValue("1");


                                    //Date & Time
                                    Calendar calFordDate = Calendar.getInstance();
                                    SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                                    String saveCurrentDate = currentDate.format(calFordDate.getTime());

                                    Calendar calFordTime = Calendar.getInstance();
                                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
                                    String saveCurrentTime = currentTime.format(calFordDate.getTime());

                                    pd.setMessage("Posting...");
                                    pd.show();

                                    //for posting image name, post id, post publish time
                                    final String timeStamp = String.valueOf(System.currentTimeMillis());

                                    final FirebaseUser thisUser = FirebaseAuth.getInstance().getCurrentUser();




                                 // اجيب معلومات اليوزر

                                    DatabaseReference ref5 = FirebaseDatabase.getInstance().getReference("users").child(thisUser.getUid());

                                    // Attach a listener to read the data at our posts reference
                                    ref5.addValueEventListener(new ValueEventListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            if (dataSnapshot.getValue() != null) {
                                                User user = dataSnapshot.getValue(User.class);

                                               name =user.getFullName() ;
                                               email= user.getEmail();
                                               phone = user.getPhone();

                                            }

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });



                                    // اجيب معلومات اليوزر


                                    DatabaseReference ref6 = FirebaseDatabase.getInstance().getReference("Post").child(pId);

                                    // Attach a listener to read the data at our posts reference
                                    ref6.addValueEventListener(new ValueEventListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            if (dataSnapshot.getValue() != null) {
                                                ModelPost post = dataSnapshot.getValue(ModelPost.class);

                                                selectedItem = post.getCollege();
                                                description_val= post.getBookDescription();

                                            }

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });


/// اسوي اد بوست جديد


                                    HashMap<Object, String> hashMap = new HashMap<>();
                                    //put post info
                                    hashMap.put("uid", thisUser.getUid());

                                    //hashMap.put("Publisher", name);
                                    // hashMap.put("uEmail", email);
                                    hashMap.put("pId", timeStamp);
                                    hashMap.put("BookTitle", BookTitle);
                                    hashMap.put("BookDescription", description_val);
                                    hashMap.put("BookStatus", "available");
                                    hashMap.put("BookPrice", BookPrice);
                                    hashMap.put("BookAuthor", BookAuthor);
                                    hashMap.put("BookEdition", BookEdition);
                                    hashMap.put("College", selectedItem);
                                    hashMap.put("BookImage", uri);
                                    hashMap.put("pTime", timeStamp);
                                    hashMap.put("PostDate", saveCurrentDate);
                                    hashMap.put("PostTime", saveCurrentTime);
                                    hashMap.put("BookID",pId); // البوست القديم


// أضفت الجديد البوست بكلاس البوست
                                    DatabaseReference ref10 = FirebaseDatabase.getInstance().getReference("Posts");
                                    //put data in this ref
                                    ref10.child(timeStamp).setValue(hashMap);
                                    pd.dismiss();


                                    // BookID احط كل الايدي لكل اللي باعوا الكتاب
                                    DatabaseReference ref100 = FirebaseDatabase.getInstance().getReference("sellers");
                                    ref100.child(pId).child(thisUser.getUid());




                                    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("users").child(thisUser.getUid());
                                    final boolean[] flag = {true};
                                    // Attach a listener to read the data at our posts reference
                                    ref2.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {


                                            // count num of all books
                                            if (flag[0]) {
                                                if (dataSnapshot.getValue() != null) {
                                                    User user = dataSnapshot.getValue(User.class);

                                                    String numAllBooks = user.getNumAllBooks();
                                                    int numAllBooksInt = Integer.valueOf(numAllBooks);
                                                    numAllBooksInt = numAllBooksInt + 1;


                                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
                                                    ref.child(thisUser.getUid()).child("numAllBooks").setValue(String.valueOf(numAllBooksInt));
                                                    flag[0] = false;
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            System.out.println("The read failed: " + databaseError.getCode());
                                        }
                                    });

                                    Toast.makeText(context,"The order has been resold",Toast.LENGTH_SHORT).show();
                                }
                            });


                    AlertDialog alertDialog = alertDialogBilder.create();
                    alertDialog.show();






                }


            }
        });




        //set data
        myHolder.BookTitle.setText(BookTitle);
        myHolder.order_date.setText(purchaseDate + " " + purchaseTime);


        if (BookPrice.equals("0"))
            myHolder.bookPrice.setText("Book is free");
        else
            myHolder.bookPrice.setText(BookPrice + " SAR");

        try {
            Picasso.get().load(uri).into(myHolder.bookImage);
        } catch (Exception e) {
        }




        // بأعرض اللون للحالة الطلب

        if (delivered.equals("1"))
        {
            myHolder.deliveredT.setImageDrawable(context.getResources().getDrawable(R.drawable.delivered__status));
        }else myHolder.deliveredT.setVisibility(View.INVISIBLE); // not delivered yet


        if (inTransit.equals("1"))
        {
            myHolder.in_transitT.setImageDrawable(context.getResources().getDrawable(R.drawable.intransit_status));
        }else myHolder.in_transitT.setVisibility(View.INVISIBLE); // not in Transit yet


            if (shipped.equals("1"))  // not in transit yet
        {
            myHolder.shippedT.setImageDrawable(context.getResources().getDrawable(R.drawable.shipped__status));
        }else myHolder.shippedT.setVisibility(View.INVISIBLE); // not in Transit yet



        if (processing.equals("1"))  // not shipped yet
        {
            myHolder.processingT.setImageDrawable(context.getResources().getDrawable(R.drawable.processing_status));
        }else    myHolder.processingT.setVisibility(View.INVISIBLE);// not in Transit yet




        myHolder.order_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent viewSingle = new Intent(context, MyOrderDetailsActivity.class);

                viewSingle.putExtra("pId", pId);
                viewSingle.putExtra("purchaseDate", purchaseDate);
                viewSingle.putExtra("purchaseTime", purchaseTime);
                viewSingle.putExtra("processing", processing);
                viewSingle.putExtra("shipped", shipped);
                viewSingle.putExtra("inTransit", inTransit);
                viewSingle.putExtra("delivered", delivered);
                viewSingle.putExtra("uri", uri);
                viewSingle.putExtra("BookTitle", BookTitle);
                viewSingle.putExtra("BookPrice", BookPrice);
                viewSingle.putExtra("BookAuthor", BookAuthor);
                viewSingle.putExtra("BookEdition", BookEdition);
                viewSingle.putExtra("TotalPayment", TotalPayment);

                context.startActivity(viewSingle);

                }

        });

/*
// لازم اغيرها بحيث اجيب سناب شوت واشوف القيمة
        Query fquery = FirebaseDatabase.getInstance().getReference("soldBooks").child(pId).orderByChild("delivered").equalTo("0");
        fquery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myHolder.confirm_order.setVisibility(View.INVISIBLE);
                myHolder.confirm_order.setClickable(false);

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

        //views from row_posts.xml
        ImageView processingT ,shippedT,in_transitT,deliveredT;
        ImageView bookImage ,order_detail2;
        TextView order_status , order_date ,BookTitle ,bookPrice,confirm_order,order_detail,orderstatusT ,total,resale;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //init views
            processingT = itemView.findViewById(R.id.processing);
            shippedT = itemView.findViewById(R.id.shipped);
            in_transitT = itemView.findViewById(R.id.in_transit);
            deliveredT = itemView.findViewById(R.id.delivered);
            bookImage = itemView.findViewById(R.id.bookImage);
            order_detail2 = itemView.findViewById(R.id.order_detail2);
            order_date = itemView.findViewById(R.id.order_date);
            BookTitle = itemView.findViewById(R.id.BookTitle);
            bookPrice = itemView.findViewById(R.id.bookPrice);
            confirm_order = itemView.findViewById(R.id.confirm_order);
            order_detail = itemView.findViewById(R.id.order_detail);
            orderstatusT = itemView.findViewById(R.id.orderstatus);
            total= itemView.findViewById(R.id.total);
            resale = itemView.findViewById(R.id.resale);

        }
    }
}//adapter class
