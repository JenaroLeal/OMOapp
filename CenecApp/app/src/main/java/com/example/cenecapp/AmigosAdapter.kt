package com.example.cenecapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import clases.Usuario
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView

class AmigosAdapter(val actividadMadre: Activity, val datos:ArrayList<Usuario>)  : RecyclerView.Adapter<Amigos_ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Amigos_ViewHolder {



        return Amigos_ViewHolder(actividadMadre.layoutInflater.inflate(R.layout.amigos_recycler,parent,false))
    }

    override fun onBindViewHolder(holder: Amigos_ViewHolder, position: Int) {
        var db= FirebaseFirestore.getInstance()
        val usuario:Usuario=datos.get(position)

        holder.nombreAmigo.text=usuario.nombre
        val fotoAmigo:CircleImageView=holder.foto
        val storageRef = FirebaseStorage.getInstance().reference.child("User/"+usuario.email.toString())
        storageRef.downloadUrl.addOnSuccessListener { uri->
            Glide.with(holder.itemView.context).load(uri).into(fotoAmigo)
        }

        holder.verAmigos.setOnClickListener(){
            val i: Intent = Intent(actividadMadre,PerfilAmigos::class.java)
            val bundle: Bundle = Bundle()
            bundle.putParcelable("usuario",usuario)
            i.putExtras(bundle)
            actividadMadre.startActivity(i)

        }


    }

    override fun getItemCount(): Int {
       return datos.size
    }



}