package com.moappdev.clienteapp.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.ListenerRegistration
import com.moappdev.clienteapp.R
import com.moappdev.clienteapp.cart.CartFragment
import com.moappdev.clienteapp.databinding.FragmentHomeBinding
import com.moappdev.clienteapp.model.Producto

class HomeFragment : Fragment(){

    private lateinit var mBinding: FragmentHomeBinding
    private lateinit var mAdapter: HomeAdapter
    private lateinit var mFirestoreListener: ListenerRegistration

    private lateinit var mViewModel: HomeViewModel

    private var mProductoSelected: Producto?= null

    private var lista= mutableListOf<Producto>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding= FragmentHomeBinding.inflate(inflater)

        mViewModel= ViewModelProvider(this).get(HomeViewModel::class.java)
        mBinding.viewModel=mViewModel
        mBinding.lifecycleOwner=this

        mViewModel.productos.observe(viewLifecycleOwner, Observer {
            mAdapter.submitList(it)
        })
        mViewModel.error.observe(viewLifecycleOwner, Observer {
            if(it) {
                Toast.makeText(context,"Error al realizar la operacion", Toast.LENGTH_SHORT).show()
            }
        })
        mViewModel.exito.observe(viewLifecycleOwner, Observer {
            if(it) {
                Toast.makeText(context,"Operacion exitosa", Toast.LENGTH_SHORT).show()
            }
        })
        configRecyclerView()


        mBinding.btnCart.setOnClickListener {
//            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCartFragment())
            CartFragment().show( childFragmentManager.beginTransaction(),CartFragment::class.java.simpleName)
        }
        return mBinding.root
    }

    private fun configRecyclerView(){
        mAdapter= HomeAdapter(
            HomeAdapter.ProductoListener {
                mProductoSelected=it
//                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetalleFragment(it))
//                AddDialogFragment().show(parentFragmentManager,AddDialogFragment::class.java.simpleName)
            },
            HomeAdapter.ImageListener{
//                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetalleFragment(it))
            }
        )
        mBinding.recyclerViewProductos.adapter= mAdapter
    }
}