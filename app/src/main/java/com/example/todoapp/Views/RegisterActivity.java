package com.example.todoapp.Views;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todoapp.Model.UserSingleton;
import com.example.todoapp.R;
import com.example.todoapp.Utils.LoginDbHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Name der Activities in der App ändern
public class RegisterActivity extends AppCompatActivity {
    EditText email, password, confirmPass;
    Button register;
    TextView alreadyReg;

    LoginDbHandler dbh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        confirmPass = findViewById(R.id.ConfirmPassw);
        register = findViewById(R.id.register);
        alreadyReg = findViewById(R.id.alreadyReg);
        dbh = new LoginDbHandler(this);
        UserSingleton userSingleton = UserSingleton.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nutzer = email.getText().toString();
                String passworddb = password.getText().toString();
                String confirmPassW = confirmPass.getText().toString();

                if (TextUtils.isEmpty(nutzer) || TextUtils.isEmpty(passworddb) || TextUtils.isEmpty(confirmPassW)) {
                    Toast.makeText(RegisterActivity.this, "Alle Felder müssen ausgefüllt sein", Toast.LENGTH_SHORT).show();
                } else {
                    if (!emailValidator(nutzer)) {
                        Toast.makeText(RegisterActivity.this, "Ungültige E-Mail-Adresse", Toast.LENGTH_SHORT).show();
                    } else {
                        if (passwordValidator(passworddb)) {
                            if (passworddb.equals(confirmPassW)) {
                                userSingleton.setEmail(nutzer); // E-Mail setzen
                                userSingleton.setPassword(passworddb); // Passwort setzen

                                Boolean checkuser = dbh.checkEmail(userSingleton);
                                if (checkuser == false) { // falls user noch nicht in der user Tabelle ist
                                    Boolean insert = dbh.insertUser(userSingleton);

                                    if (insert == true) {
                                        Toast.makeText(RegisterActivity.this, "Erfolgreich registriert", Toast.LENGTH_SHORT).show();
                                        Intent gotoSignIn = new Intent(RegisterActivity.this, SignInActivity.class);
                                        gotoSignIn.putExtra("EmailSecret", nutzer);
                                        startActivityForResult(gotoSignIn, 2);
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Anmeldung fehlgeschlagen", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Nutzer existiert bereits", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(RegisterActivity.this, "Die Passwörter stimmen nicht überein", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "Passwort muss mindestens 8 Zeichen lang sein und ein Sonderzeichen enthalten", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });

        alreadyReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoSignIn = new Intent(RegisterActivity.this, SignInActivity.class);
                startActivityForResult(gotoSignIn, 2);
            }
        });
    }

    public static boolean emailValidator(String nutzer) {

        String emailMuster = "^[a-zA-Z0-9._%+-]+@[a-zA-Z]+\\.[a-zA-Z]{2,}$"; // allg. Muster für Email, 2= mind. zwei Buchstabenmam
        Pattern muster = Pattern.compile(emailMuster); // kompiliert ein Obekt aus dem Muster
        Matcher matcher = muster.matcher(nutzer);// Muster auf Nutzereingabe anwenden

        return matcher.matches();// überprüft ob eingabe dem Muster entspricht
    }

    public static boolean passwordValidator(String passwort) {
        // mindestens 8 Zeichen lang ist
        if (passwort.length() < 8) {
            return false;
        }
        // mindestens ein Sonderzeichen
        if (!passwort.matches(".*[@#$%^&+=].*")) {
            return false;
        }
        // Das Passwort erfüllt beide Bedingungen
        return true;
    }
}