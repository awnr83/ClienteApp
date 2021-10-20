package com.moappdev.clienteapp.cart

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.moappdev.clienteapp.databinding.FragmentCartBinding
import com.moappdev.clienteapp.home.HomeAux
import com.moappdev.clienteapp.model.Producto

class CartFragment : BottomSheetDialogFragment(),OnCartListener {

    private lateinit var mBinding: FragmentCartBinding
    private lateinit var bottomSheetBeahvior: BottomSheetBehavior<*>
    private lateinit var mAdapter: ProductCartAdapter

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
            mAdapter= ProductCartAdapter(mutableListOf<Producto>(), this)

            mBinding.rvCart.adapter=mAdapter

//            (1..5).forEach{
//                val producto=Producto(it.toString(),"Producot: $it", "sadasdad","",it, (2*it).toDouble())
//                mAdapter.add(producto)
//            }
        }
    }
    private fun setupButton(){
        mBinding?.let {
            it.btncancelar.setOnClickListener {
                bottomSheetBeahvior.state= BottomSheetBehavior.STATE_HIDDEN
            }
        }
    }

    private fun getProductos(){
        Log.i("alfredo","entro en getProduct")
        (parentFragment as? HomeAux)?.getProductsCart()?.forEach {
            Log.i("alfredo","entro")
            mAdapter.add(it)
        }
    }

    override fun setCantidad(producto: Producto) {
        TODO("Not yet implemented")
    }

    override fun showTotal(total: Double) {
        TODO("Not yet implemented")
    }
}