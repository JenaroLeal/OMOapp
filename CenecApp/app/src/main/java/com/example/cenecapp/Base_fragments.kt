package com.example.cenecapp

import Fragments.*
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import clases.Usuario
import clases.UsuarioAdapter
import com.example.cenecapp.databinding.ActivityBaseFragmentsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore


/**
 * Clase que sirve de base para poder cargar los diferentes fragments que tenemos en nuestra aplicación
 */
class base_fragments : AppCompatActivity() {
    private var emailLogin:String=""
    private lateinit var binding:ActivityBaseFragmentsBinding

    private lateinit var auth:FirebaseAuth
    private lateinit var dataBaseReference:DatabaseReference


    /**
     * Método que nos permite almacenar y pasar como Companion los diferentes datos de nuestro fragment
     */
    companion object{
        var emailEnviado:String=""
        var usuarioEnviado:Usuario=Usuario()
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding=ActivityBaseFragmentsBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val bundleRecibido: Bundle? = this.intent.extras
        emailLogin =""+bundleRecibido?.getString("email")

        var user = Usuario()

        var db= FirebaseFirestore.getInstance()
        val userRef = db.collection("Usuarios").document(emailLogin)

        userRef.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                // Manejar errores
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                // Actualizar el objeto Usuario con los datos más recientes del documento Firestore
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


                // Actualizar el objeto Usuario local con los datos más recientes del documento Firestore
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


                // Actualizar la UI con los datos más recientes del objeto Usuario
                // ...
            } else {
                // El documento no existe
            }
        }
        /*db.collection("Usuarios").document(emailLogin).get().addOnSuccessListener {
            var nombre: String = it.get("nombre") as String
            var email: String = it.get("email") as String
            var contraseña: String = it.get("password") as String
            var ciudad: String = it.get("ciudad") as String
            var plataforma: String = it.get("plataforma") as String
            var juegos: ArrayList<String> = it.get("juegos") as ArrayList<String>
            var bio: String = it.get("biografia") as String
            var foto: String = it.get("foto") as String
            var amigos: ArrayList<String> = it.get("amigos") as ArrayList<String>
            var usuariosDeseados: ArrayList<String> =
                it.get("usuariosDeseados") as ArrayList<String>
            var usuariosQueQuierenConectar: ArrayList<String> =
                it.get("usuariosQueQuierenConectar") as ArrayList<String>
            var usuariosRechazadosDB: ArrayList<String> =
                it.get("usuariosRechazados") as ArrayList<String>
            var usuariosBloqueadosDB: ArrayList<String> =
                it.get("usuariosBloqueados") as ArrayList<String>


            user = Usuario(nombre, email, contraseña, ciudad, plataforma, juegos, foto, bio, false,
                usuariosDeseados, usuariosQueQuierenConectar, amigos, usuariosRechazadosDB, usuariosBloqueadosDB, 0)
        }*/
            /**
             * Aquí cargamos el fragment que esperará por primera vez
             */
            replaceFragment(bienvenida())

            /**
             * Listener para el boton chats
             * Nos almacena la informacion del usuario
             * Reemplaza el fragment por el actual
             */
            binding.irAChats.setOnClickListener(){
                emailEnviado=emailLogin
                usuarioEnviado=user
                replaceFragment(chats())
            }
            /**
             * Listener para el boton perfil
             * Nos almacena la informacion del usuario
             * Reemplaza el fragment por el actual
             */
            binding.irAPerfil.setOnClickListener(){
                emailEnviado=emailLogin
                usuarioEnviado=user
                replaceFragment(perfil())
            }
            /**
             * Listener para el boton social
             * Nos almacena la informacion del usuario
             * Reemplaza el fragment  por el actual
             */
            binding.irASocial.setOnClickListener(){
                emailEnviado=emailLogin
                usuarioEnviado=user
                replaceFragment(social())
            }
            /**
             * Listener para el boton amigos
             * Nos almacena la informacion del usuario
             * Reemplaza el fragment  por el actual
             */
            binding.irAmigos.setOnClickListener(){
                emailEnviado=emailLogin
                usuarioEnviado=user
                replaceFragment(amigos())
            }



    }



    /**
     * Método que nos permite cambiar entre los diferentes fragments de nuestra aplicacion
     * @param fragment: el nombre del fragment que queremos cargar
     */
    private fun replaceFragment(fragment: Fragment){

        val fragmentManager = supportFragmentManager
        val fragmenTransaction = fragmentManager.beginTransaction()

        fragmenTransaction.replace(R.id.contenedorFragments,fragment)

        fragmenTransaction.commit()

    }

    /**
     * Método que nos permite asignar funcionalidad al boton 'atrás' del teléfono donde se usa la app.
     * En este caso, hemos bloqueado su funcionalidad para que no se pueda volver a la pantalla Splash de la aplicacion
     */
    override fun onBackPressed() {

    }
}