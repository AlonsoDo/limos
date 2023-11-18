package com.example.limos

//import android.R
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONArray
import org.json.JSONObject
import java.net.URISyntaxException
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var mSocket: Socket
    lateinit var itemGRV: GridView
    lateinit var itemList: List<GridViewModal>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        itemGRV = findViewById(R.id.idGRV)
        itemList = ArrayList<GridViewModal>()

        itemList = itemList + GridViewModal("C++")

        val itemAdapter = GridRVAdapter(itemList = itemList, this@MainActivity)

        itemGRV.adapter = itemAdapter

        itemGRV.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            Toast.makeText(
                applicationContext, itemList[position].itemText + " selected",
                Toast.LENGTH_SHORT
            ).show()
        }

        try {
            mSocket = IO.socket("http://192.168.1.36:3000/")
        } catch (e: URISyntaxException) {
        }

        mSocket.connect()
        mSocket.emit("LoadIni")

        mSocket.on("Test") { args ->
            runOnUiThread {
                val data = args[0].toString()
                val jsonarray = JSONArray(data)

                val json = JSONObject(jsonarray[0].toString())
                val elementoId = json.getInt("ElementoId")

                val tv2: TextView = findViewById(R.id.text1)
                tv2.text = elementoId.toString()
            }
        }
    }

    fun onClickButton2(view: View?) {
        val tv3: TextView = findViewById(R.id.text1)
        tv3.text = "Test4"
    }



}