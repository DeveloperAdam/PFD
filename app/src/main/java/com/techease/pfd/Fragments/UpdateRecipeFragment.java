package com.techease.pfd.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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


public class UpdateRecipeFragment extends Fragment {

    ImageView btnAddNewetIng,btnAddNewetIns;
    EditText etTitle,etIngredients,etInstructions,etTime;
    Button btnSubmitRecipe;
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
    File destination;
    String getTitle,getTime,getIns,getTag,getIng,getImage;
    String getRecipeId;
    Spinner spinner;
    ArrayList<String> list = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_update_recipe, container, false);

        sharedPreferences = getActivity().getSharedPreferences(Links.MyPrefs, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        spinner=(Spinner)view.findViewById(R.id.spinnerUpdate);
        InstructionLayout=(LinearLayout) view.findViewById(R.id.parentLayoutInstructionsUpdate);
        IngredientsLayout=(LinearLayout) view.findViewById(R.id.parentLayoutingredientsUpdate);
        frameLayoutIngredients=(FrameLayout)view.findViewById(R.id.frameLayoutIngredientsUpdate);
        frameLayoutInstruction=(FrameLayout)view.findViewById(R.id.frameLayoutInstructionsUpdate);
        typeface=Typeface.createFromAsset(getActivity().getAssets(),"font/brandon_blk.otf");
        typeface2=Typeface.createFromAsset(getActivity().getAssets(),"font/brandon_reg.otf");
        etTitle=(EditText)view.findViewById(R.id.etTitleUpdate);
        etIngredients=(EditText)view.findViewById(R.id.etIngredientsUpdate);
        etInstructions=(EditText)view.findViewById(R.id.etInstrunctionsUpdate);
        btnAddNewetIng=(ImageView)view.findViewById(R.id.btnAddIngUpdate);
        btnAddNewetIns=(ImageView)view.findViewById(R.id.btnAddInsUpdate);
        instructionList.add(etInstructions);
        ingredientList.add(etIngredients);
        ingredientImageViewList.add(btnAddNewetIng);
        instructionImageViewList.add(btnAddNewetIns);
        etTime=(EditText)view.findViewById(R.id.etTimeUpdate);
        btnSubmitRecipe=(Button)view.findViewById(R.id.btnSubmitRecipeUpdate);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);

        etTime.setTypeface(typeface2);
        etInstructions.setTypeface(typeface2);
        etIngredients.setTypeface(typeface2);
        etTitle.setTypeface(typeface2);
        btnSubmitRecipe.setTypeface(typeface);

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


        etTitle.setText(getTitle);
        etTime.setText(getTime);
        etInstructions.setText(getIns);
        etIngredients.setText(getIng);

        list.add("Fast Food");
        list.add("Traditional");
        list.add("Sweets");
        list.add("American");
        list.add("Chinese");
        list.add("Italian");
        list.add("Baking");
        list.add("Afghani");

        adapter.notifyDataSetChanged();
        spinner.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item=parent.getItemAtPosition(position);
                if (item.equals("Fast Food"))
                {
                    Recipe_Category="Fast Food";
                }
                else
                if (item.equals("Traditional"))
                {
                    Recipe_Category="Traditional";
                }
                else
                if (item.equals("Sweets"))
                {
                    Recipe_Category="Sweets";
                }
                else
                if (item.equals("American"))
                {
                    Recipe_Category="American";
                }
                else
                if (item.equals("Italian"))
                {
                    Recipe_Category="Italian";
                }
                else
                if (item.equals("Chinese"))
                {
                    Recipe_Category="Chinese";
                }
                else
                if (equals("Baking"))
                {
                    Recipe_Category="Baking";
                }
                else
                if (item.equals("Afghani"))
                {
                    Recipe_Category="Afghani";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnAddNewetIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAddNewetIng.setVisibility(View.INVISIBLE);
                addEditTextForIngredients();
            }
        });
        btnAddNewetIns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAddNewetIns.setVisibility(View.INVISIBLE);
                addEditTextForInstructions();
            }
        });

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
                Toast.makeText(getActivity(), "Recipe Updated", Toast.LENGTH_SHORT).show();
                Fragment fragment=new Recipe();
                getFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
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
