package Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import clases.Usuario
import com.example.cenecapp.AmigosAdapter
import com.example.cenecapp.R
import com.example.cenecapp.UserChatAdapter
import com.example.cenecapp.base_fragments
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private var emailLogin:String = ""
private lateinit var contexto:Context

/**
 * Clase de tipo Fragment que nos permite usar la parte de chats con aquellos contactos que tengamos como amigos
 */

class chats : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val db = FirebaseFirestore.getInstance()
    private lateinit var mAuth:FirebaseAuth

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
        val component:View=inflater.inflate(R.layout.fragment_chats, container, false)

        contexto=requireActivity()
        val bundleRecibido:Bundle? = this.activity?.intent?.extras
        emailLogin=""+bundleRecibido?.getString("email")

        val usuario: Usuario = base_fragments.Companion.usuarioEnviado
        val db = FirebaseFirestore.getInstance()
        val stringArray = usuario.amigos.toMutableList()

        var sumatorio:Int=0

        val juegosUser = Array<String>(usuario.juegos.size) { i ->
            usuario.juegos[i]
        }
        val valores:ArrayList<Usuario> = arrayListOf<Usuario>()
        db.collection("Usuarios").get().addOnSuccessListener {
            for (usuarios in it) {
                var nombreDB: String? = usuarios.getString("nombre")
                var emailDB: String? = usuarios.getString("email")
                var passwordDB: String? = usuarios.getString("password")
                var ciudadDB: String? = usuarios.getString("ciudad")
                var plataformaBD: String? = usuarios.getString("plataforma")
                var juegosDB: ArrayList<String> = usuarios.get("juegos") as ArrayList<String>
                var bioBD: String? = usuarios.getString("biografia")
                var amigosBD: ArrayList<String> = usuarios.get("amigos") as ArrayList<String>
                var usuariosDeseadosDB: ArrayList<String> =
                    usuarios.get("usuariosDeseados") as ArrayList<String>
                var usuariosQueQuierenConectarDB: ArrayList<String> =
                    usuarios.get("usuariosQueQuierenConectar") as ArrayList<String>
                var usuariosRechazadosDB: ArrayList<String> =
                    usuarios.get("usuariosRechazados") as ArrayList<String>
                var usuariosBloqueadosDB: ArrayList<String> =
                    usuarios.get("usuariosRechazados") as ArrayList<String>

                var juegosUsuario = juegosDB
                for (i in 0 until juegosUser.size) {
                    for (j in 0 until juegosUsuario.size) {
                        val juego: String = juegosUser.get(i)
                        if (juego.equals(juegosUsuario.get(j))) {
                            sumatorio += 12
                        }
                    }
                }
                if (usuario.plataforma == plataformaBD) {
                    sumatorio += 25
                }

                if (usuario.ciudad == ciudadDB) {
                    sumatorio += 15
                }

                val user: Usuario = Usuario(nombreDB!!, emailDB!!, passwordDB!!, ciudadDB, plataformaBD, juegosDB, "", bioBD!!,
                    false, usuariosDeseadosDB, usuariosQueQuierenConectarDB, amigosBD, usuariosRechazadosDB, usuariosBloqueadosDB, sumatorio)
                if (stringArray.contains(user.email)) {
                    valores.add(user)
                }

                sumatorio = 0
            }
            val recyclerView: RecyclerView = component.findViewById(R.id.mainUserRecyclerView)

            recyclerView.layoutManager = LinearLayoutManager(component.context)
            recyclerView.adapter= this.activity?.let { it1 -> UserChatAdapter(it1,valores) }
            recyclerView.layoutManager = LinearLayoutManager(component.context)

        }




        return component
    }

    companion object {

        var emailEnviadologin:String=""
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            chats().apply {
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