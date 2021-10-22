package com.moappdev.clienteapp.cart

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.moappdev.clienteapp.R
import com.moappdev.clienteapp.databinding.FragmentCartBinding
import com.moappdev.clienteapp.home.HomeAux
import com.moappdev.clienteapp.model.Producto

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
        dismiss()   //sale del fragment
        (parentFragment as? HomeAux)?.clearCar()
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