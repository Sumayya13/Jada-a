package com.example.jadaa;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jadaa.adapters.AdapterMyPosts;
import com.example.jadaa.models.ModelMyPost;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class ProfileMyPosts extends AppCompatActivity {

    // firebase
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    StorageReference storageReference;

    // path where images of user profile and cover will be stored
    String storagePath = "Users_Profile_Cover_Imgs/";

    //page
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Menu menu;
    LinearLayout linearLayout;


    RecyclerView recyclerView;
    List<ModelMyPost> postList;
    AdapterMyPosts adapterPosts;

    ProgressDialog pd;

    //Permission constants
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;

    //image pick constants
    private static final int IMAGE_PICK_GALLERY_CODE  = 300;
    private static final int  IMAGE_PICK_CAMERA_CODE = 400;

    //Permissions array
    String cameraPermission[];
    String storagePermission[];

    Uri image_uri;

    //
    String ProfileOrCoverPhoto ;

    EditText editText;

    private static final Pattern PHONE_PATTERN= Pattern.compile("^[+]?[0-9]{10,13}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_my_posts);

        /*---------------------delete app bar ------------------------*/
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


// view xml
        final ImageView avatarIv, coverIv ;
        final TextView nameTv,emailTv,phoneTv,freeBooks,payedBooks,allPost;
        FloatingActionButton fab;

        // init firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");

        storageReference = getInstance().getReference(); //

        //init permissions arrays
        cameraPermission= new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission= new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        // inti views
        avatarIv = findViewById(R.id.avatarIv);
        coverIv = findViewById(R.id.coverIv);
        nameTv = findViewById(R.id.nameTv);
        emailTv = findViewById(R.id.emailTv);
        phoneTv = findViewById(R.id.phoneTv);
        // freeBooks=findViewById(R.id.numFreeSoldPost);
        payedBooks = findViewById(R.id.numPayedPost);
        allPost = findViewById(R.id.numPosts);
        fab = findViewById(R.id.fab);


        pd = new ProgressDialog(this); /// ///////////

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*---------------------Hooks------------------------*/
        navigationView = findViewById(R.id.nav_view );
        drawerLayout = findViewById(R.id.drawer_layout );
        //     toolbar = findViewById(R.id.toolbar);

        linearLayout = findViewById(R.id.NoOrder);

        /*---------------------Recycle view ------------------------*/
        //recyclerView = findViewById(R.id.post_list);
        recyclerView = findViewById(R.id.post_list);
        // LinearLayoutManager layoutManager = new LinearLayoutManager(HomeActivity.this);


        LinearLayoutManager layoutManager = new LinearLayoutManager(ProfileMyPosts.this);
        //show news =t posts first , for this load from last
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        //set layout to recyclerview
        recyclerView.setLayoutManager(layoutManager);

        //init  post list
        postList = new ArrayList<>();
        loadPosts();



        // Get a reference to our posts
        final FirebaseUser thisUser = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users").child(thisUser.getUid());

        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {

                    User user = dataSnapshot.getValue(User.class);

                    String name =user.getFullName() ;
                    String phone = user.getPhone() ;
                    String email = user.getEmail() ;
                    String image = user.getImage() ;
                    String cover = user.getCover() ;

                    String freeAdded = user.getNumFreeBooks() ;
                    String freeSold = user.getNumFreeSoldBooks() ;

                    String payedAdded = user.getNumPayedSoldBooks() ; // هذه اللي بتنعرض في البروفايل عدد الكتب اللي شريتها
                    String payedSold = user.getNumPayedSoldBooks() ;
                    String all = user.getNumAllBooks() ;

                    nameTv.setText(name);
                    emailTv.setText(email);
                    phoneTv.setText(phone);

                    //freeBooks.setText(freeAdded);
                    payedBooks.setText(payedAdded);
                    allPost.setText(all);


                    try{
                        // if image is resived then set
                        Picasso.get().load(image).into(avatarIv);
                    }
                    catch (Exception e){
                        Picasso.get().load(R.drawable.ic_baseline_add_a_photo_24).into(avatarIv);
                    }

                    try{
                        // if image is resived then set
                        Picasso.get().load(cover).into(coverIv);
                    }
                    catch (Exception e){

                    }



                }

            }        @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfileDialog();
            }
        });




    }// oncreate

    private void showEditProfileDialog() {

       /* show dialog contain options
       1) Edit profile pic
       2) Edit cover photo
       3) Edit Name
       4) Edit phone
        */


        String options[] = {"Edit Profile Picture","Edit Cover Photo", "Edit Name", "Edit Phone"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileMyPosts.this);
        builder.setTitle("Choose Action");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which==0)
                {
                    pd.setMessage("Updating Profile Picture");
                    ProfileOrCoverPhoto = "image";
                    showImagePicDialoge();

                }else if (which==1)
                {// cover
                    pd.setMessage("Updating Cover Photo");
                    ProfileOrCoverPhoto = "cover";
                    showImagePicDialoge();

                }else if (which==2)
                {// name
                    pd.setMessage("Updating Name");
                    showNamePhoneUpdateDialoge("name");

                }else if (which==3)
                {// phone
                    pd.setMessage("Updating Phone");
                    showNamePhoneUpdateDialoge("phone");

                }

            }


        });
        builder.create().show();
    }


    private void showNamePhoneUpdateDialoge(final String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update "+key);  // ee.g apdate name apdate phone



        // دقيقة ٤٨

        // احسن اغيرها واخليها نفس الطريقه اللي سويت فيها التيرمز

        LinearLayout linearLayout = new LinearLayout (this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);
        // add edit text
        editText = new EditText( this);
        editText.setHint("Enter "+key);
        linearLayout.addView(editText);

        builder.setView(linearLayout);

        // add button in dailog to update
        builder.setPositiveButton("Update ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String value = editText.getText().toString().trim();
                if (!TextUtils.isEmpty(value)){

                    pd.show();
                    HashMap<String ,Object> result = new HashMap<>();

                 /*  if(key.equals("name")&&!validateName(value) || key.equals("phone")&&!validatePhone(value)){
                       Toast.makeText(ProfileMyPosts.this,"Please enter a valid "+key,Toast.LENGTH_SHORT).show();
                   } */
                    if (key.equals("name") && validateName(value)) {
                        result.put("fullName", value);
                        pd.dismiss();
                        Toast.makeText(ProfileMyPosts.this, "Updated", Toast.LENGTH_SHORT).show();
                    }
                    else if (key.equals("phone") && validatePhone(value)){
                        result.put("phone",value);
                        pd.dismiss();
                        Toast.makeText(ProfileMyPosts.this, "Updated", Toast.LENGTH_SHORT).show();
                    }

                    databaseReference.child(user.getUid()).updateChildren(result);

                    if(key.equals("name")&&!validateName(value) || key.equals("phone")&&!validatePhone(value)){
                        pd.dismiss();
                        Toast.makeText(ProfileMyPosts.this,"Please enter a valid "+key,Toast.LENGTH_SHORT).show();
                    }

                 /*  .addOnSuccessListener(new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void aVoid) {

                           pd.dismiss();
                           Toast.makeText(ProfileMyPosts.this,"Updated",Toast.LENGTH_SHORT).show();
                       }
                   })
                   .addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {

                           pd.dismiss();
                           Toast.makeText(ProfileMyPosts.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                       }
                   }); */

                }
                //sumayya check
                /*else if(key.equals("name")&&!validateName(value)){
                    return;
                }
                else if(key.equals("phone")&&!validatePhone(value)){
                    return;
                } */
                else Toast.makeText(ProfileMyPosts.this,"Please enter "+key,Toast.LENGTH_SHORT).show();

            }
        });

        // add button in dailog to cancel
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        // create and show dailog
        builder.create().show();



// كملي





    }

    private boolean checkStoragePermission(){
        //check if storage permission is enabled or not
        //return true if its enabled and false if not
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);// (PackageManager.PERMISSION_GRANTED);
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

    private void pickFromCamera() {
        //intent to pick image from camera
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE, "Temp Pick");
        cv.put(MediaStore.Images.Media.DESCRIPTION, "Temp descr");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri );
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

    private void pickFromGallery() {
        //intent to pick image from gallery
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private void showImagePicDialoge() {

        String options[] = {"Camera","Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileMyPosts.this);
        builder.setTitle("Pick image From");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    //camera clicked

                    if (!checkCameraPermission())
                    {
                        requestCameraPermission();
                    }else pickFromCamera();

                } else if (which == 1) {// gallery

                    if (!checkStoragePermission())
                    {
                        requestStoragePermission();
                    }else pickFromGallery();
                }

            }
        });
        builder.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //this method will be called after picking an image from camera or gallery
        if(resultCode == RESULT_OK){

            if(requestCode == IMAGE_PICK_GALLERY_CODE){
                //image is picked from gallery, get uri from image
                //  assert data != null;
                image_uri = data.getData();

                // set to imageView
                uploadProfileCoverPhoto(image_uri);



            }
            else if(requestCode == IMAGE_PICK_CAMERA_CODE){
                //image is picked from camera, get uri from image
                //    imageIv.setImageURI(image_rui);

                uploadProfileCoverPhoto(image_uri);

            }
        }

        super.onActivityResult(requestCode, resultCode, data);


    }

    private void uploadProfileCoverPhoto(Uri uri) {

        pd.show();
        String filePathAndName= storagePath+""+ProfileOrCoverPhoto+"_"+user.getUid() ;
        StorageReference storageReference2 = storageReference.child(filePathAndName);
        storageReference2.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
// upload image

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isSuccessful());

                String downloadUri = uriTask.getResult().toString();
                // check is uploaded or not
                if (uriTask.isSuccessful()){

                    // add update uri in database
                    HashMap<String , Object >result = new HashMap<>();
                    result.put(ProfileOrCoverPhoto,downloadUri.toString());
                    databaseReference.child(user.getUid()).updateChildren(result).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            pd.dismiss();
                            Toast.makeText(ProfileMyPosts.this,"Image Updated....",Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(ProfileMyPosts.this,"Error Updating Image...",Toast.LENGTH_SHORT).show();

                        }
                    });

                }else {

                    pd.dismiss();
                    Toast.makeText(ProfileMyPosts.this,"Some error occured ",Toast.LENGTH_SHORT).show();

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                pd.dismiss();
                Toast.makeText(ProfileMyPosts.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loadPosts() {


        FirebaseAuth firebaseAuth;
        FirebaseUser user;

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        // path of all posts
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");

        Query query = ref.orderByChild("uid").equalTo(user.getUid());
        //get all from this
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot ds: snapshot.getChildren() ){

                    linearLayout.setVisibility(View.INVISIBLE);
                    ModelMyPost modelMyPost = ds.getValue(ModelMyPost.class);
                    postList.add(modelMyPost);
                    //adapter
                    adapterPosts = new AdapterMyPosts(ProfileMyPosts.this, postList);
                    //set adapter to recycler view
                    recyclerView.setAdapter(adapterPosts);
                    adapterPosts.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //in case of error
                Toast.makeText(ProfileMyPosts.this,""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean validateName(String text) {
        if (text.length() > 20) {
            editText.setError("Username is too long");
            return false;
        }
        if (text.length() < 3) {
            editText.setError("Username is too short");
            return false;
        }
        else {
            editText.setError(null);
            return true;
        }
    }


    public boolean validatePhone(String text){
        if (!PHONE_PATTERN.matcher(text).matches()) {
            editText.setError("Incorrect phone format");
            return false;
        } else {
            editText.setError(null);
            return true;
        }
    }



}