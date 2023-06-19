package com.example.cenecapp

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import clases.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import java.net.HttpURLConnection
import java.net.URL

/**
 * Clase que nos permite registrar un usuario. Esta es la 1/4 donde recogemos datos del usuario.
 * Los datos recogidos son el nombre, email y contraseña
 */
class RegActivity : AppCompatActivity() {

    private lateinit var btnSiguiente: TextView
    private lateinit var spinner: ProgressBar
    private lateinit var nombre: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var checkBox:CheckBox
    private lateinit var verTerminos:TextView



    private val TAG: String = "RegActivity"

    private lateinit var mAuth: FirebaseAuth
    private lateinit var firebaseAuthStateListener: FirebaseAuth.AuthStateListener

    /**
     * Método que nos permite almacenar y pasar la información a traves de un companion
     */
    companion object{
        var nombreEnviado:String=""
        var emailEnviado:String=""
        var passwordEnviado:String=""
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg)

        verTerminos=findViewById(R.id.leerTerminos)
        /**
         * Listener para acceder a los terminos y condiciones
         */
        verTerminos.setOnClickListener(){
            val i:Intent=Intent(this,TerminosCondiciones::class.java)
            startActivity(i)
        }
        spinner = findViewById<ProgressBar>(R.id.pBar)
        spinner.setVisibility(View.GONE)
        var existe: TextView = findViewById<TextView>(R.id.existe)

        mAuth = FirebaseAuth.getInstance()
        firebaseAuthStateListener = FirebaseAuth.AuthStateListener {

            fun onAuthStateChanged(@NonNull firebaseAuth: FirebaseAuth) {
                spinner.setVisibility(View.VISIBLE)
                val user: FirebaseUser? = FirebaseAuth.getInstance().getCurrentUser()

                if (user != null && user.isEmailVerified()) {
                    val i: Intent = Intent(this, RegActivity2::class.java)
                    startActivity(i)
                    finish()
                    spinner.setVisibility(View.GONE)
                    return
                }
                spinner.setVisibility(View.GONE)
            }

        }

        /**
         * Listener para el textView 'existe'
         * nos devuelve a la actividad de iniciar sesion si el usuario ya existe
         */
        existe.setOnClickListener() {
            val i: Intent = Intent(this, Inicio_Sesion_Activity::class.java)
            startActivity(i)
            finish()

        }


        btnSiguiente = findViewById<TextView>(R.id.btnSiguiente)
        nombre = findViewById<EditText>(R.id.nombreUsuario)
        email = findViewById<EditText>(R.id.campoEmail)
        password = findViewById<EditText>(R.id.campoPassword)
        checkBox=findViewById(R.id.boxTerminos)


        /**
         * Listener para el textView 'Siguiente'
         * Comprobamos que el valos de los distintos campos que pedimos al usuario son correctos
         * En caso de que sean correctos, almacenamos su email y contraseña en nuestra base de datos
         * Generamos un pdf que se almacena en el almacenamiento interno de la app
         * Iniciamos la animación de nuestra progress bar
         * Cambiamos de actividad
         */
        btnSiguiente.setOnClickListener() {
            val mNombre: String = nombre.getText().toString()
            val emailFinal: String = email.getText().toString()
            val pass1: String = password.getText().toString()


            if(mNombre.equals("")){
                nombre.error=getString(R.string.introduceNombre)
            }
            else if(emailFinal.equals("")){
                email.error=getString(R.string.introduceEmail)
            }
            else if(pass1.equals("")){
                password.error=getString(R.string.introduceContraseña)
            }
            else if(pass1.length<8){
                password.error=getString(R.string.contraseñaMayor8)
            }
            else if(!checkBox.isChecked){
                checkBox.error=getString(R.string.debesAceptarTerminos)
            }


            else {
                mAuth.createUserWithEmailAndPassword(emailFinal, pass1)
                    .addOnCompleteListener(this) { task ->

                        if (task.isSuccessful) {

                            mAuth.getCurrentUser()!!.sendEmailVerification()
                                .addOnCompleteListener(this) { task ->

                                    var userId: String = mAuth.getCurrentUser()!!.getUid()
                                    var usuarioBD: DatabaseReference =
                                        FirebaseDatabase.getInstance().getReference()
                                            .child("usuarios").child(userId)

                                    val userInfo = HashMap<String, String>()
                                    userInfo.put("name", mNombre)
                                    userInfo.put("profileImageUrl", "default")
                                    usuarioBD.updateChildren(userInfo as Map<String, Any>)

                                    spinner.setVisibility(View.VISIBLE)

                                    val i: Intent = Intent(this, RegActivity2::class.java)
                                    nombreEnviado=mNombre
                                    emailEnviado=emailFinal
                                    passwordEnviado=pass1

                                    val fileName = "terminosycondiciones.pdf"
                                    val inputStream = assets.open(fileName)

                                    val outputStream = openFileOutput(fileName, Context.MODE_PRIVATE)
                                    inputStream.copyTo(outputStream)

                                    inputStream.close()
                                    outputStream.close()
                                    val animator = ValueAnimator.ofInt(0, 25)

                                    /**
                                     * Método que nos permite animar la progressbar durante 3 segundos
                                     */
                                    animator.apply {
                                        duration = 1000 // 3 seconds
                                        addUpdateListener { valueAnimator ->
                                            val value = valueAnimator.animatedValue as Int
                                            spinner.progress = value
                                        }
                                        /**
                                         * Listener del animator
                                         * @param object: objeto a animar
                                         */
                                        addListener(object : Animator.AnimatorListener {
                                            /**
                                             * Método para cuando se inicializa la animacion
                                             */
                                            override fun onAnimationStart(animator: Animator) {}
                                            /**
                                             * Método para cuando finaliza la animacion
                                             * Nos lanza la siguiente actividad
                                             */
                                            override fun onAnimationEnd(animator: Animator) {

                                                startActivity(i)

                                                nombre.setText("")
                                                email.setText("")
                                                password.setText("")
                                            }
                                            /**
                                             * Método para cuando se cancela la animacion
                                             */
                                            override fun onAnimationCancel(p0: Animator) {}
                                            /**
                                             * Método para cuando se repite la animacion
                                             */
                                            override fun onAnimationRepeat(p0: Animator) {}

                                        })
                                    }
                                    animator.start()
                                }
                        } else {
                            Log.w(TAG, "createUserWithEmail: error", task.exception)
                            Toast.makeText(this, getString(R.string.emailNoValido), Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }

    }

    /**
     * método para cuando inicializa la activdaad
     */
    override fun onStart() {
        super.onStart()
        mAuth.addAuthStateListener(firebaseAuthStateListener)

    }
    /**
     * método por si nuestra aplicacion se detiene
     */
    override fun onStop() {
        super.onStop()
        mAuth.removeAuthStateListener(firebaseAuthStateListener)
    }

    /**
     * Método que nos hace accesible el boton retroceso del movil, para poder retroceder de pantalla
     */
    override fun onBackPressed() {
        super.onBackPressed()

    }


}