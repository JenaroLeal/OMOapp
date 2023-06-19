package clases

import android.os.Parcel
import android.os.Parcelable
import java.util.Random

/**
 * Clase que nos permite crear diferentes usuarios
 * @param nombre : el nombre del usuario
 * @param email : el email del usuario
 * @param password : el password del usuario
 * @param ciudad : la ciudad del usuario
 * @param plataforma : la plataforma del usuario
 * @param juego1 : el juego 1 del usuario
 * @param jeugo2 : el juego 2 del usuario
 * @param juego3 : el juego 3 del usuario
 * @param juego4 : el juego 4 del usuario
 * @param juego5 : el juego 5 del usuario
 * @param imagenPerfil : la imagen del usuario. Se guarda en formato string para poder recuperarla facilmente de firebase
 * @param biografia : la biografia  del usuario
 * @param usuarioPlus : si el usuario ha adquirido el servicio plus
 * @param afinidad : formula matematica que nos da de resultado el % de afinidad que tenemos con cada usuario
 * @param usuariosDeseados : los usuarios que queremos conectar
 * @param usuariosQueQuierenConectar : usuarios que nos quieren coenctar
 * @param amigos : usuarios que han coincidido en que queremos conectar y quieren conectar
 */
class Usuario :Parcelable {

    var nombre:String?
    var email:String?
    var password:String?
    var ciudad:String?
    var plataforma:String?
    var juegos:ArrayList<String>
    var imagenPerfil:String?
    var biografia:String?
    var usuarioPlus:Boolean=false
    var afinidad:Int=0
    var usuariosDeseados:ArrayList<String>
    var usuariosQueQuierenConectar:ArrayList<String>
    var amigos:ArrayList<String>
    var usuariosRechazados:ArrayList<String>
    var usuariosBloqueados:ArrayList<String>


    constructor(parcel: Parcel) : this() {
        nombre = parcel.readString()
        email = parcel.readString()
        password = parcel.readString()
        ciudad = parcel.readString()
        plataforma=parcel.readString()
        juegos= parcel.readArrayList(Usuario::class.java.classLoader) as ArrayList<String>
        imagenPerfil = parcel.readString()
        biografia=parcel.readString()
        usuarioPlus = parcel.readByte() != 0.toByte()
        usuariosDeseados= parcel.readArrayList(Usuario::class.java.classLoader) as ArrayList<String>
        usuariosQueQuierenConectar=parcel.readArrayList(Usuario::class.java.classLoader) as ArrayList<String>
        amigos=parcel.readArrayList(Usuario::class.java.classLoader) as ArrayList<String>
        usuariosRechazados=parcel.readArrayList(Usuario::class.java.classLoader) as ArrayList<String>
        usuariosBloqueados=parcel.readArrayList(Usuario::class.java.classLoader) as ArrayList<String>
        afinidad=parcel.readInt()
    }


    constructor(
        nombre:String, email:String, password:String, ciudad: String?, plataforma:String?,juegos:ArrayList<String>,
        imagenPerfil:String, biografia:String, usuarioplus:Boolean, usuariosDeseados: ArrayList<String>?,
        usuariosQueQuierenConectar: ArrayList<String>?,
        amigos: ArrayList<String>?, usuariosRechazados:ArrayList<String>,usuariosBloqueados:ArrayList<String>, afinidad:Int):this(){

        this.nombre=nombre
        this.email=email
        this.password=password
        this.ciudad=ciudad
        this.plataforma=plataforma
        this.juegos=juegos!!
        this.imagenPerfil=imagenPerfil
        this.biografia=biografia
        this.usuarioPlus=usuarioplus
        this.usuariosDeseados= usuariosDeseados!!
        this.usuariosQueQuierenConectar=usuariosQueQuierenConectar!!
        this.amigos=amigos!!
        this.usuariosRechazados=usuariosRechazados!!
        this.usuariosBloqueados=usuariosBloqueados!!
        this.afinidad=afinidad

    }

    /**
     * Construcotr vacio que nos permite crear usuarios con datos aleatorios
     * Actualemnte, este constructor está en des-uso
     */
    constructor(){
        val random:Random=Random()
        val nombresPosibles= arrayOf<String>("Jose Luis","Alex","Leti","Marcos","Alexis","Juan","Antonio","Abel","Joaquin","Pablo","Cristian","Jose","Ignacio","Juanmi","Juanjo","Carlos","Raul","Jero","Miguel","David","Marta","Sandra","Clara","Sonia","Ana","María","Carmen","Conchi")
        val apellidosPosibles= arrayOf<String>("Corral","López","García","Riquelme","Tizu","Carmona","Asencio","Cocargeanu","Moreno","Oña","Rosas","Aranda","Medina","Alpresa","Martín","Páramos")
        val juegosPosibles= arrayOf<String>("Age of Empires","Among Us","Apex Legends","Arena of Valor","Atlas","BattleBlock Theater","Battlefield",
            "Borderlands","Brawl Stars","Call of Duty","Call of Duty - Warzone","Castle Crashers","Chivalry","Clash Royale","Counter Strike","CupHead",
            "Dauntless","Dead by Daylight","Deadheads", "Destiny","Diablo","Divinity","DOOM","Dota 2","Dragon Ball FighterZ","Dragon Ball Legends","Elden Ring"
            ,"Evolve","F1","Fall Guys","Fallout 76","FIFA","Final Fantasy","Fortnite","Free Fire", "Gears of War","Genshin Impact","Ghost Recon","Gigantic",
            "Grand Theft Auto Online","Grounded","H1Z1","Halo","Hearthstone","Hyperscape","It takes two","Journey to the Savage Planet", "Knockout City","League of Legends"
            ,"Lego","Lost Ark","Mario Karts","Mario Strikers","Mario Tennis","Minecraft","Monopoly","Monster Hunter","Muck","NBA","Overcooked","Overwatch", "Path of Exile",
            "Plants vs Zombies","Pokemon","Predator: Hunting Grounds","PUBG Mobile","Rainbow Six","Roblox","Rocket League","Rust","Sea of Thieves","Sky Noon","SoulCalibur",
            "Splatoon","Street Fighter","Super Smash Bros. Ultimate", "Tabletop SImulator","Tekken","Temtem","Tetris 99","The Division","The Elder Scrolls Online","Titanfall",
            "Turbo Golf Racing", "Unravel Two","Valorant","Vampire: The Masquerade-Bloodhunt","Warframe","World of Tanks","World of Warships","Worms","Yu-Gi-Oh!")
        val plataformasPosibles = arrayOf<String>("PS5","PS4","PC","Nintendo","Xbox","Movil")

        this.nombre=nombresPosibles[random.nextInt(nombresPosibles.size)]
        this.email= nombre!!.replace(" ","")+"."+"@gmail.com"
        this.password= email!!.replace("@gmail.com","123")
        this.ciudad="Malaga"
        this.plataforma=plataformasPosibles[random.nextInt(plataformasPosibles.size)]
        this.juegos=ArrayList<String>()
        this.biografia="Esta es una biografía predeterminada para comprobar que estamos recogiendo bien los datos de los usuarios"
        this.imagenPerfil="NONE"
        this.usuarioPlus=false
        this.usuariosDeseados= ArrayList<String>()
        this.usuariosQueQuierenConectar=ArrayList<String>()
        this.amigos=ArrayList<String>()
        this.usuariosRechazados=ArrayList<String>()
        this.usuariosBloqueados=ArrayList<String>()
        this.afinidad=0



    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nombre)
        parcel.writeString(email)
        parcel.writeString(password)
        parcel.writeString(ciudad)
        parcel.writeString(plataforma)
        parcel.writeList(juegos)
        parcel.writeString(imagenPerfil)
        parcel.writeString(biografia)
        parcel.writeByte(if (usuarioPlus) 1 else 0)
        parcel.writeArray(arrayOf(usuariosDeseados))
        parcel.writeArray(arrayOf(usuariosQueQuierenConectar))
        parcel.writeArray(arrayOf(amigos))
        parcel.writeArray(arrayOf(usuariosRechazados))
        parcel.writeArray(arrayOf(usuariosBloqueados))
        parcel.writeInt(afinidad)
    }

    /**
    Devuelve un valor entero que describe el contenido de este objeto.
    Este método es necesario para implementar la interfaz Parcelable.
    @return Un valor entero de 0.
     */
    override fun describeContents(): Int {
        return 0
    }

    /**
    Objeto complementario que implementa la interfaz Parcelable.Creator
    para crear y devolver un nuevo objeto Usuario de un paquete dado,
    o una matriz de objetos Usuario con el tamaño especificado.
     */

    companion object CREATOR : Parcelable.Creator<Usuario> {
        /**

        Crea y devuelve un nuevo objeto Usuario del Parcel dado.
        @param parcela El objeto Parcela que contiene los datos para el nuevo objeto Usuario.
        @return Un nuevo objeto Usuario creado a partir de los datos de la Parcela.
         */
        override fun createFromParcel(parcel: Parcel): Usuario {
            return Usuario(parcel)
        }
        /**

        Crea y devuelve una matriz de objetos Usuario con el tamaño especificado.
        @param size El tamaño de la matriz a crear.
        @return Una matriz de objetos Usuario con el tamaño especificado.
         */

        override fun newArray(size: Int): Array<Usuario?> {
            return arrayOfNulls(size)
        }
    }

    /**

    Devuelve una representación de cadena de texto de este objeto.
    @return Una cadena que contiene los valores de juego1, juego2, juego3, juego4 y juego5.
     */


}