package com.example.cenecapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView

class Amigos_ViewHolder (view: View):RecyclerView.ViewHolder(view) {

    val foto:CircleImageView by lazy { view.findViewById(R.id.imgAmigo) }
    val nombreAmigo:TextView by lazy { view.findViewById(R.id.nombreAmigo) }
    val verAmigos:LinearLayout by lazy { view.findViewById(R.id.contenedorAmigo) }

}