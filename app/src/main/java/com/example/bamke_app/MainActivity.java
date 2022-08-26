package com.example.bamke_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Agregar las animaciones
        Animation animacion1 = AnimationUtils.loadAnimation(this,R.anim.desplazamiento_arriba);
        Animation animacion2 = AnimationUtils.loadAnimation(this,R.anim.desplazamiento_abajo);

        //Referenciamos los objetos a animar
        TextView tvAutores = findViewById(R.id.tvAutores);
        ImageView imlogo = findViewById(R.id.imlogo);

        tvAutores.setAnimation(animacion1);
        imlogo.setAnimation(animacion2);

        //Creamos el SplashScreen en el inicio:
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                /*Este paso es para omitir el LOGIN de un usuario que ya haya hecho el proceso del
                REGISTRO en la App*/

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(MainActivity.this);
                if(user!=null || account!= null){
                    Intent intent = new Intent(MainActivity.this,UserActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    /* Este paso es para la creacion del SplashScreen que da inicio a la App*/
                    Intent intent= new Intent(MainActivity.this, MainActivity2.class);
                    Pair[] pairs = new Pair[2];
                    pairs[0] = new Pair<View,String>(imlogo,"logoImageTrans");
                    pairs[1] = new Pair<View,String>(tvAutores,"textTrans");
                    if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
                        startActivity(intent,options.toBundle());
                    }else {
                        startActivity(intent);
                        finish();
                    }
                }
                }


        },3000);

    }

}