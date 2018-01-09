package com.techease.pfd.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.techease.pfd.Fragments.ListOfRecipesFragment;
import com.techease.pfd.Fragments.RecipeByIdFragment;
import com.techease.pfd.Fragments.UpdateReicpeFragment;
import com.techease.pfd.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Adam Noor on 01-Jan-18.
 */

public class ListOfRecipeAdapter extends RecyclerView.Adapter<ListOfRecipeAdapter.MyViewHolder> {

    List<ListOfRecipeModel> models;
    Context context;
    String strTitle,strTime,strTag,strIns,strIng,strId,strImage;
    String  api_token;
    String recipeId;
    final CharSequence[] items = {"Update", "Delete", "Cancel"};
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public ListOfRecipeAdapter(Context context, List<ListOfRecipeModel> listOfRecipeModels) {
        this.context=context;
        this.models=listOfRecipeModels;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_of_recipes,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final ListOfRecipeModel model=models.get(position);

        sharedPreferences = context.getSharedPreferences(Links.MyPrefs, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        api_token=sharedPreferences.getString("api_token","");


        holder.tvRecipeName.setText(model.getRecipeName());
        holder.tvRecipeTime.setText(model.getRecipeTime());
        holder.tvRecipeCategory.setText(model.getRecipeCatgory());
        holder.tvRecipeId.setText(model.getId());
        Glide.with(context).load(model.getRecipeImage()).into(holder.ivRecipeImage);



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
        Fragment fragment = new UpdateReicpeFragment();
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

                Fragment fragment=new ListOfRecipesFragment();
                ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();


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

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvRecipeName, tvRecipeTime, tvRecipeCategory, tvRecipeId;
        ImageView ivRecipeImage;
        Typeface typeface, typeface2;
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        LinearLayout linearLayout;
        String recipeId, api_token;
        final CharSequence[] items = {"Update", "Delete", "Cancel"};

        public MyViewHolder(final View itemView) {
            super(itemView);
            ivRecipeImage = (ImageView) itemView.findViewById(R.id.ivRecipeImage);
            tvRecipeName = (TextView) itemView.findViewById(R.id.tvRecipeName);
            tvRecipeCategory = (TextView) itemView.findViewById(R.id.tvRecipeCategory);
            tvRecipeTime = (TextView) itemView.findViewById(R.id.tvRecipeTime);
            tvRecipeId = (TextView) itemView.findViewById(R.id.tvRecipeId);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.llParentView);
            typeface = Typeface.createFromAsset(context.getAssets(), "font/brandon_blk.otf");
            typeface2 = Typeface.createFromAsset(context.getAssets(), "font/brandon_reg.otf");
            sharedPreferences = context.getSharedPreferences(Links.MyPrefs, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();

            tvRecipeName.setTypeface(typeface);
            tvRecipeTime.setTypeface(typeface2);
            tvRecipeCategory.setTypeface(typeface2);
            tvRecipeId.setTypeface(typeface);

            //  linearLayout.setOnClickListener(this);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String recipeId = tvRecipeId.getText().toString();
                    Bundle bundle = new Bundle();
                    bundle.putString("recipeId", recipeId);
                    Fragment fragment = new RecipeByIdFragment();
                    fragment.setArguments(bundle);
                    ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("abc").commit();
                }
            });

//            linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//
//                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                    builder.setTitle("Select");
//                    builder.setItems(items, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            if (items[which].equals("Update")) {
//                                UpdateRecipe();
//                            } else if (items[which].equals("Delete")) {
//                                DeleteRecipe();
//                            } else if (items[which].equals("Cancel")) {
//                                dialog.dismiss();
//                            }
//
//                        }
//                    });
//                    builder.show();
//                    return true;
//
//                }
//            });
//
        }

        @Override
        public void onClick(View v) {
            String recipeId = tvRecipeId.getText().toString();
            Bundle bundle = new Bundle();
            bundle.putString("recipeId", recipeId);
            Fragment fragment = new RecipeByIdFragment();
            fragment.setArguments(bundle);
            ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("abc").commit();

        }

//        private void UpdateRecipe() {
//            Bundle bundle = new Bundle();
//            bundle.putString("title", strTitle);
//            bundle.putString("id", strId);
//            bundle.putString("img", strImage);
//            bundle.putString("ing", strIng);
//            bundle.putString("ins", strIns);
//            bundle.putString("tag", strTag);
//            bundle.putString("time", strTime);
//            Fragment fragment = new UpdateReicpeFragment();
//            fragment.setArguments(bundle);
//            ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("abc").commit();
//
//
//        }
//
//        private void DeleteRecipe() {
//            recipeId = tvRecipeId.getText().toString();
//            api_token = sharedPreferences.getString("api_token", "");
//            apicall();
//
//
//        }
//
//        private void apicall() {
//            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, "http://pfd.techeasesol.com/api/v1/user/recipes/" + recipeId +
//                    "?api_token=" + api_token
//                    , new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    Log.d("res", response);
//
//                   Fragment fragment=new ListOfRecipesFragment();
//                    ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
//                    Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
//
//
//                }
//
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    // progressBar.setVisibility(View.INVISIBLE);
//                    Log.d("error", String.valueOf(error.getCause()));
//
//                }
//            }) {
//                @Override
//                public String getBodyContentType() {
//                    return "application/x-www-form-urlencoded;charset=UTF-8";
//                }
//
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    Map<String, String> params = new HashMap<>();
//                    return params;
//                }
//
//            };
//            RequestQueue mRequestQueue = Volley.newRequestQueue(context);
//            stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
//                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            mRequestQueue.add(stringRequest);
//        }

    }
}
