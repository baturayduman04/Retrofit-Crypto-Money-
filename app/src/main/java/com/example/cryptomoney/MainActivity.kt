package com.example.cryptomoney

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptomoney.adapter.RecyclerViewAdapter
import com.example.cryptomoney.model.CryptoModel
import com.example.cryptomoney.service.CryptoAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(),RecyclerViewAdapter.Listener {

    private val BASE_URL = "https://api.nomics.com/v1/"
    private var cryptoModels: ArrayList<CryptoModel>? = null
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter

    // Disposable
    private lateinit var compositeDisposable: CompositeDisposable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        compositeDisposable = CompositeDisposable()


        //RecyclerView
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        loadData()

    }

    private fun loadData() {

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(CryptoAPI::class.java)




        compositeDisposable?.add(
            retrofit.getData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse)
        )


        /*

        val service = retrofit.create(CryptoAPI::class.java)
        val call = service.getData()

        call.enqueue(object: Callback<List<CryptoModel>> {

            //Eger cevap varsa
            override fun onResponse(call: Call<List<CryptoModel>>, response: Response<List<CryptoModel>>) {

                if (response.isSuccessful){
                    //gelen cevap başarılı mı
                    response.body()?.let {
                        //response boş gelmediyse
                        cryptoModels = ArrayList(it)

                        cryptoModels?.let {
                            recyclerViewAdapter = RecyclerViewAdapter(cryptoModels!!,this@MainActivity)
                            recyclerView.adapter = recyclerViewAdapter
                        }


                    }
                }
            }

            // Bir hata varsa yapılacak olan
            override fun onFailure(call: Call<List<CryptoModel>>, t: Throwable) {
                t.printStackTrace()
            }


        })

         */


    }


    private fun handleResponse(cryptoList: List<CryptoModel>) {
        cryptoModels = ArrayList(cryptoList)

        cryptoModels?.let {
            recyclerViewAdapter = RecyclerViewAdapter(cryptoModels!!, this@MainActivity)
            recyclerView.adapter = recyclerViewAdapter

        }
    }


        override fun onItemClick(cryptoModel: CryptoModel) {
            Toast.makeText(this, "Tıklandı : ${cryptoModel.currency}", Toast.LENGTH_LONG).show()
        }


    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.clear()
    }

}