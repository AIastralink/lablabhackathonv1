package com.example.smartspot

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var wifiManager: WifiManager
    private lateinit var toggleHotspot: Switch
    private lateinit var deviceListContainer: LinearLayout
    private lateinit var scanWifiButton: Button
    private lateinit var wifiListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize WiFi Manager
        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        // Get references to UI elements
        toggleHotspot = findViewById(R.id.toggle_hotspot)
        deviceListContainer = findViewById(R.id.device_list_container)
        scanWifiButton = findViewById(R.id.scan_button) // Ensure this ID exists in XML
        wifiListView = findViewById(R.id.wifi_list_view) // Ensure this ID exists in XML

        // Toggle visibility of device list
        toggleHotspot.setOnCheckedChangeListener { _, isChecked ->
            deviceListContainer.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        // Scan WiFi button click listener
        scanWifiButton.setOnClickListener {
            checkPermissionsAndScanWifi()
        }

        // WiFi network selection handling
        wifiListView.setOnItemClickListener { _, _, position, _ ->
            val selectedSSID = wifiListView.adapter.getItem(position) as String
            connectToWifi(selectedSSID, "your_password_here") // Replace with user input
        }
    }

    private fun checkPermissionsAndScanWifi() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
        } else {
            scanWifiNetworks()
        }
    }

    private fun scanWifiNetworks() {
        registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val results = wifiManager.scanResults
                showWifiList(results)
            }
        }, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))

        wifiManager.startScan()
        Toast.makeText(this, "Scanning WiFi...", Toast.LENGTH_SHORT).show()
    }

    private fun showWifiList(results: List<ScanResult>) {
        val wifiNames = results.map { it.SSID }.filter { it.isNotEmpty() }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, wifiNames)
        wifiListView.adapter = adapter
    }

    // Function to connect to a WiFi network
    private fun connectToWifi(ssid: String, password: String) {
        val wifiConfig = WifiConfiguration().apply {
            SSID = "\"$ssid\""
            preSharedKey = "\"$password\""
        }

        val netId = wifiManager.addNetwork(wifiConfig)
        wifiManager.disconnect()
        wifiManager.enableNetwork(netId, true)
        wifiManager.reconnect()

        Toast.makeText(this, "Connecting to $ssid...", Toast.LENGTH_SHORT).show()
    }
}
