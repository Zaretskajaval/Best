package com.example.top_top

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.ImageButton
import android.widget.Toast
import android.widget.Toast.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import android.Manifest



class PhotoChoice(private val activity: AppCompatActivity, private val imageButton: ImageButton) {

    private val PICK_IMAGE_REQUEST = 1
    private val PERMISSION_REQUEST_CODE = 1001

    init {
        imageButton.setOnClickListener {
            openFileChooser()
        }
    }

    private fun openFileChooser() {
        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Если разрешение не предоставлено, запрашиваем его у пользователя
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        } else {
            // Если разрешение уже предоставлено, открываем галерею для выбора изображения
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            activity.startActivityForResult(
                Intent.createChooser(intent, "Выберите изображение"),
                PICK_IMAGE_REQUEST
            )
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val imageUri: Uri = data.data!!
            Glide.with(activity)
                .load(imageUri)
                .apply(RequestOptions.circleCropTransform()) // Обрезаем изображение по кругу
                .into(imageButton)
        }
    }
}
