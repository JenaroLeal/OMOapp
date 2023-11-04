package com.example.cenecapp

class msgModelclass {
    lateinit var message: String
   lateinit var senderid: String
    var timeStamp: Long = System.currentTimeMillis()

    constructor(message:String,senderid:String,time:Long):this(){
        this.message=message
        this.senderid=senderid
        this.timeStamp=time
    }

    constructor()

}