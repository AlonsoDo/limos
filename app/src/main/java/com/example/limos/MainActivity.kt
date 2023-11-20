package com.example.limos

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
    lateinit var itemTV: TextView
    //private var itemAdapter = GridRVAdapter(itemList = itemList, this@MainActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        itemGRV = findViewById(R.id.idGRV)
        itemList = ArrayList<GridViewModal>()

        itemGRV.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

            Toast.makeText(
                applicationContext, itemList[position].itemText + " selected",
                Toast.LENGTH_LONG
            ).show()

            val tv2: TextView = findViewById(R.id.text1)
            tv2.text = itemList[position].elementoId.toString()

            var nPos = itemList[position].elementoId.toString()
            itemGRV.adapter = null
            (itemList as ArrayList<GridViewModal>).clear()

            mSocket.emit("LoadElements",nPos)
        }

        try {
            mSocket = IO.socket("http://192.168.1.33:3000/")
        } catch (e: URISyntaxException) {
        }

        mSocket.connect()
        mSocket.emit("LoadElements","0")

        mSocket.on("LoadElementsBack") { args ->
            runOnUiThread {
                val data = args[0].toString()
                val jsonarray = JSONArray(data)
                val nCont = jsonarray.length() - 1

                for (i in 0..nCont) {
                    val json = JSONObject(jsonarray[i].toString())
                    val elementoId = json.getInt("ElementoId")
                    val descripcion = json.get("Descripcion")
                    itemList = itemList + GridViewModal(descripcion.toString(),elementoId)
                }

                //itemGRV.adapter = null
                var itemAdapter = GridRVAdapter(itemList = itemList, this@MainActivity)
                itemGRV.adapter = itemAdapter

            }
        }
    }

    fun onClickButton2(view: View?) {
        val tv3: TextView = findViewById(R.id.text1)
        tv3.text = "Test4"
    }



}