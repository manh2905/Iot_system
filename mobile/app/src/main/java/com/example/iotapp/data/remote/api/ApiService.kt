package com.example.iotapp.data.remote.api

import com.example.iotapp.data.remote.dto.DeviceDto
import com.example.iotapp.data.remote.dto.PaginatedSensorResponse
import com.example.iotapp.data.remote.dto.SensorListResponse
import com.example.iotapp.data.remote.dto.PaginatedActionResponse
import com.example.iotapp.data.remote.dto.DeviceLatestStatusResponse
import com.example.iotapp.data.remote.dto.DeviceListResponse
import okhttp3.Request
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {

    @POST("api/devices/action")
    suspend fun toggleDevice(
        @Body request: DeviceDto
    ): Response<Unit>

    @GET("api/sensors/data/sensor/{sensorId}")
    suspend fun getSensorData(
        @Path("sensorId") sensorId: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("sortBy") sortBy: String,
        @Query("sortOrder") sortOrder: String,
        @Query("date") date: String?,
        @Query("value") value: Double?
    ): Response<PaginatedSensorResponse>

    @GET("api/sensors")
    suspend fun getSensors(): Response<SensorListResponse>

    @GET("api/devices")
    suspend fun getDevices(): Response<DeviceListResponse>

    @GET("api/devices/latest-status")
    suspend fun getLatestDeviceStatus(): Response<DeviceLatestStatusResponse>

    @GET("api/devices/action/device/{deviceId}")
    suspend fun getActionHistory(
        @Path("deviceId") deviceId: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Query("limit") limit: Int,
        @Query("action") action: String?,
        @Query("status") status: String?,
        @Query("sortOrder") sortOrder: String,
        @Query("date") date: String?
    ): Response<PaginatedActionResponse>

    @GET("api/devices/action/count-by-day")
    suspend fun getActionCountByDay(
        @Query("date") date: String
    ): Response<com.example.iotapp.data.remote.dto.DeviceActionCountResponse>
    
}
