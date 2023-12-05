package com.example.limos

import java.io.Serializable

data class Detalle (
    val unidades: Int,
    val descripcion: String,
    val precio: Double
)

data class DetalleCompleto(
    val cuenta: String,
    val lDetalle: ArrayList<Detalle>
): Serializable
