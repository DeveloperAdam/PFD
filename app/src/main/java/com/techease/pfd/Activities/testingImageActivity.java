
package com.techease.pfd.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.techease.pfd.Adapters.ListOfRecipeAdapter;
import com.techease.pfd.Configuration.Links;
import com.techease.pfd.Controller.ListOfRecipeModel;
import com.techease.pfd.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class testingImageActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ListOfRecipeAdapter listOfRecipeAdapter;
    List<ListOfRecipeModel> listOfRecipeModels;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String api_token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_image);

        sharedPreferences = this.getSharedPreferences(Links.MyPrefs, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        api_token=sharedPreferences.getString("api_token","");
        recyclerView=(RecyclerView)findViewById(R.id.scrollableview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listOfRecipeModels=new ArrayList<>();
        apicall();
        listOfRecipeAdapter=new ListOfRecipeAdapter(this,listOfRecipeModels);
        recyclerView.setAdapter(listOfRecipeAdapter);

    }

    private void apicall() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://pfd.techeasesol.com/api/v1/recipes/all?api_token="+api_token
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("rest respo", response);
                try {
                    listOfRecipeModels.clear();
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArr=jsonObject.getJSONArray("data");
                    for (int i=0; i<jsonArr.length(); i++)
                    {
                        JSONObject temp = jsonArr.getJSONObject(i);
                        ListOfRecipeModel model=new ListOfRecipeModel();
                        model.setId(temp.getString("id"));
                        model.setRecipeName(temp.getString("title"));
                        model.setRecipeCatgory(temp.getString("tags"));
                        model.setRecipeTime(temp.getString("time_to_cook"));
                        model.setRecipeImage(temp.getString("image_url"));
                        model.setRecipeIng(temp.getString("ingredients"));
                        model.setRecipeIns(temp.getString("instructions"));

                        listOfRecipeModels.add(model);

                    }
                    listOfRecipeAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error" , String.valueOf(error.getCause()));

            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded;charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }

        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }
}
