package com.example.cenecapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import clases.Usuario
import com.bumptech.glide.Glide
import com.example.cenecapp.databinding.ActivityAjustesPerfilBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File

class AjustesPerfil : AppCompatActivity() {
    private lateinit var binding:ActivityAjustesPerfilBinding
    private val database= Firebase.database
    val  myRef=database.getReference("urlFoto")
    private lateinit var imagen: ImageView
    var REQUEST_CODE=100
    private val File=1
    private val fileResult = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAjustesPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imagen = findViewById(R.id.cambioFoto)
        var usuario: Usuario =base_fragments.Companion.usuarioEnviado
        var db=FirebaseFirestore.getInstance()
        val miEmail = FirebaseAuth.getInstance().currentUser?.email
        val miRef=db.collection("Usuarios").document(miEmail!!)
        var storageRef = FirebaseStorage.getInstance().getReference("users/${FirebaseAuth.getInstance().currentUser?.uid}/profilePicture")

       var imageUri: Uri? = null

        miRef.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                // Handle errors
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                // Update UI with the latest data
                val nombre = snapshot.getString("nombre") ?: ""
                val bio = snapshot.getString("biografia") ?: ""

                binding.nombreActual.text = nombre
                binding.bioActual.text = bio
            }
        }

        binding.nombreActual.text=usuario.nombre
        binding.bioActual.text=usuario.biografia

        val storageRefer = FirebaseStorage.getInstance().reference.child("User/"+usuario.email.toString())
        storageRefer.downloadUrl.addOnSuccessListener { uri->
            Glide.with(this).load(uri).into(binding.cambioFoto)
        }

        binding.cambioFoto.setOnClickListener(){
            fileupload()
        }




        binding.guardarCambiosPerfil.setOnClickListener(){
            var nombre:String = binding.nuevoNombre.text.toString()
            var bio:String = binding.nuevaBio.text.toString()

            if(nombre.equals("")){
                nombre=usuario.nombre!!
            }
            if(bio.equals("")){
                bio=usuario.biografia!!
            }
            else if (bio.length<30){
                binding.nuevaBio.error = "La biografía debe tener al menos 30 caracteres"
            }

            val updates = hashMapOf<String, Any>(
                "nombre" to nombre,
                "biografia" to bio)

            miRef.update(updates)
                .addOnSuccessListener {
                    Toast.makeText(this, "Datos de perfil actualizados",Toast.LENGTH_LONG).show()

                }
                .addOnFailureListener {
                    Toast.makeText(this, "Ha ocurrido un error",Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun fileupload(){
        val intent=Intent(Intent.ACTION_GET_CONTENT)
        intent.type="*/*"
        startActivityForResult(intent, File)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == fileResult) {
            if (resultCode == RESULT_OK && data != null) {
                val clipData = data.clipData
                if (clipData != null){
                    for (i in 0 until clipData.itemCount) {
                        val uri = clipData.getItemAt(i).uri
                        uri?.let { actualizarFoto(it) }
                        imagen.setImageURI(uri)
                    }
                }else {
                    val uri = data.data
                    uri?.let { actualizarFoto(it) }
                    imagen.setImageURI(uri)
                }
            }
        }
    }

    private fun actualizarFoto(mUri:Uri){
        var usuario: Usuario =base_fragments.Companion.usuarioEnviado
        val folder: StorageReference = FirebaseStorage.getInstance().reference.child("User")
        val path =mUri.lastPathSegment.toString()
        val nombreFoto:String=usuario.email.toString()
        var fileName:StorageReference = folder.child(nombreFoto)
        fileName.putFile(mUri).addOnSuccessListener {
            fileName.downloadUrl.addOnSuccessListener { uri ->
                val hashMap = HashMap<String, String>()
                hashMap["link"] = java.lang.String.valueOf(uri)
                myRef.child(myRef.push().key.toString()).setValue(hashMap)
            }
        }.addOnFailureListener {
            Toast.makeText(this,getString(R.string.fotoNoOk),Toast.LENGTH_LONG).show()
        }

    }

    override fun onDestroy() {
        super.onDestroy()

    }

}