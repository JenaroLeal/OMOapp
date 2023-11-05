package com.example.cenecapp

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth



class MessagesAdapter( private val context: Context, private val messagesAdapterArrayList: ArrayList<msgModelclass>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val ITEM_SEND = 1
    private val ITEM_RECEIVE = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType==ITEM_SEND){
            var view:View = LayoutInflater.from(context).inflate(R.layout.sender_layout,parent,false)
            return SenderViewHolder(view)
        }
        else{
            var view:View=LayoutInflater.from(context).inflate(R.layout.reciver_layout,parent,false)
            return ReceiverViewHolder(view)
        }
       /* return if (viewType == ITEM_SEND) {

            val view = LayoutInflater.from(context).inflate(R.layout.sender_layout, parent, false)
            SenderViewHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.reciver_layout, parent, false)
            ReceiverViewHolder(view)
        }*/
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val messages:msgModelclass = messagesAdapterArrayList[position]

        holder.itemView.setOnLongClickListener {
            AlertDialog.Builder(context)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete this message?")
                .setPositiveButton("Yes") { dialogInterface, _ ->
                    // Handle delete action
                }
                .setNegativeButton("No") { dialogInterface, _ -> dialogInterface.dismiss() }
                .show()
            false
        }
        if (holder is SenderViewHolder) {
            val viewHolder = holder
            viewHolder.msgtxt.text = messages.message

        } else if (holder is ReceiverViewHolder) {
            val viewHolder = holder
            viewHolder.msgtxt.text = messages.message

        }
    }

    override fun getItemCount(): Int {
        return messagesAdapterArrayList.size
    }

    override fun getItemViewType(position: Int): Int {
        val messages:msgModelclass = messagesAdapterArrayList[position]
        if (FirebaseAuth.getInstance().currentUser?.email == messages.senderid) {
            return ITEM_SEND
        } else {
           return  ITEM_RECEIVE
        }
    }

    inner class SenderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val msgtxt: TextView = itemView.findViewById(R.id.msgsendertyp)
    }

    inner class ReceiverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val msgtxt: TextView = itemView.findViewById(R.id.recivertextset)
    }
}