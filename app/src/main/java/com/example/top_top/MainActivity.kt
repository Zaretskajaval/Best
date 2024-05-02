package com.example.top_top

import SmsReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.annotation.SuppressLint
import android.content.IntentFilter
import android.content.SharedPreferences
import android.location.LocationManager
import android.net.Uri
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


class MainActivity : AppCompatActivity() {
    private lateinit var imageButtonMainPhoto: ImageButton
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var photoChoice: PhotoChoice
    private val buttonsFunctions = ButtonsFunctions()
    private val PICK_IMAGE_REQUEST = 1
    private val SMS_PERMISSION_CODE = 101
    private val LOCATION_PERMISSION_CODE = 123
    private val PERMISSION_REQUEST_CODE = 1001
    private var smsReceiver: SmsReceiver? = null

    companion object {
        const val SMS_PERMISSION_CODE = 101
        const val LOCATION_PERMISSION_CODE = 123
        private const val PERMISSION_REQUEST_CODE = 1001

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        imageButtonMainPhoto = findViewById(R.id.imageButtonMainPhoto)
        photoChoice = PhotoChoice(this, imageButtonMainPhoto)
        imageButtonMainPhoto.setOnClickListener {
            requestStoragePermission()
        }
        // Проверяем, есть ли сохраненное изображение в SharedPreferences
        val imageUriString = sharedPreferences.getString("imageUri", null)
        if (imageUriString != null) {
            val imageUri = Uri.parse(imageUriString)
            loadImageToButton(imageUri)
        }

        imageButtonMainPhoto.setOnClickListener {
            openFileChooser() // Открываем галерею для выбора изображения
        }
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE
        )


        // Проверяем разрешение на получение SMS
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECEIVE_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Если разрешение не предоставлено, запрашиваем его у пользователя
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECEIVE_SMS,Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                SMS_PERMISSION_CODE

            )
        } else {
            // Если разрешение уже предоставлено, инициализируем BroadcastReceiver для приема SMS
            buttonsFunctions.initSmsReceiver(this)
        }



    }


    private fun requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Если разрешение не предоставлено, запрашиваем его у пользователя
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        } else {
            // Если разрешение уже предоставлено, открываем галерею для выбора изображения
            openFileChooser()
        }
    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Выберите изображение"),
            PICK_IMAGE_REQUEST
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            val imageUri: Uri = data.data!!
            loadImageToButton(imageUri)
            // Сохраняем URI изображения в SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putString("imageUri", imageUri.toString())
            editor.apply()
        }
    }
    fun loadImageToButton(imageUri: Uri) {
        // Загружаем и устанавливаем изображение на кнопку
        Glide.with(this)
            .load(imageUri)
            .apply(RequestOptions.circleCropTransform()) // Обрезаем изображение по кругу
            .into(imageButtonMainPhoto)
    }
    override fun onDestroy() {
        super.onDestroy()
        // Отменяем регистрацию ресивера при уничтожении активности
        smsReceiver?.unregister(this)
    }
    fun GetGeoData(view: View) {
        val imageButtonGeoData = findViewById<ImageButton>(R.id.imageButtonGeoData)
        imageButtonGeoData.setImageResource(R.drawable.inner_map_data)
        Handler().postDelayed({
            imageButtonGeoData.setImageResource(R.drawable.geodata)
        }, 500)
        val smsFromTracker = buttonsFunctions.obtainSmsFromTracker()
        buttonsFunctions.getGeoDataFunction(this, smsFromTracker)

    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        buttonsFunctions.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
        when (requestCode) {
            SMS_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    buttonsFunctions.initSmsReceiver(this)
                }
            }
        }
    }


        @SuppressLint("ServiceCast")
        fun VeterinaryClinics(view: View) {
            val imageButtonAmbulance = findViewById<ImageButton>(R.id.imageButtonAmbulance)

            imageButtonAmbulance.setImageResource(R.drawable.inner_ambulance)
            // Задержка на полсекунды
            Handler().postDelayed({
                // Возвращаем первоначальное изображение после задержки
                imageButtonAmbulance.setImageResource(R.drawable.ambulance)
            }, 500)
            // Проверяем, есть ли разрешение на использование местоположения
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Получить текущие координаты устройства
                val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

                if (location != null) {
                    // Открыть новую активити с передачей текущих координат
                    val intent = Intent(this, VeterinaryClinicsMapActivity::class.java)
                    intent.putExtra("latitude", location.latitude)
                    intent.putExtra("longitude", location.longitude)
                    startActivity(intent)
                } else {
                    // Если не удалось получить координаты, вывести сообщение об ошибке
                    Toast.makeText(
                        this,
                        "Не удалось определить текущее местоположение",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                // Если разрешение на использование местоположения не предоставлено, запрашиваем его у пользователя
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_CODE
                )
            }
        }

        fun QuestionButton(view: View) {
            val intent = Intent(this, QuestionActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_to_top)
        }




        fun InformationAboutPat(view: View) {
            val imageButtonInformationAboutPat =
                findViewById<ImageButton>(R.id.imageButtonInfoAboutPat)

            imageButtonInformationAboutPat.setImageResource(R.drawable.inner_information_about_pat)
            // Задержка на полсекунды
            Handler().postDelayed({
                // Возвращаем первоначальное изображение после задержки
                imageButtonInformationAboutPat.setImageResource(R.drawable.informationpat)
            }, 500)
            val intent = Intent(this, PetInformationActivity::class.java)
            startActivity(intent)
        }

        fun SettingsReset(view: View) {
            val imageButtonSettingsReset = findViewById<ImageButton>(R.id.imageButtonSettingsReset)

            imageButtonSettingsReset.setImageResource(R.drawable.inner_reset_settings)
            // Задержка на полсекунды
            Handler().postDelayed({
                // Возвращаем первоначальное изображение после задержки
                imageButtonSettingsReset.setImageResource(R.drawable.settingsreset)
            }, 500)
        }

        fun Binding(view: View) {
            val imageButtonBinding = findViewById<ImageButton>(R.id.imageButtonBinding)

            imageButtonBinding.setImageResource(R.drawable.innerbinding)
            // Задержка на полсекунды
            Handler().postDelayed({
                // Возвращаем первоначальное изображение после задержки
                imageButtonBinding.setImageResource(R.drawable.binding)
            }, 500)
        }

        fun ResetTracker(view: View) {
            val imageButtonResetTracker = findViewById<ImageButton>(R.id.imageButtonResetTracker)

            imageButtonResetTracker.setImageResource(R.drawable.inner_reset_tracker)
            // Задержка на полсекунды
            Handler().postDelayed({
                // Возвращаем первоначальное изображение после задержки
                imageButtonResetTracker.setImageResource(R.drawable.reset)
            }, 500)
        }

        fun MapStory(view: View) {
            val imageButtonMapStory = findViewById<ImageButton>(R.id.imageButtonMapStory)

            imageButtonMapStory.setImageResource(R.drawable.inner_history)
            // Задержка на полсекунды
            Handler().postDelayed({
                // Возвращаем первоначальное изображение после задержки
                imageButtonMapStory.setImageResource(R.drawable.mapstory)
            }, 500)
        }

        fun openMapActivity(context: Context, smsFromTracker: String) {
            // Создаем Intent для открытия MapActivity
            val intent = Intent(context, MapActivity::class.java)
            val patOnMap = smsFromTracker.substring(8)
            intent.putExtra("PAT_ON_MAP", patOnMap)
            context.startActivity(intent)
        }

    fun VetInformation(view: View) {
        val imageButtonVetInformation =
            findViewById<ImageButton>(R.id.imageButtonVetInformation)

        imageButtonVetInformation.setImageResource(R.drawable.innervet)
        // Задержка на полсекунды
        Handler().postDelayed({
            // Возвращаем первоначальное изображение после задержки
            imageButtonVetInformation.setImageResource(R.drawable.vet)
        }, 500)
        val intent = Intent(this, VetInformation::class.java)
        startActivity(intent)
    }


}

