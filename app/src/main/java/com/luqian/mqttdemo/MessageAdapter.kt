package com.luqian.mqttdemo

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IdRes
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
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.item_message,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tvSender = holder.findView<TextView>(R.id.tv_sender)
        val ivSender = holder.findView<ImageView>(R.id.iv_sender)
        val tvOther = holder.findView<TextView>(R.id.tv_other)
        val ivOther = holder.findView<ImageView>(R.id.iv_other)

        tvSender.visibility = View.GONE
        tvOther.visibility = View.GONE
        ivSender.visibility = View.GONE
        ivOther.visibility = View.GONE

        if (mDataList[position].isPublish) {
            tvSender.visibility = View.VISIBLE
            ivSender.visibility = View.VISIBLE
            tvSender.run {
                text = mDataList[position].message
                setTextColor(Color.parseColor("#00AAAA"))
            }
        } else {
            tvOther.visibility = View.VISIBLE
            ivOther.visibility = View.VISIBLE
            tvOther.run {
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