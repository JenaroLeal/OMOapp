package com.example.cenecapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import clases.Usuario
import com.bumptech.glide.Glide
import com.example.cenecapp.databinding.ActivityAablancoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView

class AAblanco : AppCompatActivity() {

    private lateinit var binding:ActivityAablancoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAablancoBinding.inflate(layoutInflater)
        setContentView(binding.root)


        var yo: Usuario =base_fragments.Companion.usuarioEnviado
        var db= FirebaseFirestore.getInstance()
        val miEmail = FirebaseAuth.getInstance().currentUser?.email
        val miRef=db.collection("Usuarios").document(miEmail!!)
        val bundle: Bundle? = this.intent.extras
        var usuario: Usuario? = null
        usuario = bundle!!.getParcelable<Usuario>("usuario")

        var foto:CircleImageView=binding.imgFotoChat
        val storageRef = FirebaseStorage.getInstance().reference.child("User/"+ usuario!!.email.toString())



        storageRef.downloadUrl.addOnSuccessListener { uri->
            Glide.with(this).load(uri.toString()).into(foto)
        }

        binding.nombreAmigoChatActual.text=usuario.nombre

        binding.btnEnviarMensaje.setOnClickListener {
            val messageText = binding.textoMensaje.text.toString()
            if (messageText.isNotEmpty()) {
                val user1Id = yo.email
                val user2Id = usuario.email
                val conversationId = if (user1Id!! < user2Id!!) {
                    "${user1Id}_${user2Id}"
                } else {
                    "${user2Id}_${user1Id}"
                }
                val sender = db.collection("chats").document(conversationId)


                sender.get().addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            // The chat document exists; update the messages
                            val message = msgModelclass(messageText, yo.email!!,usuario.email!!, System.currentTimeMillis())
                            sender.update("messages", FieldValue.arrayUnion(message))
                                .addOnSuccessListener {
                                    // Message sent successfully
                                    binding.textoMensaje.text.clear() // Clear the input field
                                }
                                .addOnFailureListener { e ->
                                    // Handle the error
                                   Toast.makeText(this,"Error al enviar mensaje",Toast.LENGTH_LONG).show()
                                }
                        } else {
                            // The chat document doesn't exist; create it and add the first message
                            val chatData = hashMapOf(
                                "messages" to listOf(msgModelclass(messageText, yo.email!!,usuario.email!!, System.currentTimeMillis()))
                            )
                            sender.set(chatData)
                                .addOnSuccessListener {
                                    // Message sent successfully
                                    binding.textoMensaje.text.clear() // Clear the input field
                                }
                                .addOnFailureListener { e ->
                                    // Handle the error
                                    Toast.makeText(this,"Error al crear chat",Toast.LENGTH_LONG).show()
                                }
                        }
                    }

            }
        }


    }
}