package com.google.firebase.codelab.friendlychat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;

import org.json.JSONException;
import org.json.JSONObject;

public class NewCompany extends AppCompatActivity {
    EditText companyName;
    Button registerButton;
    String name;
    TextView principal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newcompany);

        companyName = findViewById(R.id.companyname);
        registerButton = findViewById(R.id.companyRegisterButton);
        principal = findViewById(R.id.companyBack);

        Firebase.setAndroidContext(this);

        principal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewCompany.this, Company.class));
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = companyName.getText().toString();

                if(name.equals("")){
                    companyName.setError("can't be blank");
                }
                else if(!name.matches("[A-Za-z0-9]+")){
                    companyName.setError("only alphabet or number allowed");
                }
                else if(name.length()<5){
                    companyName.setError("at least 5 characters long");
                }
                else {
                    final ProgressDialog pd = new ProgressDialog(NewCompany.this);
                    pd.setMessage("Loading...");
                    pd.show();

                    String url = "https://friendlychat-5677b.firebaseio.com/compania.json";

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                        @Override
                        public void onResponse(String s) {
                            Firebase reference = new Firebase("https://friendlychat-5677b.firebaseio.com/compania");

                            if(s.equals("null")) {
                                reference.child(name).child("chat").setValue(name);
                                reference.child(name).child("messages").setValue(name);
                                reference.child(name).child("users").setValue(name);
                                Toast.makeText(NewCompany.this, "registration successful", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(NewCompany.this, Company.class));
                            }
                            else {
                                try {
                                    JSONObject obj = new JSONObject(s);

                                    if (!obj.has(name)) {
                                        reference.child(name).child("chat").setValue(name);
                                        reference.child(name).child("messages").setValue(name);
                                        reference.child(name).child("users").setValue(name);
                                        Toast.makeText(NewCompany.this, "registration successful", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(NewCompany.this, Company.class));
                                    } else {
                                        Toast.makeText(NewCompany.this, "Company name already exists", Toast.LENGTH_LONG).show();
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
                            System.out.println("" + volleyError );
                            pd.dismiss();
                        }
                    });

                    RequestQueue rQueue = Volley.newRequestQueue(NewCompany.this);
                    rQueue.add(request);
                }
            }
        });

    }
}
