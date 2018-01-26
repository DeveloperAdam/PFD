package com.techease.pfd.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.techease.pfd.Configuration.Links;
import com.techease.pfd.R;
import com.techease.pfd.Utils.CheckNetwork;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ResturantMenuFrag extends Fragment {

    TabLayout tabLayout;
    String restId, api_token,catId;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String[] Categories;
    ViewPager viewPager;
FrameLayout frameLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_resturant_menu, container, false);
        if(CheckNetwork.isInternetAvailable(getActivity())) //returns true if internet available
        {
            frameLayout=(FrameLayout)view.findViewById(R.id.frameLayoutCatItems);
            sharedPreferences = getActivity().getSharedPreferences(Links.MyPrefs, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            restId = sharedPreferences.getString("restId", "");
            api_token = sharedPreferences.getString("api_token", "");
            viewPager = (ViewPager) view.findViewById(R.id.pagerMenuPizzaHut);
            tabLayout = (TabLayout) view.findViewById(R.id.tabLayoutMenuPizzaHut);

            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            apicall();

        }
        else
        {
            Toast.makeText(getActivity(),"No Internet Connection",Toast.LENGTH_SHORT).show();
        }



        return view;
    }

    private void apicall() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://pfd.techeasesol.com/api/v1/resturants/" + restId + "/categories?api_token=" + api_token
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("rest respo", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArr = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject temp = jsonArr.getJSONObject(i);
                        Categories = new String[jsonArr.length()];
                        Categories[i] = temp.getString("category_name");
                        catId=temp.getString("id");
                        Log.d("catId",catId);
                        if (i==0)
                        {
                           editor.putString("0",catId).commit();

                        }
                        else if (i==1)
                        {
                            editor.putString("1",catId).commit();
                        }
                        else
                            if (i==2)
                            {
                                editor.putString("2",catId).commit();
                            }
                        else
                            if (i==3)
                            {
                                editor.putString("3",catId).commit();
                            }
                            else
                            if (i==4)
                            {
                                editor.putString("4",catId).commit();
                            }
                            else
                            if (i==5)
                            {
                                editor.putString("5",catId).commit();
                            }
                            else
                            if (i==6)
                            {
                                editor.putString("6",catId).commit();
                            }
                            else
                            if (i==7)
                            {
                                editor.putString("7",catId).commit();
                            }
                            else
                            if (i==8)
                            {
                                editor.putString("8",catId).commit();
                            }
                            else
                            if (i==9)
                            {
                                editor.putString("9",catId).commit();
                            }
                            else
                            if (i==10)
                            {
                                editor.putString("10",catId).commit();
                            }
                        tabLayout.addTab(tabLayout.newTab().setText(Categories[i]));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(200000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
        viewPager.setAdapter(new PagerAdapter(((FragmentActivity) getActivity()).getSupportFragmentManager(), tabLayout.getTabCount()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0 ) {
                    Fragment fragment = new ResutantCategoriesFrag();
                    Bundle bundle = new Bundle();
                    bundle.putString("id",sharedPreferences.getString("0",""));
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutCatItems, fragment).commit();
                }
                    else if (tab.getPosition() == 1) {
                    Fragment fragment = new ResutantCategoriesFrag();
                    Bundle bundle = new Bundle();
                    bundle.putString("id",sharedPreferences.getString("1",""));
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutCatItems, fragment).commit();
                } else if (tab.getPosition() == 2) {
                    Fragment fragment = new ResutantCategoriesFrag();
                    Bundle bundle = new Bundle();
                    bundle.putString("id",sharedPreferences.getString("2",""));
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutCatItems, fragment).commit();
                }
                else if (tab.getPosition()==3)
                {
                    Fragment fragment = new ResutantCategoriesFrag();
                    Bundle bundle = new Bundle();
                    bundle.putString("id",sharedPreferences.getString("3",""));
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutCatItems,fragment).commit();
                }
                else if (tab.getPosition()==4)
                {
                    Fragment fragment = new ResutantCategoriesFrag();
                    Bundle bundle = new Bundle();
                    bundle.putString("id",sharedPreferences.getString("4",""));
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutCatItems,fragment).commit();
                }
                else if (tab.getPosition()==5)
                {
                    Fragment fragment = new ResutantCategoriesFrag();
                    Bundle bundle = new Bundle();
                    bundle.putString("id",sharedPreferences.getString("5",""));
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutCatItems,fragment).commit();
                }
                else if (tab.getPosition()==6)
                {
                    Fragment fragment = new ResutantCategoriesFrag();
                    Bundle bundle = new Bundle();
                    bundle.putString("id",sharedPreferences.getString("6",""));
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutCatItems,fragment).commit();
                }
                else if (tab.getPosition()==7)
                {
                    Fragment fragment = new ResutantCategoriesFrag();
                    Bundle bundle = new Bundle();
                    bundle.putString("id",sharedPreferences.getString("7",""));
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutCatItems,fragment).commit();
                }
                else if (tab.getPosition()==8)
                {
                    Fragment fragment = new ResutantCategoriesFrag();
                    Bundle bundle = new Bundle();
                    bundle.putString("id",sharedPreferences.getString("8",""));
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutCatItems,fragment).commit();
                }
                else if (tab.getPosition()==9)
                {
                    Fragment fragment = new ResutantCategoriesFrag();
                    Bundle bundle = new Bundle();
                    bundle.putString("id",sharedPreferences.getString("9",""));
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutCatItems,fragment).commit();
                }
                else if (tab.getPosition()==10)
                {
                    Fragment fragment = new ResutantCategoriesFrag();
                    Bundle bundle = new Bundle();
                    bundle.putString("id",sharedPreferences.getString("10",""));
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutCatItems,fragment).commit();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(android.support.v4.app.FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }


        @Override
        public int getCount() {
            return mNumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
            return null;
        }
    }
}
