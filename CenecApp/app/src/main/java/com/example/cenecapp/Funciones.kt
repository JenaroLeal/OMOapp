package com.example.cenecapp

import clases.Usuario
import com.google.firebase.firestore.FirebaseFirestore

class Funciones {

    fun updateBBDD(userEmail: String) {

        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("Usuarios").document(userEmail)
        val user = base_fragments.usuarioEnviado


        userRef.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                // Handle errors
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val nombre = snapshot.getString("nombre") ?: ""
                val email = snapshot.getString("email") ?: ""
                val contraseña = snapshot.getString("password") ?: ""
                val ciudad = snapshot.getString("ciudad") ?: ""
                val plataforma = snapshot.getString("plataforma") ?: ""
                val juegos = snapshot.get("juegos") as? ArrayList<String> ?: ArrayList()
                val bio = snapshot.getString("biografia") ?: ""
                val foto = snapshot.getString("foto") ?: ""
                val amigos = snapshot.get("amigos") as? ArrayList<String> ?: ArrayList()
                val usuariosDeseados = snapshot.get("usuariosDeseados") as? ArrayList<String> ?: ArrayList()
                val usuariosQueQuierenConectar = snapshot.get("usuariosQueQuierenConectar") as? ArrayList<String> ?: ArrayList()
                val usuariosRechazadosDB = snapshot.get("usuariosRechazados") as? ArrayList<String> ?: ArrayList()
                val usuariosBloqueadosDB = snapshot.get("usuariosBloqueados") as? ArrayList<String> ?: ArrayList()

                // Update the local user object with data from Firestore

                user.nombre = nombre
                user.email = email
                user.password = contraseña
                user.ciudad = ciudad
                user.plataforma = plataforma
                user.juegos = juegos
                user.imagenPerfil = foto
                user.biografia = bio
                user.amigos = amigos
                user.usuariosDeseados = usuariosDeseados
                user.usuariosQueQuierenConectar = usuariosQueQuierenConectar
                user.usuariosRechazados = usuariosRechazadosDB
                user.usuariosBloqueados = usuariosBloqueadosDB

                // Update the UI with the latest user data
                // ...

            } else {
                // The document does not exist
            }
        }
    }
}