package com.moappdev.clienteapp.home

import com.moappdev.clienteapp.model.Producto

interface HomeAux {
    //home->productCartAdapter
    fun getProductsCart(): MutableList<Producto>
    fun updateTotal()
    fun clearCar()

    //home->detalleFragment -> navigation (reemplazado)
//    fun getProductoSelect(): Producto?=null
//    fun addProductCart(producto: Producto)

}