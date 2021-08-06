package com.luqian.mqttdemo

data class Message(
    val message: String = "",
    val isPublish: Boolean = false
)