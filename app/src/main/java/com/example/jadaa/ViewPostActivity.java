
package com.example.jadaa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ViewPostActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);



        //views from row_posts.xml
        ImageView uPictureIv, pImageIv;
        final TextView uNameTv, pTimeTv, pTitle, pDescriptionTv,auther_name,edition,college,bookPrice,owner;
        ImageButton moreBtn;
        Button favoriteBtn, commentBtn, shareBtn, buy;


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
        pImageIv = findViewById(R.id.bookImage);
        uNameTv = (TextView)findViewById(R.id.uNameTv);
        pTimeTv =(TextView) findViewById(R.id.pTimeTv);
        pTitle =(TextView) findViewById(R.id.BookTitle);
        pDescriptionTv =(TextView) findViewById(R.id.description);
        auther_name =(TextView) findViewById(R.id.auther_name);
        edition =(TextView) findViewById(R.id.edition);
        college=(TextView) findViewById(R.id.college);
        bookPrice=(TextView) findViewById(R.id.bookPrice);
        // owner=(TextView) findViewById(R.id.owner);
        buy = findViewById(R.id.buy);



        final Bundle extras = getIntent().getExtras();


        String title = extras.getString("pTitle");
        String des = extras.getString("pDescription");
        final String pid = extras.getString("pId");

        pTitle.setText( " "+extras.getString("pTitle"));
        pDescriptionTv.setText(" "+extras.getString("pDescription"));
        auther_name.setText(  " by "+extras.getString("pAuthor"));
        edition.setText( " Edition "+extras.getString("pEdition"));






        String uri =extras.getString("pImage");
        //owner.setText( extras.getString("pPublisher"));
        try{
            //  Picasso.get().load(pImage).into(myHolder.pImageIv);
            Picasso.get().load(uri).into(pImageIv);
            // myHolder.pImageIv.setImageURI(Uri.parse(pImage));
        }
        catch (Exception e){
        }




        if ( extras.getString("pPrice").equals("0"))
            bookPrice.setText("Book is free");
        else
            bookPrice.setText(" "+extras.getString("pPrice")+" SAR");


        college.setText( " "+extras.getString("pCollege"));


        /*
         اذا مبلغ الكتاب مجاني ما اروح للGoogle pay
         اقول له تم الشراء واعرض الكتاب في MyOrder page



        انبه البائع ان في شخص طلب الكتاب واعطيه بيانات الزبون (جواله واسمه )
        احط صفحة فيها كتبي اللي انباعت وبيانات الزبون اللي اشتراها عشان اتواصل معه
         */


         /*


         اذ الكتاب له غيرمجاني اودي المستخدم للGoogle pay

         الحالة الأولى/ اذا تم دفع المبلغ بنجاح
        اغير حالة الكتاب في الداتابيس من متاح ل تم بيعه  available to purched
         اعرض له رسالة تم الشراء بنجاح واعرض الكتاب في MyOrder page
         اعدل الصفحة الرئيسية  واخليها تعرض الكتب المتاحة فقط
         انبه البائع ان في شخص طلب الكتاب واعطيه بيانات الزبون (جواله واسمه)
         عشان يتواصل معه
         ممكن اعطي المشتري بيانات البائع كمان

         الحالة الثانية/ اذا لم يتم دفع المبلغ
         اعرض له رسالة لم يتم الشراء بنجاح واخلي اليوزر علي صفحة  Google pay

         */

        final String price = extras.getString("pPrice");
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pay = new Intent(getApplicationContext(), googlePay.class);
               /*
                Bundle extras = getIntent().getExtras();
                pTitle.setText( " "+extras.getString("pTitle"));
                pDescriptionTv.setText(" "+extras.getString("pDescription"));
                auther_name.setText(  " by "+extras.getString("pAuthor"));
                edition.setText( " Edition "+extras.getString("pEdition"));
*/
                pay.putExtra("pPrice",price);
                pay.putExtra("pId",pid);
                startActivity(pay);
            }
        });








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

