package com.techease.pfd.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.techease.pfd.Configuration.Links;
import com.techease.pfd.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class AddingRecipeFragment extends Fragment implements View.OnClickListener {

    ImageView imageView,btnAddNewetIng,btnAddNewetIns,ivCross;
    TextView tvTitle,tvIngredients,tvInstructions,tvTags,tvTime;
    EditText etTitle,etIngredients,etInstructions,etTime;
    Button   btnTag1,btnTag2,btnTag3,btnTag4,btnTag5,btnTag6,btnTag7,btnSubmitRecipe;
    Typeface typeface,typeface2;
    LinearLayout InstructionLayout,IngredientsLayout;
    FrameLayout frameLayoutInstruction,frameLayoutIngredients;
    ArrayList<EditText> ingredientList = new ArrayList();
    ArrayList<EditText> instructionList = new ArrayList<>();
    ArrayList<ImageView> ingredientImageViewList = new ArrayList();
    ArrayList<ImageView> instructionImageViewList = new ArrayList<>();
     int hint=0;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO=2;
    final CharSequence[] items = { "Take Photo", "Choose from Library","Cancel" };
    String strIngredients,strInstructions,apiToken;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    EditText customEditText;
    ImageView customImageView;
    String Recipe_Category;
    Bitmap bm;
    Bitmap thumbnail;
    String imageString;
    Service service;
    Uri uri;
      @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_adding_recipe, container, false);

          sharedPreferences = getActivity().getSharedPreferences(Links.MyPrefs, Context.MODE_PRIVATE);
          editor = sharedPreferences.edit();
        InstructionLayout=(LinearLayout) view.findViewById(R.id.parentLayoutInstructions);
        IngredientsLayout=(LinearLayout) view.findViewById(R.id.parentLayoutingredients);
        frameLayoutIngredients=(FrameLayout)view.findViewById(R.id.frameLayoutIngredients);
        frameLayoutInstruction=(FrameLayout)view.findViewById(R.id.frameLayoutInstructions);
        typeface=Typeface.createFromAsset(getActivity().getAssets(),"font/brandon_blk.otf");
        typeface2=Typeface.createFromAsset(getActivity().getAssets(),"font/brandon_reg.otf");
        tvTitle=(TextView)view.findViewById(R.id.tvTitle);
        imageView=(ImageView)view.findViewById(R.id.ivAddingImages);
        ivCross=(ImageView)view.findViewById(R.id.ivCross);
        tvTime=(TextView)view.findViewById(R.id.tvTime);
        tvInstructions=(TextView)view.findViewById(R.id.tvInstructions);
        tvIngredients=(TextView)view.findViewById(R.id.tvIngredients);
        tvTags=(TextView)view.findViewById(R.id.tvTags);
        etTitle=(EditText)view.findViewById(R.id.etTitle);
        etIngredients=(EditText)view.findViewById(R.id.etIngredients);
        etInstructions=(EditText)view.findViewById(R.id.etInstrunctions);
          btnAddNewetIng=(ImageView)view.findViewById(R.id.btnAddIng);
          btnAddNewetIns=(ImageView)view.findViewById(R.id.btnAddIns);
          instructionList.add(etInstructions);
          ingredientList.add(etIngredients);
          ingredientImageViewList.add(btnAddNewetIng);
          instructionImageViewList.add(btnAddNewetIns);
        etTime=(EditText)view.findViewById(R.id.etTime);
        btnTag1=(Button)view.findViewById(R.id.btnTag1);
        btnTag2=(Button)view.findViewById(R.id.btnTag2);
        btnTag3=(Button)view.findViewById(R.id.btnTag3);
        btnTag4=(Button)view.findViewById(R.id.btnTag4);
        btnTag5=(Button)view.findViewById(R.id.btnTag5);
        btnTag6=(Button)view.findViewById(R.id.btnTag6);
        btnTag7=(Button)view.findViewById(R.id.btnTag7);
        btnSubmitRecipe=(Button)view.findViewById(R.id.btnSubmitRecipe);

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

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Add Photo!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (items[which].equals("Take Photo"))
                        {
                            callCamera();
                        }
                        else if (items[which].equals("Choose from Library"))
                        {
                            callGallery();
                        }
                        else if (items[which].equals("Cancel"))
                        {
                            dialog.dismiss();
                        }

                    }
                });
                builder.show();
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

          btnAddNewetIng.setOnClickListener(this);
          btnAddNewetIns.setOnClickListener(this);
          btnTag1.setOnClickListener(this);
          btnTag2.setOnClickListener(this);
          btnTag3.setOnClickListener(this);
          btnTag4.setOnClickListener(this);
          btnTag5.setOnClickListener(this);
          btnTag6.setOnClickListener(this);
          btnTag7.setOnClickListener(this);




        btnSubmitRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageView.setImageResource(R.drawable.img);
                ivCross.setVisibility(View.GONE);
            }
        });

        btnSubmitRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeDataFromFields();
               apicall();
            }
        });


          return view;
    }

   private void apicalls()
   {
       JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, Links.User_Url+"recipes?api_token="+apiToken, new Response.Listener<JSONObject>() {
           @Override
           public void onResponse(JSONObject response) {

               Log.d("zmaRespo",response.toString());
               Toast.makeText(getActivity(), "Your recipe is successfully uploaded", Toast.LENGTH_SHORT).show();


           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               //  DialogUtils.sweetAlertDialog.dismiss();
//                final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
//                pDialog.getProgressHelper().setBarColor(Color.parseColor("#295786"));
//                pDialog.setTitleText("Email already registered");
//                pDialog.setContentText("Please signup with another email");
//                pDialog.setConfirmText("OK");
//                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//                        pDialog.dismissWithAnimation();
//                    }
//                });
//                pDialog.show();
               Log.d("zma error", String.valueOf(error.getCause()));
           }
       }) {
           @Override
           public String getBodyContentType() {
               return "application/x-www-form-urlencoded;charset=UTF-8";
           }

           @Override
           protected Map<String, String> getParams() throws AuthFailureError {
               Map<String, String> params = new HashMap<>();
//                params.put("dob", "1991-12-27");
               params.put("api_token",apiToken);
               params.put("title",etTitle.getText().toString());
               params.put("instructions", strInstructions);
               params.put("ingredients",strIngredients);
               params.put("time_to_cook",etTime.getText().toString());
               params.put("tags",Recipe_Category);
               params.put("image",bm.toString());

               Log.d("zmaParams",params.toString());

               return checkParams(params);
           }
       };

       RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
       stringRequest.setRetryPolicy(new
               DefaultRetryPolicy(200000,
               DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
               DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
       mRequestQueue.add(stringRequest);

   }
    private Map<String, String> checkParams(Map<String, String> map){
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pairs = (Map.Entry<String, String>)it.next();
            if(pairs.getValue()==null){
                map.put(pairs.getKey(), "");
            }
        }
        return map;
   }
    private void apicall() {
        final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("image",convertBitmapToString(bm)));
        nameValuePairs.add(new BasicNameValuePair("title",etTitle.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("time_to_cook",etTime.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("ingredients",strIngredients));
        nameValuePairs.add(new BasicNameValuePair("instructions",strInstructions));
        nameValuePairs.add(new BasicNameValuePair("tags",Recipe_Category));
        try {
            int SDK_INT = android.os.Build.VERSION.SDK_INT;
            if (SDK_INT > 8)
            {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                //your codes here

            }

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://pfd.techeasesol.com/api/v1/user/recipes?api_token="+apiToken);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            String responseStr = EntityUtils.toString(response.getEntity());
            Toast.makeText(getActivity(), "respo"+responseStr, Toast.LENGTH_SHORT).show();
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(getActivity(), "cusr"+e.getCause().toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            Toast.makeText(getActivity(), "client"+e.getCause().toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(getActivity(), "io exception"+e.getCause().toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    private String convertBitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream); //compress to which format you want.
        byte[] byte_arr = stream.toByteArray();
       String imagePath = Base64.encodeToString(byte_arr,Base64.DEFAULT);
        return imagePath;
    }


    private void callGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        //intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),REQUEST_TAKE_PHOTO);
    }

    private void callCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ivCross.setVisibility(View.VISIBLE);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            onCaptureImageResult(data);
        }
        else if (requestCode==REQUEST_TAKE_PHOTO)
        {
            onSelectFromGalleryResult(data);

        }
    }


    private void onCaptureImageResult(Intent data) {

         thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageView.setImageBitmap(thumbnail);
    }

    private void onSelectFromGalleryResult(Intent data) {

         uri=data.getData();
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                Log.d("zmaString",imageString);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), e.getCause().toString(), Toast.LENGTH_SHORT).show();
            }
            imageView.setImageBitmap(bm);
        }
    }

    @SuppressLint("ResourceAsColor")
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
                break;
            case R.id.btnTag2:
                Recipe_Category="Traditional";
                break;
            case R.id.btnTag3:
                Recipe_Category="Afghani";
                break;
            case R.id.btnTag4:
                Recipe_Category="Chinese";
                break;
            case R.id.btnTag5:
                Recipe_Category="Sweets";
                break;
            case R.id.btnTag6:
                Recipe_Category="Baking";
                break;
            case R.id.btnTag7:
                Recipe_Category="Italian";
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
    public void takeDataFromFields() {
        strIngredients = "";
        strInstructions = "";
        for (EditText etIngred : ingredientList) {
            strIngredients += etIngred.getText().toString() + ",";
        }
        for (EditText etInstruc : instructionList) {
            strInstructions += etInstruc.getText().toString() + ",";
        }
        strIngredients = strIngredients.substring(0, strIngredients.length() - 1);
        strInstructions = strInstructions.substring(0, strInstructions.length() - 1);

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

}
