package com.example.cenecapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import java.util.*

/**
 * Clase que nos permite realizar diferentes ajustes en nuestro perfil de usuario
 */
class ActividadAjustesPropios : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_ajustes_propios)

        val mAuth = FirebaseAuth.getInstance()
        val cerrarSesion =findViewById<TextView>(R.id.cerrarSesion)
        val aplicar=findViewById<TextView>(R.id.aplicarIdioma)
        val editarPerfil=findViewById<TextView>(R.id.editarUsuario)
        val editarPremium=findViewById<TextView>(R.id.obtenerPremium)
        val editarPassword=findViewById<TextView>(R.id.cambiarPassword)
        val editarUsuariosBloqueados=findViewById<TextView>(R.id.usuariosBloqueados)
        val share=findViewById<TextView>(R.id.compartirShare)
        val ayuda=findViewById<TextView>(R.id.ayuda)
        val faq=findViewById<TextView>(R.id.faq)
        val eliminarCuenta=findViewById<TextView>(R.id.eliminarCuenta)

        /**
         * Listener para el boton cerrar sesion.
         * Recoge las variables de mAuth que hace referencia a Firebase Authentication, para poder cerrar sesión en la cuenta actual
         * Al cerrar sesion, se nos redirige a la pantalla de Regitro-Inicio
         */

        cerrarSesion.setOnClickListener(){
            mAuth.signOut()
            val i:Intent= Intent(this,Log_Reg_Activity::class.java)
            startActivity(i)
            finish()
        }
        val idioma=findViewById<Spinner>(R.id.campoIdioma)
        val idiomasPosibles = arrayOf<String>("Español","Inglés")
        val adapter = ArrayAdapter(this,R.layout.spinner_list,idiomasPosibles)
        idioma.adapter=adapter

        /**
         * Listener para el boton aplicar cambios.
         * Este listener, recoge el valor de nuestro Spinner 'idiomaElegido' y aplica a nuestras preferencias el idioma seleccionado
         * una vez que se aplica el idioma aquí, para el resto de la aplicacion habra cambiado el idioma en el que se lee. En este caso, español o inglés
         */
        aplicar.setOnClickListener(){
            val idiomaElegido=idioma.selectedItem.toString()

            val codigo = when(idiomaElegido){
                "Inglés" -> "en"
                "Español" -> "es"
                else -> "es"
            }
            /**
             * Haciendo uso de nuestro método setAppLocale, podemos cambiar el idioma elegido en el spinner y aplicarlo
             * Este idioma quedará registrado en nuestras preferencias
             */
            setAppLocale(codigo,this)
            val sharedPrefs:SharedPreferences=getSharedPreferences("Preferencias", Context.MODE_PRIVATE)
            val editor=sharedPrefs.edit()
            editor.putString("idioma",codigo)
            editor.apply()
        }

        editarPerfil.setOnClickListener(){
            val i:Intent = Intent(this, AjustesPerfil::class.java)
            startActivity(i)
        }

        editarPremium.setOnClickListener(){
            val i:Intent = Intent(this, VentajasPremium::class.java)
            startActivity(i)
        }
        editarPassword.setOnClickListener(){
            val i:Intent = Intent(this, AjustesPassword::class.java)
            startActivity(i)
        }
       editarUsuariosBloqueados.setOnClickListener(){
           val i:Intent = Intent(this,UsuariosBloqueados::class.java)
           startActivity(i)
       }
        share.setOnClickListener(){
            Toast.makeText(this,R.string.disponibilidad,Toast.LENGTH_LONG).show()
        }
        ayuda.setOnClickListener(){
            Toast.makeText(this,R.string.disponibilidad,Toast.LENGTH_LONG).show()
        }
       faq.setOnClickListener(){
           Toast.makeText(this,R.string.disponibilidad,Toast.LENGTH_LONG).show()
       }
        eliminarCuenta.setOnClickListener(){
            val i:Intent = Intent(this, AjustesEliminarCuenta::class.java)
            startActivity(i)
        }


    }

    /**
     * Método que nos permite aplciar cambios de idioma en nuestra aplicacion
     * @param language: idioma que se va a utilizar
     * @param activity: actividad donde se aplica el idioma
     */
    fun setAppLocale(language: String, activity: Activity) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        activity.baseContext.resources.updateConfiguration(config, activity.baseContext.resources.displayMetrics)
        activity.recreate()
    }

}