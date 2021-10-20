package com.moappdev.clienteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.moappdev.clienteapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mAppBarConfiguration: AppBarConfiguration

    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mAuthStateListener: FirebaseAuth.AuthStateListener

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        val response = IdpResponse.fromResultIntent(it.data)

        if (it.resultCode == RESULT_OK){
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null){
                mBinding.progresBar.visibility= View.GONE
                Snackbar.make(mBinding.root, "Bienvenido", Snackbar.LENGTH_SHORT).show()
            }
        } else {
            if (response == null){
                Snackbar.make(mBinding.root,"Hasta pronto", Snackbar.LENGTH_SHORT).show()
                finish()
            } else {
                response.error?.let {
                    if (it.errorCode == ErrorCodes.NO_NETWORK){
                        Snackbar.make(mBinding.root, "Sin red", Snackbar.LENGTH_SHORT).show()
                    } else {
                        Snackbar.make(mBinding.root,"Código de error: ${it.errorCode}", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mFirebaseAuth.addAuthStateListener(mAuthStateListener)
    }

    override fun onPause() {
        super.onPause()
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding= DataBindingUtil.setContentView(this, R.layout.activity_main)

        configAuth()

        val navCont= this.findNavController(R.id.myHostNavFragment)
        NavigationUI.setupActionBarWithNavController(this, navCont)
          mAppBarConfiguration= AppBarConfiguration(navCont.graph)
    }

    //menu y button up
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_opciones,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.mnuCerrar -> {
                AuthUI.getInstance().signOut(this)
                    .addOnSuccessListener {
                        Snackbar.make(mBinding.root,"Hasta luego", Snackbar.LENGTH_SHORT).show()
                    }
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            mBinding.progresBar.visibility= View.VISIBLE
                        }
                    }
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onSupportNavigateUp(): Boolean {
        val navCont=this.findNavController(R.id.myHostNavFragment)
        return NavigationUI.navigateUp(navCont,mAppBarConfiguration)
    }

    private fun configAuth(){
        mFirebaseAuth = FirebaseAuth.getInstance()
        mAuthStateListener = FirebaseAuth.AuthStateListener { auth ->
            if (auth.currentUser != null){
                mBinding.progresBar.visibility= View.GONE
            } else {
                val providers = arrayListOf(
                    AuthUI.IdpConfig.EmailBuilder().build(),
                    AuthUI.IdpConfig.GoogleBuilder().build())

                resultLauncher.launch(
                    AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false)       //con esto no ofrece las cuentas ya usadas cuando se cierra sesion
                        .build())
            }
        }
    }
}