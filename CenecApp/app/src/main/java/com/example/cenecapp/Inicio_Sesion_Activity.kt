package com.example.cenecapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

/**
 * Clase que nos permite iniciar sesión en nuestra aplicación si ya estamos registrado
 */
class Inicio_Sesion_Activity : AppCompatActivity() {

private lateinit var mEmail:EditText
private lateinit var mPassword:EditText
private lateinit var btnInicio:TextView
private lateinit var mAuth:FirebaseAuth
private lateinit var btnReg:TextView

private var emailFinal:String=""
private lateinit var btnRecuperacion: TextView

private lateinit var firebaseAuthStateListener:FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_sesion)

        mAuth=FirebaseAuth.getInstance()
        mEmail=findViewById(R.id.emailInicioSesion)
        mPassword=findViewById(R.id.contraseñaInicioSesion)
        btnInicio=findViewById(R.id.btnInicioSesion)
        btnReg=findViewById(R.id.btnRegistro)
        btnRecuperacion = findViewById(R.id.btnRecuperacionPassword)

        btnReg.setOnClickListener(){
            val i:Intent=Intent(this,Log_Reg_Activity::class.java)
            startActivity(i)
        }

        btnRecuperacion.setOnClickListener(){
            var i= Intent(this, RecuperacionPass::class.java)
            startActivity(i)
        }


        /**
         * Listener del boton inicio
         */
        btnInicio.setOnClickListener(){
            val email = mEmail.text.toString()
            val password = mPassword.text.toString()
            if (validarCampos(email, password)) {
                checkEmailAndPasswordExist(email, password)
            }
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
     * Método que nos permite comprobar si los campos cumplen con los requisitos
     * @param email: hace referencia a nuestro campo donde podemos escribir el email
     * @param password: hace referencia a nuestro campo donde podemos escribir la contraseña
     * @return si los campos cumplen los requisitos
     */
    private fun validarCampos(email: String, password: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(this, getString(R.string.completaLosCampos), Toast.LENGTH_SHORT).show()
            return false
        } else if (!email.matches(emailPattern.toRegex())) {
            Toast.makeText(this, getString(R.string.emailIncorrecto), Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    /**
     * Método que nos permite comprobar si el email y contraseña introducidos se corresponden con los de firebase
     * @param email: el email de firebase
     * @param password: la conrtaseña de firebase
     * Si los campos son correctos, podemos pasar a la siguiente actividad. En caso contrario, dependiendo del error, se nos avisará de lo que no es correcto
     */
    private fun checkEmailAndPasswordExist(email: String, password: String) {
        mAuth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val signInMethods = task.result?.signInMethods ?: emptyList<String>()
                    if (signInMethods.contains(EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD)) {
                        mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    var emailLogin=mEmail.text.toString()
                                    val i:Intent=Intent(this,base_fragments::class.java)
                                    val datos:Bundle=Bundle()
                                    emailFinal=mEmail.text.toString()
                                    datos.putString("email",emailFinal)
                                    i.putExtras(datos)
                                    startActivity(i)
                                } else {

                                    Toast.makeText(this, getString(R.string.completaLosCampos), Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {

                        Toast.makeText(this, getString(R.string.completaLosCampos), Toast.LENGTH_SHORT).show()
                    }
                } else {

                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }


}