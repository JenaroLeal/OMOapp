package Fragments

import com.example.cenecapp.PeticionesdeAmistad
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import clases.Usuario
import com.bumptech.glide.Glide
import com.example.cenecapp.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * Clase de tipo Fragment que nos permite acceder a nuestro perfil
 */
class perfil : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private var emailLogin:String=""

    private val db = FirebaseFirestore.getInstance()
    private lateinit var mAuth: FirebaseAuth
    private lateinit var btnAjustes:ImageButton
    private lateinit var btnNotificaciones:ImageButton

    private lateinit var storage:FirebaseStorage
    private lateinit var imagenPerfil:CircleImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    /**
     * Método que nos permite cargar el fragment y verlo
     * @param inflater: para poder cargar el fragment
     * @param container: la vista que vamos a cargar
     * @param savedInstanceState: variable de tipo Bundle para almacenar datos y no perderlos
     * @return el fragment cargado con todos los componenter
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val component:View=inflater.inflate(R.layout.fragment_perfil, container, false)

        imagenPerfil=component.findViewById(R.id.imagenPerfil)

        /**
         * Aqui accedemos a los diferentes campos donde cargaremos los datos del usuario
         */
        var nombreGamer:TextView=component.findViewById(R.id.nombreGamer)
        var juego1Gamer:TextView=component.findViewById(R.id.juego1Gamer)
        var juego2Gamer:TextView=component.findViewById(R.id.juego2Gamer)
        var juego3Gamer:TextView=component.findViewById(R.id.juego3Gamer)
        var juego4Gamer:TextView=component.findViewById(R.id.juego4Gamer)
        var juego5Gamer:TextView=component.findViewById(R.id.juego5Gamer)
        var miBiografia:TextView=component.findViewById(R.id.miBiografia)
        var plataformaGamer:TextView=component.findViewById(R.id.plataformaGamer)




        /**
         * Aqui recogemos tanto el email como el usuario recibido de la base de fragments
         */
        var emailRecibido:String = base_fragments.Companion.emailEnviado
        var usuario:Usuario=base_fragments.Companion.usuarioEnviado

        /**
         * Recogemos la foto de perfil de cada uno de los usuarios accediendo a la base de datos y, mediante comparacion en el email,
         * recogemos para cada usuario la foto que se corresponde en nombre con el email del mismo
         */
        val storageRef = FirebaseStorage.getInstance().reference.child("User/"+emailRecibido.toString())
        storageRef.downloadUrl.addOnSuccessListener { uri->
            Glide.with(this).load(uri).into(imagenPerfil)
        }

        /**
         * Recogemos los distintos datos del usuario que hemos ido pasando usando Companion object en las actividades de registtro
         */

        var nombreRecibido:String = RegActivity.Companion.nombreEnviado
        //var emailRecibidoReg:String = SocialActivity.Companion.emailEnviadoLogin
        var passwordRecibido:String= RegActivity.Companion.passwordEnviado
        var ciudadRecibida:String= RegActivity2.Companion.ciudadEnviada
        var plataformaRecibida:String= RegActivity2.Companion.plataformaEnviada
        var juegosRecibidos:ArrayList<String> = RegActivity3_juegos.Companion.juegosEnviados
        var bioRecibida:String= RegActivity_fotos.Companion.biografiaEnviada

        var juego1r=RegActivity3_juegos.Companion.juego1e
        var juego2r=RegActivity3_juegos.Companion.juego2e
        var juego3r=RegActivity3_juegos.Companion.juego3e
        var juego4r=RegActivity3_juegos.Companion.juego4e
        var juego5r=RegActivity3_juegos.Companion.juego5e



        /**
         * Aqui cargamos la informacion del usuario en los distintos campos. Estos datos varian en funcion de si el usuario ha iniciado sesion:
         * los datos vienen del companion de nuestra base_fragments. Si el usuario accede a esta pantalla habiendo hecho registro, los datos
         * se cargan usando los valores recibidos de los companion de las distintas actividades de registro
         */

        if(emailRecibido.toString()==base_fragments.Companion.emailEnviado){
            nombreGamer.text=usuario.nombre

            juego1Gamer.text=usuario.juegos[0]
            juego2Gamer.text=usuario.juegos[1]
            juego3Gamer.text=usuario.juegos[2]
            juego4Gamer.text=usuario.juegos[3]
            juego5Gamer.text=usuario.juegos[4]
            plataformaGamer.text=usuario.plataforma
            miBiografia.text=usuario.biografia



        }
        else{
            nombreGamer.text=nombreRecibido
            juego1Gamer.text=juego1r
            juego2Gamer.text=juego2r
            juego3Gamer.text=juego3r
            juego4Gamer.text=juego4r
            juego5Gamer.text=juego5r
            plataformaGamer.text=plataformaRecibida
            miBiografia.text=bioRecibida

        }

        /**
         * Listener de boton ajustes
         * Nos dirige a la actividad para realizar cambios en el perfil del usuario
         */
       btnAjustes=component.findViewById<ImageButton>(R.id.btnIraAjustes)
        btnAjustes.setOnClickListener(){
            val i: Intent =Intent(activity,ActividadAjustesPropios::class.java)
            startActivity(i)
        }

        btnNotificaciones = component.findViewById<ImageButton>(R.id.btnIraNotificaciones)

        val numeroNotificaciones = usuario.usuariosQueQuierenConectar.size

        if(numeroNotificaciones>0){
            btnNotificaciones.setImageResource(R.drawable.vectornotificacion)
        }
        else{
            btnNotificaciones.setImageResource(R.drawable.vectorsinnotificacion)
        }
        btnNotificaciones.setOnClickListener(){
            val i:Intent=Intent(activity, PeticionesdeAmistad::class.java)
            startActivity(i)
        }

        return component
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            perfil().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    override fun onResume() {
        super.onResume()
    }


}