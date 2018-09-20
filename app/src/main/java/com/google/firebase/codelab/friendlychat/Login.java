package com.google.firebase.codelab.friendlychat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Login extends AppCompatActivity {
    TextView registerUser, noCompany, admin;
    EditText username;
    Button loginButton;
    Spinner spinner;
    String user, companyselected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registerUser = (TextView)findViewById(R.id.register);
        noCompany = findViewById(R.id.noCompany);
        username = (EditText)findViewById(R.id.LoginUsername);
        loginButton = (Button)findViewById(R.id.loginButton);
        spinner = findViewById(R.id.spinner);
        admin = findViewById(R.id.admin);

        addItemsOnSpinner();

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        admin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(Login.this, AdminLogin.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                companyselected = String.valueOf(spinner.getSelectedItem().toString());

                if(user.equals("")){
                    username.setError("can't be blank");
                }
                else{
                    String url = "https://friendlychat-5677b.firebaseio.com/compania/"+companyselected+"/users.json";
                    final ProgressDialog pd = new ProgressDialog(Login.this);
                    pd.setMessage("Loading...");
                    pd.show();

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                        @Override
                        public void onResponse(String s) {
                            if(s.equals("null")){
                                Toast.makeText(Login.this, "user not found in the company selected", Toast.LENGTH_LONG).show();
                            }
                            else{
                                try {
                                    JSONObject obj = new JSONObject(s);

                                    if(!obj.has(user)){
                                        Toast.makeText(Login.this, "user not found in the company selected", Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        UserDetails.username = user;
                                        CompanyDetails.companyName = companyselected;
                                        startActivity(new Intent(Login.this, Users.class));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            pd.dismiss();
                        }
                    },new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            System.out.println("" + volleyError);
                            pd.dismiss();
                        }
                    });

                    RequestQueue rQueue = Volley.newRequestQueue(Login.this);
                    rQueue.add(request);
                }

            }
        });
    }

    public void addItemsOnSpinner(){

        String url = "https://friendlychat-5677b.firebaseio.com/compania.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                doOnSuccess(s);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(Login.this);
        rQueue.add(request);
    }

    public void doOnSuccess(String s) {

        List<String> al = new ArrayList<>();
        int totalCompany = 0;

        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";

            while (i.hasNext()) {
                key = i.next().toString();
                al.add(key);

                totalCompany++;
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        if (totalCompany >= 1) {
            List<String> al2 = new ArrayList<>(al);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, al2);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
        }else{
            noCompany.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.GONE);
        }
    }
}