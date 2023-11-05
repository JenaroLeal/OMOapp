package com.example.cenecapp

class msgModelclass {
    lateinit var message: String
    lateinit var senderid: String
    lateinit var receiverid:String
    var timeStamp: Long = System.currentTimeMillis()

    constructor(message:String,senderid:String,receiverid:String,time:Long):this(){
        this.message=message
        this.senderid=senderid
        this.receiverid=receiverid
        this.timeStamp=time
    }

    constructor()

}