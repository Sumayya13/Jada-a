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

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        /*---------------------Hooks------------------------*/
        navigationView = findViewById(R.id.nav_view );
        drawerLayout = findViewById(R.id.drawer_layout );
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /*---------------------Navigation------------------------*/
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle=new
                ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_Profile);

    }//onCreate

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

    /*---------------------Move among pages ------------------------*/
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                Intent profile = new Intent(ProfileActivity.this, HomeActivity.class);
                startActivity(profile); break;
            case R.id.nav_Profile: break;
            case R.id.nav_MyPost:
                Intent profile1 = new Intent(ProfileActivity.this, MyPostActivity.class);
                startActivity(profile1);break;
            case R.id.nav_order:
                Intent myOrder = new Intent(ProfileActivity.this, MyOrderActivity.class);
                startActivity(myOrder); break;
            case R.id.nav_heart:
                Intent heart = new Intent(ProfileActivity.this, FavoriteActivity.class);
                startActivity(heart); break;
            case R.id.nav_out:
                Intent logout = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(logout); break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}