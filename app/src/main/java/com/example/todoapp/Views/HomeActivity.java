package com.example.todoapp.Views;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todoapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class HomeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Todo1Fragment todo1 = new Todo1Fragment();
    Todo2Fragment todo2 = new Todo2Fragment();
    Todo3Fragment todo3 = new Todo3Fragment();
    Todo4Fragment todo4 = new Todo4Fragment();
// Fragments initialisieren

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, todo1).commit();
//activity_home.xml FrameLayout="container" dieses Layout soll bei Auswahl eines Fragments ersetzt werden
// . Als default todo1 festgelegt somit wird beim starten immer
// todo1 geöffnet --> .replace(..., todo1) Framelayout wird also mit todo1 ersetzt


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //überprüft welches item/Fragment ausgewählt wurd
                switch (item.getItemId()) {

                    case R.id.Todo1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, todo1).commit();
                        return true;

                    case R.id.Todo2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, todo2).commit();
                        // wenn also auf todo2 geklickt wird soll das FrameLayout('container') durch todo2Fragment ersetzt werden
                        return true;

                    case R.id.Todo3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, todo3).commit();
                        return true;

                    case R.id.Todo4:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, todo4).commit();
                        return true;

                    default:
                        Toast.makeText(HomeActivity.this, "Kein Case trifft zu", Toast.LENGTH_SHORT).show();

                }

                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Cant go back", Toast.LENGTH_SHORT).show();
// deaktiviert, weil die app immer neu gestarted werden muss, damit eine neue
// Instanz des UserSingleton erzeugt werden kann

    }
}