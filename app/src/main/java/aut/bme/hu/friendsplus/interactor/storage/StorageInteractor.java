package aut.bme.hu.friendsplus.interactor.storage;


import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import aut.bme.hu.friendsplus.ui.listeners.StorageListener;

public class StorageInteractor {
    private static final String TAG = "StorageInteractor";
    private StorageReference mStorageRef;

    private StorageListener listener;

    public StorageInteractor() {
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    public void setStorageListener(StorageListener storageListener) {
        listener = storageListener;
    }

    public void saveImage(String uid, Uri profileImageUri) {
        StorageReference riversRef = mStorageRef.child("images/" + uid + ".jpg");
        UploadTask uploadTask = riversRef.putFile(profileImageUri);

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return mStorageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    listener.onImageSaved(downloadUri.toString());
                }
            }
        });
    }
}
