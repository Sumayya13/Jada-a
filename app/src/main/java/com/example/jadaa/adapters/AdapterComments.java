package com.example.jadaa.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jadaa.PostDetail;
import com.example.jadaa.R;
import com.example.jadaa.User;
import com.example.jadaa.ViewPostActivity;
import com.example.jadaa.models.ModelComment;
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

public class AdapterComments extends RecyclerView.Adapter<AdapterComments.MyHolder>{

   // String uid,pTitle,pDescription,pAuthor,pEdition,pImage,pPrice,pStatus,pCollege,pDate,pTime,pPublisher;
    Context context;
    List<ModelComment> postList;


    public AdapterComments(Context context, List<ModelComment> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        //inflate layout row_posts.xml
        View view = LayoutInflater.from(context).inflate(R.layout.row_comments,viewGroup,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, int i) {



         //get data
        final String uid = postList.get(i).getUid();
        final String comment = postList.get(i).getComment();
        final String cId = postList.get(i).getcId();
        final String commentTime = postList.get(i).getCommentTime();
        final String commentDate = postList.get(i).getCommentDate();


myHolder.pTimeTv.setText(commentDate+"  "+commentTime);
myHolder.pTitle.setText(comment);



// to view or hide delete button
        final FirebaseUser thisUser = FirebaseAuth.getInstance().getCurrentUser();
        if( !uid.equals(thisUser.getUid())) {
            myHolder.deleteCommentIv.setVisibility(View.INVISIBLE);
            myHolder.deleteCommentIv.setClickable(false);
        }



// to show user info
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users").child(uid);

        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    User user = dataSnapshot.getValue(User.class);

                    String name =user.getFullName() ;

                    myHolder.uNameTv.setText(name);


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });




    }

    @Override
    public int getItemCount() {
        return postList.size();
    }


    //view holder class
    class MyHolder extends RecyclerView.ViewHolder{

        //views from row_posts.xml
        ImageView uPictureIv,deleteCommentIv ;
        TextView uNameTv, pTimeTv, pTitle;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //init views
            uPictureIv = itemView.findViewById(R.id.uPictureIv);
            uNameTv = itemView.findViewById(R.id.uNameTv);
            pTimeTv = itemView.findViewById(R.id.pTimeTv);
            pTitle = itemView.findViewById(R.id.comment);
            deleteCommentIv =itemView.findViewById(R.id.deleteComment);

        }
    }
}//adapter class
