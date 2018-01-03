package com.techease.pfd.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techease.pfd.Controller.ListOfRecipeModel;
import com.techease.pfd.R;

import java.util.List;

/**
 * Created by Adam Noor on 01-Jan-18.
 */

public class ListOfRecipeAdapter extends RecyclerView.Adapter<ListOfRecipeAdapter.MyViewHolder> {

    List<ListOfRecipeModel> models;
    Context context;


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
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ListOfRecipeModel model=models.get(position);

        holder.tvRecipeName.setText(model.getRecipeName());
        holder.tvRecipeTime.setText(model.getRecipeTime());
        holder.tvRecipeCategory.setText(model.getRecipeCatgory());
        holder.tvRecipeId.setText(model.getId());
        Glide.with(context).load(model.getRecipeImage()).into(holder.ivRecipeImage);

    }



    @Override
    public int getItemCount() {
        return models.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvRecipeName,tvRecipeTime,tvRecipeCategory,tvRecipeId;
        ImageView ivRecipeImage;
        Typeface typeface,typeface2;
        public MyViewHolder(View itemView)
        {
            super(itemView);
            ivRecipeImage=(ImageView)itemView.findViewById(R.id.ivRecipeImage);
            tvRecipeName=(TextView)itemView.findViewById(R.id.tvRecipeName);
            tvRecipeCategory=(TextView)itemView.findViewById(R.id.tvRecipeCategory);
            tvRecipeTime=(TextView)itemView.findViewById(R.id.tvRecipeTime);
            tvRecipeId=(TextView)itemView.findViewById(R.id.tvRecipeId);
            typeface=Typeface.createFromAsset(context.getAssets(),"font/brandon_blk.otf");
            typeface2=Typeface.createFromAsset(context.getAssets(),"font/brandon_reg.otf");

            tvRecipeName.setTypeface(typeface);
            tvRecipeTime.setTypeface(typeface2);
            tvRecipeCategory.setTypeface(typeface2);
            tvRecipeId.setTypeface(typeface);

        }
    }
}
