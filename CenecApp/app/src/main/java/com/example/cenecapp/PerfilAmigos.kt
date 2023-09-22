package com.example.cenecapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import clases.Usuario
import com.bumptech.glide.Glide
import com.example.cenecapp.databinding.ActivityPerfilAmigosBinding
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView

class PerfilAmigos : AppCompatActivity() {
    lateinit var binding:ActivityPerfilAmigosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilAmigosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle:Bundle?=this.intent.extras
        var usuario:Usuario?=null

        if(bundle!=null){
            usuario=bundle.getParcelable<Usuario>("usuario")

            if(usuario!=null){

                var foto: CircleImageView =binding.fotoAmigo
                val storageRef = FirebaseStorage.getInstance().reference.child("User/"+usuario.email.toString())
                storageRef.downloadUrl.addOnSuccessListener { uri->
                    Glide.with(this).load(uri.toString()).into(foto)
                }

                binding.nombreAmigo.text=usuario.nombre
                binding.juego1Amigo.text = usuario.juegos[0]
                binding.juego2Amigo.text = usuario.juegos[1]
                binding.juego3Amigo.text = usuario.juegos[2]
                binding.juego4Amigo.text = usuario.juegos[3]
                binding.juego5Amigo.text = usuario.juegos[4]
                binding.afinidadAmigo.text=usuario.afinidad.toString()
                binding.plataformaAmigo.text=usuario.plataforma
                binding.biografiaAmigo.text=usuario.biografia
            }
        }

            binding.opcionesAmigos.setOnClickListener(){
                val i:Intent = Intent(this, AjustesAmigos::class.java)
                val bundle = Bundle()
                bundle.putParcelable("usuario",usuario)
                i.putExtras(bundle)
                startActivity(i)
}
    }
}