package com.moappdev.clienteapp.cart

import com.moappdev.clienteapp.model.Producto

interface OnCartListener {
    fun setCantidad(producto: Producto)
    fun showTotal(total: Double)
}