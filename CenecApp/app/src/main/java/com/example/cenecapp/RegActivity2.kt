package com.example.cenecapp

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.view.get
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date

/**
 * Clase que nos permite registrar un usuario. Esta es la 2/4 donde recogemos datos del usuario.
 * Los datos recogidos son la ciudad donde vive el usuario y su plataforma de juego
 */
class RegActivity2 : AppCompatActivity() {


    private lateinit var campoCiudad:Spinner
    private lateinit var selectorFecha:DatePicker
    private lateinit var btnContinuar:Button
    private lateinit var selectorPlataforma:Spinner
    private lateinit var spinner:ProgressBar
    /**
     * Método que nos permite almacenar y pasar la información a traves de un companion
     */
    companion object{

        var ciudadEnviada:String=""
        var plataformaEnviada:String=""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg2)

        campoCiudad=findViewById(R.id.campoCiudad)
        selectorPlataforma=findViewById(R.id.spinnerJuegos)
        btnContinuar=findViewById(R.id.btn3)
        spinner = findViewById(R.id.spinner2)
        spinner.setVisibility(View.INVISIBLE)

        val plataformasPosibles = arrayOf<String>("---","PS5","PS4","PC","Nintendo","Xbox","Movil")
        val adapter = ArrayAdapter(this,R.layout.spinner_list,plataformasPosibles)
        selectorPlataforma.adapter=adapter

        val ciudadesPosibles = arrayOf<String>("---","A Coruña","Alava","Albacete","Alicante","Almería","Asturias","Avila","Badajoz","Barcelona","Burgos","Cáceres","Cádiz","Cantabria","Castellón","Ceuta",
            "Ciudad Real","Córdoba","Cuenca","Formentera","Girona","Granada","Guadalajara","Guipuzcoa","Huelva","Huesca","Ibiza","Jaén","La Rioja","Las Palmas de Gran Canaria","Gran Canaria",
            "Fuerteventura","Lanzarote","León","Lérida","Lugo","Madrid","Málaga","Mallorca","Menorca","Murcia","Navarra","Orense","Palencia","Pontevedra","Salamanca","Santa Cruz de Tenerife",
            "Tenerife","La Gomera","La Palma","El Hierro","Segovia","Sevilla","Soria","Tarragona","Teruel","Toledo","Valencia","Valladolid","Vizcaya","Zamora","Zaragoza")
        val adapterCiudad = ArrayAdapter(this, R.layout.spinner_list,ciudadesPosibles)
        campoCiudad.adapter=adapterCiudad

        /**
         * Listener para el textView ' Siguiente'
         * Comprobamos que los campos solicitados al usuario son correctos y están completos. En caso de que sean correctos, lanzamos la siguiente actividad
         * En caso contrario, informamos al usuario de los datos incorrectos
         */
        btnContinuar.setOnClickListener(){


            if (campoCiudad.selectedItem.toString().equals("---")){
                var errorCiudad:TextView= campoCiudad.selectedView as TextView
                errorCiudad.error=getString(R.string.ciudadNoValida)
                }
            if(selectorPlataforma.selectedItem.toString().equals("---")){
                var errorPlataforma:TextView=selectorPlataforma.selectedView as TextView
                errorPlataforma.error=getString(R.string.plataformaNoValida)
            }

            else {

                val ciudadUsuario:String = campoCiudad.selectedItem.toString()
                val plataformaUsuario:String = selectorPlataforma.selectedItem.toString()
                spinner.setVisibility(View.VISIBLE)
                val i:Intent = Intent (this, RegActivity3_juegos::class.java)


                ciudadEnviada=ciudadUsuario
                plataformaEnviada=plataformaUsuario

                val animator = ValueAnimator.ofInt(25, 50)
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
                            campoCiudad.setSelection(0)
                            selectorPlataforma.setSelection(0)
                            spinner.progress=25

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
        }
    }
}