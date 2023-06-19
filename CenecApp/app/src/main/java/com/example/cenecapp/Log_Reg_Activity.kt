package com.example.cenecapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

/**
 * Clase que se muestra al iniciar la aplicacion. Sirve de menú principal. Donde podremos registrarnos o acceder a nuestro perfil
 */
class Log_Reg_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_reg)
    }

    /**
     * Método que nos inicia las variables que necesitamos de esta clase al iniciar la aplicación
     */
    override fun onStart() {
        super.onStart()

        val btnInicio:TextView = findViewById<Button>(R.id.btnInicio)
        val btnReg:TextView = findViewById<Button>(R.id.btnReg)


        /**
         * Listner para el boton registro. Nos dirige a la actividad para poder iniciar el registro en nuestra aplicación
         */
        btnReg.setOnClickListener(){
            val cambio: Intent = Intent(this, RegActivity::class.java)
            this.startActivity(cambio)
        }
        /**
         * Listener para el boton inicio. Nos dirige a la actividad para poder iniciar sesion en nuestra aplicación si ya tenemos cuenta
         */
        btnInicio.setOnClickListener(){
            val i:Intent=Intent(this,Inicio_Sesion_Activity::class.java)
            startActivity(i)
        }
    }

    /**
     * Método que nos permite asignar funcionalidad al boton 'atrás' del teléfono donde se usa la app.
     * En este caso, hemos bloqueado su funcionalidad para que no se pueda volver a la pantalla Splash de la aplicacion
     */
    override fun onBackPressed() {

    }
}