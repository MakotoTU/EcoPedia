package com.makoto.ecopedia.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.makoto.ecopedia.MainActivity
import com.makoto.ecopedia.R

class EcoTipsWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        // Daftar tips ramah lingkungan
        val ecoTips = listOf(
            "Tahukah kamu? Botol plastik butuh 450 tahun untuk terurai. Yuk kurangi pemakaiannya hari ini!",
            "Bawa tas belanja sendiri saat ke minimarket bisa menyelamatkan ratusan kantong plastik lho.",
            "Jangan buang baterai sembarangan ya! Baterai bekas termasuk sampah B3 (Bahan Berbahaya dan Beracun).",
            "Pisahkan sampah organik dan anorganik di rumah untuk memudahkan proses daur ulang.",
            "Air sisa cucian beras sangat bagus untuk menyiram tanaman lho. Yuk manfaatkan!",
            "Sering belanja online? Kumpulkan kardus bekasnya dan berikan ke bank sampah atau pemulung."
        )

        // Pilih tip secara acak
        val randomTip = ecoTips.random()

        showNotification("Tips EcoPedia Hari Ini \uD83C\uDF3F", randomTip)

        return Result.success()
    }

    private fun showNotification(title: String, message: String) {
        val channelId = "ecopedia_tips_channel"
        val notificationId = 1001

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Buat Notification Channel untuk Android 8.0 (Oreo) ke atas
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Notifikasi Edukasi & Tips",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Channel untuk tips harian ramah lingkungan dari EcoPedia"
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Intent untuk membuka aplikasi saat notifikasi di-tap
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Bangun notifikasi
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.leaf) // Menggunakan logo leaf sebagai icon
            .setColor(ContextCompat.getColor(context, R.color.primary_green))
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message)) // Supaya text panjang bisa dibaca semua
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // Tampilkan notifikasi
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}
