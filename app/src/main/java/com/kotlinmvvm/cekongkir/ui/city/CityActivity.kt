package com.kotlinmvvm.cekongkir.ui.city

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kotlinmvvm.cekongkir.BuildConfig
import com.kotlinmvvm.cekongkir.R
import com.kotlinmvvm.cekongkir.database.preferences.CekOngkirPreference
import com.kotlinmvvm.cekongkir.network.ApiService
import com.kotlinmvvm.cekongkir.network.RajaOngkirRepository
import com.kotlinmvvm.cekongkir.network.Resource
import timber.log.Timber

class CityActivity : AppCompatActivity() {

    private val api by lazy { ApiService.getClient() }
    private val pref by lazy { CekOngkirPreference(this) }

    private lateinit var viewModelFactory: CityViewModelFactory
    private lateinit var viewModel: CityViewModel
    private lateinit var repository: RajaOngkirRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city)
        setupViewModel()
        setupObserver()


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun setupViewModel() {
        repository = RajaOngkirRepository(api, pref)
        viewModelFactory = CityViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CityViewModel::class.java)
    }

    private fun setupObserver() {
        viewModel.titleBar.observe(this, Observer { title ->
            supportActionBar!!.title = title
        })
        viewModel.cityResponse.observe(this, Observer {
            if (BuildConfig.DEBUG) {
                Timber.plant(Timber.DebugTree())
            }
            when (it) {
                is Resource.Loading -> {
                    Timber.e("rajaongkir : isLoading")
                }
                is Resource.Success -> {
                    Timber.e("rajaongkir : ${it.data!!.rajaongkir}")
                }
                is Resource.Error -> {
                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}