package com.google.firebase.codelab.friendlychat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

public class CompanyUsers extends AppCompatActivity {

    ListView CompanyUsersList;
    TextView noCompanyUsersText;
    ArrayList<String> al = new ArrayList<>();
    int totalCompanyUsers = 0;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        CompanyUsersList = (ListView)findViewById(R.id.usersList);
        noCompanyUsersText = (TextView)findViewById(R.id.noUsersText);

        pd = new ProgressDialog(CompanyUsers.this);
        pd.setMessage("Loading...");
        pd.show();

        String url = "https://friendlychat-5677b.firebaseio.com/compania/"+CompanyDetails.companyName+"/users.json";

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

        RequestQueue rQueue = Volley.newRequestQueue(CompanyUsers.this);
        rQueue.add(request);

        CompanyUsersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CompanyDetails.chatAndUser = al.get(position);
                startActivity(new Intent(CompanyUsers.this, Chat.class));
            }
        });
    }

    public void doOnSuccess(String s){
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";

            while(i.hasNext()){
                key = i.next().toString();
                al.add(key);

                totalCompanyUsers++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(totalCompanyUsers <=0){
            noCompanyUsersText.setVisibility(View.VISIBLE);
            CompanyUsersList.setVisibility(View.GONE);
        }
        else{
            noCompanyUsersText.setVisibility(View.GONE);
            CompanyUsersList.setVisibility(View.VISIBLE);
            CompanyUsersList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al));
        }

        pd.dismiss();
    }
}
