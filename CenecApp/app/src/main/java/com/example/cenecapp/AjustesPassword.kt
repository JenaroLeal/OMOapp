package com.example.cenecapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AjustesPassword : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth
    private lateinit var db:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajustes_password)

        var actual:EditText= findViewById(R.id.passwordActual)
        var nueva:EditText = findViewById(R.id.nuevaPassword)
        var confirmada:EditText = findViewById(R.id.confirmarPassword)
        var guardar:Button = findViewById(R.id.guardarCambiosPassword)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        guardar.setOnClickListener(){
            var passwordAnterior = actual.text.toString()
            var new = nueva.text.toString()
            var confirm = confirmada.text.toString()
            val user = auth.currentUser
            if(user!=null){
                val datos = EmailAuthProvider.getCredential(user.email!!,passwordAnterior)
                user.reauthenticate(datos).addOnSuccessListener {
                    if(new.equals(confirm)){
                        user.updatePassword(confirm)
                        db.collection("Usuarios").document(user.email!!).update("password",confirm)
                        Toast.makeText(this,"Contraseña actualizada",Toast.LENGTH_LONG).show()
                    }
                    else{
                       confirmada.error = "Las contraseñas no coinciden"
                    }
                }
                    .addOnFailureListener(){
                        Toast.makeText(this,"Error al actualizar",Toast.LENGTH_LONG).show()
                    }
            }

        }


    }
}