package com.example.launderup.ui.view.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.launderup.R
import com.example.launderup.data.api.HerokuInstance
import com.example.launderup.data.models.ShopDetails
import com.example.launderup.ui.adapter.ClothListAdapter
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.tabs.TabLayout
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClothesListActivity : AppCompatActivity() {
    private lateinit var clothesRecyclerView:RecyclerView
    private lateinit var progressIndicator:CircularProgressIndicator
    private lateinit var shopID:String
    private var totalCost:Int=0
    private lateinit var layout: ConstraintLayout
    private lateinit var clothesCategoryTabLayout: TabLayout
    private var clothesCategory: String="mens"

    private val sharedPrefFile = "LaunderUp"
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clothes_list)

        progressIndicator=findViewById(R.id.clothes_activity_progress)
        clothesCategoryTabLayout=findViewById(R.id.clothes_category_tl)

        //val arrayList : ArrayList<String> = getItemsList()
//        val searchView: SearchView =findViewById(R.id.searchView)
        clothesRecyclerView=findViewById(R.id.clothes_rv)
        clothesRecyclerView.layoutManager= LinearLayoutManager(this)
        sharedPreferences=this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        layout=findViewById(R.id.clothes_list)
        val intent: Intent =intent
        shopID=intent.getStringExtra("shid").toString()
        getShopDetails(shopID,clothesCategory)

//        val  db=Room.databaseBuilder(
//            applicationContext,
//            AppLocalDatabase::class.java, "Clothes"
//        ).build()
//
//        val clothesDao=db.clothesDao()
//        val clothes:List<ClothesDataClass> = clothesDao.getAll()

        clothesCategoryTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab!!.position){
                    0->clothesCategory="mens"
                    1->clothesCategory="womans"
                    2->clothesCategory="kids"
                }
                getShopDetails(shopID,clothesCategory)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Handle tab reselect
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                progressIndicator.show()
            }
        })

    }

    private fun getShopDetails(shopID:String,category: String){
        val getShopDetails= HerokuInstance.herokuapi.getShopDetails(shopID = shopID)
        getShopDetails.enqueue(object : Callback<ShopDetails> {
            override fun onResponse(call: Call<ShopDetails>, response: Response<ShopDetails>) {
                progressIndicator.hide()
                val data: ShopDetails =response.body()!!
                val clothType= JSONObject(data.cloth_types)
                val cloth=clothType.getJSONObject("service")
                val clothArray: JSONArray? =cloth.optJSONArray(category)
                val clothListAdapter = ClothListAdapter(this@ClothesListActivity,clothArray!!)
                clothesRecyclerView.adapter=clothListAdapter

            }

            override fun onFailure(call: Call<ShopDetails>, t: Throwable) {
                Log.i(MainActivity::class.simpleName, "Shop Fetch Failed")
            }
        })
    }

}

