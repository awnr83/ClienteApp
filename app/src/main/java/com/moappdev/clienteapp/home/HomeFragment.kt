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
    private lateinit var mFirestoreListener: ListenerRegistration

    private lateinit var mViewModel: HomeViewModel

    private var lista= mutableListOf<Producto>()

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
            HomeAdapter.ProductoListener {
//                mProductoSelected=it

                val fragment=DetalleFragment()

//                fragment.requireActivity().supportFragmentManager.beginTransaction()
//                    .add(R.id.clDetalle, fragment)
//                    .addToBackStack(null)
//                    .commit()
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetalleFragment(it))

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
        if(productoList.contains(producto))
            productoList[productoList.indexOf(producto)]=producto
        else
            productoList.add(producto)
    }

    override fun getProductsCart(): MutableList<Producto> {
 //       val productoList= mutableListOf<Producto>()
//        mProductoSelected?.let { productoList.add(it) }
//        (1..5).forEach{
//            productoList.add(Producto(it.toString(),"Producot: $it", "sadasdad","",it, (2*it).toDouble()))
//        }
        return productoList
    }

    override fun addProductCart(producto: Producto) {
//        Log.i("alfredo","producto: ${producto.name} - ${producto.newCantidad}")
//        if(producto.cantidad>0)
//            productoList.add(producto)
    }

//    override fun getProductoSelect(): Producto? {
//        return mProductoSelected
//    }
}