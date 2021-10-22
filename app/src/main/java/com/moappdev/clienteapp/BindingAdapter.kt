package com.moappdev.clienteapp

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("loadImage")
fun loadImage(imgView: ImageView, imgUrl: String){
    imgUrl?.let {
        Glide.with(imgView.context)
            .load(it)
            .circleCrop()
            .apply(
                RequestOptions()
                .placeholder(R.drawable.animation_load)
                .error(R.drawable.ic_broken))
            .into(imgView)
    }
}

@BindingAdapter("loadImageR")
fun loadImageR(imgView: ImageView, imgUrl: String){
    imgUrl?.let {
        Glide.with(imgView.context)
            .load(it)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.animation_load)
                    .error(R.drawable.ic_broken))
            .into(imgView)
    }
}

@BindingAdapter("precio")
fun precio(tv: TextView, precio: Double){
    tv?.let{
        it.text="$ ${precio.toString()}"
    }
}
@BindingAdapter("disponible")
fun disponible(tv: TextView, cant: Int){
    tv?.let{
        it.text="Disponibles: ${cant.toString()}"
    }
}