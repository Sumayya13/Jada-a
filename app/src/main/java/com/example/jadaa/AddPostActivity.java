package com.example.jadaa;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.net.URL;


public class AddPostActivity extends AppCompatActivity {

    private ImageView bookImage;
    private TextView heading, status,paymentOption;
    private EditText BookTitle, bookPrice,description;
    private Button clear,post;
    private RadioGroup StatusRadioGroup,paymentRadioGroup;
    private RadioButton radioAvailable, radioUnavailable, radioForFree, radioGetPaid;
    private static final int GALLERY_REQUEST = 1;
    private Uri imageUri;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);



        mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
        mStorage = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        heading =(TextView)findViewById(R.id.heading);
        bookImage = (ImageView)findViewById(R.id.bookImage);
        BookTitle =(EditText)findViewById(R.id.BookTitle);
        bookPrice =(EditText)findViewById(R.id.bookPrice);
        description =(EditText)findViewById(R.id.description);
        status = findViewById(R.id.status);
        StatusRadioGroup = findViewById(R.id.StatusRadioGroup);
        radioAvailable = findViewById(R.id.radio_available);
        radioUnavailable = findViewById(R.id.radio_unavailable);
        paymentOption = findViewById(R.id.paymentOption);
        paymentRadioGroup = findViewById(R.id.paymentRadioGroup);
        radioForFree = findViewById(R.id.radio_forFree);
        radioGetPaid = findViewById(R.id.radio_getPaid);
        clear = findViewById(R.id.clear);
        post = findViewById(R.id.post);
        Spinner college =(Spinner) findViewById(R.id.mySpinner);
        progressDialog = new ProgressDialog(this);



        // if user choose a collage I will save position in string to save it in database
        college.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedItem = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(AddPostActivity.this, selectedItem, Toast.LENGTH_SHORT).show();

            }
            //         // if user does not choose any one I will print msg
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(AddPostActivity.this, "You Not Select", Toast.LENGTH_SHORT).show();
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







    }//onCreate



    public void startPosting() {

        final String title_val = BookTitle.getText().toString().trim();
        final String description_val = description.getText().toString().trim();
        final String statusOption_val;
        final String price_val;
        //String radioAvailable_val = radioAvailable.getText().toString().trim();
        //String radioUnavailable_val = radioUnavailable.getText().toString().trim();
        //String radioForFree_val = radioForFree.getText().toString().trim();
        //String radioGetPaid_val = radioGetPaid.getText().toString().trim();
        //String bookPrice_val = bookPrice.getText().toString().trim();

        if(radioAvailable.isChecked()){
            statusOption_val = radioAvailable.getText().toString().trim();
        } else {
            statusOption_val = radioUnavailable.getText().toString().trim();
        }

        if(radioForFree.isChecked()){
            bookPrice.setEnabled(false);
            price_val = "0 SR";
        } else{
            price_val = bookPrice.getText().toString().trim();
        }

        checkEnteredData();


        if(!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(description_val) && imageUri != null
                && !TextUtils.isEmpty(statusOption_val) && !TextUtils.isEmpty(price_val)){
            progressDialog.setMessage("Posting... ");
            progressDialog.show();
            StorageReference filePath = mStorage.child("Posts_Images").child(imageUri.getLastPathSegment());
            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Get a URL to the uploaded content
                    // Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    //Wrong method but the correct one resulted in an error
                    Uri downloadUrl = taskSnapshot.getUploadSessionUri();

                    //The push method here creates a unique random ID for the post
                    DatabaseReference newPost = mDatabase.push();
                    newPost.child("Book Title").setValue(title_val);
                    newPost.child("Book Description").setValue(description_val);
                    newPost.child("Book Image").setValue(downloadUrl.toString());
                    newPost.child("Book Status").setValue(statusOption_val);
                    newPost.child("Book Price").setValue(price_val);
                    newPost.child("uid").setValue(mAuth.getCurrentUser().getUid());


                    progressDialog.dismiss();
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