package com.example.cenecapp

class msgModelclass {
    var message: String? = null
    var senderid: String? = null
    var timeStamp: Long = 0

    fun msgModelclass() {}

    fun msgModelclass(message: String?, senderid: String?, timeStamp: Long) {
        this.message = message
        this.senderid = senderid
        this.timeStamp = timeStamp
    }



}