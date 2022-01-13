package com.uniovi.mytasks;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private int idGoogleResult = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setup();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EditText email = (EditText) findViewById(R.id.editTextTextEmailAddress2);
        EditText password = (EditText) findViewById(R.id.editTextTextPassword2);

        email.setText("");
        password.setText("");
    }

    private void setup() {
        Button btnRegistro = (Button) findViewById(R.id.btnRegistro2);
        Button btnLogin = (Button) findViewById(R.id.btnLogin2);
        Button btnGoogle = (Button) findViewById(R.id.loginGoogle);
        EditText email = (EditText) findViewById(R.id.editTextTextEmailAddress2);
        EditText password = (EditText) findViewById(R.id.editTextTextPassword2);

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().length() != 0 && password.getText().length() != 0){
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Intent mainIntent = new Intent(LoginActivity.this, NavActivity.class);
                                startActivity(mainIntent);
                            } else {
                                Toast.makeText(LoginActivity.this, "Error en el registro", Toast.LENGTH_SHORT);
                            }
                        }
                    });
                }
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().length() != 0 && password.getText().length() != 0){
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Intent mainIntent = new Intent(LoginActivity.this, NavActivity.class);
                                startActivity(mainIntent);
                            } else {
                                Toast.makeText(LoginActivity.this, "Error en el inicio de sesion", Toast.LENGTH_SHORT);
                            }
                        }
                    });
                }
            }
        });
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken("454078945586-0cqkqgg55qd051rkolo4h83uo4f7fd2a.apps.googleusercontent.com")
                        .requestEmail()
                        .build();
                GoogleSignInClient googleClient = GoogleSignIn.getClient(LoginActivity.this, gso);

                googleClient.signOut();

                startActivityForResult(googleClient.getSignInIntent(), idGoogleResult);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == idGoogleResult){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInAccount account = task.getResult();

            if (account != null){
                AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                FirebaseAuth.getInstance().signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent mainIntent = new Intent(LoginActivity.this, NavActivity.class);
                            startActivity(mainIntent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Error en el inicio de sesion con Google", Toast.LENGTH_SHORT);
                        }
                    }
                });
            }
        }
    }
}