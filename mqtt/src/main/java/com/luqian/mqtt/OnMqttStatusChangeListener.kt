package com.luqian.mqtt

interface OnMqttStatusChangeListener {

    fun onChange(state: MqttStatus, throwable: Throwable?)

}