package com.google.firebase.codelab.friendlychat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

public class Company extends AppCompatActivity {
    ListView companyList;
    TextView noCompanyText;
    TextView newCompany;
    ArrayList<String> al = new ArrayList<>();
    int totalCompany = 0;
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compania);

        companyList = findViewById(R.id.companyList);
        noCompanyText = findViewById(R.id.noCompanyText);
        newCompany = findViewById(R.id.newCompany);

        pd = new ProgressDialog(Company.this);
        pd.setMessage("Loading...");
        pd.show();

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

        RequestQueue rQueue = Volley.newRequestQueue(Company.this);
        rQueue.add(request);

        companyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CompanyDetails.chatAndUser = al.get(position);
                startActivity(new Intent(Company.this, Users.class));
            }
        });

        newCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Company.this, NewCompany.class));
            }
        });
    }

    public void doOnSuccess(String s){
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";

            while (i.hasNext()) {
                key = i.next().toString();

                if (!key.equals(CompanyDetails.companyName)) {
                    al.add(key);
                }

                totalCompany++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (totalCompany <= 0) {
            noCompanyText.setVisibility(View.VISIBLE);
            companyList.setVisibility(View.GONE);
        } else {
            noCompanyText.setVisibility(View.GONE);
            companyList.setVisibility(View.VISIBLE);
            companyList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al));
        }

        pd.dismiss();
    }
}
