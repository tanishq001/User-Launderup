package com.example.launderup.ui.view.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.launderup.R
import com.example.launderup.utils.LanguageLocalHelper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ExpressModalBottomSheet: BottomSheetDialogFragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private val sharedPrefFile = "LaunderUp"
    private lateinit var expressServiceTextView: TextView
    private lateinit var ironingTextView: TextView
    private lateinit var dryCleaningTextView: TextView
    private lateinit var laundryTextView: TextView
    private lateinit var steamIroningTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        val root=inflater.inflate(R.layout.express_modal_bottom_sheet_content, container, false)

        expressServiceTextView=root.findViewById(R.id.bottom_sheet_top)
        ironingTextView=root.findViewById(R.id.iron_text_view)
        dryCleaningTextView=root.findViewById(R.id.dry_clean_text_view)
        laundryTextView=root.findViewById(R.id.laundry_text_view)
        steamIroningTextView=root.findViewById(R.id.steam_iron_text_view)

        sharedPreferences=context!!.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val shareLang: String? =sharedPreferences.getString("SELECTED_LANGUAGE","ABC")
        if(shareLang=="en")
            translateToEnglish()
        else if(shareLang=="hi")
            translateToHindi()

        val expressIronCard: LinearLayout =root.findViewById(R.id.express_ironing_view)
        val expressDryCleaningCard:LinearLayout =root.findViewById(R.id.express_dry_clean_view)
        val expressLaundryCard: LinearLayout =root.findViewById(R.id.express_laundry_view)
        val expressSteamIroningCard: LinearLayout =root.findViewById(R.id.express_steam_iron_view)

        expressIronCard.setOnClickListener {
            val intent= Intent(activity, ShopListActivity::class.java)
            intent.putExtra("typeOfService","ironing")
                .putExtra("Express","true")
            startActivity(intent)
            dismiss()
        }

        expressDryCleaningCard.setOnClickListener {
            val intent= Intent(activity,ShopListActivity::class.java)
            intent.putExtra("typeOfService","drycleaning")
                .putExtra("Express","true")
            startActivity(intent)
            dismiss()
        }

        expressLaundryCard.setOnClickListener {
            val intent= Intent(activity,ShopListActivity::class.java)
            intent.putExtra("typeOfService","laundry")
                .putExtra("Express","true")
            startActivity(intent)
            dismiss()
        }

        expressSteamIroningCard.setOnClickListener {
            val intent= Intent(activity,ShopListActivity::class.java)
            intent.putExtra("typeOfService","steam_ironing")
                .putExtra("Express","true")
            startActivity(intent)
            dismiss()
        }
        return root
    }

    private fun translateToHindi() {
        val language= LanguageLocalHelper()
        val context: Context = language.setLocale(activity!!,"hi")
        val resources: Resources = context.resources
        expressServiceTextView.text=resources.getString(R.string.express_services)
        ironingTextView.text=resources.getString(R.string.express_ironing)
        dryCleaningTextView.text=resources.getString(R.string.express_dry_cleaning)
        laundryTextView.text=resources.getString(R.string.express_laundry)
        steamIroningTextView.text=resources.getString(R.string.express_steam_ironing)
    }

    private fun translateToEnglish() {
        val language= LanguageLocalHelper()
        val context: Context = language.setLocale(activity!!,"en")
        val resources: Resources = context.resources
        expressServiceTextView.text=resources.getString(R.string.express_services)
        ironingTextView.text=resources.getString(R.string.express_ironing)
        dryCleaningTextView.text=resources.getString(R.string.express_dry_cleaning)
        laundryTextView.text=resources.getString(R.string.express_laundry)
        steamIroningTextView.text=resources.getString(R.string.express_steam_ironing)
    }

    companion object {
        const val TAG = "ExpressModalBottomSheet"
    }
}