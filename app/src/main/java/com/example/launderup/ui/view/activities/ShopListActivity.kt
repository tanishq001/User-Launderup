package com.example.launderup.ui.view.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.launderup.R
import com.example.launderup.data.api.HerokuInstance
import com.example.launderup.data.models.Data
import com.example.launderup.data.models.ShopsList
import com.example.launderup.ui.adapter.ShopListAdapter
import com.google.android.material.progressindicator.CircularProgressIndicator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ShopListActivity : AppCompatActivity() {
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private var searchText=" "
    private lateinit var serviceAvailable: String
    private lateinit var expressAvailable:String
    private lateinit var progressIndicator:CircularProgressIndicator

    private val sharedPrefFile = "LaunderUp"
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_list)

        val intent:Intent=intent
        serviceAvailable= intent.getStringExtra("typeOfService").toString()
        expressAvailable= intent.getStringExtra("Express").toString()

        searchView=findViewById(R.id.searchView) //Binding SearchView
        recyclerView=findViewById(R.id.recycler_view) //Binding RecyclerView
        progressIndicator=findViewById(R.id.shop_activity_progress)
        sharedPreferences=this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

        recyclerView.layoutManager= LinearLayoutManager(this)
        getShopsList(searchText)

//        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                 TODO("Not yet implemented")
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                arrayList.clear()
//                searchText= newText!!.lowercase(Locale.getDefault())
//                if(searchText.isNotEmpty()){
////                    getItemsList().forEach{
////                        if(it.lowercase(Locale.getDefault()).contains(searchText)){
////                            arrayList.add(it)
////                        }
////                    }
//                    if (serviceAvailable != null) {
//                        getShopsList(serviceAvailable,searchText)
//                    }
//                    recyclerView.adapter!!.notifyDataSetChanged()
//                }
//                else{
//                    arrayList.clear()
//                    arrayList.addAll(getItemsList())
//                    recyclerView.adapter!!.notifyDataSetChanged()
//                }
//                return false
//            }
//        })



    }



     private fun getShopsList(searchText:String){
        val getShopsList= HerokuInstance.herokuapi.getShopsList(expressService = expressAvailable,serviceAvailable=serviceAvailable,search = searchText)
         getShopsList.enqueue(object : Callback<ShopsList> {
            override fun onResponse(call: Call<ShopsList>, response: Response<ShopsList>) {
                progressIndicator.hide()
                val data: List<Data> =response.body()!!.data
                Log.i(data.toString(),"Shop Data")
                val shopListAdapter = ShopListAdapter(this@ShopListActivity,data)
                recyclerView.adapter=shopListAdapter

                shopListAdapter.setOnItemClickListener(object : ShopListAdapter.OnItemClickListener{
                    override fun onItemClick(position: Int) {
                        val editor:SharedPreferences.Editor=sharedPreferences.edit()
                        editor.putString("shopID",data[position].shid)
                        editor.apply()
                        editor.commit()
                        startActivity(Intent(applicationContext, ClothesListActivity::class.java).putExtra("shid",data[position].shid))
                    }
                })
            }
            override fun onFailure(call: Call<ShopsList>, t: Throwable) {
                Log.i(MainActivity::class.simpleName, "Shop Fetch Failed")
            }
        })
    }
}

