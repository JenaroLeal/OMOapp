package com.example.cenecapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView

class UsuariosBloqueados_ViewHolder(view: View):RecyclerView.ViewHolder(view){

    val nombreUsuarioBloqueado:TextView by lazy { view.findViewById(R.id.nombreUsuarioBloqueado) }
    val fotoUsuarioBloqueado:CircleImageView by lazy { view.findViewById(R.id.imgUsuarioBloqueado) }
    val verPerfilBloqueado:LinearLayout by lazy { view.findViewById(R.id.verPerfilUsuarioBloqueado) }
    val ajustes:ImageButton by lazy { view.findViewById(R.id.ajustesUsuarioBloqueado) }

}