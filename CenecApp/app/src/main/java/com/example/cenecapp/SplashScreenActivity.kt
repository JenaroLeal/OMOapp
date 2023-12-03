package com.example.cenecapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Clase que lanzamos al ejecutar la app. Sirve de introducción
 */

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        /**
         * Método que nos permite mostrar la clase durante el periodo de tiempo que indicamos, en este caso, de 3 segundos
         */
        Handler(Looper.getMainLooper()).postDelayed({
            val i:Intent = Intent(this, Log_Reg_Activity::class.java)
            startActivity(i)
        },3000)
    }
}
