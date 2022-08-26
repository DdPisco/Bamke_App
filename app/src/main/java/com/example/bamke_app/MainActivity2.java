package com.example.bamke_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.regex.Pattern;

public class MainActivity2 extends AppCompatActivity {
    /*Se declaran todas las variables que se usaran en este Layout*/
    TextView bienvenidolabel, continuarlabel, nuevoUsuario;
    ImageView loginImageView;
    TextInputLayout usuarioTextField, contrasenaTextField;
    MaterialButton inicioSesion;
    TextInputEditText emailEditText, passwordEditText;
    private FirebaseAuth mAuth;

    //
    SignInButton signInButton;
    GoogleSignInClient mGoogleSignInClient;

    public static final int RC_SIGN_IN = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        /*Se instancian todas las variables a usarse dentro de este Layout*/
        loginImageView = findViewById(R.id.loginImageView);
        bienvenidolabel = findViewById(R.id.bienvenidoLabel);
        continuarlabel = findViewById(R.id.continuarLabel);
        nuevoUsuario = findViewById(R.id.nuevoUsuario);
        usuarioTextField = findViewById(R.id.usuarioTextField);
        contrasenaTextField = findViewById(R.id.contraseñaTextField);
        inicioSesion = findViewById(R.id.inicioSesion);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        mAuth = FirebaseAuth.getInstance();


        /*Este metodo se da uso cuando un nuevo usuario desea registrar sus datos en la App*/
        nuevoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Con este metodo nos proyectamos a otro Layout en donde el nuevo usuario podra
                ingresar sus datos.
                 */
                Intent intent = new Intent(MainActivity2.this, SignUpActivity.class);
                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View, String>(loginImageView, "logoImageTrans");
                pairs[1] = new Pair<View, String>(bienvenidolabel, "textTrans");
                pairs[2] = new Pair<View, String>(continuarlabel, "iniciaSesionTextTrans");
                pairs[3] = new Pair<View, String>(nuevoUsuario, "newUserTrans");
                pairs[4] = new Pair<View, String>(usuarioTextField, "emailInputTextTrans");
                pairs[5] = new Pair<View, String>(contrasenaTextField, "passwordInputTextTrans");
                pairs[6] = new Pair<View, String>(inicioSesion, "buttonSignInTrans");

                /*Aqui se da una verificacion de la version del android que nuestro usuario emplee
                para asi poder hacerlo accesible para el o ella.
                 */
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity2.this, pairs);
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                    finish();
                }
            }
        });

        /*
        Este metodo es unicamente cuando ya se encuentran las credenciales del usuario luego de
        que se haya registrado.
         */
        inicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validar();
            }
        });
        signInButton = findViewById(R.id.loginGoogle);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogle();
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();

    }

    private void signInWithGoogle (){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode ==RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(MainActivity2.this,"Fallo Google", Toast.LENGTH_LONG).show();
            }
        }
    }


    public void firebaseAuthWithGoogle(String idToken){
        AuthCredential credencial = GoogleAuthProvider.getCredential(idToken,null);
        mAuth.signInWithCredential(credencial)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(MainActivity2.this,UserActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(MainActivity2.this, "Fallo al iniciar sesion", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    /*
    Con este metodo se validan las credenciales ingresadas en el caso de ya tener una cuenta registrada
     */
    public void validar() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Correo Invalido");
            return;
        } else {
            emailEditText.setError(null);
        }
        if (password.isEmpty() || password.length() < 8) {
            passwordEditText.setError("Se necesitan mas de 8 caracteres");
            return;
        } else if (!Pattern.compile("[0-9]").matcher(password).find()) {
            passwordEditText.setError("Al menos 1 numero");
            return;
        } else {
            passwordEditText.setError(null);
        }
        iniciarSesion(email,password);
    }

    /*
    Metodo general de ingreso a la aplicacion si ya se cuenta con un usuario registrado
     */
    public void iniciarSesion(String email, String password){
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(MainActivity2.this,UserActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(MainActivity2.this,"Credenciales incorrectas,intentalo de nuevo",Toast.LENGTH_LONG).show();
                        }
                }
        });
    }
}
