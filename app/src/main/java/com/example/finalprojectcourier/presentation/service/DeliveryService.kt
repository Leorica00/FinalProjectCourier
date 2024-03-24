package com.example.finalprojectcourier.presentation.service

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavDeepLinkBuilder
import com.example.finalprojectcourier.R
import com.example.finalprojectcourier.presentation.model.location.LocationDelivery
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DeliveryService : Service() {

    private val locationManager: LocationManager by lazy {
        getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private val databaseReference: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().getReference("deliveries/$deliveryId/0")
    }

    private val handler = Handler(Looper.getMainLooper())
    private val notificationChannelId = "delivery_channel"
    private val notificationId = 1

    private var deliveryId: String = ""

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        deliveryId = intent?.getStringExtra("deliveryId") ?: ""
        if (deliveryId.isEmpty()) {
            stopSelf()
            return START_NOT_STICKY
        }

        startLocationUpdates()
        startForeground(notificationId, buildNotification())

        return START_NOT_STICKY
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            notificationChannelId,
            "Delivery Updates",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "Notification for ongoing delivery"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun buildNotification(): Notification {
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            Intent(this, DeliveryCompleteReceiver::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        val navigationIntent = NavDeepLinkBuilder(this)
            .setGraph(R.navigation.nested_home_nav_graph)
            .setDestination(R.id.courierDeliveryMapFragment)
            .createPendingIntent()

        val notificationBuilder = NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle("Delivery in Progress")
            .setContentText("Delivering to...")
            .setSmallIcon(R.drawable.ic_delivery)
            .addAction(R.drawable.ic_check, "Mark Delivered", pendingIntent)
            .setOngoing(true)
            .setContentIntent(navigationIntent)
            .setStyle(NotificationCompat.BigTextStyle())
            .setPriority(NotificationCompat.PRIORITY_LOW)

        return notificationBuilder.build()
    }

    private fun startLocationUpdates() {
        val locationPermissionGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (locationPermissionGranted) {
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                10000,
                0f,
                locationListener
            )
        } else {
            stopSelf()
        }
    }

    private val locationListener = LocationListener { location ->
        val locationData = LocationDelivery(
            true,
            location.latitude,
            location.longitude
        )
        databaseReference.setValue(locationData)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        locationManager.removeUpdates(locationListener)
        stopForeground(STOP_FOREGROUND_REMOVE)    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}

class DeliveryCompleteReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val deliveryService = Intent(context, DeliveryService::class.java)
        context.stopService(deliveryService)
    }
}