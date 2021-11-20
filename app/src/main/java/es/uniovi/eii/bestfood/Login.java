package es.uniovi.eii.bestfood;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;


    public void register(String user, String pass) {
        mAuth.createUserWithEmailAndPassword(user, pass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user1 = mAuth.getCurrentUser();
                        Intent myIntent = new Intent(Login.this, MainActivity.class);
                        Login.this.startActivity(myIntent);
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast toast = Toast.makeText(getApplicationContext(), R.string.correoya, Toast.LENGTH_SHORT);
                        toast.show();

                    }


                });
    }

    public void login(String user, String pass) {
        mAuth.signInWithEmailAndPassword(user, pass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user1 = mAuth.getCurrentUser();
                        Intent myIntent = new Intent(Login.this, MainActivity.class);
                        Login.this.startActivity(myIntent);
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast toast = Toast.makeText(getApplicationContext(), R.string.usinc, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final Button login = findViewById(R.id.login);
        final Button register = findViewById(R.id.register);
        final EditText user = findViewById(R.id.username);
        final EditText pass = findViewById(R.id.password);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        login.setOnClickListener(v -> {
            if (user.getText().length() == 0) {
                Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                user.startAnimation(shake);
                user.setError(getString(R.string.correo));
            }
            if (pass.getText().length() == 0) {
                Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                pass.startAnimation(shake);
                pass.setError(getString(R.string.passvacia));
            } else {

                login(user.getText().toString(), pass.getText().toString());
            }
        });
        register.setOnClickListener(v -> {
            if (user.getText().length() == 0) {
                Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                user.startAnimation(shake);
                user.setError(getString(R.string.correo));
            }
            if (pass.getText().length() == 0) {
                Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                pass.startAnimation(shake);
                pass.setError(getString(R.string.passvacia));
            } else {

                register(user.getText().toString(), pass.getText().toString());
            }
        });


    }

}
