package Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cenecapp.R
import com.example.cenecapp.base_fragments
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

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
        var emailRecibido:String = base_fragments.Companion.emailEnviado
        mAuth=FirebaseAuth.getInstance()
        db.collection("Usuarios").document(emailRecibido).get().addOnSuccessListener {
            var nombre:String=it.get("nombre") as String
        }

        return inflater.inflate(R.layout.fragment_chats, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            chats().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}