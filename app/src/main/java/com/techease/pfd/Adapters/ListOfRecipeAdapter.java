package com.techease.pfd.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.techease.pfd.Controller.ListOfRecipeModel;
import com.techease.pfd.Fragments.Recipe;
import com.techease.pfd.Fragments.RecipeByIdFragment;
import com.techease.pfd.Fragments.UpdateRecipeFragment;
import com.techease.pfd.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Adam Noor on 01-Jan-18.
 */

public class ListOfRecipeAdapter extends BaseAdapter {

    ArrayList<ListOfRecipeModel> models;
    Context context;
    String strTitle,strTime,strTag,strIns,strIng,strImage;
    String  api_token;
    String recipeId;
    final CharSequence[] items = {"Update", "Delete", "Cancel"};
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    LayoutInflater layoutInflater;

    public ListOfRecipeAdapter(Context context, ArrayList<ListOfRecipeModel> listOfRecipeModels) {
        this.context=context;
        this.models=listOfRecipeModels;
        this.layoutInflater=LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        if (models!=null) return models.size();

        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(models != null && models.size() > position) return  models.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        final ListOfRecipeModel model=models.get(position);
        if(models != null && models.size() > position) return  models.size();
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ListOfRecipeModel model=models.get(position);
        MyViewHolder holder = null;
        holder=new MyViewHolder() ;
        convertView=layoutInflater.inflate(R.layout.custom_list_of_recipes,parent,false);
        sharedPreferences = context.getSharedPreferences(Links.MyPrefs, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        holder.ivRecipeImage = (ImageView) convertView.findViewById(R.id.ivRecipeImage);
        holder. tvRecipeName = (TextView) convertView.findViewById(R.id.tvRecipeName);
        holder. linearLayout = (LinearLayout) convertView.findViewById(R.id.llParentView);
        holder.typeface = Typeface.createFromAsset(context.getAssets(), "font/brandon_blk.otf");
        holder. typeface2 = Typeface.createFromAsset(context.getAssets(), "font/brandon_reg.otf");
        sharedPreferences = context.getSharedPreferences(Links.MyPrefs, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        holder. tvRecipeName.setTypeface(holder.typeface);
        api_token=sharedPreferences.getString("api_token","");
        holder.tvRecipeName.setText(model.getRecipeName());
        Glide.with(context).load(model.getRecipeImage()).into(holder.ivRecipeImage);


        //  linearLayout.setOnClickListener(this);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("recipeId", model.getId());
                Fragment fragment = new RecipeByIdFragment();
                fragment.setArguments(bundle);
                ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("abc").commit();
            }
        });

        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                recipeId= model.getId();
                strTitle=model.getRecipeName();
                strTime=model.getRecipeTime();
                strTag=model.getRecipeCatgory();
                strIns=model.getRecipeIns();
                strIng=model.getRecipeIng();
                strImage=model.getRecipeImage();

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Select");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (items[which].equals("Update")) {
                            UpdateRecipe();
                        } else if (items[which].equals("Delete")) {
                            DeleteRecipe();
                        } else if (items[which].equals("Cancel")) {
                            dialog.dismiss();
                        }

                    }
                });
                builder.show();
                return true;
            }
        });
       convertView.setTag(holder);
        return convertView;
    }


    private void UpdateRecipe() {
        Bundle bundle = new Bundle();
        bundle.putString("title", strTitle);
        bundle.putString("id", recipeId);
        bundle.putString("img", strImage);
        bundle.putString("ing", strIng);
        bundle.putString("ins", strIns);
        bundle.putString("tag", strTag);
        bundle.putString("time", strTime);
        Fragment fragment = new UpdateRecipeFragment();
        fragment.setArguments(bundle);
        ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("abc").commit();
    }

    private void DeleteRecipe()
    {
        apicall();
    }

    private void apicall() {
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, "http://pfd.techeasesol.com/api/v1/user/recipes/" + recipeId +
                "?api_token=" + api_token
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("res", response);

                Fragment fragment=new Recipe();
                ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // progressBar.setVisibility(View.INVISIBLE);
                Log.d("error", String.valueOf(error.getCause()));

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
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }




    public class MyViewHolder {
        TextView tvRecipeName;
        ImageView ivRecipeImage;
        Typeface typeface, typeface2;
        LinearLayout linearLayout;
        final CharSequence[] items = {"Update", "Delete", "Cancel"};

    }
}
