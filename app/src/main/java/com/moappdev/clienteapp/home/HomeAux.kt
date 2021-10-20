package com.moappdev.clienteapp.home

import com.moappdev.clienteapp.model.Producto

interface HomeAux {
    fun getProductsCart(): MutableList<Producto>
}