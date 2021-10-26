package com.moappdev.clienteapp.orden

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moappdev.clienteapp.R
import com.moappdev.clienteapp.databinding.ItemOrdenBinding
import com.moappdev.clienteapp.model.Orden

class OrdenAdapter(private val orderList:MutableList<Orden>,private val listener: OnOrdenListener):RecyclerView.Adapter<OrdenAdapter.ViewHolder>() {

    private lateinit var context: Context

    inner class ViewHolder (view: View): RecyclerView.ViewHolder(view){
        val binding= ItemOrdenBinding.bind(view)

        fun setListener(orden:Orden){
            binding.btnTrack.setOnClickListener {
                listener.onTrack(orden)
            }
            binding.btnChat.setOnClickListener {
                listener.onStartChat(orden)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context=parent.context
        val view=LayoutInflater.from(context).inflate(R.layout.item_orden, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val orden= orderList[position]
        var nombres=""
        orden.productList.forEach{
            nombres+="${it.value.name}, "
        }
        holder.setListener(orden)
        holder.binding.tvId.text= orden.id
        holder.binding.tvProductoName.text= nombres.dropLast(2)
        holder.binding.tvTotal.text= orden.total.toString()
    }

    override fun getItemCount()=orderList.size

    fun add(orden: Orden){
        orderList.add(orden)
        notifyItemInserted(orderList.size-1)
    }
    
}