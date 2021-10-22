package com.moappdev.clienteapp.detalle

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moappdev.clienteapp.model.Producto

class DetalleViewModel(private var mProducto: Producto): ViewModel() {

    val producto=mProducto

    private val _cantidad=MutableLiveData<Int>()
    val cantidad: LiveData<Int>
        get()=_cantidad

    private val _total=MutableLiveData<String>()
    val total: LiveData<String>
        get()=_total

    private val _navigate=MutableLiveData<Boolean>()
    val navigate: LiveData<Boolean>
        get()=_navigate

    private val _cancelar=MutableLiveData<Boolean>()
    val cancelar: LiveData<Boolean>
        get()=_cancelar

    init {
        _cantidad.value=producto.newCantidad
        _total.value=""
        _navigate.value=false
        _cancelar.value=false
    }

    fun add(){
        if (_cantidad.value!! < producto.cantidad)
            _cantidad.value = _cantidad.value!! + 1
        total()
    }
    fun remove(){
        if(_cantidad.value!! > 0)
            _cantidad.value = _cantidad.value!! - 1
        total()
    }
    fun agregar(){
        mProducto.newCantidad = _cantidad.value!!
        _navigate.value = true
    }

    private fun total(){
        if(_cantidad.value==0)
            _total.value=""
        else
            _total.value= "Total: $ ${_cantidad.value!! * producto.precio} pesos (${_cantidad.value!!} x $${producto.precio})"
    }
}