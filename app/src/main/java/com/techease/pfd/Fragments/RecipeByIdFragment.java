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
import com.techease.pfd.Utils.Alert_Utils;

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
    TextView tvRecipeName;
    ImageView imageButton;
    android.support.v7.app.AlertDialog alertDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_recipe_by_id, container, false);

        sharedPreferences = getActivity().getSharedPreferences(Links.MyPrefs, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
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
        tvRecipeName=(TextView)view.findViewById(R.id.tvRecipeNameOnImage);
        imageButton=(ImageView)view.findViewById(R.id.ib);

        Title.setTypeface(typeface2);
        Time.setTypeface(typeface2);
        Ing.setTypeface(typeface2);
        Ins.setTypeface(typeface2);
        Cat.setTypeface(typeface2);
        tvRecipeName.setTypeface(typeface);

        ShowIns.setTypeface(typeface);
        ShowIng.setTypeface(typeface);
        ShowTitle.setTypeface(typeface);
        ShowTime.setTypeface(typeface);
        ShowCat.setTypeface(typeface);


        api_token=sharedPreferences.getString("api_token","");
        recipeId=getArguments().getString("recipeId");
        if (alertDialog==null)
        {
            alertDialog= Alert_Utils.createProgressDialog(getActivity());
            alertDialog.show();
        }
        apicall();


        return  view;
    }

    private void apicall() {
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
                        tvRecipeName.setText(object.getString("title"));
                        ShowIns.setText(object.getString("instructions"));
                        Glide.with(getActivity()).load(object.getString("image_url")).into(imageView);

                    if (alertDialog!=null)
                        alertDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                    if (alertDialog!=null)
                        alertDialog.dismiss();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error" , String.valueOf(error.getCause()));
                if (alertDialog!=null)
                    alertDialog.dismiss();

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

}
