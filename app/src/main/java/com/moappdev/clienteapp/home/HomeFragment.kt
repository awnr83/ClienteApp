package com.moappdev.clienteapp.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.ListenerRegistration
import com.moappdev.clienteapp.cart.CartFragment
import com.moappdev.clienteapp.databinding.FragmentHomeBinding
import com.moappdev.clienteapp.model.Producto

class HomeFragment : Fragment(), HomeAux{

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
        configBtnCart()

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

    private fun configBtnCart() {
        mBinding.btnCart.setOnClickListener {
            CartFragment().show( childFragmentManager.beginTransaction(),CartFragment::class.java.simpleName)
        }
    }

    override fun getProductsCart(): MutableList<Producto> {
        val productoList= mutableListOf<Producto>()
        (1..5).forEach{
            productoList.add(Producto(it.toString(),"Producot: $it", "sadasdad","",it, (2*it).toDouble()))
        }
        return productoList
    }
}