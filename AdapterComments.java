package com.example.jadaa.adapters;

import android.app.AlertDialog;
import android.app.MediaRouteButton;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jadaa.MainActivity;
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
import com.google.firebase.database.Query;
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
        final DatabaseReference commentRef= FirebaseDatabase.getInstance().getReference("Posts").child("postId").child("Comment");

myHolder.pTimeTv.setText(commentDate+"  "+commentTime);
myHolder.pTitle.setText(comment);



// to view or hide delete button
        final FirebaseUser thisUser = FirebaseAuth.getInstance().getCurrentUser();
       if( !uid.equals(thisUser.getUid())) {
            myHolder.deleteCommentIv.setVisibility(View.INVISIBLE);
            myHolder.deleteCommentIv.setClickable(false);
        }//else {
        final String pId;
        //final Bundle extras = getIntent().getExtras();
        //pId = extras.getString("postId");
            //myHolder.deleteCommentIv.setVisibility(View.VISIBLE);
            //myHolder.deleteCommentIv.setClickable(true);
            myHolder.deleteCommentIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (uid.equals(thisUser.getUid())) {
                        myHolder.deleteCommentIv.setVisibility(View.VISIBLE);
                        myHolder.deleteCommentIv.setClickable(true);
                        android.app.AlertDialog.Builder alertDialogBilder = new AlertDialog.Builder(context);
                        alertDialogBilder.setTitle("Delete Comment");
                        alertDialogBilder.setMessage("Are you sure you want to delet comment?")
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // close the dialog
                                    }
                                })
                                .setPositiveButton("yes", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int id) {
                                        Query fQuery = FirebaseDatabase.getInstance().getReference("Posts").child("pId").child("Comment").child(cId);
                                        fQuery.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                    ds.getRef().removeValue();
                                                }
                                                Toast.makeText(context, "Delete it successfully", Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                });


                        AlertDialog alertDialog = alertDialogBilder.create();
                        alertDialog.show();

                        //
                       /* AlertDialog.Builder alertDialogBilder = new AlertDialog.Builder(context);
                        alertDialogBilder.setMessage("do you want to delete this comment? ")
                                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })//how
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Query fQuery = FirebaseDatabase.getInstance().getReference("Posts").child("postId").child("Comment").child(cId);
                                        fQuery.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                    ds.getRef().removeValue();
                                                }
                                                Toast.makeText(context, "Delete it successfully", Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                        //
                                    }
                                });
                   /* commentRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshothh) {
                            for (DataSnapshot messageSnapshoti : snapshothh.getChildren()) {
                                ModelComment cobj = messageSnapshoti.getValue(ModelComment.class);
                                final String cobjId = cobj.getcId();
                                if (cobj.getUid().equals(uid)) {
                                    if (cobj.getcId().equals(cId)) {
                                        AlertDialog alertDialog = new AlertDialog.Builder(context)
                                                .setTitle("are you sure?")
                                                .setMessage("do you want to delete this comment? ")
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        deleteNote(cobjId);
                                                    }
                                                })
                                                .setNegativeButton("No", null)
                                                .show();
                                    }
                                }
                            }
                        }

                        public void deleteNote(String noteKey) {
                            commentRef.child(noteKey.trim()).removeValue();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });*/
                    }
                }
            });
        //}

            ///




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
        ImageView uPictureIv;
        ImageButton deleteCommentIv;
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



//Ghada
