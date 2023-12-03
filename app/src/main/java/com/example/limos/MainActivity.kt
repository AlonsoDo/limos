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
import android.widget.Button
import android.widget.EditText
import android.widget.GridView
import android.widget.Toast
import com.google.gson.Gson
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var mSocket: Socket
    private lateinit var itemGRV: GridView
    private lateinit var itemList: List<GridViewModal>
    private var nodoPadre: Int = 0
    private var aNodos = mutableListOf(0)
    private var nProfundidad: Int = 0
    private var ultimoElementoId: Int = 0
    private var ultimoPadreId: Int = 0
    var itemsListDetalle = ArrayList<Detalle>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        itemGRV = findViewById(R.id.idGRV)
        itemList = ArrayList<GridViewModal>()

        itemGRV.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

            /*Toast.makeText(
                applicationContext, itemList[position].itemText + " selected",
                Toast.LENGTH_LONG
            ).show()*/

            val tv2: TextView = findViewById(R.id.text1)
            tv2.text = itemList[position].itemText

            val tvNombreCuenta: TextView = findViewById(R.id.nombCuen)

            if (itemList[position].final == 1){
                var objDetalle = Detalle(1,itemList[position].itemText,itemList[position].precio)
                itemsListDetalle.add(objDetalle)
            }

            ultimoPadreId = itemList[position].padreId
            ultimoElementoId = itemList[position].elementoId
            var nPos = itemList[position].elementoId.toString()

            val final = itemList[position].final

            if (final == 0){
                nodoPadre = itemList[position].padreId
                aNodos.add(nodoPadre)
                nProfundidad++
                itemGRV.adapter = null
                (itemList as ArrayList<GridViewModal>).clear()
                mSocket.emit("LoadElements",nPos)
            }

        }

        try {
            mSocket = IO.socket("http://192.168.1.34:3000/")
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
                    val padreId = json.getInt("PadreId")
                    val precio = json.getDouble("Precio")
                    val final = json.getInt("Final")
                    itemList = itemList + GridViewModal(descripcion.toString(),elementoId,padreId,precio,final)
                }

                var itemAdapter = GridRVAdapter(itemList = itemList, this@MainActivity)
                itemGRV.adapter = itemAdapter

            }
        }

        mSocket.on("LoadExtrasBack") { args ->
            runOnUiThread {
                val data = args[0].toString()
                val jsonarray = JSONArray(data)
                val nCont = jsonarray.length() - 1

                itemGRV.adapter = null
                (itemList as ArrayList<GridViewModal>).clear()

                for (i in 0..nCont) {
                    val json = JSONObject(jsonarray[i].toString())
                    val elementoId = json.getInt("ExtraId")
                    val descripcion = json.get("Descripcion")
                    val padreId = ultimoPadreId //nodoPadre
                    val precio = json.getDouble("Precio")
                    val final = 1
                    itemList = itemList + GridViewModal(descripcion.toString(),elementoId,padreId,precio,final)
                }

                var itemAdapter = GridRVAdapter(itemList = itemList, this@MainActivity)
                itemGRV.adapter = itemAdapter

            }
        }
    }

    // Enviar lote
    fun onClickButton2(view: View?) {

        //val tv3: TextView = findViewById(R.id.text1)
        //tv3.text = "Test4"

        val tvNombreCuenta: TextView = findViewById(R.id.nombCuen)

        var oDetalleCompleto = DetalleCompleto(tvNombreCuenta.text.toString(),itemsListDetalle)
        var gs= Gson()
        var sListaDetalleCompleto = gs.toJson(oDetalleCompleto)

        mSocket.emit("EnviarDetalle",sListaDetalleCompleto)

        tvNombreCuenta.text = ""
        itemsListDetalle.clear()

        //tv3.text = sListaDetalleCompleto.toString()
        Toast.makeText(
                applicationContext, "Pedido enviado",
                Toast.LENGTH_LONG
            ).show()
    }

    fun onClickbtIni(view: View?) {

        itemGRV.adapter = null
        (itemList as ArrayList<GridViewModal>).clear()

        mSocket.emit("LoadElements",0)
        nProfundidad = 0
        aNodos.clear()
        nodoPadre = 0
        aNodos.add(0)

        var boton: Button = findViewById(R.id.btExtras)
        boton.setEnabled(true)

    }

    fun onClickbtVolver(view: View?) {

        nodoPadre = aNodos[nProfundidad]

        val tv3: TextView = findViewById(R.id.text1)
        tv3.text = nodoPadre.toString()

        itemGRV.adapter = null
        (itemList as ArrayList<GridViewModal>).clear()

        mSocket.emit("LoadElements",nodoPadre)

        if (nProfundidad != 0) {
            aNodos.removeAt(nProfundidad)
            nProfundidad--
        }

        var boton: Button = findViewById(R.id.btExtras)
        boton.setEnabled(true)

    }

    fun onClickbtnombCuen(view: View?) {
        val cuenta: EditText = findViewById(R.id.nombCuen)
        cuenta.text.clear()
    }

    fun onClickbtExtras(view: View?) {
        nodoPadre = ultimoPadreId
        aNodos.add(nodoPadre)
        nProfundidad++
        var boton: Button = findViewById(R.id.btExtras)
        boton.setEnabled(false)
        mSocket.emit("LoadExtras",ultimoElementoId.toString())
    }

    fun onClickbtBajar(view: View?){
        nodoPadre = ultimoPadreId
        aNodos.add(nodoPadre)
        nProfundidad++
        itemGRV.adapter = null
        (itemList as ArrayList<GridViewModal>).clear()
        mSocket.emit("LoadElements",ultimoElementoId.toString())
    }

}