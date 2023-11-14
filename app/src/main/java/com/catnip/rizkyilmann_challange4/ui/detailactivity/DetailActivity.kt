package com.catnip.rizkyilmann_challange4.ui.detailactivity

import DetailViewModel
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.catnip.rizkyilmann_challange4.data.database.AppDatabase
import com.catnip.rizkyilmann_challange4.data.database.datasource.CartDataSource
import com.catnip.rizkyilmann_challange4.data.database.datasource.CartDatabaseDataSource
import com.catnip.rizkyilmann_challange4.data.repository.CartRepository
import com.catnip.rizkyilmann_challange4.data.repository.CartRepositoryImpl
import com.catnip.rizkyilmann_challange4.databinding.ActivityDetailBinding
import com.catnip.rizkyilmann_challange4.model.DetailMenu
import com.catnip.rizkyilmann_challange4.network.api.datasource.AppApiDataSource
import com.catnip.rizkyilmann_challange4.network.api.service.AppApiService
import com.catnip.rizkyilmann_challange4.utils.GenericViewModelFactory
import com.catnip.rizkyilmann_challange4.utils.proceedWhen
import com.catnip.rizkyilmann_challange4.utils.toCurrencyFormat
import com.chuckerteam.chucker.api.ChuckerInterceptor

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mendapatkan objek DetailMenu yang dikirimkan melalui intent
        val detailMenu = intent.getParcelableExtra<DetailMenu>(EXTRA_PRODUCT)

        // Inisialisasi UI elemen
        val ivBannerDetail: ImageView = binding.ivBannerDetail
        val tvHeadlineMenu: TextView = binding.tvHeadlineMenu
        val tvDescriptionMenu: TextView = binding.tvDescriptionMenu
        val tvPriceDetail: TextView = binding.tvPriceDetail

        // Tampilkan data DetailMenu di layout DetailActivity
        if (detailMenu != null) {
            ivBannerDetail.load(detailMenu.imgUrl) {
                crossfade(true)
            }
            tvHeadlineMenu.text = detailMenu.name
            tvDescriptionMenu.text = detailMenu.desc
            tvPriceDetail.text = detailMenu.harga.toString()
        }

        // Handle tombol kembali
        val ivBackIcon: ImageView = binding.ivBackIcon
        ivBackIcon.setOnClickListener {
            onBackPressed()
        }

        // Handle tombol lokasi
        val tvLocationDetail: TextView = binding.tvLocationDetail
        tvLocationDetail.setOnClickListener {
            openGoogleMaps()
        }
        supportActionBar?.hide()

        setContentView(binding.root)
        bindProduct(viewModel.product)
        observeData()
        setClickListener()
    }

    private fun bindProduct(product: DetailMenu?) {
        product?.let { item ->
            binding.ivBannerDetail.load(item.imgUrl) {
                crossfade(true)
            }
            binding.tvHeadlineMenu.text = item.name
            binding.tvDescriptionMenu.text = item.desc
            binding.tvPriceDetail.text = item.harga.toCurrencyFormat()
        }
    }

    private fun openGoogleMaps() {
        val gmmIntentUri = Uri.parse("https://maps.app.goo.gl/h4wQKqaBuXzftGK77")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        startActivity(mapIntent)
    }

    private val viewModel: DetailViewModel by viewModels {
        val database = AppDatabase.getInstance(this)
        val cartDao = database.cartDao()
        val cartDataSource: CartDataSource = CartDatabaseDataSource(cartDao)
        val chuckerInterceptor = ChuckerInterceptor(applicationContext)
        val service = AppApiService.invoke(chuckerInterceptor)
        val apiDataSource = AppApiDataSource(service)
        val repo: CartRepository = CartRepositoryImpl(cartDataSource, apiDataSource)
        GenericViewModelFactory.create(
            DetailViewModel(intent?.extras, repo)
        )
    }

    private fun setClickListener() {
        binding.ivBackIcon.setOnClickListener {
            onBackPressed()
        }
        binding.ivMinus.setOnClickListener {
            viewModel.minus()
        }
        binding.ivPlus.setOnClickListener {
            viewModel.add()
        }
        binding.btnAddtocart.setOnClickListener {
            viewModel.addToCart()
        }
    }

    private fun observeData() {
        viewModel.productCountLiveData.observe(this) {
            binding.tvProductCount.text = it.toString()
        }
        viewModel.addToCartResult.observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    Toast.makeText(this, "Add to cart success !", Toast.LENGTH_SHORT).show()
                    finish()
                },
                doOnError = {
                    Toast.makeText(this, it.exception?.message.orEmpty(), Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    companion object {
        const val EXTRA_PRODUCT = "EXTRA_PRODUCT"

        // Fungsi untuk memulai DetailActivity dari suatu Activity/Fragment
        fun startActivity(context: Context, product: DetailMenu) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(EXTRA_PRODUCT, product)
            context.startActivity(intent)
        }
    }
}
