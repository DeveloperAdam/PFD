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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.techease.pfd.Configuration.Links;
import com.techease.pfd.R;
import com.techease.pfd.Utils.HTTPMultiPartEntity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
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

public class CreateNewRecipeFragment extends Fragment {

    ImageView imageView,btnAddNewetIng,btnAddNewetIns,ivCross;
    EditText etTitle,etIngredients,etInstructions,etTime;
    Typeface typeface,typeface2;
    Spinner spinner;
    Button btnSubmitRecipe;
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
    String strIngredients,strInstructions,apiToken,strTime,strTitle,strSpinnerItem;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Bitmap bm;
    Bitmap thumbnail;
    File destination;
    Uri uri;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_create_new_recipe, container, false);

        sharedPreferences = getActivity().getSharedPreferences(Links.MyPrefs, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        apiToken=sharedPreferences.getString("api_token","");
        InstructionLayout=(LinearLayout) view.findViewById(R.id.parentLayoutInstructions);
        IngredientsLayout=(LinearLayout) view.findViewById(R.id.parentLayoutingredients);
        frameLayoutIngredients=(FrameLayout)view.findViewById(R.id.frameLayoutIngredients);
        frameLayoutInstruction=(FrameLayout)view.findViewById(R.id.frameLayoutInstructions);
        typeface=Typeface.createFromAsset(getActivity().getAssets(),"font/brandon_blk.otf");
        typeface2=Typeface.createFromAsset(getActivity().getAssets(),"font/brandon_reg.otf");
        spinner=(Spinner)view.findViewById(R.id.spinner);
        btnSubmitRecipe=(Button)view.findViewById(R.id.btnSubmitRecipe);
        imageView=(ImageView)view.findViewById(R.id.iv);
        ivCross=(ImageView)view.findViewById(R.id.ivDelete);
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

        //typeface
        etTime.setTypeface(typeface2);
        etInstructions.setTypeface(typeface2);
        etIngredients.setTypeface(typeface2);
        etTitle.setTypeface(typeface2);
        btnSubmitRecipe.setTypeface(typeface);

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

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item=parent.getItemAtPosition(position);
                if (item.equals("Fast Food"))
                {
                    strSpinnerItem="Fast Food";
                }
                else
                    if (item.equals("Traditional"))
                    {
                        strSpinnerItem="Traditional";
                    }
                    else
                        if (item.equals("Sweets"))
                        {
                            strSpinnerItem="Sweets";
                        }
                        else
                            if (item.equals("American"))
                            {
                                strSpinnerItem="American";
                            }
                            else
                                if (item.equals("Italian"))
                                {
                                    strSpinnerItem="Italian";
                                }
                                else
                                    if (item.equals("Chinese"))
                                    {
                                        strSpinnerItem="Chinese";
                                    }
                                    else
                                        if (equals("Baking"))
                                        {
                                            strSpinnerItem="Baking";
                                        }
                                        else
                                            if (item.equals("Afghani"))
                                            {
                                                strSpinnerItem="Afghani";
                                            }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

        ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageView.setImageResource(R.drawable.recipe);
                ivCross.setVisibility(View.GONE);
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
    private void callCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }
    private void callGallery() {
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

    private void onSelectFromGalleryResult(Intent data) {

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
    public void takeDataFromFields() {
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
    private void onDataInput() {
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

            CreateNewRecipeFragment.UploadFileToServer uploadFileToServer=new UploadFileToServer();
            uploadFileToServer.execute();
        }

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
                    imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_add_black_24dp));
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
    class UploadFileToServer extends AsyncTask<Void, Integer, String> {
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
            HttpPost httppost = new HttpPost(Links.User_Url +"recipes?api_token="+apiToken);
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
                entity.addPart("tags", new StringBody(strSpinnerItem));
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
                Log.d("zmaClient",e.getCause().toString());
//                new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
//                        .setTitleText("Oops...")
//                        .setContentText("Something went wrong!")
//                        .show();
            } catch (IOException e) {
                responseString = e.toString();
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
