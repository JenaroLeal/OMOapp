package com.example.cenecapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import clases.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AAblanco : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aablanco)
        var txt1:TextView=findViewById(R.id.txt1)
        var txt2:TextView=findViewById(R.id.txt2)
        var txt3:TextView=findViewById(R.id.txt3)

        var yo: Usuario =base_fragments.Companion.usuarioEnviado
        var db= FirebaseFirestore.getInstance()
        val miEmail = FirebaseAuth.getInstance().currentUser?.email
        val miRef=db.collection("Usuarios").document(miEmail!!)
        val bundle: Bundle? = this.intent.extras
        var usuario: Usuario? = null
        usuario = bundle!!.getParcelable<Usuario>("usuario")

        txt1.text=yo.nombre+ usuario!!.nombre
    }
}