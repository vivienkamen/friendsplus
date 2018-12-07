package aut.bme.hu.friendsplus.ui.account;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.IOException;

import aut.bme.hu.friendsplus.BuildConfig;
import aut.bme.hu.friendsplus.R;
import aut.bme.hu.friendsplus.model.User;
import aut.bme.hu.friendsplus.ui.BaseActivity;
import aut.bme.hu.friendsplus.ui.authpicker.AuthPickerActivity;
import aut.bme.hu.friendsplus.util.NavigationDrawer;
import aut.bme.hu.friendsplus.util.PermissionChecker;

public class AccountActivity extends BaseActivity implements AccountScreen {

    private static final String TAG = "AccountActivity";
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final int CHOOSE_IMAGE = 101;

    AccountPresenter presenter;
    private User updatedUser;
    private User currentUser;
    private Uri profileImageUri;
    private boolean edited;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private NavigationDrawer navigationDrawer;

    private ImageView imageView;
    private Button changePhotoButton;
    private EditText nameEditText;
    private EditText emailEditText;
    private Button saveButton;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        setTitle("Account");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        presenter = new AccountPresenter();

        edited = false;
        updatedUser = new User();
        currentUser = new User();

        presenter.setCurrentUser();

        initDrawerLayout();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.attachScreen(this);
    }

    @Override
    protected void onStop() {
        presenter.detachScreen();
        super.onStop();
    }

    private void initDrawerLayout() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationDrawer = new NavigationDrawer(drawerLayout, navigationView, AccountActivity.this);
        navigationDrawer.initNavigationDrawer();
        navigationView.getMenu().getItem(0).setChecked(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initUI() {
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        imageView = (ImageView) findViewById(R.id.profilePicture);
        changePhotoButton = (Button) findViewById(R.id.changePictureButton);
        nameEditText = (EditText) findViewById(R.id.editTextName);
        emailEditText = (EditText) findViewById(R.id.editTextEmail);
        saveButton = (Button) findViewById(R.id.saveButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);

        changePhotoButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!PermissionChecker.checkStoragePermission(getBaseContext())) {
                            PermissionChecker.requestStoragePermission(AccountActivity.this);
                        }
                        showImageChooser();
                    }
                }
        );

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edited = true;
                updateButtonState();
            }
        };

        nameEditText.addTextChangedListener(textWatcher);
        emailEditText.addTextChangedListener(textWatcher);

        saveButton.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if(validateForm()) {
                            presenter.saveUserInformation();
                            edited = false;
                            updateUI();

                        }
                    }
                }
        );

        cancelButton.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        updatedUser = new User();
                        edited = false;
                        updateUI();
                    }
                }
        );
    }

    @Override
    public void updateUI() {
        if(currentUser.username != null) {
            nameEditText.setText(currentUser.username);
        }
        if(currentUser.email != null) {
            emailEditText.setText(currentUser.email);
        }


        if(currentUser.imageUri != null || updatedUser.imageUri != null) {
            Glide.with(this).load(currentUser.imageUri).into(imageView);
        } else {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.account_placeholder));
        }
        updateButtonState();

    }

    private void updateButtonState() {
        if(edited) {
            saveButton.setEnabled(true);
        } else {
            saveButton.setEnabled(false);
        }
    }

    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), CHOOSE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            profileImageUri = data.getData();
            edited = true;
            updateButtonState();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), profileImageUri);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void setImageUri(String uri) {
        updatedUser.imageUri = uri;
    }

    @Override
    public Uri getProfileImageUri() {
        return profileImageUri;
    }

    @Override
    public void setUpdatedUser() {
        updatedUser.uid = currentUser.uid;
        updatedUser.username = nameEditText.getText().toString();
        updatedUser.email = emailEditText.getText().toString();

        if(currentUser.imageUri != null && profileImageUri == null) {
            updatedUser.imageUri = currentUser.imageUri;
        }

    }

    @Override
    public User getUpdatedUser() {
        return updatedUser;
    }

    @Override
    public void setCurrentUser(User user) {
        currentUser = user;
    }

    @Override
    public void updateCurrentUser() {
        currentUser = updatedUser;
    }

    @Override
    public void resetUpdatedUser() {
        updatedUser = new User();
    }

    @Override
    public void showSuccessfulUpdate() {
        showToast( "Account is updated.");

    }

    @Override
    public void signOut() {
        presenter.signOut();
        Intent intent = new Intent(AccountActivity.this, AuthPickerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {

                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {

                showSnackbar(getString(R.string.permission_denied_explanation),
                        getString(R.string.settings), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = emailEditText.getText().toString();
        String name = nameEditText.getText().toString();


        if(TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Enter a valid e-mail address");
            emailEditText.requestFocus();
            valid = false;
        }


        if(TextUtils.isEmpty(name)) {
            nameEditText.setError("Required");
            nameEditText.requestFocus();
            valid = false;
        }


        return valid;
    }

}
