package com.example.limos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import android.widget.Toast

class Segunda : AppCompatActivity() {

    lateinit var gridView: GridView
    private var playerNames = arrayOf("Cristiano Ronaldo","Joao Felix","Bernado Silva","Andre Silve","Bruno Fernandez","William Carvalho","Nelson Semedo","Pepe","Rui Patricio")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_segunda)
        val objDetalleCompleto = intent.getSerializableExtra("obj") as DetalleCompleto
        val tvMens : TextView = findViewById(R.id.tvMensage)
        tvMens.text = objDetalleCompleto.cuenta

        gridView = findViewById(R.id.gridView)
        val mainAdapter = MainAdapter(this@Segunda, playerNames)
        gridView.adapter = mainAdapter
        gridView.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            Toast.makeText(applicationContext, "You CLicked " + playerNames[+position],
                Toast.LENGTH_SHORT).show()
        }

    }

    fun onClickbtFin(view: View?){
        finish()
    }
}