package com.moappdev.clienteapp.detalle

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navArgument
import androidx.navigation.ui.navigateUp
import com.moappdev.clienteapp.R
import com.moappdev.clienteapp.databinding.FragmentDetalleBinding
import com.moappdev.clienteapp.home.HomeAux
import com.moappdev.clienteapp.model.Producto

class DetalleFragment:Fragment() {
    private lateinit var mBinding: FragmentDetalleBinding
    private lateinit var mViewModel: DetalleViewModel
//    private var producto: Producto?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding= FragmentDetalleBinding.inflate(inflater)

        var producto= DetalleFragmentArgs.fromBundle(arguments!!).producto
        val viewmodelFactory=DetalleViewModelFactory(producto)
        mViewModel= ViewModelProvider(this, viewmodelFactory).get(DetalleViewModel::class.java)
        mBinding.viewModel=mViewModel
        mBinding.lifecycleOwner=this

        mViewModel.navigate.observe(viewLifecycleOwner, Observer {
            if(it)
                findNavController().navigate(DetalleFragmentDirections.actionDetalleFragmentToHomeFragment().setProducto(producto))
        })
        mViewModel.cancelar.observe(viewLifecycleOwner, Observer {
            if(it)
                findNavController().navigateUp()
        })

        return mBinding.root
    }

    class DetalleViewModelFactory(private var producto:Producto):ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(DetalleViewModel::class.java))
                return DetalleViewModel(producto) as T
            throw IllegalArgumentException("Error al crear el ViewModel")
        }
    }
}