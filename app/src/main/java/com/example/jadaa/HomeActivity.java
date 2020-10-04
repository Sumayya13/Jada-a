package com.example.jadaa;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hom);

        /*---------------------Hooks------------------------*/
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view );
        drawerLayout = findViewById(R.id.drawer_layout );
        setSupportActionBar(toolbar);


        /*---------------------Navigation------------------------*/
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
       // to change color when clicK on item
        navigationView.setNavigationItemSelectedListener(this);
       // send this page
        navigationView.setCheckedItem(R.id.nav_home);


        // if want to hide item of navigation
        /* menu = navigationView.getMenu();
        menu.findItem(R.id.nav_out).setVisible(false);
        menu.findItem(R.id.nav_Profile).setVisible(false); */

         /*---------------------to move to add post page ------------------------*/
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent move_To_Add_Post = new Intent(HomeActivity.this, AddPostActivity.class);
                startActivity(move_To_Add_Post);
            }
        });
    }// on create

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

// to move to page when click
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home: break;
            case R.id.nav_Profile:
                Intent profile = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(profile); break;
            case R.id.nav_MyPost:
                Intent myPost = new Intent(HomeActivity.this, MyPostActivity.class);
                startActivity(myPost); break;
            case R.id.nav_order:
                Intent myOrder = new Intent(HomeActivity.this, MyOrderActivity.class);
                startActivity(myOrder); break;
            case R.id.nav_heart:
                Intent favorite = new Intent(HomeActivity.this, FavoriteActivity.class);
                startActivity(favorite); break;
            case R.id.nav_out:
                Intent logout = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(logout); break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}