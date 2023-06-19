package com.example.cenecapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cenecapp.R
import de.hdodenhof.circleimageview.CircleImageView

class Solicitud_ViewHolder(view: View):RecyclerView.ViewHolder(view){

    val nombre:TextView by lazy {view.findViewById(R.id.nombreUsuarioSolicitud)}
    val imagen:CircleImageView by lazy { view.findViewById(R.id.imgSolicitud) }
    val afinidad:TextView by lazy { view.findViewById(R.id.afinidadSolicitud) }
    val amigosComun:TextView by lazy { view.findViewById(R.id.amigosComunSolicitud) }

    val btnAceptar: ImageButton by lazy { view.findViewById(R.id.btnAceptarSolicitud) }
    val btnRechazar:ImageButton by lazy { view.findViewById(R.id.btnRechazarSolicitud) }
    val verPerfil:LinearLayout by lazy { view.findViewById(R.id.verPerfilSolicitud) }


}