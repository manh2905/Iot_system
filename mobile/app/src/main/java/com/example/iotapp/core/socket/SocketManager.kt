package com.example.iotapp.core.socket

import android.util.Log
import androidx.compose.ui.unit.Constraints
import com.example.iotapp.core.utils.Constants
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object SocketManager {

    private lateinit var socket: Socket
    private val _sensorData = MutableStateFlow<JSONObject?>(null);
    val sensorData : StateFlow<JSONObject?> = _sensorData

    private val _devidestatus = MutableStateFlow<Pair<String, String>?>(null)
    val deviceStatus : StateFlow<Pair<String, String>?> = _devidestatus

    fun connect() {
        try {
            socket = IO.socket(Constants.SOCKET_URL)

            socket.connect()

            socket.on(Socket.EVENT_CONNECT) {
                Log.d("Socket", "Connected")
            }

            socket.on(Constants.EVENT_SENSOR) { args ->
                if (args.isNotEmpty()) {
                    val data = args[0] as JSONObject
                    Log.d("Socket", "Sensor: $data")

                    _sensorData.value = data
                }
            }

            socket.on(Constants.EVENT_STATUS) { args ->
                if(args.isNotEmpty()) {
                    val json = args[0] as JSONObject

                    val deviceId = json.optString("deviceId")
                    val status = json.optString("status")

                    _devidestatus.value = deviceId to status
                }
            }

            socket.on(Socket.EVENT_DISCONNECT) {
                Log.d("Socket", "Disconnected")
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun disconnect() {
        socket.disconnect()
    }



}