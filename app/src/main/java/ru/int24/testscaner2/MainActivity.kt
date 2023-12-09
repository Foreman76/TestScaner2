package ru.int24.testscaner2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var myView: TextView
    private val TAG = "IntentApiSample"
    private val ACTION_BARCODE_DATA = "com.honeywell.sample.action.BARCODE_DATA"
    private val ACTION_CLAIM_SCANNER = "com.honeywell.aidc.action.ACTION_CLAIM_SCANNER"
    private val ACTION_RELEASE_SCANNER = "com.honeywell.aidc.action.ACTION_RELEASE_SCANNER"
    private val EXTRA_SCANNER = "com.honeywell.aidc.extra.EXTRA_SCANNER"
    private val EXTRA_PROFILE = "com.honeywell.aidc.extra.EXTRA_PROFILE"
    private val EXTRA_PROPERTIES = "com.honeywell.aidc.extra.EXTRA_PROPERTIES"
    private lateinit var myReceiver: BroadcastReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        myView = findViewById(R.id.scanText)
        initReceiver()
    }


    private fun initReceiver() {
        myReceiver = object: BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                if (ACTION_BARCODE_DATA.equals(p1?.getAction())) {
                    val version = p1?.getIntExtra("version", 0)
                    version?.let { it ->
                        if (it >= 1) {

//                            val aimId = p1.getStringExtra("aimId")
//                            val charset = p1.getStringExtra("charset")
//                            val codeId = p1.getStringExtra("codeId")
                            val data = p1.getStringExtra("data")
//                            val dataBytes = p1.getByteArrayExtra("dataBytes")
//                            val dataBytesStr: String? = bytesToHexString(dataBytes)
//                            val timestamp = p1.getStringExtra("timestamp")

//                            val text = java.lang.String.format(
//                                """
//                                      Data:%s
//                                      Charset:%s
//                                      Bytes:%s
//                                      AimId:%s
//                                      CodeId:%s
//                                      Timestamp:%s
//
//                                      """.trimIndent(),
//                                data, charset, dataBytesStr, aimId, codeId, timestamp
//                            )
                            data?.let { datanotnull ->
                                setText(datanotnull)
                            }
                            setText(data!!)


                        }
                    }

                    }

                }
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(myReceiver)
        releaseScanner()

    }
    override fun onResume() {
        super.onResume()
        val myintentFilter = IntentFilter()
        myintentFilter.addAction(ACTION_BARCODE_DATA)
        registerReceiver(myReceiver, myintentFilter)
        claimScanner();

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(myReceiver)
        releaseScanner()
    }
    private fun releaseScanner() {
        sendBroadcast(
            Intent(ACTION_RELEASE_SCANNER)
                .setPackage("com.intermec.datacollectionservice")
        )
    }

    private fun claimScanner() {
        val properties = Bundle()
        properties.putBoolean("DPR_DATA_INTENT", true)
        properties.putString("DPR_DATA_INTENT_ACTION", ACTION_BARCODE_DATA)
        sendBroadcast(
            Intent(ACTION_CLAIM_SCANNER)
                .setPackage("com.intermec.datacollectionservice")
                .putExtra(EXTRA_SCANNER, "dcs.scanner.imager")
                .putExtra(EXTRA_PROFILE, "MyProfile1")
                .putExtra(EXTRA_PROPERTIES, properties)
        )
    }
    private fun bytesToHexString(arr: ByteArray?): String? {
        var s = "[]"
        if (arr != null) {
            s = "["
            for (i in arr.indices) {
                s += "0x" + Integer.toHexString(arr[i].toInt()) + ", "
            }
            s = s.substring(0, s.length - 2) + "]"
        }
        return s
    }
    private fun setText(text: String) {
            runOnUiThread { myView.setText(text) }

    }
}