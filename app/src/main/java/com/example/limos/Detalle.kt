package com.example.limos

import java.io.Serializable

data class Detalle (
    var unidades: Int,
    val descripcion: String,
    val precio: Double,
    val tablevel: Int
): Serializable

data class DetalleCompleto(
    val cuenta: String,
    val lDetalle: ArrayList<Detalle>
): Serializable
