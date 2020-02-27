package com.whatsdue.whatsdue2

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.whatsdue.whatsdue2.beacon.RadBeacon
import kotlinx.android.synthetic.main.activity_beacon.*

class BeaconActivity : AppCompatActivity() {

    private val TAG = "BeaconActivity"

    private lateinit var bluetoothAdapter: BluetoothAdapter

    private val BluetoothAdapter.isDisabled: Boolean get() = !isEnabled

    lateinit var scanner: BluetoothLeScanner

    private var beacons = hashMapOf<String, RadBeacon>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beacon)
        beaconList.adapter =
            BeaconAdapter(this, beacons.values)
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        scanner = bluetoothAdapter.bluetoothLeScanner

        startScan()

    }

    override fun onResume() {
        super.onResume()

        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "NO LE SUPPORT!", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // Start Scanning
        startScan()

    }

    override fun onPause() {
        super.onPause()
        stopScan()
    }


    private fun startScan() {
//        val beaconFilter = ScanFilter.Builder()
//            .setServiceUuid(RadBeacon.BEACON_UUID)
//            .build()
//
//        var filters = arrayListOf<ScanFilter>()
//        filters.add(beaconFilter)
//
//        var settings = ScanSettings.Builder()
//            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
//            .build()
//        scanner.startScan(filters, settings, scanCallback)

        scanner.startScan(scanCallback)
    }

    private fun stopScan() = scanner.stopScan(scanCallback)

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            Log.d(TAG, "onScanResult")
            processResult(result!!)
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            super.onBatchScanResults(results)
            Log.d(TAG, "onBatchScanResults: ${results?.size} results")
            results?.forEach(::processResult)
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            Log.w(TAG, "BLE Scan failed: $errorCode")
        }

        private fun processResult(scanResult: ScanResult) {
            Log.i(TAG, "New BLE Device: ${scanResult.device.name} @ ${scanResult.rssi}")

            val beacon = RadBeacon(scanResult.scanRecord!!, scanResult.device.address,
                scanResult.rssi)

            bleHandler.sendMessage(Message.obtain(null, 0, beacon))
        }
    }


    private val bleHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            val beacon = msg.obj as RadBeacon
            beacons.put(beacon.name!!, beacon)

        }
    }

    class BeaconAdapter(context: Context, list: MutableCollection<RadBeacon>) :
        ArrayAdapter<RadBeacon>(context, android.R.layout.simple_list_item_1) {

        init {
            super.addAll(list)
        }


    }



}