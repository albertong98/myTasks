package com.uniovi.mytasks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        setup();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EditText email = (EditText) findViewById(R.id.editTextTextEmailAddress);
        EditText password = (EditText) findViewById(R.id.editTextTextPassword);

        email.setText("");
        password.setText("");
    }

    private void setup() {
        Button btnRegistro = (Button) findViewById(R.id.btnRegistro);
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        EditText email = (EditText) findViewById(R.id.editTextTextEmailAddress);
        EditText password = (EditText) findViewById(R.id.editTextTextPassword);

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().length() != 0 && password.getText().length() != 0){
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
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
                                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(mainIntent);
                            } else {
                                Toast.makeText(LoginActivity.this, "Error en el inicio de sesion", Toast.LENGTH_SHORT);
                            }
                        }
                    });
                }
            }
        });
    }
}