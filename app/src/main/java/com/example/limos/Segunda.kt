package com.example.limos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import android.widget.Toast

class Segunda : AppCompatActivity() {

    private lateinit var gridView: GridView
    private var playerNames = arrayOf("Cristiano Ronaldo","Joao Felix","Bernado Silva","Andre Silve","Bruno Fernandez","William Carvalho","Nelson Semedo","Pepe","Rui Patricio")
    private var aFilas = ArrayList<String>()

    //android:gravity="center"
    //android:textAlignment="center"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_segunda)
        val objDetalleCompleto = intent.getSerializableExtra("obj") as DetalleCompleto

        val tvMens : TextView = findViewById(R.id.tvMensage)
        tvMens.text = objDetalleCompleto.cuenta

        for (item in objDetalleCompleto.lDetalle) {
            var cFila = item.unidades.toString() + "  " + item.descripcion + "  " + item.precio.toString()
            aFilas.add(cFila)
        }

        //tvMens.text = aFilas[0]

        gridView = findViewById(R.id.gridView)
        val mainAdapter = MainAdapter(this@Segunda, aFilas)
        gridView.adapter = mainAdapter
        gridView.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            Toast.makeText(applicationContext, "You CLicked " + aFilas[+position],
                Toast.LENGTH_SHORT).show()
        }

    }

    fun onClickbtFin(view: View?){
        finish()
    }
}