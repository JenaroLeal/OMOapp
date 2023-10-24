package com.example.cenecapp

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import clases.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.annotations.Nullable
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class chatwindo : AppCompatActivity() {

    lateinit var reciverimg: String
    lateinit var reciverUid: String
    lateinit var reciverName: String
    lateinit var SenderUID: String
    lateinit var profile: CircleImageView
    lateinit var reciverNName: TextView
    lateinit var database: FirebaseDatabase
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var senderImg: String
    lateinit var reciverIImg: String
    lateinit var sendbtn: CardView
    lateinit var textmsg: EditText
    lateinit var senderRoom: String
    lateinit var reciverRoom: String
    lateinit var messageAdpter: RecyclerView
    lateinit var messagesArrayList: ArrayList<msgModelclass>
    lateinit var mmessagesAdpter:MessagesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatwindo)

        var miUsurio = base_fragments.Companion.usuarioEnviado
        sendbtn=findViewById(R.id.sendbtnn)
        textmsg=findViewById(R.id.textmsg)
        reciverNName=findViewById(R.id.recivername)
        profile=findViewById(R.id.profileimgg)
        messageAdpter=findViewById(R.id.msgadapter)
        var linearLayoutManager:LinearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.stackFromEnd
        messageAdpter.layoutManager = linearLayoutManager
        mmessagesAdpter = MessagesAdapter(this,messagesArrayList)
        messageAdpter.adapter = mmessagesAdpter

        reciverName=miUsurio.nombre!!

        val bundle:Bundle?=this.intent.extras
        var usuario: Usuario?=null

        if (bundle!=null) {
            usuario = bundle.getParcelable<Usuario>("usuario")

            if(usuario!=null){
                reciverName=usuario.nombre!!

                database = FirebaseDatabase.getInstance()
                firebaseAuth = FirebaseAuth.getInstance()
                reciverName = usuario.nombre!!
                reciverUid = usuario.email!!
                messagesArrayList = ArrayList()

                SenderUID = miUsurio.email!!
                senderRoom = SenderUID+reciverUid
                reciverRoom = reciverUid+SenderUID

                var chatReference:DatabaseReference = database.reference.child("chats").child(senderRoom).child("messages")

                chatReference.addChildEventListener(object : ChildEventListener {
                    override fun onChildAdded(
                        snapshot: DataSnapshot,
                        @Nullable previousChildName: String?
                    ) {
                        val message = snapshot.getValue(msgModelclass::class.java)
                        messagesArrayList.add(message!!)
                        mmessagesAdpter.notifyDataSetChanged()
                    }

                    override fun onChildChanged(
                        snapshot: DataSnapshot,
                        previousChildName: String?
                    ) {
                    }

                    override fun onChildRemoved(snapshot: DataSnapshot) {}
                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                    override fun onCancelled(error: DatabaseError) {} // Other ChildEventListener methods (onChildChanged, onChildRemoved, onChildMoved, onCancelled)
                })

            }
        }

        sendbtn.setOnClickListener(View.OnClickListener {
            val message = textmsg.text.toString()
            if (message.isEmpty()) {
                Toast.makeText(this@chatwindo, "Enter The Message First", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            textmsg.setText("")
            val date = Date()
            val messagess = msgModelclass(message, SenderUID, date.time)
            val senderRoomRef =
                database.reference.child("chats").child(senderRoom).child("messages").push()
            val receiverRoomRef =
                database.reference.child("chats").child(reciverRoom).child("messages").push()
            senderRoomRef.setValue(messagess)
            receiverRoomRef.setValue(messagess).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Message sent successfully to both sender and receiver
                } else {
                    // Handle the error
                }
            }
        })
    }
}