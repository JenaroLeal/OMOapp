package com.example.cenecapp

import android.Manifest
import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

/**
 * Clase que nos permite registrar un usuario. Esta es la 4/4 donde recogemos datos del usuario.
 * Los datos recogidos son la biografia y foto de perfil del usuario
 */
class RegActivity_fotos : AppCompatActivity() {

    private val File=1
    private val database= Firebase.database
    val  myRef=database.getReference("urlFoto")
    private val fileResult = 1
    private var emailFinal:String=""
    private val permisoGaleria:Int = 100
    private lateinit var imagen:CircleImageView
    private lateinit var btnFin: Button
    private lateinit var bioJugador:EditText
    private val db = FirebaseFirestore.getInstance()
    private lateinit var spinner:ProgressBar
    private var tieneFoto=false
    private lateinit var mAuth:FirebaseAuth

    /**
     * Método que nos permite almacenar y pasar la información a traves de un companion
     */
    companion object{
        var biografiaEnviada:String=""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg_fotos)

        mAuth = FirebaseAuth.getInstance()
        spinner=findViewById(R.id.spinner4)
        spinner.setVisibility(View.INVISIBLE)

        /**
         * Asignamos a cada uno de los campos, los distintos datos que hemos ido almacenando en los métodos companion de las distintas actividades
         */
        var nombreRecibido:String = RegActivity.Companion.nombreEnviado
        var emailRecibido:String = RegActivity.Companion.emailEnviado
        var passwordRecibido:String=RegActivity.Companion.passwordEnviado
        var ciudadRecibida:String=RegActivity2.Companion.ciudadEnviada
        var plataformaRecibida:String=RegActivity2.Companion.plataformaEnviada
        var juegosRecibidos = ArrayList<String>()

        var juego1r=RegActivity3_juegos.Companion.juego1e
        var juego2r=RegActivity3_juegos.Companion.juego2e
        var juego3r=RegActivity3_juegos.Companion.juego3e
        var juego4r=RegActivity3_juegos.Companion.juego4e
        var juego5r=RegActivity3_juegos.Companion.juego5e

        juegosRecibidos.add(juego1r)
        juegosRecibidos.add(juego2r)
        juegosRecibidos.add(juego3r)
        juegosRecibidos.add(juego4r)
        juegosRecibidos.add(juego5r)

        imagen=findViewById(R.id.foto1)
        btnFin=findViewById(R.id.btnFinalizar)
        bioJugador=findViewById(R.id.campoBiografia)


        btnFin.setOnClickListener(){

            val bio:String=bioJugador.text.toString()
            if(bio.isEmpty() || bio.length<30){
                bioJugador.error=getString(R.string.completaBio)
            }
            else if(tieneFoto==false){
                Toast.makeText(this,"Elige una foto",Toast.LENGTH_LONG).show()
            }
            /**
             * Registramos al usuario en nuestra base de datos de Firebase, con los distintos campos
             */
            else {
                mAuth.createUserWithEmailAndPassword(emailRecibido, passwordRecibido)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            val user = mAuth.currentUser


                            val userData = hashMapOf(
                                "password" to passwordRecibido,
                                "email" to emailRecibido,
                                "nombre" to nombreRecibido,
                                "ciudad" to ciudadRecibida,
                                "plataforma" to plataformaRecibida,
                                "juegos" to juegosRecibidos,
                                "biografia" to bio,
                                "foto" to emailRecibido,
                                "usuariosDeseados" to arrayListOf<String>(),
                                "usuariosQueQuierenConectar" to arrayListOf<String>(),
                                "usuariosRechazados" to arrayListOf<String>(),
                                "usuariosBloqueados" to arrayListOf<String>(),
                                "amigos" to arrayListOf<String>(),
                                "afinidad" to 0 as Int
                            )

                            db.collection("Usuarios").document(emailRecibido).set(userData)
                                .addOnCompleteListener { firestoreTask ->
                                    if (firestoreTask.isSuccessful) {

                                        val i: Intent = Intent(this, base_fragments::class.java)
                                        val bundle: Bundle = Bundle()
                                        biografiaEnviada = bio
                                        emailFinal = emailRecibido
                                        bundle.putString("email", emailFinal)
                                        i.putExtras(bundle)

                                        spinner.setVisibility(View.VISIBLE)
                                        val animator = ValueAnimator.ofInt(75, 100)

                                        animator.apply {
                                            duration = 1000 // 3 seconds
                                            addUpdateListener { valueAnimator ->
                                                val value = valueAnimator.animatedValue as Int
                                                spinner.progress = value
                                            }

                                            addListener(object : Animator.AnimatorListener {
                                                override fun onAnimationStart(animator: Animator) {}
                                                override fun onAnimationEnd(animator: Animator) {
                                                    startActivity(i)
                                                }

                                                override fun onAnimationCancel(p0: Animator) {}
                                                override fun onAnimationRepeat(p0: Animator) {}
                                            })
                                        }
                                        animator.start()
                                    } else {
                                        // Error occurred while storing data in Firestore
                                        Toast.makeText(this, "Error occurred: ${firestoreTask.exception?.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        } else {
                            // User creation failed, show an error
                            Toast.makeText(this, "Error occurred: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }

        }
        /**
         * Listener para la imagen de usuario
         * Nos almacena la foto elegida en firebase
         */
        imagen.setOnClickListener(){
            fileupload()
        }
    }

    /**
     * Método que nos permite elegir la imagen de nuestra galeria del móvil
     */
     fun fileupload(){
        val intent=Intent(Intent.ACTION_GET_CONTENT)
        intent.type="*/*"
        startActivityForResult(intent,File)
    }

    /**
     * Método que nos permite comprobar los permisos de acceso a la galeria del usuario. Si estos son aceptados, podemos elegir la foto de la galeria.
     * una vez elegida la foto, la almacenaremos en nuestra carpeta de firebase Storage, cambiando el nombre por el del email del usuario para evitar duplicados
     * @param requestCode: código requerido para aceptar permisos
     * @param resultCode: codigo resultate de la decision de aceptar permisos
     * @param data: el intent que lanzamos al coincidir ambos códigos
     */
      @JvmName("onActivityResult1")
      fun onActivityResult(requestCode: Int, resultCode: Int, data:Intent) {
         super.onActivityResult(requestCode, resultCode, data)
         if (requestCode == File) {
             if (resultCode == RESULT_OK) {
                 val FileUri = data!!.data
                 val Folder: StorageReference =
                     FirebaseStorage.getInstance().getReference().child("User")
                 val file_name: StorageReference = Folder.child("file" + FileUri!!.lastPathSegment)
                 file_name.putFile(FileUri).addOnSuccessListener { taskSnapshot ->
                     file_name.getDownloadUrl().addOnSuccessListener { uri ->
                         val hashMap =
                             HashMap<String, String>()
                         hashMap["link"] = java.lang.String.valueOf(uri)
                         myRef.setValue(hashMap)
                         Toast.makeText(this,getString(R.string.archivoOk),Toast.LENGTH_LONG).show()
                     }
                 }
             }
         }
     }

    /**
     * Método que nos permite solicitar los diferentes permisos al usuario
     * @param requestCode: código requerido
     * @param permissions: el array con los distintos permisos solicitados
     * @param grantResults: los codigos de los diferentes permisos solicitados
     */

     override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==this.permisoGaleria && grantResults.size>=1 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            val intent:Intent = Intent (Intent.ACTION_GET_CONTENT)
            startActivity(intent)
        }
        else{
            Toast.makeText(this, getString(R.string.permisosDenegados), Toast.LENGTH_SHORT).show()
        }
    }
    /**
     * Método que nos permite almacenar los datos
     * @param outState: los datos que se pasarán como bundle
     */
     override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("email",emailFinal)
    }
    /**
     * Método que nos permite recuperar los datos
     * @param savedInstanceState: los datos que se recuperarán como bundle
     */
     override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        emailFinal = savedInstanceState.getString("email").toString()
    }

    /**
     * Método que nos permite comprobar los permisos de acceso a la galeria del usuario. Si estos son aceptados, podemos elegir la foto de la galeria.
     * una vez elegida la foto, la almacenaremos en nuestra carpeta de firebase Storage, cambiando el nombre por el del email del usuario para evitar duplicados
     * @param requestCode: código requerido para aceptar permisos
     * @param resultCode: codigo resultate de la decision de aceptar permisos
     * @param data: el intent que lanzamos al coincidir ambos códigos
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == fileResult) {
            if (resultCode == RESULT_OK && data != null) {
                val clipData = data.clipData
                if (clipData != null){
                    for (i in 0 until clipData.itemCount) {
                        val uri = clipData.getItemAt(i).uri
                        uri?.let { fileUpload(it) }
                        imagen.setImageURI(uri)
                        tieneFoto=true
                    }
                }else {
                    val uri = data.data
                    uri?.let { fileUpload(it) }
                    imagen.setImageURI(uri)
                    tieneFoto=true
                }
            }
        }
    }

    /**
     * Método que nos permite almacenar la foto seleccionada
     * @param mUri: el uri de la foto elegida
     */
    private fun fileUpload(mUri: Uri) {
        val folder: StorageReference = FirebaseStorage.getInstance().reference.child("User")
        val path =mUri.lastPathSegment.toString()
        val nombreFoto:String = RegActivity.Companion.emailEnviado.toString()
        var fileName: StorageReference = folder.child(nombreFoto)
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
}