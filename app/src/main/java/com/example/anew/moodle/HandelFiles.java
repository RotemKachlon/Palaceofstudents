package com.example.anew.moodle;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

//קוד אשר נקרא כאשר צריך להוסיף/להציג קובץ במשימה באפליקציה, הקוד מטפל בבקשה בעזרת הכנסת/שליפת הקובץ בפיירבייס סטורג'
public class HandelFiles {
    public static void saveFileToStorage(Uri fileeUri, String fileNameForStorage){
        final StorageReference fileRef;//מצביע על מקום האיחסון בפיירבייס

        StorageReference mStorageRef;
        //יצירת אובייקט שיצביע על המקום באחסון שבו ישמר המסמך
        mStorageRef = FirebaseStorage.getInstance().getReference();
        fileRef = mStorageRef.child("files/" + fileNameForStorage);
        fileRef.putFile(fileeUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String fileURL = uri.toString();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }
    //הצגת הקובץ הנבחר על המסך
    public static Intent getFile(String uri, String fid, final Context context){
        StorageReference mStorageRef;
        File temp = null;
        final String[] fileType= new String[1];
        Intent promptInstall;
        final Uri[] uriReturn = new Uri[1];
        Log.d("getFile", "1");
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("בטעינה..."); // Setting Message
        progressDialog.setTitle("נא להמתין לטעינת הקובץ"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        //יצירת אובייקט שיצביע על הקובץ הנבחר באחסון
        StorageReference fileRef = mStorageRef.child("files/" + fid);
        try
        {
            temp = File.createTempFile("tmp", "." + Uri.parse(uri).getLastPathSegment());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        fileType[0] = "application/pdf";
        //שליפה של סוג הקובץ כדי שנדע איזה אפליקציה תציג אותו
        fileRef.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {

                // Metadata now contains the metadata for 'images/forest.jpg'
                fileType[0] = storageMetadata.getContentType();
                Log.d("getFile","Meta"+fileType[0]);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });
        //הצגת הקובץ באמצעות אינטנט שמקבל את הקובץ עצמו ואת סוג הקובץ
        final File finalTemp = temp;
        fileRef.getFile(temp).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                uriReturn[0] = FileProvider.getUriForFile(context, "com.example.palaceofstudents.fileprovider", finalTemp);
                Log.d("getFile",uriReturn[0].getPath());
                Intent promptInstall = new Intent(Intent.ACTION_VIEW).setDataAndType(uriReturn[0], fileType[0]);
                promptInstall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                promptInstall.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(promptInstall);
                Log.d("getFile","55");
            }
        });

        //יצירת דיאלוג טעינה עד שניתן להציג את הקובץ על המסך
        new Thread(new Runnable() {
            int timer=0;
            public void run() {
                try {
                    while(uriReturn[0]==null) {
                        Log.d("getFile","timer: " + timer);
                        timer+=100;
                        Thread.sleep(100);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }).start();
        Log.d("getFile","end"+fileType[0]);
        promptInstall = new Intent(Intent.ACTION_VIEW).setDataAndType(uriReturn[0], fileType[0]);
//        promptInstall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        promptInstall.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        context.startActivity(promptInstall);
        return promptInstall;
    }
}
