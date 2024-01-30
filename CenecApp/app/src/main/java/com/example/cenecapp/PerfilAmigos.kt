package com.example.cenecapp

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import clases.Usuario
import com.bumptech.glide.Glide
import com.example.cenecapp.databinding.ActivityPerfilAmigosBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView

class PerfilAmigos : AppCompatActivity() {
    lateinit var binding: ActivityPerfilAmigosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilAmigosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle: Bundle? = this.intent.extras
        var usuario: Usuario? = null


        if (bundle != null) {
            usuario = bundle.getParcelable<Usuario>("usuario")

            if (usuario != null) {

                var foto: CircleImageView = binding.fotoAmigo
                val storageRef =
                    FirebaseStorage.getInstance().reference.child("User/" + usuario.email.toString())
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    Glide.with(this).load(uri.toString()).into(foto)
                }

                binding.nombreAmigo.text = usuario.nombre
                binding.juego1Amigo.text = usuario.juegos[0]
                binding.juego2Amigo.text = usuario.juegos[1]
                binding.juego3Amigo.text = usuario.juegos[2]
                binding.juego4Amigo.text = usuario.juegos[3]
                binding.juego5Amigo.text = usuario.juegos[4]
                binding.afinidadAmigo.text = usuario.afinidad.toString()
                binding.plataformaAmigo.text = usuario.plataforma
                binding.biografiaAmigo.text = usuario.biografia
            }
        }
        var db = FirebaseFirestore.getInstance()
        val miEmail = FirebaseAuth.getInstance().currentUser?.email
        val suEmail = usuario?.email
        val miRef = db.collection("Usuarios").document(miEmail!!)
        val referencia = db.collection("Usuarios").document(suEmail.toString())
        var funciones = Funciones()

        binding.opcionesAmigos.setOnClickListener(){
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_ajustes_amigo)

            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams.x = 0
            layoutParams.y = 200
            layoutParams.gravity = Gravity.LEFT
            dialog.window?.attributes = layoutParams
            /*
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
*/
            val btnBloquear = dialog.findViewById<Button>(R.id.btnBloquear)
            val btnDenunciar = dialog.findViewById<Button>(R.id.btnDenunciar)

            btnBloquear.setOnClickListener(){
                val builder = AlertDialog.Builder(this)
                builder.setTitle("¿Bloquear Amigo?")
                builder.setPositiveButton("Sí") { _, _ ->
                    // Perform action when "Yes" button is clicked

                    referencia.update("amigos", FieldValue.arrayRemove(miEmail)).addOnSuccessListener {
                        funciones.updateBBDD(miEmail)
                        Log.d("EmailEnviado", miEmail)

                    }.addOnFailureListener {

                    }


                    miRef.update("amigos", FieldValue.arrayRemove(suEmail)).addOnSuccessListener {
                        funciones.updateBBDD(miEmail)

                    }.addOnFailureListener {

                    }

                    miRef.update("usuariosBloqueados", FieldValue.arrayUnion(suEmail))
                        .addOnSuccessListener {
                            funciones.updateBBDD(miEmail)

                        }.addOnFailureListener {

                        }
                    funciones.updateBBDD(miEmail)
                        dialog.dismiss()


                }
                builder.setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }

                val dialog = builder.create()
                dialog.show()
            }

            btnDenunciar.setOnClickListener(){

            }
            dialog.show()
        }
    }


}