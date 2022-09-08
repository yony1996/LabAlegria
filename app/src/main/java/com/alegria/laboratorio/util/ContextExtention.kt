package com.alegria.laboratorio.util

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import java.io.File

fun Context.toast(message:CharSequence)=
    Toast.makeText(this,message,Toast.LENGTH_LONG).show()

fun datePickerFormatter(yy: Int, mm: Int, dd: Int, separator: String = "-"): String {
    val month = if(mm > 9) "$mm" else "0$mm"
    val day = if(dd > 9) "$dd" else "0$dd"
    return "$yy$separator$month$separator$day"
}
fun download(url: String,context: Context) {
    try {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val fileLink = Uri.parse(url)
        val request = DownloadManager.Request(fileLink)

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
            .setMimeType("application/pdf")
            .setAllowedOverRoaming(false)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle("Descarga de Examen")
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                File.separator +"Examen.pdf"
            )
        downloadManager.enqueue(request)
        Toast.makeText(context, "Se ha realizado la descarga", Toast.LENGTH_LONG).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Ocurrio un error", Toast.LENGTH_LONG).show()
    }
}
