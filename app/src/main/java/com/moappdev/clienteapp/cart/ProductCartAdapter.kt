package com.moappdev.clienteapp.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moappdev.clienteapp.R
import com.moappdev.clienteapp.databinding.ItemProductoCartBinding
import com.moappdev.clienteapp.model.Producto


class ProductCartAdapter(private val productoList: MutableList<Producto>,
                         private val listener: OnCartListener):RecyclerView.Adapter<ProductCartAdapter.ViewHolder>(){

    private lateinit var context: Context

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding= ItemProductoCartBinding.bind(view)

        fun setListener(producto: Producto){
            binding.btnAdd.setOnClickListener {
                listener.setCantidad(producto)
            }
            binding.btnRemove.setOnClickListener {
                listener.setCantidad(producto)
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
            if(_producto.cantidad>1)
                tvCantidad.text= _producto.newCantidad.toString()

        }

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
            productoList.set(index, producto)
            notifyItemChanged(productoList.size-1)
        }
    }
    fun delete(producto: Producto){
        val index=productoList.indexOf(producto)
        if(index!=-1){
            productoList.removeAt(index)
            notifyItemRemoved(productoList.size-1)
        }
    }
}


//class ProductCartFragment(val click: CarListener): ListAdapter<Producto, ProductCartFragment.ViewHolder>(CartDiffUtil()) {
//    class ViewHolder private constructor(val binding: ItemProductoCartBinding): RecyclerView.ViewHolder(binding.root) {
//        fun bind(item: Producto, click: CarListener){
//            binding.producto=item
//            binding.click=click
//            binding.executePendingBindings()
//        }
//        companion object{
//            fun from(parent: ViewGroup):ViewHolder {
//                val layoutInflater= LayoutInflater.from(parent.context)
//                val binding= ItemProductoCartBinding.inflate(layoutInflater,parent, false)
//                return ViewHolder(binding)
//            }
//        }
//    }
//    class CarListener(val click: (producto: Producto)->Unit) {
//        fun onClick(producto: Producto)= click(producto)
//    }
//    class CartDiffUtil:DiffUtil.ItemCallback<Producto>() {
//        override fun areItemsTheSame(oldItem: Producto, newItem: Producto): Boolean {
//            return oldItem===newItem
//        }
//
//        override fun areContentsTheSame(oldItem: Producto, newItem: Producto): Boolean {
//            return oldItem.id==newItem.id
//        }
//
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        return ViewHolder.from(parent)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(getItem(position), click)
//    }
//
//}