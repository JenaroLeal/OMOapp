package clases

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

import com.example.cenecapp.R
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Clase que nos permite recoger los diferentes datos que queremos utilizar en nuestro Reciclerview
 * @param view: La vista sobre la que vamos a trabajar, en este caso, nuestro usuariosAdapter
 */
class Usuario_ViewHolder(view: View):ViewHolder(view) {


    val nombre:TextView by lazy { view.findViewById(R.id.nombreJugadorLista) }
    val amigos:TextView by lazy {view.findViewById(R.id.amigosJugador)}
    val amigosComun:TextView by lazy {view.findViewById(R.id.amigosEnComunJugador)}
    val afinidad:TextView by lazy { view.findViewById(R.id.afinidadConJugador) }
    val fotoPerfil:CircleImageView by lazy {view.findViewById(R.id.imgPerfilJugador)}
    val biografia:TextView by lazy { view.findViewById(R.id.biografiaJugadorLista) }


    val btnEliminar:ImageButton by lazy {view.findViewById(R.id.btnDescartarAJugador)}
    val btnVer:CardView by lazy { view.findViewById(R.id.cardVerPerfil) }
    val btnConectar:ImageButton by lazy{view.findViewById(R.id.btnConectarAJugador)}

}