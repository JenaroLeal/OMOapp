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
import com.google.firebase.firestore.FirebaseFirestore
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

        var usuario: Usuario =base_fragments.Companion.usuarioEnviado
        var db= FirebaseFirestore.getInstance()
        val miEmail = FirebaseAuth.getInstance().currentUser?.email
        val miRef=db.collection("Usuarios").document(miEmail!!)

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


    }
}