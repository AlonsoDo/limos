package com.example.limos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class Segunda : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_segunda)
        val objDetalleCompleto = intent.getSerializableExtra("obj") as DetalleCompleto
        val tvMens : TextView = findViewById(R.id.tvMensage)
        tvMens.text = objDetalleCompleto.cuenta
    }

    fun onClickbtFin(view: View?){
        finish()
    }
}