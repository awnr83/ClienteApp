package com.moappdev.clienteapp.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.moappdev.clienteapp.databinding.ItemProductoBinding
import com.moappdev.clienteapp.model.Producto

class HomeAdapter(val clickListener: ProductoListener)
    : ListAdapter<Producto, HomeAdapter.Viewholder>(ProductoCallback()) {

    class Viewholder private constructor(val binding: ItemProductoBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(item: Producto, clickListener: ProductoListener) {
            binding.producto=item
            binding.tvPrecio.text= "Precio: $ ${item.precio.toString()}"    //migrar
            binding.tvCantidad.text= "Stock: ${item.cantidad.toString()}"   //migrar
            binding.clickListener= clickListener
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup):Viewholder{
                val layoutInflater= LayoutInflater.from(parent.context)
                var binding= ItemProductoBinding.inflate(layoutInflater,parent,false)
                return Viewholder(binding)
            }
        }
    }

    class ProductoCallback: DiffUtil.ItemCallback<Producto>() {
        override fun areItemsTheSame(oldItem: Producto, newItem: Producto): Boolean {
            return oldItem===newItem
        }
        override fun areContentsTheSame(oldItem: Producto, newItem: Producto): Boolean {
            return oldItem==newItem
        }
    }
    class ProductoListener(val clickListener: (producto: Producto)->Unit) {
        fun onClick(producto: Producto)= clickListener(producto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        return Viewholder.from(parent)
    }
    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }
}