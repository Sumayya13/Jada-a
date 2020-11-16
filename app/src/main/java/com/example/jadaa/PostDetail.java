package com.example.jadaa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class PostDetail extends AppCompatActivity {


    // details of users
    String myUid , myEmail="" , myName="" , myDp
            , postId , hisDp, hid, hisName ;

    private DatabaseReference userDbRef;
    private FirebaseAuth mAuth;

    ImageView uPictureIv,pImageIv ;
    TextView nameTv, pTimeTiv , pTitleTv ,pDescriptionTv ;
    EditText commentEt ;
    ImageButton sendBtn ;
    ImageView cAvatarIv ;

    ProgressDialog pd ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        /*---------------------delete app bar ------------------------*/
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Intent intent = getIntent() ;
        postId = intent.getStringExtra("postId");


        uPictureIv = findViewById(R.id.uPictureIv);
        pImageIv = findViewById(R.id.pImageIv);
        nameTv = findViewById(R.id.uNameTv);
        pTimeTiv = findViewById(R.id.pTimeTv);
        pTitleTv = findViewById(R.id.pTitle);

        pDescriptionTv = findViewById(R.id.pDescriptionTv);

        commentEt = findViewById(R.id.commentEt);
        sendBtn = findViewById(R.id.sendBtn);
        cAvatarIv = findViewById(R.id.cAvatarIv);


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

        loadPostInfo();

        sendBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               postComment();
           }
       });




    }// create

    private void loadPostInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        Query query = ref.orderByChild("pId").equalTo(postId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren() ){
                    String pTitle = ""+ds.child("BookTitle").getValue();
                    String pPrice = ""+ds.child("BookPrice").getValue();
                    String Time = ""+ds.child("PostTime").getValue();
                    String Date = ""+ds.child("PostDate").getValue();
                    String uri = ""+ds.child("BookImage").getValue();
                    String uid = ""+ds.child("uid").getValue();
                    // Get a reference to our posts

// to show user data

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference("users").child(uid);

                    // Attach a listener to read the data at our posts reference
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.getValue() != null) {
                                User user = dataSnapshot.getValue(User.class);

                                String name =user.getFullName() ;

                                nameTv.setText(user.getFullName());

                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });



                   // set data
                    pTitleTv.setText(pTitle);

                    if (pPrice.equals("0"))
                        pDescriptionTv.setText("Book is free");
                    else
                        pDescriptionTv.setText("  "+pPrice+" SAR");

                    pTimeTiv.setText(Date+" "+Time );

                    try{
                        Picasso.get().load(uri).into(pImageIv);

                    }
                    catch (Exception e){
                    }




                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void postComment() {

        pd = new ProgressDialog(this);
        pd.setMessage("Adding comment...");

// get data from comment edit text
        final String comment = commentEt.getText().toString().trim();
        // validate

        //Date & Time
        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        String saveCurrentDate = currentDate.format(calFordDate.getTime());

        Calendar calFordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        String saveCurrentTime = currentTime.format(calFordDate.getTime());



        if (TextUtils.isEmpty(comment)){
            // No value
            Toast.makeText(PostDetail.this, "Comment is empty...", Toast.LENGTH_SHORT).show();
            return;
        }


        final FirebaseUser thisUser = FirebaseAuth.getInstance().getCurrentUser();
        myUid = thisUser.getUid();

        final FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        DatabaseReference ref1 = database1.getReference("users").child(myUid);

        // Attach a listener to read the data at our posts reference
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    User user = dataSnapshot.getValue(User.class);

                    myName  =user.getFullName() ;
                    myEmail =user.getEmail() ;

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });




        final String timeStamp = String.valueOf(System.currentTimeMillis());

        // each child have child comment
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("Comment");

        HashMap<Object, String> hashMap = new HashMap<>();
        //put post info

        hashMap.put("cId",timeStamp);
        hashMap.put("comment",comment);
        hashMap.put("CommentDate",saveCurrentDate);
        hashMap.put("commentTime",saveCurrentTime);
        hashMap.put("timeStamp",timeStamp);
        hashMap.put("uid",myUid);
       //hashMap.put("uEmail",myEmail);
        //hashMap.put("uName",myName);

        ref.child(timeStamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
// added
                        pd.dismiss();
                        Toast.makeText(PostDetail.this, "Comment Added...", Toast.LENGTH_SHORT).show();
                        commentEt.setText("");
                        // updadteCommentCount();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
// not added
                        pd.dismiss();
                        Toast.makeText(PostDetail.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });







    }
}