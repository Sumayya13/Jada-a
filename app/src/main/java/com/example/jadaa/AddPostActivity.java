package com.example.jadaa;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Pattern;


public class AddPostActivity extends AppCompatActivity {

    private ImageView bookImage;
    private TextView heading, status,paymentOption;
    private EditText BookTitle, bookPrice,description, BookAuthor, BookEdition;
    private Button clear,post;
    private RadioGroup StatusRadioGroup,paymentRadioGroup;
    private RadioButton radioAvailable, radioUnavailable, radioForFree, radioGetPaid;
    private static final int GALLERY_REQUEST = 1;
    private Uri imageUri;
    private DatabaseReference mDatabase, userRef;
    private StorageReference mStorage;
    private FirebaseAuth mAuth;
    private String currentUserId;
    private ProgressDialog progressDialog;
    private String selectedItem ="";
    private String saveCurrentDate, saveCurrentTime;
    private String userName = "";
   DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);


        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
        userRef= FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId);
        mStorage = FirebaseStorage.getInstance().getReference();



        bookImage = (ImageView)findViewById(R.id.bookImage);
        BookTitle =(EditText)findViewById(R.id.BookTitle);
        bookPrice =(EditText)findViewById(R.id.bookPrice);
        description =(EditText)findViewById(R.id.description);
        BookAuthor =(EditText)findViewById(R.id.auther_name);
        BookEdition =(EditText)findViewById(R.id.edition);
        paymentRadioGroup = findViewById(R.id.paymentRadioGroup);
        radioForFree = findViewById(R.id.radio_forFree);
        radioGetPaid = findViewById(R.id.radio_getPaid);
        post = findViewById(R.id.post);
        Spinner college =(Spinner) findViewById(R.id.mySpinner);
        progressDialog = new ProgressDialog(this);


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



        // if user choose a collage I will save position in string to save it in database
        college.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedItem = adapterView.getItemAtPosition(position).toString();
                //Toast.makeText(AddPostActivity.this, selectedItem, Toast.LENGTH_SHORT).show();




            }
            //         // if user does not choose any one I will print msg
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(AddPostActivity.this, "You you have to select a college", Toast.LENGTH_SHORT).show();
            }

        });

        //When the user clicked on the ImageButton
        bookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });

        //When the user clicks the Post button
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        });

        radioForFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookPrice.setEnabled(false);
            }
        });

        radioGetPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookPrice.setEnabled(true);
            }
        });
        



    }//onCreate



    public void startPosting() {

        final String title_val = BookTitle.getText().toString().trim();
        final String description_val = description.getText().toString().trim();
        final String statusOption_val = "available";
        final String price_val;
        String bookAuthor_val = BookAuthor.getText().toString().trim();
        String bookEdition_val = BookEdition.getText().toString().trim();
        //String radioAvailable_val = radioAvailable.getText().toString().trim();
        //String radioUnavailable_val = radioUnavailable.getText().toString().trim();
        //String radioForFree_val = radioForFree.getText().toString().trim();
        //String radioGetPaid_val = radioGetPaid.getText().toString().trim();
        //String bookPrice_val = bookPrice.getText().toString().trim();

       

        if(radioForFree.isChecked()){


            price_val = "0";
            bookPrice.setVisibility(View.GONE);
        } else{
            price_val = bookPrice.getText().toString().trim();
        }

        if (isEmpty(BookAuthor)) {
            bookAuthor_val = "Unknown";
        }

        if (isEmpty(BookEdition)) {
            bookEdition_val = "Unknown";
        }

        checkEnteredData();



        if(!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(description_val) && imageUri != null
                &&  !TextUtils.isEmpty(price_val)){
            progressDialog.setMessage("Posting... ");
            progressDialog.show();
            StorageReference filePath = mStorage.child("Posts_Images").child(imageUri.getLastPathSegment());
            final String finalBookAuthor_val = bookAuthor_val;
            final String finalBookEdition_val = bookEdition_val;
            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Get a URL to the uploaded content
                     //Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    //Wrong method but the correct one resulted in an error
                 Uri downloadUrl = taskSnapshot.getUploadSessionUri();

                    //Date & Time
                    Calendar calFordDate = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                    saveCurrentDate = currentDate.format(calFordDate.getTime());

                    Calendar calFordTime = Calendar.getInstance();
                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
                    saveCurrentTime = currentTime.format(calFordDate.getTime());

                      String postKey= saveCurrentDate+saveCurrentTime;


                      //Query query = databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    Query fquery = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId);
                    fquery.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists())
                            {
                                userName = snapshot.child("fullName").getValue().toString();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

  String timestamp = String.valueOf(System.currentTimeMillis());

                    //The push method here creates a unique random ID for the post
                    DatabaseReference newPost = mDatabase.push();
                    newPost.child("pId").setValue(timestamp);
                    newPost.child("BookTitle").setValue(title_val);
                    newPost.child("BookDescription").setValue(description_val);
                    newPost.child("BookImage").setValue(String.valueOf(downloadUrl));
                    newPost.child("BookStatus").setValue(statusOption_val);
                    newPost.child("BookPrice").setValue(price_val);
                    newPost.child("uid").setValue(currentUserId );
                    newPost.child("College").setValue(selectedItem);
                    newPost.child("BookAuthor").setValue(finalBookAuthor_val);
                    newPost.child("BookEdition").setValue(finalBookEdition_val);
                    newPost.child("PostDate").setValue(saveCurrentDate);
                    newPost.child("PostTime").setValue(saveCurrentTime);
                    newPost.child("Publisher").setValue(userName);


                    progressDialog.dismiss();
                    Toast.makeText(AddPostActivity.this, "Posted Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddPostActivity.this, HomeActivity.class));


                }
            });


        }

    }

     private void checkEnteredData() {

        if (isEmpty(BookTitle)) {
            BookTitle.setError("Book title is required!");
        }

        if (isEmpty(description)) {
            description.setError("Book description is required!");
        }

        if(radioGetPaid.isChecked() && isEmpty(bookPrice)){
            bookPrice.setError("Book price is required!" +"\n in case you want to sell the book");
        }

        if(imageUri == null){
            Toast.makeText(AddPostActivity.this, "please insert an image", Toast.LENGTH_SHORT).show();
        }

         if (selectedItem.equals("0") || selectedItem.equals("Choose College") || selectedItem.equals("") )
             Toast.makeText(AddPostActivity.this, "you have to select a college", Toast.LENGTH_SHORT).show();
    }


    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            imageUri = data.getData();
            bookImage.setImageURI(imageUri);
        }

    }



}//class