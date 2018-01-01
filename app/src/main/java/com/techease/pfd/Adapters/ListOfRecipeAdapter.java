package com.techease.pfd.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.techease.pfd.Controller.ListOfRecipeModel;

import java.util.List;

/**
 * Created by Adam Noor on 01-Jan-18.
 */

public class ListOfRecipeAdapter extends RecyclerView.Adapter {

    List<ListOfRecipeModel> models;
    Context context;


    public ListOfRecipeAdapter(Context context, List<ListOfRecipeModel> listOfRecipeModels) {
        this.context=context;
        this.models=listOfRecipeModels;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
