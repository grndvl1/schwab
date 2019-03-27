package com.matthewscorp.schwab.interview.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import com.matthewscorp.schwab.interview.R
import com.matthewscorp.schwab.interview.data.PizzaPlaces
import com.matthewscorp.schwab.interview.utils.formatWithHtml

import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.row_scrolling_activity_list.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val details = intent.getParcelableExtra<PizzaPlaces>(DETAIL_OBJECT)
        tv_activity_scrolling_name.text = details.name
        tv_activity_scrolling_address.text = details.Address.formattedAddress
        tv_activity_scrolling_phone.text = details.PhoneNumber
        tv_activity_scrolling_url.text = "<a href='${details.Website}'>Company Website</a>".formatWithHtml()
        tv_activity_scrolling_url.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(details.Website)
            startActivity(intent)
        }

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    companion object {
        const val DETAIL_OBJECT = "details"

        @JvmStatic
        fun getStartIntent(context: Context, details: PizzaPlaces): Intent {
            val intent = Intent(context.applicationContext, DetailActivity::class.java)
            intent.putExtra(DETAIL_OBJECT, details)
            return intent
        }
    }
}