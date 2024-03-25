package com.example.cenecapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class RecuperacionPass : AppCompatActivity() {

    private lateinit var emailRec:EditText
    private lateinit var btnRec:Button
    private lateinit var auth:FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperacion_pass)

        emailRec= findViewById(R.id.emailRec)
        btnRec = findViewById(R.id.btnRec)
        auth = FirebaseAuth.getInstance()

        btnRec.setOnClickListener(){
            if (emailRec.equals("")){
                Toast.makeText(this, "Ha ocurrio un error", Toast.LENGTH_LONG).show()
            }
            else{
                auth.sendPasswordResetEmail(emailRec.text.toString())
                Toast.makeText(this, "Email enviado", Toast.LENGTH_LONG).show()
                var i:Intent = Intent(this, Inicio_Sesion_Activity::class.java)
                startActivity(i)
            }
        }
    }
}