package com.example.cenecapp

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import clases.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage


class AjustesEliminarCuenta : AppCompatActivity() {
    val auth = Firebase.auth
    val db = FirebaseFirestore.getInstance()
    val storage = Firebase.storage
    val storageRef = storage.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajustes_eliminar_cuenta)



        var campoEmail:EditText = findViewById(R.id.emailEliminar)
        var campoPass:EditText = findViewById(R.id.passwordEliminar)
        var btnEliminar:Button = findViewById(R.id.btnEliminarCuenta)

        var email = campoEmail.text.toString()
        var password = campoPass.text.toString()


        btnEliminar.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }
    fun deleteAuthUser(user: FirebaseUser) {
        user.delete()
            .addOnSuccessListener {
               Toast.makeText(this,"Auth eliminado",Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this,"Auth no eliminado",Toast.LENGTH_LONG).show()
            }
    }

    fun deleteFirestoreDocument(user: FirebaseUser) {
        var usuario:Usuario = base_fragments.Companion.usuarioEnviado
        val email = user.email
        if (email != null) {
            // Remove the user document from the "users" collection
            val documentRef = db.collection("Usuarios").document(user.email!!)
            documentRef.delete().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User document deleted successfully")
                } else {
                    Log.e(TAG, "Error deleting user document", task.exception)
                }
            }

            // Remove the email of the deleted user from any documents that contain it
            val collection = db.collection("Usuarios")
            collection.whereArrayContains("amigos", usuario.email!!)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val documentRef = collection.document(document.id)
                        documentRef.update("amigos", FieldValue.arrayRemove(usuario.email))
                        Log.d(TAG, "Email $email removed from document ${document.id}")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error getting documents", exception)
                }

            collection.whereArrayContains("usuariosBloqueados", usuario.email!!)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val documentRef = collection.document(document.id)
                        documentRef.update("usuariosBloqueados", FieldValue.arrayRemove(usuario.email))
                        Log.d(TAG, "Email $email removed from document ${document.id}")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error getting documents", exception)
                }

            collection.whereArrayContains("usuariosDeseados", usuario.email!!)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val documentRef = collection.document(document.id)
                        documentRef.update("usuariosDeseados", FieldValue.arrayRemove(usuario.email))
                        Log.d(TAG, "Email $email removed from document ${document.id}")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error getting documents", exception)
                }

            collection.whereArrayContains("usuariosQueQuierenConectar", usuario.email!!)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val documentRef = collection.document(document.id)
                        documentRef.update("usuariosQueQuierenConectar", FieldValue.arrayRemove(usuario.email))
                        Log.d(TAG, "Email $email removed from document ${document.id}")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error getting documents", exception)
                }

            collection.whereArrayContains("usuariosRechazados", usuario.email!!)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val documentRef = collection.document(document.id)
                        documentRef.update("usuariosRechazados", FieldValue.arrayRemove(usuario.email))
                        Log.d(TAG, "Email $email removed from document ${document.id}")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error getting documents", exception)
                }
        }
    }
    fun deleteStorageDocument(user: FirebaseUser) {
        val email = user.email ?: return
        val imageRef = storageRef.child("User/${email}")
        imageRef.delete()
            .addOnSuccessListener {
                Toast.makeText(this,"Foto eliminado",Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this,"Foto no eliminado",Toast.LENGTH_LONG).show()
            }
    }

    fun showDeleteConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminar cuenta")
            .setMessage("¿Seguro que quieres eliminar tu cuenta?")
            .setPositiveButton("Sí") { _, _ ->
                val user = auth.currentUser
                if (user != null) {
                    deleteAuthUser(user)
                    deleteFirestoreDocument(user)
                    deleteStorageDocument(user)
                }
                val i:Intent= Intent(this,Log_Reg_Activity::class.java)
                startActivity(i)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

}