package com.example.cenecapp

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import clases.Usuario
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView

class UsuariosBloqueadosAdapter(val actividadMadre: Activity, val datos: ArrayList<Usuario>) : RecyclerView.Adapter<UsuariosBloqueados_ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuariosBloqueados_ViewHolder {
        return UsuariosBloqueados_ViewHolder(actividadMadre.layoutInflater.inflate(R.layout.usuariosbloqueados, parent, false))
    }

    override fun onBindViewHolder(holder: UsuariosBloqueados_ViewHolder, position: Int) {


        var db = FirebaseFirestore.getInstance()
        val usuario: Usuario = datos.get(position)
        holder.nombreUsuarioBloqueado.text = usuario.nombre

        val fotoUsuariobloqueado: CircleImageView = holder.fotoUsuarioBloqueado
        val storageRef =
            FirebaseStorage.getInstance().reference.child("User/" + usuario.email.toString())
        storageRef.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(holder.itemView.context).load(uri).into(fotoUsuariobloqueado)
        }
        val miEmail = FirebaseAuth.getInstance().currentUser?.email
        val suEmail = usuario.email
        val miRef = db.collection("Usuarios").document(miEmail!!)
        val referencia = db.collection("Usuarios").document(suEmail.toString())
        val base = base_fragments()
        var funciones = Funciones()

        holder.verPerfilBloqueado.setOnClickListener() {
            val i: Intent = Intent(actividadMadre, Perfil_Jugadores::class.java)
            val bundle: Bundle = Bundle()
            bundle.putParcelable("usuario", usuario)
            i.putExtras(bundle)
            actividadMadre.startActivity(i)
        }

        holder.ajustes.setOnClickListener() {
            val dialog = Dialog(actividadMadre)
            dialog.setContentView(R.layout.dialog_layout)
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )


            // Configurar acciones para los botones dentro del diálogo
            val btnEliminar = dialog.findViewById<Button>(R.id.btnEliminar)
            val btnDesbloquear = dialog.findViewById<Button>(R.id.btnDesbloquear)

            btnEliminar.setOnClickListener {
                val builder = AlertDialog.Builder(actividadMadre)
                builder.setTitle("¿Eliminar amigo de forma permanente?")
                builder.setPositiveButton("sí") { _, _ ->

                    miRef.update("usuariosRechazados", FieldValue.arrayUnion(suEmail))
                        .addOnSuccessListener {
                            datos.removeAt(position)
                            this.notifyDataSetChanged()
                            funciones.updateBBDD(miEmail)
                            dialog.dismiss()
                        }.addOnFailureListener {

                    }
                    miRef.update("usuariosBloqueados", FieldValue.arrayUnion(suEmail))
                        .addOnSuccessListener {
                            datos.removeAt(position)
                            this.notifyDataSetChanged()
                            dialog.dismiss()
                        }.addOnFailureListener {

                        }
                    referencia.update("usuariosRechazados", FieldValue.arrayUnion(miEmail))
                        .addOnSuccessListener {
                            datos.removeAt(position)
                            this.notifyDataSetChanged()
                            funciones.updateBBDD(miEmail)
                            dialog.dismiss()
                        }.addOnFailureListener {

                        }



                }
                builder.setNegativeButton("No") { dialog, _ ->
                    // Perform action when "No" button is clicked
                    dialog.dismiss()
                }
                val dialog = builder.create()
                dialog.show()

            }

            btnDesbloquear.setOnClickListener {
                val builder = AlertDialog.Builder(actividadMadre)
                builder.setTitle("¿Desbloquear usuario?")
                builder.setPositiveButton("Sí") { _, _ ->
                    // Perform action when "Yes" button is clicked

                    miRef.update("usuariosBloqueados", FieldValue.arrayRemove(suEmail))
                        .addOnSuccessListener {
                            funciones.updateBBDD(miEmail)

                        }.addOnFailureListener {

                    }


                    miRef.update("amigos", FieldValue.arrayUnion(suEmail)).addOnSuccessListener {
                        funciones.updateBBDD(miEmail)
                    }.addOnFailureListener {

                    }

                    referencia.update("amigos", FieldValue.arrayUnion(miEmail))
                        .addOnSuccessListener {

                        }.addOnFailureListener {

                    }

                    datos.removeAt(position)
                    this.notifyDataSetChanged()
                    dialog.dismiss()

                }
                builder.setNegativeButton("No") { dialog, _ ->

                    dialog.dismiss()
                }
                val dialog = builder.create()
                dialog.show()

            }

            // Mostrar el diálogo
            dialog.show()
        }


    }

    override fun getItemCount(): Int {
        return datos.size
    }
}