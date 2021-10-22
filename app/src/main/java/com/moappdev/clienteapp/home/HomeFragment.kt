package com.moappdev.clienteapp.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStateManagerControl
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.firestore.ListenerRegistration
import com.moappdev.clienteapp.R
import com.moappdev.clienteapp.cart.CartFragment
import com.moappdev.clienteapp.databinding.FragmentHomeBinding
import com.moappdev.clienteapp.detalle.DetalleFragment
import com.moappdev.clienteapp.model.Producto

private var productoList= mutableListOf<Producto>()

class HomeFragment : Fragment(), HomeAux{

    private lateinit var mBinding: FragmentHomeBinding
    private lateinit var mAdapter: HomeAdapter

    private lateinit var mViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding= FragmentHomeBinding.inflate(inflater)

        val args= HomeFragmentArgs.fromBundle(requireArguments())
        args.producto?.let {
            actualizarListCarrito(it)
        }

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
            HomeAdapter.ProductoListener {producto->
                var ok=true
                productoList.forEach {
                    if(it.id == producto.id){
                        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetalleFragment(it))
                        ok=false
                    }
                }
                if(ok)
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetalleFragment(producto))
            }
        )
        mBinding.recyclerViewProductos.adapter= mAdapter
    }
    private fun configBtnCart() {
        mBinding.btnCart.setOnClickListener {
            CartFragment().show( childFragmentManager.beginTransaction(),CartFragment::class.java.simpleName)
        }
    }
    private fun actualizarListCarrito(producto: Producto){
        var ok=true
        productoList.forEach {
            if(it.id==producto.id) {
                if(it.newCantidad==0){
                    productoList.remove(it)
                    ok=false
                    return@forEach
                }else{
                    it.newCantidad= producto.newCantidad
                    ok=false
                    return@forEach
                }
            }
        }
        if(ok && producto.newCantidad>0)
            productoList.add(producto)
        updateTotal()
    }

    //interface HomeAux
    override fun getProductsCart()= productoList
    override fun updateTotal() {
        var total= 0.0
        productoList.forEach {
            total+= it.newCantidad*it.precio
        }
        if(total==0.0)
            mBinding.tvCar.text="Carrito vacio"
        else
            mBinding.tvCar.text="Total: $$total pesos"
    }

    override fun clearCar() {
        productoList.clear()
    }
}