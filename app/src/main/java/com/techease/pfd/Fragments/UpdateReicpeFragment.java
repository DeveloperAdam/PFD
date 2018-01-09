package com.techease.pfd.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.techease.pfd.Configuration.Links;
import com.techease.pfd.R;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class UpdateReicpeFragment extends Fragment implements View.OnClickListener {

    ImageView btnAddNewetIng,btnAddNewetIns;
    TextView tvTitle,tvIngredients,tvInstructions,tvTags,tvTime;
    EditText etTitle,etIngredients,etInstructions,etTime;
    Button btnTag1,btnTag2,btnTag3,btnTag4,btnTag5,btnTag6,btnTag7,btnSubmitRecipe;
    Typeface typeface,typeface2;
    LinearLayout InstructionLayout,IngredientsLayout;
    FrameLayout frameLayoutInstruction,frameLayoutIngredients;
    ArrayList<EditText> ingredientList = new ArrayList();
    ArrayList<EditText> instructionList = new ArrayList<>();
    ArrayList<ImageView> ingredientImageViewList = new ArrayList();
    ArrayList<ImageView> instructionImageViewList = new ArrayList<>();
    String strIngredients,strInstructions,apiToken,strTime,strTitle;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int hint=1;
    String Recipe_Category;
    ProgressBar progressBar;
    int progressbarstatus = 0;
    File destination;
    String getTitle,getTime,getIns,getTag,getIng,getImage;
    String getRecipeId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_reicpe, container, false);

        sharedPreferences = getActivity().getSharedPreferences(Links.MyPrefs, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        progressBar=(ProgressBar)view.findViewById(R.id.proressbarRecipe2);
        InstructionLayout=(LinearLayout) view.findViewById(R.id.parentLayoutInstructions2);
        IngredientsLayout=(LinearLayout) view.findViewById(R.id.parentLayoutingredients2);
        frameLayoutIngredients=(FrameLayout)view.findViewById(R.id.frameLayoutIngredients2);
        frameLayoutInstruction=(FrameLayout)view.findViewById(R.id.frameLayoutInstructions2);
        typeface=Typeface.createFromAsset(getActivity().getAssets(),"font/brandon_blk.otf");
        typeface2=Typeface.createFromAsset(getActivity().getAssets(),"font/brandon_reg.otf");
        tvTitle=(TextView)view.findViewById(R.id.tvTitle2);
        tvTime=(TextView)view.findViewById(R.id.tvTime2);
        tvInstructions=(TextView)view.findViewById(R.id.tvInstructions2);
        tvIngredients=(TextView)view.findViewById(R.id.tvIngredients2);
        tvTags=(TextView)view.findViewById(R.id.tvTags2);
        etTitle=(EditText)view.findViewById(R.id.etTitle2);
        etIngredients=(EditText)view.findViewById(R.id.etIngredients2);
        etInstructions=(EditText)view.findViewById(R.id.etInstrunctions2);
        btnAddNewetIng=(ImageView)view.findViewById(R.id.btnAddIng2);
        btnAddNewetIns=(ImageView)view.findViewById(R.id.btnAddIns2);
        instructionList.add(etInstructions);
        ingredientList.add(etIngredients);
        ingredientImageViewList.add(btnAddNewetIng);
        instructionImageViewList.add(btnAddNewetIns);
        etTime=(EditText)view.findViewById(R.id.etTime2);
        btnTag1=(Button)view.findViewById(R.id.btnTag12);
        btnTag2=(Button)view.findViewById(R.id.btnTag22);
        btnTag3=(Button)view.findViewById(R.id.btnTag32);
        btnTag4=(Button)view.findViewById(R.id.btnTag42);
        btnTag5=(Button)view.findViewById(R.id.btnTag52);
        btnTag6=(Button)view.findViewById(R.id.btnTag62);
        btnTag7=(Button)view.findViewById(R.id.btnTag72);
        btnSubmitRecipe=(Button)view.findViewById(R.id.btnSubmitRecipe2);

        tvTags.setTypeface(typeface);
        tvTitle.setTypeface(typeface);
        tvIngredients.setTypeface(typeface);
        tvInstructions.setTypeface(typeface);
        tvTime.setTypeface(typeface);
        etTime.setTypeface(typeface2);
        etInstructions.setTypeface(typeface2);
        etIngredients.setTypeface(typeface2);
        etTitle.setTypeface(typeface2);
        btnSubmitRecipe.setTypeface(typeface);
        btnTag1.setTypeface(typeface);
        btnTag2.setTypeface(typeface);
        btnTag3.setTypeface(typeface);
        btnTag4.setTypeface(typeface);
        btnTag5.setTypeface(typeface);
        btnTag6.setTypeface(typeface);
        btnTag7.setTypeface(typeface);

        apiToken=sharedPreferences.getString("api_token","");

        //bundle data for updation and deletion
        btnSubmitRecipe.setText("Update Recipe");
        getTime=getArguments().getString("time");
        getTag=getArguments().getString("tag");
        getIns=getArguments().getString("ins");
        getIng=getArguments().getString("ing");
        getTitle=getArguments().getString("title");
        getImage=getArguments().getString("img");
        getRecipeId=getArguments().getString("id");
        Recipe_Category=getTag;

        Log.d("meriId",getTitle);

        if (Recipe_Category.equals("Fast-Food"))
        {
            btnTag1.setBackgroundColor(Color.DKGRAY);
        }else
            if (Recipe_Category.equals("Traditional"))
        {
            btnTag2.setBackgroundColor(Color.DKGRAY);
        }
        else
            if (Recipe_Category.equals("Afghani"))
        {
            btnTag3.setBackgroundColor(Color.DKGRAY);
        }
        else
            if (Recipe_Category.equals("Chinese"))
        {
            btnTag4.setBackgroundColor(Color.DKGRAY);
        }
        else
            if (Recipe_Category.equals("Sweets"))
        {
            btnTag5.setBackgroundColor(Color.DKGRAY);
        }
        else
            if (Recipe_Category.equals("Baking"))
            {
                btnTag6.setBackgroundColor(Color.DKGRAY);
            }
            else
                if (Recipe_Category.equals("Italian"))
                {
                    btnTag7.setBackgroundColor(Color.DKGRAY);
                }

        //set text and image to their own place
        etTitle.setText(getTitle);
        etTime.setText(getTime);
        etInstructions.setText(getIns);
        etIngredients.setText(getIng);

        btnAddNewetIng.setOnClickListener(this);
        btnAddNewetIns.setOnClickListener(this);
        btnTag1.setOnClickListener(this);
        btnTag2.setOnClickListener(this);
        btnTag3.setOnClickListener(this);
        btnTag4.setOnClickListener(this);
        btnTag5.setOnClickListener(this);
        btnTag6.setOnClickListener(this);
        btnTag7.setOnClickListener(this);

        etInstructions.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() < 1) {
                    btnAddNewetIns.setVisibility(View.INVISIBLE);
                } else {
                    btnAddNewetIns.setVisibility(View.VISIBLE);
                }
            }
        });


        etIngredients.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() < 1) {
                    btnAddNewetIng.setVisibility(View.INVISIBLE);
                } else {
                    btnAddNewetIng.setVisibility(View.VISIBLE);
                }
            }
        });






        btnSubmitRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeDataFromFields();
                onDataInput();

            }
        });

        return view;
    }


    private void onDataInput(){
        strTime=etTime.getText().toString();
        strTitle=etTitle.getText().toString();
        if (strTitle.equals(""))
        {
            etTitle.setError("Please enter the title");
        }
        else
        if (strTime.equals(""))
        {
            etTime.setError("Please enter time to cook");
        }
        else
        if (strIngredients.equals(""))
        {
            etIngredients.setError("Please enter ingredients");
        }
        else
        if (strInstructions.equals(""))
        {
            etInstructions.setError("Please enter instructions");
        }
        else
        {
           // progressBar.setVisibility(View.VISIBLE);
           // setProgressValue(progressbarstatus);
            apicall();
//            UpdateReicpeFragment.UploadFileToServer uploadFileToServer=new UpdateReicpeFragment.UploadFileToServer();
//            uploadFileToServer.execute();
        }

    }




    private void setProgressValue(final int progressbarstatus) {
        // set the progress
        progressBar.setProgress(progressbarstatus);
        // thread is used to change the progress value
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setProgressValue(progressbarstatus + 10);
            }
        });
        thread.start();
    }



    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.btnAddIng:
                btnAddNewetIng.setVisibility(View.INVISIBLE);
                addEditTextForIngredients();
                break;
            case R.id.btnAddIns:
                btnAddNewetIns.setVisibility(View.INVISIBLE);
                addEditTextForInstructions();
                break;
            case R.id.btnTag1:
                Recipe_Category="Fast-Food";
                Toast.makeText(getActivity(), "Recipe Category FastFood is selected ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnTag2:
                Recipe_Category="Traditional";
                Toast.makeText(getActivity(), "Recipe Category Traditional is selected ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnTag3:
                Recipe_Category="Afghani";
                Toast.makeText(getActivity(), "Recipe Category Afghani is selected ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnTag4:
                Recipe_Category="Chinese";
                Toast.makeText(getActivity(), "Recipe Category Chinese is selected ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnTag5:
                Recipe_Category="Sweets";
                Toast.makeText(getActivity(), "Recipe Category Sweets is selected ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnTag6:
                Recipe_Category="Baking";
                Toast.makeText(getActivity(), "Recipe Category Baking is selected ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnTag7:
                Recipe_Category="Italian";
                Toast.makeText(getActivity(), "Recipe Category Italian is selected ", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private void addEditTextForInstructions() {
        instructionImageViewList.get(instructionImageViewList.size() - 1).setVisibility(View.INVISIBLE);
        final FrameLayout frameLayout = new FrameLayout(getActivity());
        frameLayout.setLayoutParams(frameLayoutInstruction.getLayoutParams());
        frameLayout.setTag(instructionList.size());
        EditText editText = new EditText(getActivity());
        editText.setLayoutParams(etInstructions.getLayoutParams());
        editText.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edittext_back));
        editText.setHint("Add Instruction");
        editText.setTypeface(typeface2);
        hint++;
        editText.setPadding(etInstructions.getPaddingLeft(), 0, 0, 0);
        frameLayout.addView(editText);
        final ImageView imageView = new ImageView(getActivity());
        imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.delete));
        imageView.setLayoutParams(btnAddNewetIns.getLayoutParams());
        frameLayout.addView(imageView);
        imageView.setTag(0);
        imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.delete));
        instructionImageViewList.add(imageView);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() < 1) {
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.delete));
                    imageView.setTag(0);
                } else {
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.plus));
                    imageView.setTag(1);
                }

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((int) (imageView.getTag()) == 1) {
                    addEditTextForInstructions();
                } else {
                    InstructionLayout.removeView(frameLayout);
                    instructionList.remove((int) (frameLayout.getTag()));
                    instructionImageViewList.remove((int) (frameLayout.getTag()));
                    instructionImageViewList.get(instructionImageViewList.size() - 1).setVisibility(View.VISIBLE);

                }
            }
        });
        InstructionLayout.addView(frameLayout);
        instructionList.add(editText);
    }

    private void addEditTextForIngredients() {
        ingredientImageViewList.get(ingredientImageViewList.size() - 1).setVisibility(View.INVISIBLE);
        final FrameLayout frameLayout = new FrameLayout(getActivity());
        frameLayout.setLayoutParams(frameLayoutIngredients.getLayoutParams());
        frameLayout.setTag(ingredientList.size());
        EditText  editText = new EditText(getActivity());
        editText.setLayoutParams(etIngredients.getLayoutParams());
        editText.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edittext_back));
        editText.setHint("Add Ingredient");
        editText.setTypeface(typeface2);
        editText.setPadding(etIngredients.getPaddingLeft(), 0, 0, 0);
        frameLayout.addView(editText);
        final ImageView imageView = new ImageView(getActivity());
        imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.delete));
        imageView.setLayoutParams(btnAddNewetIng.getLayoutParams());
        frameLayout.addView(imageView);
        imageView.setTag(0);
        imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.delete));
        ingredientImageViewList.add(imageView);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() < 1) {
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.delete));
                    imageView.setTag(0);
                } else {
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.plus));
                    imageView.setTag(1);
                }

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((int) (imageView.getTag()) == 1) {
                    addEditTextForIngredients();
                } else {
                    IngredientsLayout.removeView(frameLayout);
                    ingredientList.remove((int) (frameLayout.getTag()));
                    ingredientImageViewList.remove((int) (frameLayout.getTag()));
                    ingredientImageViewList.get(ingredientImageViewList.size() - 1).setVisibility(View.VISIBLE);

                }
            }
        });
        IngredientsLayout.addView(frameLayout);
        ingredientList.add(editText);
    }
    private void takeDataFromFields() {
        strIngredients = "";
        strInstructions = "";
        for (EditText etIngred : ingredientList) {
            strIngredients += etIngred.getText().toString() + ",";
            Log.d("zmaData",etIngred.getText().toString());
        }
        for (EditText etInstruc : instructionList) {
            strInstructions += etInstruc.getText().toString() + ",";
        }
        strIngredients = strIngredients.substring(0, strIngredients.length() - 1);
        strInstructions = strInstructions.substring(0, strInstructions.length() - 1);
    }

    private void apicall() {

        Log.d("zmaTit",etTitle.getText().toString());
        Log.d("zmaIns",strInstructions);
        Log.d("zmaIng",strIngredients);
        Log.d("zmaTime",etTime.getText().toString());
        Log.d("zmaToken",apiToken);
        Log.d("zmaTag",Recipe_Category);
        Log.d("zmaRid",getRecipeId);

        String timeToCook=etTime.getText().toString().trim();
        String strTitle=etTitle.getText().toString().trim();

        //removing space from text
        String strTimeExcSpace  = timeToCook.replaceAll(" ","");
        String strTitleExcSpace= strTitle.replaceAll(" ","");
        String strInsExcSpace=strInstructions.replaceAll(" ","");
        String strIngExcSpace=strIngredients.replaceAll(" ","");

        final String url ="http://pfd.techeasesol.com/api/v1/user/recipes/"+getRecipeId+"?api_token="+apiToken
                +"&title="+strTitleExcSpace+"&instructions="+strInsExcSpace+"&ingredients="+
                strIngExcSpace+"&time_to_cook="+strTimeExcSpace+"&tags="+Recipe_Category;
        Log.d("zma respo", url);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.PATCH,url , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("zma respo", response+"\n"+url);
                Fragment fragment=new UserListOfRecipes();
                getFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
              //  Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // progressBar.setVisibility(View.INVISIBLE);
                Log.d("error" , String.valueOf(error.getMessage()));

            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded;charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
//                params.put("title",title);
//                params.put("time_to_cook",time);
//                params.put("instructions",strInstructions);
//                params.put("ingredients",strIngredients);
//                params.put("tags",Recipe_Category);
//                params.put("api_token",apiToken);
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
