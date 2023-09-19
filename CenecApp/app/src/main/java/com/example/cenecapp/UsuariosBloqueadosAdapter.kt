package com.example.cenecapp

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import clases.Usuario
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView

class UsuariosBloqueadosAdapter (val actividadMadre: Activity, val datos:ArrayList<Usuario>): RecyclerView.Adapter<UsuariosBloqueados_ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuariosBloqueados_ViewHolder {
        return UsuariosBloqueados_ViewHolder(actividadMadre.layoutInflater.inflate(R.layout.usuariosbloqueados,parent,false))
    }

    override fun onBindViewHolder(holder: UsuariosBloqueados_ViewHolder, position: Int) {

        var db = FirebaseFirestore.getInstance()
        val usuario:Usuario = datos.get(position)
        holder.nombreUsuarioBloqueado.text = usuario.nombre

        val fotoUsuariobloqueado:CircleImageView = holder.fotoUsuarioBloqueado
        val storageRef = FirebaseStorage.getInstance().reference.child("User/"+usuario.email.toString())
        storageRef.downloadUrl.addOnSuccessListener { uri->
            Glide.with(holder.itemView.context).load(uri).into(fotoUsuariobloqueado)
        }



    }

    override fun getItemCount(): Int {
        return datos.size
    }
}