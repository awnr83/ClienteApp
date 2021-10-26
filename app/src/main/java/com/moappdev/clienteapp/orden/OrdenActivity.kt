package com.moappdev.clienteapp.orden

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.moappdev.clienteapp.R
import com.moappdev.clienteapp.databinding.ActivityOrdenBinding
import com.moappdev.clienteapp.model.Orden

class OrdenActivity : AppCompatActivity(), OnOrdenListener {

    private lateinit var mBinding:ActivityOrdenBinding
    private lateinit var mAdapter: OrdenAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding= ActivityOrdenBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setupRecyclerView()
        setupFirestore()
    }

    private fun setupRecyclerView() {
        mAdapter= OrdenAdapter(mutableListOf(),this)
        mBinding.rvOrdenes.adapter=mAdapter
    }

    private fun setupFirestore(){
        val userId= FirebaseAuth.getInstance().currentUser!!.uid

        val db= FirebaseFirestore.getInstance()
        db.collection("request").get().addOnSuccessListener {
            for (document in it){
                val orden= document.toObject(Orden::class.java)
                if(userId == orden.clienteId) {
                    orden.id = document.id
                    mAdapter.add(orden)
                }
            }
        }
            .addOnFailureListener {
                Toast.makeText(this, "Error al consultar los datos",Toast.LENGTH_SHORT).show()
            }
    }

    override fun onTrack(orden: Orden) {
        TODO("Not yet implemented")
    }

    override fun onStartChat(orden: Orden) {
        TODO("Not yet implemented")
    }
}