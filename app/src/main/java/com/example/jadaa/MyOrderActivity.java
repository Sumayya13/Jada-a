package com.example.jadaa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.jadaa.adapters.AdapterMyOrder;
import com.example.jadaa.adapters.AdapterPosts;
import com.example.jadaa.models.ModelPost;
import com.example.jadaa.models.soldBooks;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MyOrderActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Menu menu;

    public int getIs_has_order() {
        return is_has_order;
    }

    public void setIs_has_order(int is_has_order) {
        this.is_has_order = is_has_order;
    }

    private int is_has_order;  // عشان اشيك هل عنده طلبات او لا

    RecyclerView recyclerView;
    List<soldBooks> postList;
    AdapterMyOrder adapterPosts;
    LinearLayout linearLayout;


    public MyOrderActivity(){
        is_has_order = 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        /*---------------------delete app bar ------------------------*/
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        /*---------------------Hooks------------------------*/
        navigationView = findViewById(R.id.nav_view );
        drawerLayout = findViewById(R.id.drawer_layout );
        toolbar = findViewById(R.id.toolbar);
        linearLayout = findViewById(R.id.NoOrder);

        setSupportActionBar(toolbar);







        /*---------------------Recycle view ------------------------*/

        recyclerView = findViewById(R.id.post_list);


        LinearLayoutManager layoutManager = new LinearLayoutManager(MyOrderActivity.this);

        //show news =t posts first , for this load from last
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        //set layout to recyclerview
        recyclerView.setLayoutManager(layoutManager);

        //init  post list
        postList = new ArrayList<>();
        loadPosts();

/*
        if (getIs_has_order() == 0) {
            linearLayout.setVisibility(View.VISIBLE);
        }else linearLayout.setVisibility(View.INVISIBLE);

*/

        /*---------------------Navigation------------------------*/
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle=new
                ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_order);

    }//onCreate



    private void loadPosts() {
        final FirebaseUser thisUser = FirebaseAuth.getInstance().getCurrentUser();
        final boolean num = false ;
        // path of all posts
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("soldBooks");

        //get all from this
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();

                for (DataSnapshot ds: snapshot.getChildren() ){
                    soldBooks modelPost = ds.getValue(soldBooks.class);

                    // لو اي دي المشتري نفس الاي دي حقي اعرض له البوست
                    if (modelPost.getPurchaserID().equals(thisUser.getUid())  ) {

                        //setIs_has_order(1);
                        linearLayout.setVisibility(View.INVISIBLE);
                        postList.add(modelPost);
                        //adapter
                        adapterPosts = new AdapterMyOrder(MyOrderActivity.this, postList);
                        //set adapter to recycler view
                        recyclerView.setAdapter(adapterPosts);
                        adapterPosts.notifyDataSetChanged();

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //in case of error
                Toast.makeText(MyOrderActivity.this,""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }





    /*---------------------to Open or close Navigation ------------------------*/
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                Intent profile = new Intent(MyOrderActivity.this, HomeActivity.class);
                startActivity(profile); break;
            case R.id.nav_Profile:
                Intent profile1 = new Intent(MyOrderActivity.this, ProfileMyPosts.class);
                startActivity(profile1); break;

            case R.id.nav_order: break;
            case R.id.nav_heart:
                Intent heart = new Intent(MyOrderActivity.this, FavoriteActivity.class);
                startActivity(heart); break;
            case R.id.nav_paople:
                Intent myCustomers = new Intent(MyOrderActivity.this, MyCustomersActivity.class);
                startActivity(myCustomers); break;
            case R.id.nav_out:
                android.app.AlertDialog.Builder alertDialogBilder = new AlertDialog.Builder(this);
                alertDialogBilder.setTitle("Log out");
                alertDialogBilder.setMessage("Are you sure you want to log out?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // close the dialog
                            }
                        })
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {

                                FirebaseAuth.getInstance().signOut();
                                finish();
                                Intent logout = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(logout);
                            }
                        });


                AlertDialog alertDialog = alertDialogBilder.create();
                alertDialog.show();break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}