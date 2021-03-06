package com.picke.dishnow_owner.Utility;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.picke.dishnow_owner.R;

import java.io.InputStream;


public class TermsServiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_service);

        Toolbar toolbar = findViewById(R.id.service_terms_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView Tview = findViewById(R.id.service_terms_txtview);
        //약관 읽어옴
        AssetManager am = getResources().getAssets();
        InputStream is = null;

        byte buf[] = new byte[25000];
        String text = "";

        try {
            is = am.open("TermsService.txt");

            if (is.read(buf) > 0) {
                text = new String(buf);
            }


            Tview.setText(text);

            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (is != null) {
            try {
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Tview.setMovementMethod(new ScrollingMovementMethod());
    }

    //툴바 뒤로가기 버튼 클릭 시
    @Override
    public boolean onOptionsItemSelected(MenuItem item ){
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}