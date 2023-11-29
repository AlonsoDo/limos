package com.example.limos

data class Detalle (
    val unidades: Int,
    val descripcion: String,
    val precio: Double
)

data class DetalleCompleto(
    val cuenta: String,
    val lDetalle: ArrayList<Detalle>
)
