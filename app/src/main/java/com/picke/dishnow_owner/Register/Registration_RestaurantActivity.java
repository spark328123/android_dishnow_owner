package com.picke.dishnow_owner.Register;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.picke.dishnow_owner.Guide.JudgingActivity;
import com.picke.dishnow_owner.Owner_User.UserInfoClass;
import com.picke.dishnow_owner.R;
import com.picke.dishnow_owner.Utility.JusoActivity;
import com.picke.dishnow_owner.Utility.VolleySingleton;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Registration_RestaurantActivity extends AppCompatActivity {

    private EditText Eresname;
    private EditText Eresphone;
    private EditText Eresaddress;
    private EditText Eresadd_num;
    private EditText Eresadd_detail;
    private Button Btfindaddress;
    private Button Btressignup;
    private UserInfoClass userInfoClass;
    private RequestQueue requestQueue;
    private String lat;
    private String lon;
    final String resinfo_url = "http://claor123.cafe24.com/ResSignup.php";
    private Boolean flag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_restuarant);

        Eresname = findViewById(R.id.res_info_res_name);
        Eresphone = findViewById(R.id.res_info_resphone);
        Eresaddress = findViewById(R.id.res_info_address_edt);
        Eresadd_num = findViewById(R.id.res_info_address_num_edt);
        Eresadd_detail = findViewById(R.id.res_info_detail_address_edt);
        Btfindaddress = findViewById(R.id.res_info_search_address_btn);
        Btressignup = findViewById(R.id.res_info_register);

        Eresname.getBackground().setColorFilter(getResources().getColor(R.color.color_bolder), PorterDuff.Mode.SRC_ATOP);
        Eresphone.getBackground().setColorFilter(getResources().getColor(R.color.color_bolder), PorterDuff.Mode.SRC_ATOP);
        Eresadd_detail.getBackground().setColorFilter(getResources().getColor(R.color.color_bolder), PorterDuff.Mode.SRC_ATOP);
        Eresaddress.getBackground().setColorFilter(getResources().getColor(R.color.color_bolder), PorterDuff.Mode.SRC_ATOP);
        Eresadd_num.getBackground().setColorFilter(getResources().getColor(R.color.color_bolder), PorterDuff.Mode.SRC_ATOP);

        userInfoClass = UserInfoClass.getInstance(getApplicationContext());
        requestQueue = VolleySingleton.getmInstance(getApplicationContext()).getRequestQueue();

        Eresadd_num.setText(userInfoClass.getResadd_num());
        Eresaddress.setText(userInfoClass.getResaddress());
        Eresadd_detail.setText(userInfoClass.getResadd_detail());
        Toolbar toolbar = findViewById(R.id.res_res_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(Eresadd_num.getText().toString().length()!=0) {
            Eresname.setText(userInfoClass.getResname());
        }

        SharedPreferences res_name = getSharedPreferences("res_name", Activity.MODE_PRIVATE);
        SharedPreferences.Editor res_name_editor = res_name.edit();
        res_name_editor.putString("res_name",userInfoClass.getResname());
        res_name_editor.commit();

        Eresphone.setText(userInfoClass.getResphone());

        final Geocoder geocoder = new Geocoder(this);

        Btfindaddress.setOnClickListener(v -> {
            userInfoClass.setResname(Eresname.getText().toString());
            userInfoClass.setResphone(Eresphone.getText().toString());
            startActivity(new Intent(Registration_RestaurantActivity.this,JusoActivity.class));
            finish();
        });

        Btressignup.setOnClickListener(v -> {
            if(Eresaddress.getText().toString().length()!=0 && Eresphone.getText().toString().length()!=0) {
                userInfoClass.setResphone(Eresphone.getText().toString());
                userInfoClass.setResname(Eresname.getText().toString());
                userInfoClass.setResadd_detail(Eresadd_detail.getText().toString());
                List<Address> list = null;
                try {
                    list = geocoder.getFromLocationName(userInfoClass.getResaddress(), 10);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "인터넷 상태를 확인해 주세요.", Toast.LENGTH_LONG).show();
                }
                if (list != null) {
                    if (list.size() == 0) {
                        Toast.makeText(getApplicationContext(), "정확한 주소를 입력해 주세요.", Toast.LENGTH_LONG).show();
                    } else {
                        lat = Double.toString(list.get(0).getLatitude());
                        lon = Double.toString(list.get(0).getLongitude());
                        userInfoClass.setLat(lat);
                        userInfoClass.setLon(lon);
                    }
                }
                requestQueue.add(stringRequest2);
                Intent intent = new Intent(Registration_RestaurantActivity.this, JudgingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }else if(Eresaddress.getText().toString().length()==0){
                Toast.makeText(getApplicationContext(), "주소를 입력해 주세요.", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(), "음식점 전화번호를 입력해 주세요.", Toast.LENGTH_LONG).show();

            }
            //TODO ALL PAGE MUST BE FINISHED

        });
    }
    final StringRequest stringRequest2 = new StringRequest(Request.Method.POST, resinfo_url, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

        }
    }, error -> Log.d("spark123","errorv")) {
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> params = new HashMap<>();
            params.put("m_id",userInfoClass.getuId());
            params.put("m_ownername",userInfoClass.getOwnername());
            if(userInfoClass.getLat().length()==0){params.put("m_lat","0");}
            else{
            params.put("m_lat",userInfoClass.getLat());
            }
            if(userInfoClass.getLon().length()==0){params.put("m_lon","0");}
            else {
                params.put("m_lon", userInfoClass.getLon());
            }
            params.put("m_address",userInfoClass.getResaddress()+" "+userInfoClass.getResadd_detail());
            params.put("m_resname",userInfoClass.getResname());
            params.put("m_resnum",userInfoClass.getResid());
            params.put("m_resphone",userInfoClass.getResphone());
            return params;
        }
    };
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
