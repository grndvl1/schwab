package com.matthewscorp.schwab.interview.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import com.matthewscorp.schwab.interview.R
import com.matthewscorp.schwab.interview.data.PizzaPlaces

import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val details = intent.getParcelableExtra<PizzaPlaces>(DETAIL_OBJECT)

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