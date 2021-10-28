package com.moappdev.clienteapp.cart

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.moappdev.clienteapp.R
import com.moappdev.clienteapp.databinding.FragmentCartBinding
import com.moappdev.clienteapp.home.HomeAux
import com.moappdev.clienteapp.model.Orden
import com.moappdev.clienteapp.model.Producto
import com.moappdev.clienteapp.model.ProductoOrden
import com.moappdev.clienteapp.orden.OrdenActivity

class CartFragment : BottomSheetDialogFragment(),OnCartListener {

    private lateinit var mBinding: FragmentCartBinding
    private lateinit var bottomSheetBeahvior: BottomSheetBehavior<*>
    private lateinit var mAdapter: CartAdapter
    private var mTotal=0.0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mBinding= FragmentCartBinding.inflate(LayoutInflater.from(context))

        mBinding?.let {
            val bottomSheetDialog= super.onCreateDialog(savedInstanceState) as BottomSheetDialog
            bottomSheetDialog.setContentView(it.root)

            bottomSheetBeahvior= BottomSheetBehavior.from(it.root.parent as View)
            bottomSheetBeahvior.state= BottomSheetBehavior.STATE_EXPANDED

            setupRecyclerView()
            setupButton()

            getProductos()

            return bottomSheetDialog
        }
        return super.onCreateDialog(savedInstanceState)
    }
    private fun setupRecyclerView(){
        mBinding?.let {
            mAdapter= CartAdapter(mutableListOf<Producto>(), this)
            mBinding.rvCart.adapter=mAdapter
        }
    }
    private fun setupButton(){
        mBinding?.let {
            it.btncancelar.setOnClickListener {
                bottomSheetBeahvior.state= BottomSheetBehavior.STATE_HIDDEN
            }
            it.fabPagar.setOnClickListener {
                requestOrder()
            }
        }
    }

    private fun requestOrder() {
        val user= FirebaseAuth.getInstance().currentUser
        user?.let {
            enableUI(false)
            val productos= hashMapOf<String, ProductoOrden>()
            var total=0.0
            mAdapter.getProductos().forEach { producto ->
                productos.put(producto.id!!, ProductoOrden(producto.id!!, producto.name!!, producto.newCantidad ))
                total += producto.newCantidad * producto.precio
            }
            val orden=Orden(clienteId = it.uid, productList = productos, total = total, status = 1)

            val db= FirebaseFirestore.getInstance()
            db.collection("request")
                .add(orden)
                .addOnSuccessListener {
                    dismiss()   //sale del fragment

                    (parentFragment as? HomeAux)?.clearCar()

                    startActivity(Intent(context, OrdenActivity::class.java))
                    Toast.makeText(activity, "Compra realizada", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(activity, "Error al confirmar la compra", Toast.LENGTH_SHORT).show()
                }
                .addOnCompleteListener {
                    enableUI(true)
                }
        }
    }

    private fun enableUI(enable: Boolean){
        mBinding.btncancelar.isEnabled=enable
        mBinding.fabPagar.isEnabled=enable
    }

    private fun getProductos(){
        (parentFragment as? HomeAux)?.getProductsCart()?.forEach {
            mAdapter.add(it)
        }
    }

    override fun setCantidad(producto: Producto) {
        mAdapter.update(producto)
    }

    override fun showTotal(total: Double) {
        mTotal= total
        mBinding?.let {
            if(mTotal!=0.0)
                it.tvTotal.text= getString(R.string.fc_carrito_total,total)
            else
                it.tvTotal.text= getString(R.string.fc_carrito_vacio)
        }
    }

    override fun onDestroyView() {
        (parentFragment as? HomeAux)?.updateTotal()
        super.onDestroyView()
    }
}