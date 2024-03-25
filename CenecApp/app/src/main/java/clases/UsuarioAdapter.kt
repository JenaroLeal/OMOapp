package clases

import Fragments.amigos
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cenecapp.Perfil_Jugadores
import com.example.cenecapp.R
import com.example.cenecapp.base_fragments
import com.example.cenecapp.databinding.FragmentSocialBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView



class UsuarioAdapter(val actividadMadre:Activity, val datos:ArrayList<Usuario>):RecyclerView.Adapter<Usuario_ViewHolder>() {

private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Usuario_ViewHolder {
        context = parent.context
        return Usuario_ViewHolder(actividadMadre.layoutInflater.inflate(R.layout.elementos_chats_recicler,parent,false))
    }


    override fun onBindViewHolder(holder: Usuario_ViewHolder, position: Int) {
        var db= FirebaseFirestore.getInstance()
        val usuario:Usuario=datos.get(position)
        var amigosComun:Int = 0
        val rojo = ContextCompat.getColor(context,R.color.rojoRecluciente)
        val verde = ContextCompat.getColor(context,R.color.verde)


        holder.nombre.text=usuario.nombre+" "
        holder.afinidad.text=usuario.afinidad.toString()+"%"



        holder.afinidad.setTextColor(getColorForNumber(usuario.afinidad))

        holder.amigos.text=usuario.amigos.size.toString()

        var yo:Usuario= base_fragments.Companion.usuarioEnviado

        for (amigos in yo.amigos){
            if(amigos in usuario.amigos){
                amigosComun++
            }
        }
        holder.amigosComun.text=amigosComun.toString()
        val fotopefil:CircleImageView=holder.fotoPerfil

        val storageRef = FirebaseStorage.getInstance().reference.child("User/"+usuario.email.toString())
        storageRef.downloadUrl.addOnSuccessListener { uri->
            Glide.with(holder.itemView.context).load(uri).into(fotopefil)
        }

        val miEmail = FirebaseAuth.getInstance().currentUser?.email
        val suEmail = usuario.email
        val referencia = db.collection("Usuarios").document(suEmail.toString())
        val miRef=db.collection("Usuarios").document(miEmail!!)


        holder.btnVer.setOnClickListener(){

            val i: Intent = Intent(actividadMadre,Perfil_Jugadores::class.java)
            val bundle:Bundle = Bundle()
            bundle.putStringArrayList("juegos",usuario.juegos)
            bundle.putParcelable("usuario",usuario)
            i.putExtras(bundle)
            actividadMadre.startActivity(i)
        }


        holder.btnConectar.setOnClickListener(){
            val builder = AlertDialog.Builder(actividadMadre,R.style.CustomAlertDialogTheme)
            builder.setTitle("¿Enviar invitación?")
            builder.setPositiveButton("Sí") { _, _ ->

                referencia.update("usuariosQueQuierenConectar", FieldValue.arrayUnion(miEmail)).addOnSuccessListener {

                }.addOnFailureListener {

                }


                miRef.update("usuariosDeseados",FieldValue.arrayUnion(suEmail)).addOnSuccessListener {

                }.addOnFailureListener {

                }

                Toast.makeText(actividadMadre, "Invitacion enviada", Toast.LENGTH_SHORT).show()
                datos.removeAt(position)
                this.notifyDataSetChanged()
            }
            builder.setNegativeButton("Quizás más tarde") { dialog, _ ->
                // Perform action when "No" button is clicked
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }

        holder.btnEliminar.setOnClickListener(){
            val builder = AlertDialog.Builder(actividadMadre,R.style.CustomAlertDialogTheme)
            builder.setTitle("¿Descartar usuario?")
            builder.setPositiveButton("Sí") { _, _ ->

                miRef.update("usuariosRechazados",FieldValue.arrayUnion(suEmail)).addOnSuccessListener {

                    datos.removeAt(position)
                    this.notifyDataSetChanged()
                }.addOnFailureListener {

                }

                Toast.makeText(actividadMadre, "Jugador descartado", Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("Ahora no") { dialog, _ ->
                // Perform action when "No" button is clicked
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()


        }


    }
    fun getColorForNumber(number: Int): Int {
        val colors = listOf(
            "#FF0056",
            "#FF006B",
            "#FF0094",
            "#FF5576",
            "#FFB27F",
            "#BDCC67",
            "#96B547",
            "#66CF33",
            "#33E51A",
            "#00FF00"
        )
        // Calculate the index in the colors list based on the number
        val index = when {
            number >= 100 -> 0
            number < 0 -> 9
            else -> (number / 10)
        }
        // Convert color string to Color Int
        return Color.parseColor(colors[index])
    }

    override fun getItemCount(): Int {
        return datos.size
    }



}