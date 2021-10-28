package com.moappdev.clienteapp.track

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.moappdev.clienteapp.Conts
import com.moappdev.clienteapp.R
import com.moappdev.clienteapp.databinding.FragmentTrackBinding
import com.moappdev.clienteapp.model.Orden

class TrackFragment : Fragment() {

    private var binding: FragmentTrackBinding?=null
    private var orden:Orden?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentTrackBinding.inflate(inflater,container, false)

        binding?.let {
            return it.root
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getOrden()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding=null
    }

    override fun onDestroy() {
        (activity as? AppCompatActivity)?.let {
            it.supportActionBar?.setDisplayHomeAsUpEnabled(false)
            it.supportActionBar?.title= getString(R.string.order_title)
            setHasOptionsMenu(false)
        }
        super.onDestroy()
    }

    private fun getOrden() {
        orden= (activity as? OrdenAux)?.getOrdeSelect()
        orden?.let {
            updateUI(it)
            getOrdenRealTime(it.id)
            setupAcctionBar()
        }
    }

    private fun setupAcctionBar() {
        (activity as? AppCompatActivity)?.let {
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            it.supportActionBar?.title= getString(R.string.ao_rastreo)
            setHasOptionsMenu(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home)
            activity?.onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    private fun updateUI(orden: Orden) {
        binding?.let {
            Log.i("alfredo", "orden ${orden.status}")
            it.pbLineal.progress= orden.status * (100/3) -15

            Log.i("alfredo", "pedido ${orden.status > 0}")
            it.cbOrdenPedido.isChecked= orden.status > 0
            Log.i("alfredo", "prepa ${orden.status > 1}")
            it.cbOrdenPre.isChecked= orden.status > 1
            Log.i("alfredo", "Env ${orden.status > 2}")
            it.cbOrdenEnv.isChecked= orden.status > 2
            Log.i("alfredo", "recibido ${orden.status > 3}")
            it.cbOrdenEnv.isChecked= orden.status > 3
        }
    }

    private fun getOrdenRealTime(id:String) {
        val db= FirebaseFirestore.getInstance()
        val Ordenreferencia= db.collection(Conts.FR_REQUESTS).document(id)
        Ordenreferencia.addSnapshotListener { value, error ->
            if(error!=null) {
                Toast.makeText(activity, "error al consultar la orden", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            if (value!=null && value.exists()){
                val orden= value.toObject(Orden::class.java)
                orden?.let {
                    it.id= value.id
                    updateUI(it)
                }
            }
        }
    }


}