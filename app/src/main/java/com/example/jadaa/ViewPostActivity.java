package com.example.jadaa;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);



        //views from row_posts.xml
        ImageView uPictureIv, pImageIv;
        TextView uNameTv, pTimeTv, pTitle, pDescriptionTv,auther_name,edition,college,bookPrice,owner;
        ImageButton moreBtn;
        Button favoriteBtn, commentBtn, shareBtn;

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


        //init views
        uPictureIv =findViewById(R.id.uPictureIv);
        pImageIv = findViewById(R.id.pImageIv);
        uNameTv = (TextView)findViewById(R.id.uNameTv);
        pTimeTv =(TextView) findViewById(R.id.pTimeTv);
        pTitle =(TextView) findViewById(R.id.BookTitle);
        pDescriptionTv =(TextView) findViewById(R.id.description);
        auther_name =(TextView) findViewById(R.id.auther_name);
        edition =(TextView) findViewById(R.id.edition);
        college=(TextView) findViewById(R.id.college);
        bookPrice=(TextView) findViewById(R.id.bookPrice);
        // owner=(TextView) findViewById(R.id.owner);



        Bundle extras = getIntent().getExtras();
        String title;
        title =  " "+extras.getString("pTitle");
        // message.setContent(title, "text/html; charset=utf-8");


        //    if (extras != null)
        pTitle.setText(title);
        pDescriptionTv.setText(" "+extras.getString("pDescription"));
        auther_name.setText(  " by "+extras.getString("pAuthor"));
        edition.setText( " Edition "+extras.getString("pEdition"));
        //owner.setText( extras.getString("pPublisher"));



        //String someHtmlMessage = "Hello this is test message <b style='color:blue;'>bold color</b>";
        //message.setContent(someHtmlMessage, "text/html; charset=utf-8");


        if ( extras.getString("pPrice").equals("0"))
            bookPrice.setText("Book is free");
        else
            bookPrice.setText(" "+extras.getString("pPrice")+" RS");
        //
        //
        college.setText( " "+extras.getString("pCollege"));
        //   pTitle.setText( extras.getString("pPublisher"));
        // and get whatever type user account id is






      /*  Bundle extras = getIntent().getExtras();
        //    if (extras != null)
        pTitle.setText( extras.getString("pTitle"));
        pDescriptionTv.setText( extras.getString("pDescription"));
        auther_name.setText(  extras.getString("pAuthor"));
        edition.setText( extras.getString("pEdition"));
        //owner.setText(extras.getString("pPublisher"));

        if ( extras.getString("pPrice").equals("0"))
            bookPrice.setText("Free");
        else
            bookPrice.setText(extras.getString("pPrice")+" RS");
        //
        //
        college.setText( extras.getString("pCollege"));
        //   pTitle.setText( extras.getString("pPublisher"));
        // and get whatever type user account id is

*/




    }
}