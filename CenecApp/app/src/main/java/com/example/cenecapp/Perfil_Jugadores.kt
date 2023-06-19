package com.example.cenecapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import clases.Usuario
import com.bumptech.glide.Glide
import com.example.cenecapp.databinding.ActivityPerfilJugadoresBinding
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Clase que nos permite acceder al perfil de los distintos jugadores
 */
class Perfil_Jugadores : AppCompatActivity() {

    lateinit var binding:ActivityPerfilJugadoresBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding=ActivityPerfilJugadoresBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * Recogemos los datos del usuario
         */
        val bundle:Bundle?=this.intent.extras
        var usuario:Usuario?=null
        var juegos:ArrayList<String> = ArrayList<String>()

        /**
         * Comprobamos si los datos recibidos existen o no
         * En caso de que los datos sean recogidos correctamente, cargamos la informacion del usuario en los distintos campos de la clase
         * Los datos son recogidos de la base de datos
         */
        if(bundle!=null){
            usuario=bundle.getParcelable<Usuario>("usuario")

            if(usuario!=null){

                var foto:CircleImageView=binding.fotoJugador
                val storageRef = FirebaseStorage.getInstance().reference.child("User/"+usuario.email.toString())
                storageRef.downloadUrl.addOnSuccessListener { uri->
                    Glide.with(this).load(uri.toString()).into(foto)
                }



                binding.nombreJugador.text=usuario.nombre



                binding.juego1Jugador.text = usuario.juegos[0]
                binding.juego2Jugador.text = usuario.juegos[1]
                binding.juego3Jugador.text = usuario.juegos[2]
                binding.juego4Jugador.text = usuario.juegos[3]
                binding.juego5Jugador.text = usuario.juegos[4]

                binding.afinidadJugador.text=usuario.afinidad.toString()
                binding.plataformaJugador.text=usuario.plataforma
                binding.biografiaJugador.text=usuario.biografia
            }
        }


        /**
         * Listener del boton eliminar
         * Se implementará en la versión 2, disponible en junio
         */
        binding.btnDescarteJugador.setOnClickListener(){

        }
        /**
         * Listener del boton conectar
         * Se implementará en la versión 2, disponible en junio
         */
        binding.btnConectarConJugador.setOnClickListener(){

        }
    }
}