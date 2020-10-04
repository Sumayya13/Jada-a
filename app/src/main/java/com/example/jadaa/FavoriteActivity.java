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

public class FavoriteActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {



     DrawerLayout drawerLayout;
     NavigationView navigationView;
     Toolbar toolbar;
     Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        navigationView = findViewById(R.id.nav_view );
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle=new
                ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_heart);



    }// oncreate

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
        super.onBackPressed();
    }}


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
            Intent profile = new Intent(FavoriteActivity.this, HomeActivity.class);
            startActivity(profile); break;
            case R.id.nav_Profile:
                Intent profile1 = new Intent(FavoriteActivity.this, ProfileActivity.class);
                startActivity(profile1); break;
            case R.id.nav_MyPost:
                Intent myPost = new Intent(FavoriteActivity.this, MyPostActivity.class);
                startActivity(myPost); break;
            case R.id.nav_order:
                Intent myOrder = new Intent(FavoriteActivity.this, MyOrderActivity.class);
                startActivity(myOrder); break;
            case R.id.nav_heart: break;
            case R.id.nav_out:
                Intent logout = new Intent(FavoriteActivity.this, MainActivity.class);
                startActivity(logout); break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}