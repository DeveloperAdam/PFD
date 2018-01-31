package com.techease.pfd.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.techease.pfd.Adapters.ListOfRecipeAdapter;
import com.techease.pfd.Configuration.Links;
import com.techease.pfd.Controller.ListOfRecipeModel;
import com.techease.pfd.R;
import com.techease.pfd.Utils.Alert_Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class UserListOfRecipes extends Fragment {

    GridView gridView;
    ArrayList<ListOfRecipeModel> listOfRecipeModels;
    ListOfRecipeAdapter listOfRecipeAdapter;
    String api_token;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    android.support.v7.app.AlertDialog alertDialog;
    MaterialSearchBar searchView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_user_list_of_recipes, container, false);

        sharedPreferences = getActivity().getSharedPreferences(Links.MyPrefs, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
      //  searchView=(MaterialSearchBar) view.findViewById(R.id.sv3);
      //  searchEducationList();
        api_token=sharedPreferences.getString("api_token","");
        gridView=(GridView) view.findViewById(R.id.gridview);

        if (alertDialog==null)
        {
            alertDialog= Alert_Utils.createProgressDialog(getActivity());
            alertDialog.show();
        }
        apicall();
        return view;
    }
//    private void searchEducationList() {
//        searchView.addTextChangeListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence query, int i, int i1, int i2) {
//                Log.d("LOG_TAG", getClass().getSimpleName() + " text changed " + searchView.getText());
//
//                query = query.toString().toLowerCase();
//                List<ListOfRecipeModel> newData = new ArrayList<>();
//                for (int j = 0; j < listOfRecipeModels.size(); j++) {
//                    final String test2 = listOfRecipeModels.get(j).getRecipeName().toLowerCase();
//                    if (test2.startsWith(String.valueOf(query))) {
//                        newData.add(listOfRecipeModels.get(j));
//                    }
//                }
//             //   listOfRecipeAdapter = new ListOfRecipeAdapter(getActivity(), newData);
//              //  recyclerView.setAdapter(listOfRecipeAdapter);
//                listOfRecipeAdapter.notifyDataSetChanged();
//            }
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//    }
    private void apicall() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://pfd.techeasesol.com/api/v1/recipes/all?api_token="+api_token
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("rest respo", response);

                try {
//                    listOfRecipeModels.clear();
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArr=jsonObject.getJSONArray("data");
                    listOfRecipeModels=new ArrayList<>();
                    for (int i=0; i<jsonArr.length(); i++)
                    {
                        JSONObject temp = jsonArr.getJSONObject(i);
                        ListOfRecipeModel model=new ListOfRecipeModel();
                        model.setId(temp.getString("id"));
                        model.setRecipeName(temp.getString("title"));
                        model.setRecipeCatgory(temp.getString("tags"));
                        model.setRecipeTime(temp.getString("time_to_cook"));
                        model.setRecipeImage(temp.getString("image_url"));
                        listOfRecipeModels.add(model);
                        if (alertDialog!=null)
                            alertDialog.dismiss();
                    }
                    if (getActivity()!=null)
                    {
                        listOfRecipeAdapter=new ListOfRecipeAdapter(getActivity(),listOfRecipeModels);
                        gridView.setAdapter(listOfRecipeAdapter);
                        listOfRecipeAdapter.notifyDataSetChanged();
                    }

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
    @Override
    public void onResume() {
        super.onResume();
        apicall();
       listOfRecipeAdapter=new ListOfRecipeAdapter(getActivity(),listOfRecipeModels);
        gridView.setAdapter(listOfRecipeAdapter);

    }
}
