package Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import clases.Usuario
import clases.UsuarioAdapter
import com.example.cenecapp.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Clase de tipo Fragment que nos permite tener la parte social de nuestra aplicación
 */
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private var emailLogin:String=""
private lateinit var mAuth:FirebaseAuth
private lateinit var contexto:Context


class social : Fragment() {
    private lateinit var banner:LinearLayout

    private var param1: String? = null
    private var param2: String? = null

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
        val component:View = inflater.inflate(R.layout.fragment_social, container, false)
        contexto=requireActivity()


        /**
         * Con esta variable recibimos el email, para poder cargar los datos del usuario
         */
        val bundleRecibido: Bundle? = this.activity?.intent?.extras
        emailLogin=""+bundleRecibido?.getString("email")

        /**
         * Creamos el usuario en base a los datos recibidos despues del registro
         */
        var usuario:Usuario=base_fragments.Companion.usuarioEnviado


        this.activity?.let { MobileAds.initialize(it) {} }
        val adView = this.activity?.let { AdView(it) }
        adView!!.setAdSize(AdSize.BANNER)
        adView!!.adUnitId = "ca-app-pub-3940256099942544/6300978111"

        banner= component.findViewById(R.id.bannerAnuncios)
        banner.addView(adView)

        if (usuario.usuarioPlus){
            banner.visibility = View.GONE
        }else{
            banner.visibility = View.VISIBLE
        }

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)


        val stringArray = mutableListOf<String>()
        val arrayEmail = usuario.usuariosRechazados
        val arrayAmigos = usuario.amigos
        val arrayDeseados = usuario.usuariosDeseados

        stringArray.addAll(arrayEmail)
        stringArray.addAll(arrayAmigos)
        stringArray.addAll(arrayDeseados)
        stringArray.add(usuario.email!!)



        var sumatorio:Int=0
        var ciudadRecibida:String= RegActivity2.Companion.ciudadEnviada
        var plataformaRecibida:String= RegActivity2.Companion.plataformaEnviada
        var juegosRecibidos:ArrayList<String> = RegActivity3_juegos.Companion.juegosEnviados
        var misJuegos= juegosRecibidos

        val juegosUser = Array<String>(usuario.juegos.size) { i ->
            usuario.juegos[i]
        }


        var db= FirebaseFirestore.getInstance()
        val valores:ArrayList<Usuario> = arrayListOf<Usuario>()


        db.collection("Usuarios").get().addOnSuccessListener {

            for(usuarios in it){
                var nombreDB:String?=usuarios.getString("nombre")
                var emailDB:String?=usuarios.getString("email")
                var passwordDB:String?=usuarios.getString("password")
                var ciudadDB:String?=usuarios.getString("ciudad")
                var plataformaBD:String?=usuarios.getString("plataforma")
                var juegosDB:ArrayList<String> = usuarios.get("juegos") as ArrayList<String>
                var bioBD:String?=usuarios.getString("biografia")
                var amigosBD:ArrayList<String> =usuarios.get("amigos") as ArrayList<String>
                var usuariosDeseadosDB:ArrayList<String> =usuarios.get("usuariosDeseados") as ArrayList<String>
                var usuariosQueQuierenConectarDB:ArrayList<String> =usuarios.get("usuariosQueQuierenConectar") as ArrayList<String>
                var usuariosRechazadosBD:ArrayList<String> = usuarios.get("usuariosRechazados") as ArrayList<String>
                var usuariosBloqueados:ArrayList<String> = usuarios.get("usuariosBloqueados") as ArrayList<String>

                /**
                 * Aqui comparamos los valores de dos arrays de juegos, el del usuario actual que esta registrado, con los usuarios de base de datos
                 * por cada juego coincidente, añadimos una afinidad de 12%
                 */
                var juegosUsuario= juegosDB
                for (i in 0 until juegosUser.size) {
                    for (j in 0 until juegosUsuario.size) {
                        val juego:String = juegosUser.get(i)
                        if (juego.equals(juegosUsuario.get(j))) {
                            sumatorio += 12
                        }
                    }
                }
                /**
                 * Aqui comparamos las plataformas en las que juegan. Sumamaos 25% de afinidad si coinciden
                 */
                if(usuario.plataforma==plataformaBD){
                    sumatorio+=25
                }
                /**
                 * Comparamos la ciudad de cada usuario. Añadimos un 15% de afinidad si coinciden
                 */
                if(usuario.ciudad==ciudadDB){
                    sumatorio+=15
                }

                /**
                 * Creamos el usuario añadiendo los valos recibidos de base de datos, asi como el calculo final de la afinidad y lo asginamos a nuestro array
                 */
                val user: Usuario = Usuario(nombreDB!!,emailDB!!,passwordDB!!,ciudadDB,plataformaBD,juegosDB,"",bioBD!!,false,
                    usuariosDeseadosDB,usuariosQueQuierenConectarDB,amigosBD,usuariosRechazadosBD,usuariosBloqueados,sumatorio)

                if(!stringArray.contains(user.email)){
                    valores.add(user)
                }


                /**
                 * Reiniciamos para que el siguiente usuario su valor no sea acumulativo ocn respecto al anterior
                 */
                sumatorio=0


            }

            /**
             * Cargamos los datos de los distintos usuarios obtenidos de base de dtos en nuestro recicler
             */
            val recyclerView: RecyclerView =component.findViewById<RecyclerView>(R.id.contenedorReciclerFragment)
            recyclerView.adapter= this.activity?.let { it1 -> UsuarioAdapter(it1,valores) }
            recyclerView.layoutManager= LinearLayoutManager(component.context)




        }


        return component
    }

    companion object {
        var emailEnviadoLogin:String=""

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            social().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onResume() {
        super.onResume()
    }

    private fun anuncios(){

    }

}