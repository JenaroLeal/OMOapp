package com.example.cenecapp

import Fragments.chats
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import clases.Usuario
import clases.Usuario_ViewHolder
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView

class UserChatAdapter (val actividadMadre: Activity, val datos:ArrayList<Usuario>) :RecyclerView.Adapter<UserChatAdapter.viewholder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
       return viewholder(actividadMadre.layoutInflater.inflate(R.layout.chats_recycler,parent,false))
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        var db = FirebaseFirestore.getInstance()
        val usuario:Usuario=datos.get(position)

        holder.username.text=usuario.nombre
        val foto:CircleImageView = holder.userimg
        val storageRef = FirebaseStorage.getInstance().reference.child("User/"+usuario.email.toString())
        storageRef.downloadUrl.addOnSuccessListener { uri->
            Glide.with(holder.itemView.context).load(uri).into(foto)
        }

        holder.usuarioChat.setOnClickListener(){

            val i: Intent = Intent(actividadMadre,chatwindo::class.java)
            //val bundle: Bundle = Bundle()
            //bundle.putParcelable("usuario",usuario)
            //i.putExtras(bundle)
            actividadMadre.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
      return datos.size
    }


    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var userimg: CircleImageView
        var username: TextView
        var usuarioChat:LinearLayout

        init {
            userimg = itemView.findViewById(R.id.imgAmigoChat)
            username = itemView.findViewById(R.id.nombreAmigoChat)
            usuarioChat = itemView.findViewById(R.id.chatAmigo)

        }
    }
}