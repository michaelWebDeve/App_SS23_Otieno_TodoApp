package com.example.todoapp.Views;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todoapp.Model.UserSingleton;
import com.example.todoapp.R;
import com.example.todoapp.Utils.LoginDbHandler;

public class SignInActivity extends AppCompatActivity {
    EditText e_mail, password;
    LoginDbHandler dbH;
    Button signIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        e_mail = findViewById(R.id.E_MailSi);
        password = findViewById(R.id.PasswordSi);
        signIn = findViewById(R.id.buttonSignIn);
        dbH = new LoginDbHandler(this);
        UserSingleton userSingleton= UserSingleton.getInstance();

        String message = getIntent().getStringExtra("EmailSecret");
        e_mail.setText(message); // email an die nächste activity übergeben
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nutzer = e_mail.getText().toString();
                String passworddb = password.getText().toString();

                if (TextUtils.isEmpty(nutzer) || TextUtils.isEmpty(passworddb)) {
                    Toast.makeText(SignInActivity.this, "Alle Felder müssen ausgefüllt sein", Toast.LENGTH_SHORT).show();
                } else {
                    userSingleton.setEmail(nutzer); // E-Mail setzen
                    int user_id= dbH.getUserId(nutzer);// userId aus der user Tabelle
                    userSingleton.setId(user_id);
                    userSingleton.setPassword(passworddb);

                    Boolean checkuserdata = dbH.checkEmailPassword(userSingleton);
                    if (checkuserdata == true) {

                        Toast.makeText(SignInActivity.this, "Erfolgreich eingeloggt", Toast.LENGTH_SHORT).show();
                        Intent gotoHome = new Intent(SignInActivity.this, HomeActivity.class);
                        startActivityForResult(gotoHome, 1);
                    } else {
                        Toast.makeText(SignInActivity.this, "Anmeldung fehlgeschlagen", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }); }

}