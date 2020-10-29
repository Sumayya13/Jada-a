package com.example.jadaa;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.HashMap;
import java.util.regex.Pattern;


public class AddPostActivity extends AppCompatActivity {

    private ImageView imageIv;
    private TextView heading, status,paymentOption;
    private EditText BookTitle, bookPrice,description, BookAuthor, BookEdition;
    private Button clear,post;
    private RadioGroup StatusRadioGroup,paymentRadioGroup;
    private RadioButton radioAvailable, radioUnavailable, radioForFree, radioGetPaid;

    //Permission constants
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;

    //image pick constants
    private static final int IMAGE_PICK_CAMERA_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;


    //Permissions array
    String[] cameraPermission;
    String[] storagePermission;

    //image picked will be saved in this uri
    Uri image_rui = null;

    // private Uri imageUri; /*delete*/

    private DatabaseReference userDbRef;
    // private StorageReference mStorage;
    private FirebaseAuth mAuth;
    // private String currentUserId;
    //  private ProgressDialog progressDialog; /*Delete this*/
    private String selectedItem ="";
    private String saveCurrentDate, saveCurrentTime;
    // private String userName = "";
    // DatabaseReference databaseReference; /*delete*/

    //user info
    String name, email, uid;

    //Posting progress bar
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        //init permissions arrays
        cameraPermission= new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission= new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        pd = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        checkUserStatus();
        //get some user info to include in the post
        userDbRef = FirebaseDatabase.getInstance().getReference("users");
        Query query = userDbRef.orderByChild("email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    name = ""+ ds.child("fullName").getValue();
                    email = ""+ ds.child("email").getValue();
                    //user image goes here

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        // currentUserId = mAuth.getCurrentUser().getUid();
        //  mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
        // userRef= FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId);
        // mStorage = FirebaseStorage.getInstance().getReference();



        imageIv = (ImageView)findViewById(R.id.bookImage);
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
        //progressDialog = new ProgressDialog(this);


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
                Toast.makeText(AddPostActivity.this, "You have to select a college", Toast.LENGTH_SHORT).show();
            }

        });

        //When the user clicked on the ImageView
        imageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show image pick dialog
                showImagePickDialog();

                /*Delete the next 3 lines*/
               /* Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, STORAGE_REQUEST_CODE); */
            }
        });

        //When the user clicks the Post button
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title_val = BookTitle.getText().toString().trim();
                String description_val = description.getText().toString().trim();
                String statusOption_val = "available";
                String price_val;
                String bookAuthor_val = BookAuthor.getText().toString().trim();
                String bookEdition_val = BookEdition.getText().toString().trim();


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

                if(title_val.matches("")){
                    BookTitle.setError("Book title is required!");
                }

                if(description_val.matches("")){
                    description.setError("Book description is required!");
                }


                checkEnteredData();


                if(image_rui != null){
                    //post with image
                    uploadData(title_val, description_val, statusOption_val, price_val, bookAuthor_val, bookEdition_val, String.valueOf(image_rui));
                }

                /* startPosting(); Delete this */
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

    private void uploadData(final String title_val, final String description_val, final String statusOption_val, final String price_val, final String bookAuthor_val, final String bookEdition_val, String uri) {
        //Date & Time
        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());

        Calendar calFordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(calFordDate.getTime());

        pd.setMessage("Posting...");
        pd.show();

        //for posting image name, post id, post publish time
        final String timeStamp = String.valueOf(System.currentTimeMillis());
        String filePathAndName = "Posts/" + "post_" + timeStamp;

        if(!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(description_val) && image_rui != null
                &&  !TextUtils.isEmpty(price_val)){
            //post with image
            StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);
            ref.putFile(Uri.parse(uri))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //image is uploaded to firebase storage, get its url
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while(!uriTask.isSuccessful());

                            String downloadUri = uriTask.getResult().toString();

                            if(uriTask.isSuccessful()){
                                //url is received, upload post to firebase database

                                HashMap<Object, String> hashMap = new HashMap<>();
                                //put post info
                                hashMap.put("uid", uid);
                                hashMap.put("Publisher", name);
                                hashMap.put("uEmail", email);
                                hashMap.put("pId", timeStamp);
                                hashMap.put("BookTitle", title_val);
                                hashMap.put("BookDescription", description_val);
                                hashMap.put("BookStatus", statusOption_val);
                                hashMap.put("BookPrice", price_val);
                                hashMap.put("BookAuthor", bookAuthor_val);
                                hashMap.put("BookEdition", bookEdition_val);
                                hashMap.put("College", selectedItem);
                                hashMap.put("BookImage", downloadUri);
                                hashMap.put("pTime", timeStamp);
                                hashMap.put("PostDate", saveCurrentDate);
                                hashMap.put("PostTime", saveCurrentTime);

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
                                //put data in this ref
                                ref.child(timeStamp).setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //added in the database
                                                pd.dismiss();
                                                Toast.makeText(AddPostActivity.this, "Posted Successfully", Toast.LENGTH_SHORT).show();
                                                Intent move_To_Home = new Intent(AddPostActivity.this, HomeActivity.class);
                                                startActivity(move_To_Home);
                                                //reset views
                                                BookTitle.setText("");
                                                description.setText("");
                                                bookPrice.setText("");
                                                BookAuthor.setText("");
                                                BookEdition.setText("");
                                                imageIv.setImageURI(null);
                                                image_rui = null;


                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //failed adding post in database
                                                pd.dismiss();
                                                Toast.makeText(AddPostActivity.this, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        });


                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //failed uploading image
                            pd.dismiss();
                            Toast.makeText(AddPostActivity.this, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }

            /*
            //post without image
            HashMap<Object, String> hashMap = new HashMap<>();

            //put post info
            hashMap.put("uid", uid);
            hashMap.put("Publisher", name);
            hashMap.put("uEmail", email);
            hashMap.put("pId", timeStamp);
            hashMap.put("BookTitle", title_val);
            hashMap.put("BookDescription", description_val);
            hashMap.put("BookStatus", statusOption_val);
            hashMap.put("BookPrice", price_val);
            hashMap.put("BookAuthor", bookAuthor_val);
            hashMap.put("BookEdition", bookEdition_val);
            hashMap.put("College", selectedItem);
            hashMap.put("pImage", "noImage");
            hashMap.put("pTime", timeStamp);
            hashMap.put("PostDate", saveCurrentDate);
            hashMap.put("PostTime", saveCurrentTime);

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
            //put data in this ref
            ref.child(timeStamp).setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //added in the database
                            pd.dismiss();
                            Toast.makeText(AddPostActivity.this, "Posted Successfully", Toast.LENGTH_SHORT).show();
                            Intent move_To_Home = new Intent(AddPostActivity.this, HomeActivity.class);
                            startActivity(move_To_Home);
                            //reset views
                            BookTitle.setText("");
                            description.setText("");
                            bookPrice.setText("");
                            BookAuthor.setText("");
                            BookEdition.setText("");
                            imageIv.setImageURI(null);
                            image_rui = null;


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //failed adding post in database
                            pd.dismiss();
                            Toast.makeText(AddPostActivity.this, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }); */

    }

    private void showImagePickDialog() {
        //options {camera, gallery} to show in dialog
        String[] options = {"Camera", "Gallery"};
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image from");
        // set options to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //item click handle
                if(which == 0){
                    //camera clicked

                    if(!checkCameraPermission()){
                        requestCameraPermission();
                    }else{
                        pickFromCamera();
                    }
                }
                if(which == 1){
                    //gallery clicked

                    if(!checkStoragePermission()){
                        requestStoragePermission();
                    }else{
                        pickFromGallery();
                    }
                }

            }


        });
        //create and show dialog
        builder.create().show();
    }

    private void pickFromCamera() {
        //intent to pick image from camera
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE, "Temp Pick");
        cv.put(MediaStore.Images.Media.DESCRIPTION, "Temp descr");
        image_rui = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_rui);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

    private void pickFromGallery() {
        //intent to pick image from gallery
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private boolean checkStoragePermission(){
        //check if storage permission is enabled or not
        //return true if its enabled and false if not
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission(){
        //request runtime storage permission
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission(){
        //check if camera permission is enabled or not
        //return true if its enabled and false if not
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission(){
        //request runtime camera permission
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }

    private void checkUserStatus(){
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            //user is signed in stay here
            //set email of logged user
            email = user.getEmail();
            uid = user.getUid();
            //mProfile.setText(user.getEmail());
        }else{
            //user not signed in go to main activity
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUserStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkUserStatus();
    }


  /*  public void startPosting() {

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

    } */

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

        if(image_rui == null){
            Toast.makeText(AddPostActivity.this, "please insert an image", Toast.LENGTH_SHORT).show();
        }

        if (selectedItem.equals("0") || selectedItem.equals("Choose College") || selectedItem.equals("") )
            Toast.makeText(AddPostActivity.this, "You have to select a college", Toast.LENGTH_SHORT).show();
    }


    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == STORAGE_REQUEST_CODE && resultCode == RESULT_OK){
            imageUri = data.getData();
            bookImage.setImageURI(imageUri);
        }

    } */


    //handle permission results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && storageAccepted){
                        //both permissions are granted
                        pickFromCamera();
                    }
                    else{
                        // Camera or Gallery or both permissions were denied
                        Toast.makeText(this, "Camera and Storage both permissions are necessary", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(storageAccepted){
                        //storage permission is granted
                        pickFromGallery();
                    }
                    else{
                        // Camera or Gallery or both permissions were denied
                        Toast.makeText(this, "Storage permissions is necessary", Toast.LENGTH_SHORT).show();
                    }
                }
                else{

                }
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //this method will be called after picking an image from camera or gallery
        if(resultCode == RESULT_OK){
            if(requestCode == IMAGE_PICK_GALLERY_CODE){
                //image is picked from gallery, get uri from image
                assert data != null;
                image_rui = data.getData();

                // set to imageView
                imageIv.setImageURI(image_rui);



            }
            else if(requestCode == IMAGE_PICK_CAMERA_CODE){
                //image is picked from camera, get uri from image
                imageIv.setImageURI(image_rui);

            }
        }

        super.onActivityResult(requestCode, resultCode, data);


    }

}//class