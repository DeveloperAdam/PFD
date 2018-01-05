package com.techease.pfd.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
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

import com.bumptech.glide.Glide;
import com.techease.pfd.Configuration.Links;
import com.techease.pfd.R;
import com.techease.pfd.Utils.HTTPMultiPartEntity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


public class UpdateReicpeFragment extends Fragment implements View.OnClickListener {

    ImageView imageView,btnAddNewetIng,btnAddNewetIns,ivCross;
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
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO=2;
    final CharSequence[] items = { "Take Photo", "Choose from Library","Cancel" };
    String strIngredients,strInstructions,apiToken,strTime,strTitle;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int hint=1;
    String Recipe_Category;
    Bitmap bm;
    Bitmap thumbnail;
    ProgressBar progressBar;
    int progressbarstatus = 0;
    File destination;
    Uri uri;
    String strImage,strId,strIng,strIns,strTag,strTimetoCook;

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
        imageView=(ImageView)view.findViewById(R.id.ivAddingImages2);
        ivCross=(ImageView)view.findViewById(R.id.ivCross2);
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

        btnSubmitRecipe.setText("Update Recipe");
        strTime=getArguments().getString("time");
        strTag=getArguments().getString("tag");
        strIns=getArguments().getString("ins");
        strIng=getArguments().getString("ing");
        strTitle=getArguments().getString("title");

        //bundle data for updation and deletion
        strImage=getArguments().getString("img");
        etTitle.setText(strTitle);
        etTime.setText(strTime);
        etInstructions.setText(strIns);
        etIngredients.setText(strIng);
        Glide.with(getActivity()).load(strImage).into(imageView);
        destination=new File(strImage);

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
            progressBar.setVisibility(View.VISIBLE);
            setProgressValue(progressbarstatus);
            UpdateReicpeFragment.UploadFileToServer uploadFileToServer=new UpdateReicpeFragment.UploadFileToServer();
            uploadFileToServer.execute();
        }

    }

    private void callCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }
    private void callGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),REQUEST_TAKE_PHOTO);
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
        destination = new File(Environment.getExternalStorageDirectory(),
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
    private void onSelectFromGalleryResult(Intent data)
    {

        uri=data.getData();
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                String picpath=getPath(uri);
                destination=new File(picpath);

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), e.getCause().toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(projection[0]);
        String filePath = cursor.getString(columnIndex);
        imageView.setImageBitmap(BitmapFactory.decodeFile(filePath));
        return cursor.getString(column_index);
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

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {

        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {

            String responseString;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPatch httppost = new HttpPatch(Links.User_Url +"recipes?api_token="+apiToken);
            try {
                HTTPMultiPartEntity entity = new HTTPMultiPartEntity(
                        new HTTPMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) 100) * 100));
                            }
                        });
                // Adding file data to http body
                // Extra parameters if you want to pass to server
                entity.addPart("image", new FileBody(destination));
                Looper.prepare();
                entity.addPart("title", new StringBody(etTitle.getText().toString()));
                entity.addPart("instructions", new StringBody(strInstructions));
                entity.addPart("ingredients", new StringBody(strIngredients));
                entity.addPart("time_to_cook", new StringBody(etTime.getText().toString()));
                entity.addPart("tags", new StringBody(Recipe_Category));
                entity.addPart("api_token", new StringBody(apiToken));
//                     pDialog.dismiss();
                Bundle args = new Bundle();

                httppost.setEntity(entity);
                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                int statusCode = response.getStatusLine().getStatusCode();
                responseString = EntityUtils.toString(r_entity);
                if (statusCode==201) {
                    Fragment fragment=new Recipe();
                    getFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
                progressBar.setVisibility(View.INVISIBLE);
                Log.d("zmaClient",e.getCause().toString());
//                new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
//                        .setTitleText("Oops...")
//                        .setContentText("Something went wrong!")
//                        .show();
            } catch (IOException e) {
                responseString = e.toString();
                progressBar.setVisibility(View.INVISIBLE);
                Log.d("zmaIo", e.getCause().toString());
//                new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
//                        .setTitleText("Oops...")
//                        .setContentText("Something went wrong!")
//                        .show();
            }

            Log.d("zma return string", responseString);
            return responseString;

        }
    }

}
