package com.techease.pfd.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.techease.pfd.Configuration.Links;
import com.techease.pfd.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class RecipeByIdFragment extends Fragment {

    ImageView imageView;
    Typeface typeface,typeface2;
    TextView Title,Time,Ing,Ins,Cat,ShowTitle,ShowTime,ShowCat,ShowIns,ShowIng;
    String api_token,recipeId;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressBar progressBar;
    int progressbarstatus = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_recipe_by_id, container, false);

        sharedPreferences = getActivity().getSharedPreferences(Links.MyPrefs, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        progressBar=(ProgressBar)view.findViewById(R.id.pbRecipeById);
        typeface=Typeface.createFromAsset(getActivity().getAssets(),"font/brandon_reg.otf");
        typeface2=Typeface.createFromAsset(getActivity().getAssets(),"font/brandon_blk.otf");
        imageView=(ImageView)view.findViewById(R.id.ivRecipeByIdItemImage);
        Title=(TextView)view.findViewById(R.id.tvRecipeTitle);
        Time=(TextView)view.findViewById(R.id.tvRecipeTimeToCook);
        Ing=(TextView)view.findViewById(R.id.tvIngredientsRecipe);
        Ins=(TextView)view.findViewById(R.id.tvInstructionsRecipe);
        Cat=(TextView)view.findViewById(R.id.tvRecipeCategory);
        ShowTitle=(TextView)view.findViewById(R.id.showTitle);
        ShowTime=(TextView)view.findViewById(R.id.showTime);
        ShowCat=(TextView)view.findViewById(R.id.showCateogry);
        ShowIns=(TextView)view.findViewById(R.id.showInstructions);
        ShowIng=(TextView)view.findViewById(R.id.showIngredients);

        Title.setTypeface(typeface2);
        Time.setTypeface(typeface2);
        Ing.setTypeface(typeface2);
        Ins.setTypeface(typeface2);
        Cat.setTypeface(typeface2);

        ShowIns.setTypeface(typeface);
        ShowIng.setTypeface(typeface);
        ShowTitle.setTypeface(typeface);
        ShowTime.setTypeface(typeface);
        ShowCat.setTypeface(typeface);

        api_token=sharedPreferences.getString("api_token","");
        recipeId=getArguments().getString("recipeId");

        apicall();


        return  view;
    }

    private void apicall() {
//        progressBar.setVisibility(View.VISIBLE);
//        setProgressValue(progressbarstatus);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://pfd.techeasesol.com/api/v1/user/recipes/"+recipeId+
                "?api_token="+api_token
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("rbd respo", response);
                try {

                        JSONObject jsonObject=new JSONObject(response);
                        JSONObject object=jsonObject.getJSONObject("data");
                        ShowTime.setText(object.getString("time_to_cook"));
                        ShowCat.setText(object.getString("tags"));
                        ShowIng.setText(object.getString("ingredients"));
                        ShowTitle.setText(object.getString("title"));
                        ShowIns.setText(object.getString("instructions"));
                        Glide.with(getActivity()).load(object.getString("image_url")).into(imageView);
                     //   progressBar.setVisibility(View.INVISIBLE);



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.INVISIBLE);
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
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }

    private void setProgressValue(final int progress) {

        // set the progress
        progressBar.setProgress(progress);
        // thread is used to change the progress value
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setProgressValue(progress + 10);
            }
        });
        thread.start();
    }
}
