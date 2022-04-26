package com.example.firebaseauthwithuploadimagesdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtEmail,edtPassword;
    TextView txtLogin,txtNotAcc;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login");
        mAuth = FirebaseAuth.getInstance();
        initViews();
    }

    private void initViews(){
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        txtLogin = findViewById(R.id.txtLogin);
        txtNotAcc = findViewById(R.id.txtNotAcc);

        txtLogin.setOnClickListener(this);
        txtNotAcc.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txtLogin:
                if(TextUtils.isEmpty(edtEmail.getText().toString())){
                    Toast.makeText(LoginActivity.this,"Email not be empty",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(edtPassword.getText().toString())){
                    Toast.makeText(LoginActivity.this,"password not be empty",Toast.LENGTH_SHORT).show();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString()).matches()){
                    Toast.makeText(LoginActivity.this,"Email address is invalid",Toast.LENGTH_SHORT).show();
                }else {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    loginUser(edtEmail.getText().toString(), edtPassword.getText().toString());
                }
                break;
            case R.id.txtNotAcc:
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                finish();
                break;
        }
    }

    private void loginUser(String email,String password){
        if (CheckInternetConnection.getInstance(this).isOnline()) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(LoginActivity
                                                .this, "Successfully Logged in",
                                        Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, UploadImageActivity.class).putExtra("UID", user.getUid()));
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(LoginActivity
                                                .this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else {
            Toast.makeText(this,"No Internet!",Toast.LENGTH_SHORT).show();
        }
    }
}