package com.techease.pfd.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.techease.pfd.Adapters.ListOfRecipeAdapter;
import com.techease.pfd.Controller.ListOfRecipeModel;
import com.techease.pfd.R;

import java.util.ArrayList;
import java.util.List;

public class ListOfRecipesFragment extends Fragment {

    ProgressBar progressBar;
    int progressbarstatus = 0;
    RecyclerView recyclerView;
    ListOfRecipeAdapter listOfRecipeAdapter;
    List<ListOfRecipeModel> listOfRecipeModels;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_list_of_recipes, container, false);

        progressBar=(ProgressBar)view.findViewById(R.id.progressbarListofRecipe);
        recyclerView=(RecyclerView)view.findViewById(R.id.rvListOfRecipe);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listOfRecipeModels=new ArrayList<>();
        listOfRecipeAdapter=new ListOfRecipeAdapter(getActivity(),listOfRecipeModels);
        recyclerView.setAdapter(listOfRecipeAdapter);


        return view;
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
