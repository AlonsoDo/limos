package com.example.limos

import android.os.Bundle
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Segunda : AppCompatActivity() {

    private lateinit var gridView: GridView
    private var aFilas = ArrayList<String>()
    private var nTotal: Double = 0.0
    private var nPos: Int = 0
    private lateinit var objDetalleCompleto: DetalleCompleto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_segunda)
        objDetalleCompleto = intent.getSerializableExtra("obj") as DetalleCompleto

        val tvMens : TextView = findViewById(R.id.tvMensage)
        tvMens.text = objDetalleCompleto.cuenta

        nTotal = 0.0
        for (item in objDetalleCompleto.lDetalle) {
            var cFila = item.unidades.toString() + "  " + item.descripcion + "  " + item.precio.toString()
            var nLogCad = cFila.length
            cFila = cFila.padStart((item.tablevel*3)+nLogCad,'-')
            aFilas.add(cFila)
            nTotal += item.precio
        }

        tvMens.text = nTotal.toString()

        gridView = findViewById(R.id.gridView)
        val mainAdapter = MainAdapter(this@Segunda, aFilas)
        gridView.adapter = mainAdapter
        gridView.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            Toast.makeText(applicationContext, "You CLicked " + aFilas[+position],
                Toast.LENGTH_SHORT).show()
            nPos = position
        }

    }

    fun onClickbtFin(view: View?){
        finish()
    }

    fun onClickbtMas(view: View?){

        nTotal = 0.0

        if (aFilas.isNotEmpty()){
            var oDetalle = objDetalleCompleto.lDetalle
            var nUnid = oDetalle[nPos].unidades
            nUnid++
            objDetalleCompleto.lDetalle[nPos].unidades = nUnid
            var cFila = nUnid.toString() + "  " + objDetalleCompleto.lDetalle[nPos].descripcion + "  " + objDetalleCompleto.lDetalle[nPos].precio.toString()
            var nLogCad = cFila.length
            cFila = cFila.padStart((objDetalleCompleto.lDetalle[nPos].tablevel*3)+nLogCad,'-')
            aFilas[nPos] = cFila
            val mainAdapter = MainAdapter(this@Segunda, aFilas)
            gridView.adapter = mainAdapter
        }
    }
}