package com.moappdev.clienteapp.cart

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.moappdev.clienteapp.databinding.FragmentCartBinding
import com.moappdev.clienteapp.databinding.FragmentHomeBinding
import com.moappdev.clienteapp.databinding.ItemProductoBinding

class CartFragment : BottomSheetDialogFragment() {

    private var mBinding: FragmentCartBinding?=null
    private lateinit var bottomSheetBeahvior: BottomSheetBehavior<*>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mBinding= FragmentCartBinding.inflate(LayoutInflater.from(context))
        mBinding?.let {
            val bottomSheetDialog= super.onCreateDialog(savedInstanceState) as BottomSheetDialog
            bottomSheetDialog.setContentView(it.root)

            bottomSheetBeahvior= BottomSheetBehavior.from(it.root.parent as View)
            bottomSheetBeahvior.state= BottomSheetBehavior.STATE_EXPANDED

            return bottomSheetDialog
        }
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding=null
    }
}