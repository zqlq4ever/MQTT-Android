package com.luqian.mqttdemo

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.RecyclerView

class MessageAdapter : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    private val mDataList = ArrayList<Message>()

    fun addMessage(message: String, isPublish: Boolean = false) {
        if (message.isNotEmpty() && message.isNotBlank()) {
            mDataList.add(Message(message, isPublish))
            notifyItemInserted(mDataList.size - 1)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = View.inflate(parent.context, R.layout.item_message, null)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tv_sender = holder.findView<TextView>(R.id.tv_sender)
        val tv_other = holder.findView<TextView>(R.id.tv_other)
        val sender = holder.findView<Group>(R.id.sender)
        val other = holder.findView<Group>(R.id.other)

        sender.visibility = View.GONE
        other.visibility = View.GONE
        if (mDataList[position].isPublish) {
            sender.visibility = View.VISIBLE
            tv_sender.run {
                text = mDataList[position].message
                setTextColor(Color.parseColor("#00AAAA"))
            }
        } else {
            other.visibility = View.VISIBLE
            tv_other.run {
                text = mDataList[position].message
                setTextColor(Color.parseColor("#666666"))
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun <T : View> findView(@IdRes vId: Int): T {
            return itemView.findViewById(vId)
        }

    }

}