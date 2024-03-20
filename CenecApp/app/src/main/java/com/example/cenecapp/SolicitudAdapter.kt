package com.example.cenecapp

import Fragments.amigos
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import clases.Usuario
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView

class SolicitudAdapter(val actividadMadre: Activity, val datos:ArrayList<Usuario>)  : RecyclerView.Adapter<Solicitud_ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Solicitud_ViewHolder {

        return Solicitud_ViewHolder(actividadMadre.layoutInflater.inflate(R.layout.solicitud,parent,false))
    }

    override fun onBindViewHolder(holder: Solicitud_ViewHolder, position: Int) {
        var db= FirebaseFirestore.getInstance()
        val usuario:Usuario=datos.get(position)
        holder.nombre.text=usuario.nombre
        holder.afinidad.text=usuario.afinidad.toString()+"%"
        holder.amigosComun.text=usuario.amigos.size.toString()


        val fotopefil: CircleImageView =holder.imagen

        val storageRef = FirebaseStorage.getInstance().reference.child("User/"+usuario.email.toString())
        storageRef.downloadUrl.addOnSuccessListener { uri->
            Glide.with(holder.itemView.context).load(uri).into(fotopefil)
        }

        val miEmail = FirebaseAuth.getInstance().currentUser?.email
        val suEmail = usuario.email
        val miRef=db.collection("Usuarios").document(miEmail!!)
        val referencia = db.collection("Usuarios").document(suEmail.toString())
        val base = base_fragments()


        holder.btnAceptar.setOnClickListener(){
            val builder = AlertDialog.Builder(actividadMadre,R.style.CustomAlertDialogTheme)
            builder.setTitle("¿Agregar como amigo?")
            builder.setPositiveButton("Sí") { _, _ ->
                // Perform action when "Yes" button is clicked

                referencia.update("usuariosQueQuierenConectar", FieldValue.arrayRemove(miEmail)).addOnSuccessListener {

                }.addOnFailureListener {

                }
                referencia.update("usuariosDeseados", FieldValue.arrayRemove(miEmail)).addOnSuccessListener {

                }.addOnFailureListener {

                }


                miRef.update("usuariosDeseados", FieldValue.arrayRemove(suEmail)).addOnSuccessListener {

                }.addOnFailureListener {

                }
                miRef.update("usuariosQueQuierenConectar", FieldValue.arrayRemove(suEmail)).addOnSuccessListener {

                }.addOnFailureListener {

                }

                miRef.update("amigos",FieldValue.arrayUnion(suEmail)).addOnSuccessListener {

                }.addOnFailureListener {

                }

                referencia.update("amigos",FieldValue.arrayUnion(miEmail)).addOnSuccessListener {

                }.addOnFailureListener {

                }


                datos.removeAt(position)
                this.notifyDataSetChanged()
            }
            builder.setNegativeButton("No") { dialog, _ ->
                // Perform action when "No" button is clicked

                referencia.update("usuariosQueQuierenConectar", FieldValue.arrayRemove(miEmail)).addOnSuccessListener {

                }.addOnFailureListener {

                }
                referencia.update("usuariosDeseados", FieldValue.arrayRemove(miEmail)).addOnSuccessListener {

                }.addOnFailureListener {

                }


                miRef.update("usuariosDeseados", FieldValue.arrayRemove(suEmail)).addOnSuccessListener {

                }.addOnFailureListener {

                }
                miRef.update("usuariosQueQuierenConectar", FieldValue.arrayRemove(suEmail)).addOnSuccessListener {

                }.addOnFailureListener {

                }


                datos.removeAt(position)
                this.notifyDataSetChanged()
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()


        }

        holder.btnRechazar.setOnClickListener(){

            val builder = AlertDialog.Builder(actividadMadre,R.style.CustomAlertDialogTheme)
            builder.setTitle("¿Rechazar solicitud?")
            builder.setPositiveButton("sí") { _, _ ->
                // Perform action when "Yes" button is clicked

                referencia.update("usuariosQueQuierenConectar", FieldValue.arrayRemove(miEmail)).addOnSuccessListener {

                }.addOnFailureListener {

                }
                referencia.update("usuariosDeseados", FieldValue.arrayRemove(miEmail)).addOnSuccessListener {

                }.addOnFailureListener {

                }


                miRef.update("usuariosDeseados", FieldValue.arrayRemove(suEmail)).addOnSuccessListener {

                }.addOnFailureListener {

                }
                miRef.update("usuariosQueQuierenConectar", FieldValue.arrayRemove(suEmail)).addOnSuccessListener {

                }.addOnFailureListener {

                }

                miRef.update("usuariosRechazados",FieldValue.arrayUnion(suEmail)).addOnSuccessListener {

                }.addOnFailureListener {

                }

                referencia.update("usuariosRechazados",FieldValue.arrayUnion(miEmail)).addOnSuccessListener {

                }.addOnFailureListener {

                }

                Toast.makeText(actividadMadre, "Jugador rechazado", Toast.LENGTH_SHORT).show()
                datos.removeAt(position)
                this.notifyDataSetChanged()
            }
            builder.setNegativeButton("No") { dialog, _ ->
                Toast.makeText(actividadMadre,"Esperamos entonces",Toast.LENGTH_LONG).show()
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()

        }

        holder.verPerfil.setOnClickListener(){
            val i: Intent = Intent(actividadMadre,Perfil_Jugadores::class.java)
            val bundle: Bundle = Bundle()
            bundle.putParcelable("usuario",usuario)
            i.putExtras(bundle)
            actividadMadre.startActivity(i)

        }
    }

    override fun getItemCount(): Int {
        return datos.size
    }



}