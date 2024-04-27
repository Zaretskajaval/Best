package com.example.top_top

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner

class PetInformationActivity : AppCompatActivity() {
    private lateinit var spinner: Spinner
    private lateinit var editText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_information)

        // Получаем ссылки на Spinner и EditText из макета
        spinner = findViewById(R.id.spinner)
        editText = findViewById(R.id.editText)
        val spinnerOptions = arrayOf("Option 1", "Option 2", "Option 3")

        val spinner = findViewById<Spinner>(R.id.spinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Настраиваем обработчик выбора элемента в Spinner
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                editText.setText(selectedItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Обработка события, если ничего не выбрано
            }
        }
    }
}
