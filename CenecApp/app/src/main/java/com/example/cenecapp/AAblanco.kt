package com.example.cenecapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    private val messagesLiveData = MutableLiveData<List<msgModelclass>>()

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
        val valores = ArrayList<msgModelclass>()

        val adapter: MessagesAdapter = MessagesAdapter(this,valores)
        val recyclerView: RecyclerView =findViewById<RecyclerView>(R.id.recyclerChats)
        recyclerView.adapter=adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        messagesLiveData.observe(this, Observer { updatedMessages ->
            valores.clear()
            valores.addAll(updatedMessages)
            adapter.notifyDataSetChanged()
        })

        storageRef.downloadUrl.addOnSuccessListener { uri->
            Glide.with(this).load(uri.toString()).into(foto)
        }

        binding.nombreAmigoChatActual.text=usuario.nombre

        val user1Id = yo.email
        val user2Id = usuario.email
        val conversationId = if (user1Id!! < user2Id!!) {
            "${user1Id}_${user2Id}"
        } else {
            "${user2Id}_${user1Id}"
        }

        db.collection("chats").document(conversationId).get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val messagesArray = documentSnapshot.get("messages") as ArrayList<HashMap<String, Any>>?

                if (messagesArray != null) {
                    val updatedmessages = ArrayList<msgModelclass>()
                    for (messageData in messagesArray) {
                        val texto: String? = messageData["message"] as String?
                        val enviado: String? = messageData["senderid"] as String?
                        val recibido: String? = messageData["receiverid"] as String?
                        val time: Long? = messageData["timeStamp"] as Long?

                        if (texto != null && enviado != null && recibido != null && time != null) {
                            val mensaje = msgModelclass(texto, enviado, recibido, time)
                            updatedmessages.add(mensaje)

                        }
                    }
messagesLiveData.value=updatedmessages
                    adapter.notifyDataSetChanged() // Notify the adapter that data has changed
                }
            }
        }.addOnFailureListener { exception ->
            // Handle the failure case
        }

        binding.btnEnviarMensaje.setOnClickListener {
            val messageText = binding.textoMensaje.text.toString()
            if (messageText.isNotEmpty()) {

                val sender = db.collection("chats").document(conversationId)


                sender.get().addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            // The chat document exists; update the messages
                            val message = msgModelclass(messageText, yo.email!!,usuario.email!!, System.currentTimeMillis())
                            sender.update("messages", FieldValue.arrayUnion(message))
                                .addOnSuccessListener {
                                    // Message sent successfully
                                    binding.textoMensaje.text.clear()

                                    val newMessageIndex = valores.size
                                    valores.add(message)
                                    adapter.notifyItemInserted(newMessageIndex)


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
                                    valores.add(msgModelclass(messageText, yo.email!!, usuario.email!!, System.currentTimeMillis()))
                                    adapter.notifyItemInserted(0)
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