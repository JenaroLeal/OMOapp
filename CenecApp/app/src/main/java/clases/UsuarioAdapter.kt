package clases

import Fragments.amigos
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Usuario_ViewHolder {
        return Usuario_ViewHolder(actividadMadre.layoutInflater.inflate(R.layout.elementos_chats_recicler,parent,false))
    }


    override fun onBindViewHolder(holder: Usuario_ViewHolder, position: Int) {
        var db= FirebaseFirestore.getInstance()
        val usuario:Usuario=datos.get(position)
        var amigosComun:Int = 0
        holder.nombre.text=usuario.nombre
        holder.afinidad.text=usuario.afinidad.toString()+"%"
        holder.amigos.text=usuario.amigos.size.toString()

        var yo:Usuario= base_fragments.Companion.usuarioEnviado

        for (amigos in yo.amigos){
            if(amigos in usuario.amigos){
                amigosComun++
            }
        }
        holder.amigosComun.text=amigosComun.toString()
        val fotopefil:CircleImageView=holder.fotoPerfil
        holder.biografia.text=usuario.biografia





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
            val builder = AlertDialog.Builder(actividadMadre)
            builder.setTitle("¿Enviar invitación?")
            builder.setPositiveButton("Yes") { _, _ ->

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
            builder.setNegativeButton("No") { dialog, _ ->
                // Perform action when "No" button is clicked
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }

        holder.btnEliminar.setOnClickListener(){
            val builder = AlertDialog.Builder(actividadMadre)
            builder.setTitle("¿Descartar usuario?")
            builder.setPositiveButton("Yes") { _, _ ->

                miRef.update("usuariosRechazados",FieldValue.arrayUnion(suEmail)).addOnSuccessListener {

                    datos.removeAt(position)
                    this.notifyDataSetChanged()
                }.addOnFailureListener {

                }

                Toast.makeText(actividadMadre, "Jugador descartado", Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("No") { dialog, _ ->
                // Perform action when "No" button is clicked
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()


        }


    }

    override fun getItemCount(): Int {
        return datos.size
    }



}