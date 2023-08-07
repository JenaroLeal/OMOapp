package com.example.cenecapp

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.view.get
import com.example.cenecapp.databinding.ActivityRegActivity3JuegosBinding

/**
 * Clase que nos permite registrar un usuario. Esta es la 3/4 donde recogemos datos del usuario.
 * Los datos recogidos son los diferentes juegos
 */
class RegActivity3_juegos : AppCompatActivity() {
    lateinit var binding: ActivityRegActivity3JuegosBinding

    /**
     * Método que nos permite almacenar y pasar la información a traves de un companion
     */
    companion object {
        var juegosEnviados: ArrayList<String> = arrayListOf()
        var juego1e = ""
        var juego2e = ""
        var juego3e = ""
        var juego4e = ""
        var juego5e = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityRegActivity3JuegosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.spinner3.setVisibility(View.INVISIBLE)
        val juegos = arrayOf<String>(
            "---",
            "Age of Empires",
            "Among Us",
            "Apex Legends",
            "Arena of Valor",
            "Atlas",
            "BattleBlock Theater",
            "Battlefield",
            "Borderlands",
            "Brawl Stars",
            "Call of Duty",
            "Call of Duty - Warzone",
            "Castle Crashers",
            "Chivalry",
            "Clash Royale",
            "Counter Strike",
            "CupHead",
            "Dauntless",
            "Dead by Daylight",
            "Deadheads",
            "Destiny",
            "Diablo",
            "Divinity",
            "DOOM",
            "Dota 2",
            "Dragon Ball FighterZ",
            "Dragon Ball Legends",
            "Elden Ring",
            "Evolve",
            "F1",
            "Fall Guys",
            "Fallout 76",
            "FIFA",
            "Final Fantasy",
            "Fortnite",
            "Free Fire",
            "Gears of War",
            "Genshin Impact",
            "Ghost Recon",
            "Gigantic",
            "Grand Theft Auto Online",
            "Grounded",
            "H1Z1",
            "Halo",
            "Hearthstone",
            "Hyperscape",
            "It takes two",
            "Journey to the Savage Planet",
            "Knockout City",
            "League of Legends",
            "Lego",
            "Lost Ark",
            "Mario Karts",
            "Mario Strikers",
            "Mario Tennis",
            "Minecraft",
            "Monopoly",
            "Monster Hunter",
            "Muck",
            "NBA",
            "Overcooked",
            "Overwatch",
            "Path of Exile",
            "Plants vs Zombies",
            "Pokemon",
            "Predator: Hunting Grounds",
            "PUBG Mobile",
            "Rainbow Six",
            "Roblox",
            "Rocket League",
            "Rust",
            "Sea of Thieves",
            "Sky Noon",
            "SoulCalibur",
            "Splatoon",
            "Street Fighter",
            "Super Smash Bros. Ultimate",
            "Tabletop SImulator",
            "Tekken",
            "Temtem",
            "Tetris 99",
            "The Division",
            "The Elder Scrolls Online",
            "Titanfall",
            "Turbo Golf Racing",
            "Unravel Two",
            "Valorant",
            "Vampire: The Masquerade-Bloodhunt",
            "Warframe",
            "World of Tanks",
            "World of Warships",
            "Worms",
            "Yu-Gi-Oh!"
        )

        val adapter = ArrayAdapter(this, R.layout.spinner_list, juegos)
        binding.juego1.adapter = adapter
        binding.juego2.adapter = adapter
        binding.juego3.adapter = adapter
        binding.juego4.adapter = adapter
        binding.juego5.adapter = adapter

        var losJuegos = ArrayList<String>()


        binding.btn4.setOnClickListener() {


            if (binding.juego1.selectedItem.toString().equals("---")) {
                var errorJuego: TextView = binding.juego1.selectedView as TextView
                errorJuego.error = getString(R.string.juegoInvalido)
            } else if (binding.juego2.selectedItem.toString().equals("---")) {
                var errorJuego: TextView = binding.juego2.selectedView as TextView
                errorJuego.error = getString(R.string.juegoInvalido)
            } else if (binding.juego3.selectedItem.toString().equals("---")) {
                var errorJuego: TextView = binding.juego3.selectedView as TextView
                errorJuego.error = getString(R.string.juegoInvalido)
            } else if (binding.juego4.selectedItem.toString().equals("---")) {
                var errorJuego: TextView = binding.juego4.selectedView as TextView
                errorJuego.error = getString(R.string.juegoInvalido)
            } else if (binding.juego5.selectedItem.toString().equals("---")) {
                var errorJuego: TextView = binding.juego5.selectedView as TextView
                errorJuego.error = getString(R.string.juegoInvalido)
            } else {

                losJuegos.add(binding.juego1.selectedItem.toString())
                losJuegos.add(binding.juego2.selectedItem.toString())
                losJuegos.add(binding.juego3.selectedItem.toString())
                losJuegos.add(binding.juego4.selectedItem.toString())
                losJuegos.add(binding.juego5.selectedItem.toString())

                juego1e = binding.juego1.selectedItem.toString()
                juego2e = binding.juego2.selectedItem.toString()
                juego3e = binding.juego3.selectedItem.toString()
                juego4e = binding.juego4.selectedItem.toString()
                juego5e = binding.juego5.selectedItem.toString()

                if (checkDuplicates(losJuegos)) {
                    Toast.makeText(this, "juego repetido", Toast.LENGTH_LONG).show()
                    losJuegos.clear()
                } else {
                    juegosEnviados = losJuegos
                    val cambio: Intent = Intent(this, RegActivity_fotos::class.java)


                    binding.spinner3.setVisibility(View.VISIBLE)
                    val animator = ValueAnimator.ofInt(50, 75)

                    animator.apply {
                        duration = 1000 // 3 seconds
                        addUpdateListener { valueAnimator ->
                            val value = valueAnimator.animatedValue as Int
                            binding.spinner3.progress = value
                        }

                        addListener(object : Animator.AnimatorListener {
                            override fun onAnimationStart(animator: Animator) {}
                            override fun onAnimationEnd(animator: Animator) {
                                startActivity(cambio)
                            }

                            override fun onAnimationCancel(p0: Animator) {}
                            override fun onAnimationRepeat(p0: Animator) {}
                        })
                    }
                    animator.start()
                }
            }
        }
    }

    fun checkDuplicates(list: List<String>): Boolean {
        val duplicates = mutableListOf<String>()
        var hasDuplicates = false

        for (i in 0 until list.size) {
            for (j in i + 1 until list.size) {
                if (list[i] == list[j] && !duplicates.contains(list[i])) {
                    duplicates.add(list[i])
                    hasDuplicates = true
                }
            }
        }

        return hasDuplicates
    }


}