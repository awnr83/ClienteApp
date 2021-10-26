package com.moappdev.clienteapp.cart

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moappdev.clienteapp.R
import com.moappdev.clienteapp.databinding.ItemProductoCartBinding
import com.moappdev.clienteapp.model.Producto


class CartAdapter(private val productoList: MutableList<Producto>,
                  private val listener: OnCartListener):RecyclerView.Adapter<CartAdapter.ViewHolder>(){

    private lateinit var context: Context

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding= ItemProductoCartBinding.bind(view)

        fun setListener(producto: Producto){
            binding.btnAdd.setOnClickListener {
                if(producto.newCantidad< producto.cantidad) {
                    producto.newCantidad++
                    listener.setCantidad(producto)
                }
            }
            binding.btnRemove.setOnClickListener {
                if(producto.newCantidad>=1) {
                    producto.newCantidad--
                    listener.setCantidad(producto)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_producto_cart ,parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var _producto= productoList[position]
        holder.setListener(_producto)
        holder.binding.apply {
            producto= _producto
            if(_producto.cantidad>=1)
                tvCantidad.text= _producto.newCantidad.toString()
        }
        calcularTotal()
    }

    override fun getItemCount()= productoList.size

    fun add(producto: Producto){
        if(!productoList.contains(producto)){
            productoList.add(producto)
            notifyItemInserted(productoList.size-1)
        }else
            update(producto)
    }
    fun update(producto: Producto){
        val index=productoList.indexOf(producto)
        if(index!=-1){
            if(producto.newCantidad!=0) {
                productoList.set(index, producto)
                notifyItemChanged(productoList.size - 1)
            }else{
                productoList.removeAt(index)
                notifyItemRemoved(productoList.size-1)
            }
            calcularTotal()
        }
    }

    private fun calcularTotal(){
        var result=0.0
        for(product in productoList)
            result+= product.newCantidad*product.precio
        listener.showTotal(result)
    }

    fun getProductos():List<Producto> = productoList //se usa para armar el OrdenPRoducto
}