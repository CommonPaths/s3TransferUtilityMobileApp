/*
 * Copyright 2015-2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.amazonaws.demo.s3transferutility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

/*
 * This is the beginning screen that lets the user select if they want to upload or download
 */
public class MainActivity extends Activity {

    String fieldRID;
    private Button btnDownload;
    private Button btnUpload;
    private Button btnC;
    private EditText editTextNumberFieldRID;
    static final int REQUEST_TAKE_PHOTO = 1;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    private void initUI() {
        btnDownload = findViewById(R.id.buttonDownloadMain);
        btnUpload = findViewById(R.id.buttonUploadMain);
        btnC = findViewById(R.id.buttonCamera);


        btnDownload.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, DownloadActivity.class)));
        btnUpload.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, UploadActivity.class)));
        editTextNumberFieldRID = findViewById(R.id.editTextNumberFieldRID);

        btnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fieldRID = editTextNumberFieldRID.getText().toString();
                if (fieldRID.matches("")) {
                    showToast("Please Enter Field Review ID !");
                    return;
                }

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;

                    try {
                        photoFile = createImageFile(fieldRID);
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(context,
                                getApplicationContext().getPackageName() + ".provider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    }
                }


            }
        });

    }

    @NonNull
    private File createImageFile(String fieldRIDTag) throws IOException {
        // Create an image file name
        String currentPhotoPath;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
        String imageFileName = "FSMA_"  + fieldRIDTag + "_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void showToast(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
    }
}
