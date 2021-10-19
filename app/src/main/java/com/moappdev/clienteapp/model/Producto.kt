package com.moappdev.clienteapp.model

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Producto (
    @get:Exclude var id: String?=null,  //con esto no se toma encuenta el id en firesbase,
    // recordar q el id q se usa es de firestore
    var name: String?=null,
    var descripcion: String?=null,
    var imgUrl: String?=null,
    var cantidad: Int=0,
    var precio: Double=0.0
): Parcelable