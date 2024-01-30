package com.example.cenecapp

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import clases.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class AjustesAmigos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajustes_amigos)

        val bundle = intent.extras
        val usuario: Usuario? = bundle?.getParcelable("usuario")

        val denunciar: TextView = findViewById(R.id.denunciarAmigo)
        val bloquear: TextView = findViewById(R.id.bloquearAmigo)
        val infoAmigo: TextView = findViewById(R.id.infoAmigo)
        val eliminar: TextView = findViewById(R.id.eliminarAmigo)

        var db = FirebaseFirestore.getInstance()
        val miEmail = FirebaseAuth.getInstance().currentUser?.email
        val suEmail = usuario?.email
        val miRef = db.collection("Usuarios").document(miEmail!!)
        val referencia = db.collection("Usuarios").document(suEmail.toString())

        var funciones = Funciones()


        denunciar.setOnClickListener() {

        }

        bloquear.setOnClickListener() {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("¿Bloquear Amigo?")
            builder.setPositiveButton("Sí") { _, _ ->
                // Perform action when "Yes" button is clicked

                referencia.update("amigos", FieldValue.arrayRemove(miEmail)).addOnSuccessListener {
                    funciones.updateBBDD(miEmail)

                }.addOnFailureListener {

                }


                miRef.update("amigos", FieldValue.arrayRemove(suEmail)).addOnSuccessListener {
                    funciones.updateBBDD(miEmail)

                }.addOnFailureListener {

                }

                miRef.update("usuariosBloqueados", FieldValue.arrayUnion(suEmail))
                    .addOnSuccessListener {
                        funciones.updateBBDD(miEmail)

                    }.addOnFailureListener {

                }



            }
            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()

        }

        infoAmigo.setOnClickListener() {

        }

        eliminar.setOnClickListener() {
            val builder = androidx.appcompat.app.AlertDialog.Builder(this)
            builder.setTitle("¿Eliminar amigo de forma permanente?")
            builder.setPositiveButton("Sí") { _, _ ->

                miRef.update("usuariosRechazados", FieldValue.arrayUnion(suEmail))
                    .addOnSuccessListener {
                        funciones.updateBBDD(miEmail)

                    }.addOnFailureListener {

                    }
                referencia.update("usuariosRechazados", FieldValue.arrayUnion(miEmail))
                    .addOnSuccessListener {
                        funciones.updateBBDD(miEmail)

                    }.addOnFailureListener {

                    }

            }
            builder.setNegativeButton("No") { dialog, _ ->
                // Perform action when "No" button is clicked
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()

        }
    }

    override fun onBackPressed() {
        var funciones = Funciones()
        val miEmail = FirebaseAuth.getInstance().currentUser?.email
        funciones.updateBBDD(miEmail!!)
        super.onBackPressed()
    }
}