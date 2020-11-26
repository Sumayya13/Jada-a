package com.example.jadaa.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jadaa.PostDetail;
import com.example.jadaa.R;
import com.example.jadaa.User;
import com.example.jadaa.ViewMyPostActivity;
import com.example.jadaa.ViewPostActivity;
import com.example.jadaa.models.ModelPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterPosts extends RecyclerView.Adapter<AdapterPosts.MyHolder>{

   // String uid,pTitle,pDescription,pAuthor,pEdition,pImage,pPrice,pStatus,pCollege,pDate,pTime,pPublisher;
    Context context;
    List<ModelPost> postList;

  // like
    boolean likechecker=false;
    private DatabaseReference likeRef;

    public AdapterPosts(Context context, List<ModelPost> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        //inflate layout row_posts.xml
        View view = LayoutInflater.from(context).inflate(R.layout.row_posts,viewGroup,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, int i) {
         //get data
        final String uid = postList.get(i).getUid();
        final String pId = postList.get(i).getpId();
        final String pTitle = postList.get(i).getBookTitle();
        final String pDescription = postList.get(i).getBookDescription();
        final String pAuthor = postList.get(i).getBookAuthor();
        final String pEdition = postList.get(i).getBookEdition();
        final String pImage = (postList.get(i).getBookImage());
        final String pPrice = postList.get(i).getBookPrice();
        final String pStatus = postList.get(i).getBookStatus();
        final String pCollege = postList.get(i).getCollege();
        final String pDate = postList.get(i).getPostDate();
        final String pTime = postList.get(i).getPostTime();
        final String pPublisher = postList.get(i).getPublisher();

        final DatabaseReference firebaseDatabase=FirebaseDatabase.getInstance().getReference("Posts");
        String Postkay=firebaseDatabase.getKey();


        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        myHolder.likeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                likechecker=true;
                likeRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String CuserID=FirebaseAuth.getInstance().getCurrentUser().getUid();
                        if (dataSnapshot.child(pId).hasChild(CuserID)){
                            myHolder.likeBtn.setImageResource(R.drawable.ic_baseline_favorite_24);
                        }else {
                            myHolder.likeBtn.setImageResource(R.drawable.ic_baseline_favorite_border_50);
                        }
                        if (likechecker==true){
                            if (dataSnapshot.child(pId).hasChild(firebaseUser.getUid())){
                                likeRef.child(pId).child(firebaseUser.getUid()).removeValue();
                                likechecker=false;
                            }else {
                                likeRef.child(pId).child(firebaseUser.getUid()).setValue(true);
                                likechecker=false;
                            }}
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        //set data
        myHolder.pTimeTv.setText(pDate+" "+pTime);
        myHolder.pTitle.setText(pTitle);


        if (pPrice.equals("0"))
            myHolder.pDescriptionTv.setText("Book is free");
        else
            myHolder.pDescriptionTv.setText(pPrice+" SAR");


// to show user info
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users").child(uid);

        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    User user = dataSnapshot.getValue(User.class);

                    String name =user.getFullName() ;
                    String image= user.getImage();
                    myHolder.uNameTv.setText(name);

                    try{
                        if (image!= null || image!= ""||image!= " " )
                        Picasso.get().load(user.getImage()).into(myHolder.uPictureIv);
                    }
                    catch (Exception e){
                        myHolder.uPictureIv.setImageDrawable(context.getDrawable(R.drawable.user1));
                    }


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



        myHolder.commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // move data to ViewPostActivity page to view
                Intent viewSingle = new Intent(context, PostDetail.class);
                viewSingle.putExtra("pTitle", pTitle);
                viewSingle.putExtra("pImage",pImage);
                viewSingle.putExtra("pDescription", pDescription);
                viewSingle.putExtra("pAuthor", pAuthor);
                viewSingle.putExtra("pEdition", pEdition);
                viewSingle.putExtra("pPrice", pPrice);
                viewSingle.putExtra("pStatus", pStatus);
                viewSingle.putExtra("pCollege", pCollege);
                viewSingle.putExtra("pDate", pDate);
                viewSingle.putExtra("pTime", pTime);
                viewSingle.putExtra("pPublisher", pPublisher);
                viewSingle.putExtra("uid", uid);
                viewSingle.putExtra("postId", pId);

                context.startActivity(viewSingle);
            }
        });

        myHolder.pTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // move data to ViewPostActivity page to view
                Intent viewSingle = new Intent(context, ViewPostActivity.class);
                viewSingle.putExtra("pTitle", pTitle);
                viewSingle.putExtra("pImage",pImage);
                viewSingle.putExtra("pDescription", pDescription);
                viewSingle.putExtra("pAuthor", pAuthor);
                viewSingle.putExtra("pEdition", pEdition);
                viewSingle.putExtra("pPrice", pPrice);
                viewSingle.putExtra("pStatus", pStatus);
                viewSingle.putExtra("pCollege", pCollege);
                viewSingle.putExtra("pDate", pDate);
                viewSingle.putExtra("pTime", pTime);
                viewSingle.putExtra("pPublisher", pPublisher);
                viewSingle.putExtra("uid", uid);
                viewSingle.putExtra("pId", pId);

                context.startActivity(viewSingle);
            }
        });

        myHolder.pImageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // move data to ViewPostActivity page to view
                Intent viewSingle = new Intent(context, ViewPostActivity.class);
                viewSingle.putExtra("pTitle", pTitle);
                viewSingle.putExtra("pImage",pImage);
                viewSingle.putExtra("pDescription", pDescription);
                viewSingle.putExtra("pAuthor", pAuthor);
                viewSingle.putExtra("pEdition", pEdition);
                viewSingle.putExtra("pPrice", pPrice);
                viewSingle.putExtra("pStatus", pStatus);
                viewSingle.putExtra("pCollege", pCollege);
                viewSingle.putExtra("pDate", pDate);
                viewSingle.putExtra("pTime", pTime);
                viewSingle.putExtra("pPublisher", pPublisher);
                viewSingle.putExtra("uid", uid);
                viewSingle.putExtra("pId", pId);

                context.startActivity(viewSingle);
            }
        });

        myHolder.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String value = "الآن يمكنك الحصول على كتاب/ملزمة"+pTitle+"وبسعر/n"+pPrice+"سارع بالحصول عليه";
                intent.putExtra(Intent.EXTRA_TEXT,value);
                context.startActivity(intent.createChooser(intent,"share via"));
            }
        });

        myHolder.pDescriptionTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // move data to ViewPostActivity page to view
                Intent viewSingle = new Intent(context, ViewPostActivity.class);
                viewSingle.putExtra("pTitle", pTitle);
                viewSingle.putExtra("pImage", pImage);
                viewSingle.putExtra("pDescription", pDescription);
                viewSingle.putExtra("pAuthor", pAuthor);
                viewSingle.putExtra("pEdition", pEdition);
                viewSingle.putExtra("pPrice", pPrice);
                viewSingle.putExtra("pStatus", pStatus);
                viewSingle.putExtra("pCollege", pCollege);
                viewSingle.putExtra("pDate", pDate);
                viewSingle.putExtra("pTime", pTime);
                viewSingle.putExtra("pPublisher", pPublisher);
                viewSingle.putExtra("uid", uid);
                viewSingle.putExtra("pId", pId);

                context.startActivity(viewSingle);
            }
        });


        try{
            Picasso.get().load(pImage).into(myHolder.pImageIv);
        }
        catch (Exception e){
        }





    }

    @Override
    public int getItemCount() {
        return postList.size();
    }


    //view holder class
    class MyHolder extends RecyclerView.ViewHolder{

        //views from row_posts.xml
        ImageView uPictureIv, pImageIv;
        TextView uNameTv, pTimeTv, pTitle, pDescriptionTv;
        Button favoriteBtn;
        ImageView commentBtn;
        ImageButton moreBtn,likeBtn,shareBtn;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //init views
            uPictureIv = itemView.findViewById(R.id.uPictureIv);
            pImageIv = itemView.findViewById(R.id.pImageIv);
            uNameTv = itemView.findViewById(R.id.uNameTv);
            pTimeTv = itemView.findViewById(R.id.pTimeTv);
            pTitle = itemView.findViewById(R.id.pTitle);
            pDescriptionTv = itemView.findViewById(R.id.pDescriptionTv);
            moreBtn = itemView.findViewById(R.id.moreBtn);
            commentBtn = itemView.findViewById(R.id.commentBtn1);
            shareBtn=itemView.findViewById(R.id.SharaBtn);


            // like
            likeBtn=itemView.findViewById(R.id.favoraiteBtn);
            likeRef=FirebaseDatabase.getInstance().getReference().child("fav");
        }
    }
}//adapter class
