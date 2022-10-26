package com.example.launderup.ui.view.activities

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.TextView
import com.example.launderup.R
import com.example.launderup.utils.LanguageLocalHelper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class LanguageModalBottomSheet: BottomSheetDialogFragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private val sharedPrefFile = "LaunderUp"
    private lateinit var selectLanguageTextView:TextView
    private val language=LanguageLocalHelper()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root=inflater.inflate(R.layout.language_modal_bottom_sheet_content, container, false)
        val radioGroup:RadioGroup=root.findViewById(R.id.radioGroup)
        selectLanguageTextView=root.findViewById(R.id.select_language_text)
        sharedPreferences=activity!!.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

        val shareLang: String? =sharedPreferences.getString("SELECTED_LANGUAGE","ABC")
        if(shareLang=="en"){
            translateToEnglish()
            radioGroup.check(R.id.radio_button_1)
        }

        else if(shareLang=="hi") {
            translateToHindi()
            radioGroup.check(R.id.radio_button_2)
        }

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.radio_button_1->translateToEnglish()
                R.id.radio_button_2->translateToHindi()
            }
        }
        return root
    }

    private fun translateToHindi() {
        val context: Context = language.setLocale(activity!!,"hi")
        val resources: Resources = context.resources
        selectLanguageTextView.text=resources.getString(R.string.select_language)
    }

    private fun translateToEnglish() {
        val context: Context = language.setLocale(activity!!,"en")
        val resources: Resources = context.resources
        selectLanguageTextView.text=resources.getString(R.string.select_language)
    }

    companion object {
        const val TAG = "LanguageModalBottomSheet"
    }


}