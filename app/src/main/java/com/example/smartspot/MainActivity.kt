package com.example.smartspot

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get references to UI elements
        val toggleHotspot = findViewById<Switch>(R.id.toggle_hotspot)
        val deviceListContainer = findViewById<LinearLayout>(R.id.device_list_container)

        // Set switch listener to show/hide WiFi devices
        toggleHotspot.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Switch is ON - Show device list
                deviceListContainer.visibility = LinearLayout.VISIBLE
            } else {
                // Switch is OFF - Hide device list
                deviceListContainer.visibility = LinearLayout.GONE
            }
        }
    }
}
