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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class chatwindo : AppCompatActivity() {
    private lateinit var reciverimg: String
    private lateinit var reciverUid: String
    private lateinit var reciverName: String
    private lateinit var senderUid: String
    private lateinit var profile: CircleImageView
    private lateinit var reciverNName: TextView
    private lateinit var database: FirebaseDatabase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var senderImg: String
    private lateinit var reciverIImg: String
    private lateinit var sendbtn: CardView
    private lateinit var textmsg: EditText
    private lateinit var senderRoom: String
    private lateinit var reciverRoom: String
    private lateinit var messageAdapter: RecyclerView
    private lateinit var messagesArrayList: ArrayList<msgModelclass>
    private lateinit var messagesAdapter: MessagesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatwindo)
        supportActionBar?.hide()
        database = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        var bundle:Bundle? = this.intent.extras
        var usuario: Usuario? = null
        usuario = bundle!!.getParcelable<Usuario>("usuario")

        var yo:Usuario = base_fragments.Companion.usuarioEnviado

        reciverName = usuario!!.nombre!!
        reciverUid = usuario!!.email!!

        messagesArrayList = ArrayList()

        sendbtn = findViewById(R.id.sendbtnn)
        textmsg = findViewById(R.id.textmsg)
        reciverNName = findViewById(R.id.recivername)
        profile = findViewById(R.id.profileimgg)
        messageAdapter = findViewById(R.id.msgadapter)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.stackFromEnd = true
        messageAdapter.layoutManager = linearLayoutManager
        messagesAdapter = MessagesAdapter(this, messagesArrayList)
        messageAdapter.adapter = messagesAdapter


        reciverNName.text = reciverName

        senderUid = yo.email!!

        senderRoom = "$senderUid$reciverUid"
        reciverRoom = "$reciverUid$senderUid"




    }
}