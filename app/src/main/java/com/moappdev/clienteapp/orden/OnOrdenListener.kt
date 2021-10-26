package com.moappdev.clienteapp.orden

import com.moappdev.clienteapp.model.Orden

interface OnOrdenListener {
    fun onTrack(orden:Orden)
    fun onStartChat(orden:Orden)
}