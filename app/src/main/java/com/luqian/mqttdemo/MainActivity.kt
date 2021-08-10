package com.luqian.mqttdemo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.luqian.mqtt.*
import com.luqian.mqttdemo.databinding.ActivityMainBinding

@SuppressLint("HardwareIds")
class MainActivity : AppCompatActivity(),
    View.OnClickListener,
    OnMqttMsgListener,
    OnMqttStatusChangeListener {

    private companion object {
        private const val TOPIC_PUB = "/rtc/luqian/android"
        private const val TOPIC_SUB = "/rtc/luqian/server"
    }


    private lateinit var mqttHelper: IMqtt

    lateinit var bind: ActivityMainBinding

    private val mAdapter: MessageAdapter by lazy {
        MessageAdapter()
    }


    private val mAndroidId: String by lazy {
        Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_STATUS_BAR).init()
        bind = DataBindingUtil.setContentView(this, R.layout.activity_main)
        bind.btnSend.setOnClickListener(this)
        bind.txtTitle.text = "通信中..."

        bind.recyclerView.adapter = mAdapter
        val options = MqttOptions(
            serviceUrl = "tcp://broker.hivemq.com:1883",
            username = "admin",
            password = "password",
            clientId = "android-${System.currentTimeMillis()}",
            willTopic = "will/android",
            willMsg = "I'm Died - $mAndroidId"
        )
        mqttHelper = MqttHelper(this, options).apply {
            addOnMsgListener(this@MainActivity)
            addOnStatusChangeListener(this@MainActivity)
            connect()
        }
    }


    override fun onClick(v: View?) {
        if (v?.id == R.id.btnSend) {
            val content: String? = bind.editContent.text?.toString()
            if (content.isNullOrEmpty()) {
                Toast.makeText(this, "请输入要发送的内容", Toast.LENGTH_SHORT).show()
            } else {
                mqttHelper.pubMessage(TOPIC_PUB, content.toByteArray())
                bind.editContent.setText("")
            }
        }
    }


    override fun onSubMessage(topic: String, payload: ByteArray) {
        mAdapter.addMessage(String(payload))
        bind.recyclerView.smoothScrollToPosition(mAdapter.itemCount - 1)
    }


    override fun onPubMessage(payload: ByteArray) {
        mAdapter.addMessage(String(payload), true)
        bind.recyclerView.smoothScrollToPosition(mAdapter.itemCount - 1)
    }


    /**
     *  MQTT 连接状态改变:
     *  [MqttStatus.SUCCESS]    // 连接成功
     *  [MqttStatus.FAILURE]    // 连接失败
     *  [MqttStatus.LOST]       // 连接中断
     */
    @SuppressLint("SetTextI18n")
    override fun onChange(state: MqttStatus, throwable: Throwable?) {
        bind.btnSend.isEnabled = state == MqttStatus.SUCCESS
        bind.editContent.isEnabled = state == MqttStatus.SUCCESS
        if (state == MqttStatus.SUCCESS) {
            mqttHelper.subscribe(TOPIC_SUB)
            bind.txtTitle.text = TOPIC_SUB
        } else {
            bind.txtTitle.text = state.name
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mqttHelper.disConnect()
    }


    override fun onBackPressed() {
//        moveTaskToBack(true)
        val intent = Intent(Intent.ACTION_MAIN)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addCategory(Intent.CATEGORY_HOME)
        startActivity(intent)
    }

}