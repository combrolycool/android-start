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

import org.json.JSONException;
import org.json.JSONObject;

public class AdminLogin extends AppCompatActivity {

    TextView AdminBack;
    EditText AdminUsername;
    Button AdminLoginButton;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        AdminBack = findViewById(R.id.adminBack);
        AdminUsername =findViewById(R.id.adminUsername);
        AdminLoginButton = findViewById(R.id.adminsLoginButton);

        AdminBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminLogin.this, Login.class));
            }
        });

        AdminLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = AdminUsername.getText().toString();

                if(user.equals("")){
                    AdminUsername.setError("can't be blank");
                }
                else{
                    String url = "https://friendlychat-5677b.firebaseio.com/Admin/users.json";
                    final ProgressDialog pd = new ProgressDialog(AdminLogin.this);
                    pd.setMessage("Loading...");
                    pd.show();

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                        @Override
                        public void onResponse(String s) {
                            if(s.equals("null")){
                                Toast.makeText(AdminLogin.this, "Admin not found", Toast.LENGTH_LONG).show();
                            }
                            else{
                                try {
                                    JSONObject obj = new JSONObject(s);

                                    if(!obj.has(user)){
                                        Toast.makeText(AdminLogin.this, "Admin not found", Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        UserDetails.username = user;
                                        startActivity(new Intent(AdminLogin.this, Company.class));
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

                    RequestQueue rQueue = Volley.newRequestQueue(AdminLogin.this);
                    rQueue.add(request);
                }

            }
        });

    }
}

