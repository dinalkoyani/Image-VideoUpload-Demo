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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener
{
    EditText edtFirstname,edtLastname,edtEmail,edtPassword;
    TextView txtSignUp,txtAlreadyAcc;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference dbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initViews();
        mAuth = FirebaseAuth.getInstance();
         database = FirebaseDatabase.getInstance();
         dbRef = database.getReference("User");
        getSupportActionBar().setTitle("Sign Up");
    }

    private void initViews(){
        edtFirstname = findViewById(R.id.edtFirstname);
        edtLastname = findViewById(R.id.edtLastname);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        txtSignUp = findViewById(R.id.txtSignup);
        txtAlreadyAcc = findViewById(R.id.txtAlreadyAcc);

        txtSignUp.setOnClickListener(this);
        txtAlreadyAcc.setOnClickListener(this);
    }

    private void createSignUp(String email,String password){
        if(CheckInternetConnection.getInstance(this).isOnline()) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                dbRef.child(user.getUid()).setValue(updateUserData());
                                Toast.makeText(SignupActivity.this, "Sign Up successfully.",
                                        Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                finish();
                            } else {
                                Toast.makeText(SignupActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else {
            Toast.makeText(this,"No Internet!",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txtSignup:
                if(TextUtils.isEmpty(edtFirstname.getText().toString())){
                   Toast.makeText(SignupActivity.this,"First name not be empty",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(edtLastname.getText().toString())){
                    Toast.makeText(SignupActivity.this,"Last name not be empty",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(edtEmail.getText().toString())){
                    Toast.makeText(SignupActivity.this,"Email not be empty",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(edtPassword.getText().toString())){
                    Toast.makeText(SignupActivity.this,"password not be empty",Toast.LENGTH_SHORT).show();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString()).matches()){
                    Toast.makeText(SignupActivity.this,"Email address is invalid",Toast.LENGTH_SHORT).show();
                }else {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    createSignUp(edtEmail.getText().toString(), edtPassword.getText().toString());
                }
                break;
            case R.id.txtAlreadyAcc:
                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                finish();
                break;
        }
    }

    private UserClass updateUserData(){
        UserClass userClass = new UserClass();
        userClass.firstName = edtFirstname.getText().toString();
        userClass.lastName = edtLastname.getText().toString();
        userClass.email = edtEmail.getText().toString();
        userClass.password = edtPassword.getText().toString();

        return userClass;
    }
}