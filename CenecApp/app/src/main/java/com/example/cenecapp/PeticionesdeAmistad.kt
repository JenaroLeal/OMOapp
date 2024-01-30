package com.example.cenecapp



import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import clases.Usuario
import com.google.firebase.firestore.FirebaseFirestore
import com.example.cenecapp.Funciones
import okhttp3.internal.notify

class PeticionesdeAmistad : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_peticionesde_amistad)

        val bundleRecibido: Bundle? = this.intent?.extras
        val emailLogin =""+bundleRecibido?.getString("email")
        var sumatorio:Int=0
        var usuario: Usuario = base_fragments.Companion.usuarioEnviado
        var db= FirebaseFirestore.getInstance()
        val valores = ArrayList<Usuario>()
        val stringArray = mutableListOf<String>()
        val arrayEmail = usuario.usuariosQueQuierenConectar
        stringArray.addAll(arrayEmail)

        val userRef = FirebaseFirestore.getInstance().collection("Usuarios").document(usuario.email!!)

        // Add a real-time listener to observe changes in usuariosQueQuierenConectar field
        userRef.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                // Handle errors
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                // Get the updated usuariosQueQuierenConectar
                var usuariosQueQuierenConectar = snapshot.get("usuariosQueQuierenConectar") as? ArrayList<String>

                // Update the UI with the latest data
                if (usuariosQueQuierenConectar != null) {

                }
            }
        }

        var juegosUser= arrayOf<String>(usuario.juegos[0],usuario.juegos[1],usuario.juegos[2],usuario.juegos[3],usuario.juegos[4])

        db.collection("Usuarios").get().addOnSuccessListener {

            for (usuarios in it) {
                var nombreDB: String? = usuarios.getString("nombre")
                var emailDB: String? = usuarios.getString("email")
                var passwordDB: String? = usuarios.getString("password")
                var ciudadDB: String? = usuarios.getString("ciudad")
                var plataformaBD: String? = usuarios.getString("plataforma")
                var juegosDB:ArrayList<String> = usuarios.get("juegos") as ArrayList<String>
                var bioBD: String? = usuarios.getString("biografia")
                var amigosBD: ArrayList<String> = usuarios.get("amigos") as ArrayList<String>
                var usuariosDeseadosDB: ArrayList<String> =
                    usuarios.get("usuariosDeseados") as ArrayList<String>
                var usuariosQueQuierenConectarDB: ArrayList<String> =
                    usuarios.get("usuariosQueQuierenConectar") as ArrayList<String>
                var usuariosRechazadosBD : ArrayList<String> = usuarios.get("usuariosRechazados") as ArrayList<String>
                var usuariosBloqueadosBD : ArrayList<String> = usuarios.get("usuariosBloqueados") as ArrayList<String>

                /**
                 * Aqui comparamos los valores de dos arrays de juegos, el del usuario actual que esta registrado, con los usuarios de base de datos
                 * por cada juego coincidente, añadimos una afinidad de 12%
                 */
                var juegosUsuario = juegosDB
                for (i in 0 until juegosUser.size) {
                    for (j in 0 until juegosUsuario.size) {
                        val juego: String = juegosUser.get(i)
                        if (juego.equals(juegosUsuario.get(j))) {
                            sumatorio += 12
                        }
                    }
                }
                /**
                 * Aqui comparamos las plataformas en las que juegan. Sumamaos 25% de afinidad si coinciden
                 */
                if (usuario.plataforma == plataformaBD) {
                    sumatorio += 25
                }
                /**
                 * Comparamos la ciudad de cada usuario. Añadimos un 15% de afinidad si coinciden
                 */
                if (usuario.ciudad == ciudadDB) {
                    sumatorio += 15
                }

                /**
                 * Creamos el usuario añadiendo los valos recibidos de base de datos, asi como el calculo final de la afinidad y lo asginamos a nuestro array
                 */
                val user: Usuario = Usuario(nombreDB!!, emailDB!!, passwordDB!!, ciudadDB, plataformaBD, juegosDB, "", bioBD!!, false,
                    usuariosDeseadosDB, usuariosQueQuierenConectarDB, amigosBD, usuariosRechazadosBD, usuariosBloqueadosBD, sumatorio)

                if(stringArray.contains(user.email)){
                    valores.add(user)
                }

                /**
                 * Reiniciamos para que el siguiente usuario su valor no sea acumulativo ocn respecto al anterior
                 */
                sumatorio = 0
            }
            val adapter: SolicitudAdapter = SolicitudAdapter(this,valores)
            val recyclerView:RecyclerView=findViewById<RecyclerView>(R.id.contenedorReciclerSolicitudes)
            recyclerView.adapter=adapter
            recyclerView.layoutManager = LinearLayoutManager(this)

        }

    }

    override fun onBackPressed() {
        super.onBackPressed()

        val user = base_fragments.usuarioEnviado
        val userEmail = user.email

        var funciones = Funciones()
        funciones.updateBBDD(userEmail!!)

    }




}