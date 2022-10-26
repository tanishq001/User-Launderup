package com.example.launderup.ui.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.launderup.R
import com.example.launderup.ui.view.activities.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {
    private lateinit var languageModalBottomSheet: ImageView
    private lateinit var ironingCardView: CardView
    private lateinit var dryCleaningCardView: CardView
    private lateinit var laundryCardView: CardView
    private lateinit var steamIroningCardView: CardView
    private lateinit var threeDots:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root= inflater.inflate(R.layout.fragment_home, container, false)

        languageModalBottomSheet=root.findViewById(R.id.language)
        ironingCardView=root.findViewById(R.id.iron_card)
        dryCleaningCardView=root.findViewById(R.id.dry_clean_card)
        laundryCardView=root.findViewById(R.id.laundry_card)
        steamIroningCardView=root.findViewById(R.id.steam_iron_card)
        threeDots=root.findViewById(R.id.three_dots)

        languageModalBottomSheet.setOnClickListener{
            val languageModalBottomSheet = LanguageModalBottomSheet()
            languageModalBottomSheet.show(childFragmentManager, ExpressModalBottomSheet.TAG)
        }

        threeDots.setOnClickListener{
            val popupMenu=PopupMenu(context,it)
            popupMenu.menuInflater.inflate(R.menu.overflow_menu, popupMenu.menu);
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.need_help -> startActivity(Intent(context, NeedHelpActivity::class.java))
                    R.id.about_us -> startActivity(Intent(context, AboutUsActivity::class.java))
                    R.id.tnc -> startActivity(Intent(context, TermsConditionsActivity::class.java))
                }
                true
            }
            popupMenu.show()
        }


        ironingCardView.setOnClickListener{
           val intent=Intent(context,ShopListActivity::class.java)
            intent.putExtra("typeOfService","ironing")
                .putExtra("Express","false")
            startActivity(intent)
        }

        dryCleaningCardView.setOnClickListener{
            val intent=Intent(context,ShopListActivity::class.java)
            intent.putExtra("typeOfService","dryCleaning")
                .putExtra("Express","false")
            startActivity(intent)
        }

        laundryCardView.setOnClickListener{
            val intent=Intent(context,ShopListActivity::class.java)
            intent.putExtra("typeOfService","laundry")
                .putExtra("Express","false")
            startActivity(intent)
        }

        steamIroningCardView.setOnClickListener{
            val intent=Intent(context,ShopListActivity::class.java)
            intent.putExtra("typeOfService","steam_ironing")
                .putExtra("Express","false")
            startActivity(intent)
        }
        return root
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

