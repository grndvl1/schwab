package com.matthewscorp.schwab.interview.activities

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.*
import com.matthewscorp.schwab.interview.R
import com.matthewscorp.schwab.interview.data.PizzaList
import com.matthewscorp.schwab.interview.data.PizzaSet
import com.matthewscorp.schwab.interview.data.Status
import com.matthewscorp.schwab.interview.viewmodels.PizzaViewModel
import kotlinx.android.synthetic.main.activity_scrolling.*
import kotlinx.android.synthetic.main.content_scrolling.*
import org.koin.android.viewmodel.ext.android.viewModel

class ScrollingActivity : AppCompatActivity() {

    private val pizzaSetViewModel: PizzaViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        // Grab the data now, DI with ViewModels
        val pizzaData = pizzaSetViewModel.getPizzaData()
        pizzaData.observe(this, Observer {resource ->
            when (resource?.status) {
                Status.LOADING -> {
                    //show Progress
                }
                Status.ERROR -> {
                    //Show Error
                }
                Status.SUCCESS -> {
                    resource.data?.let {
                        bindViews(it)
                        val adapter = MyPizzaAdapter(it.resources)
                        rv_scrolling_activity.adapter = adapter
                        pizzaData.removeObservers(this)
                    }
                }
            }
        })
        pizzaSetViewModel.init("pizza", "41.6271023,-88.2355845")
    }

    private fun bindViews(data: PizzaSet) {

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    internal inner class MyPizzaAdapter(private val items: List<PizzaList>) : RecyclerView.Adapter<MyPizzaAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.row_scrolling_activity_list, parent, false)
            return ViewHolder(v)
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getItemViewType(position: Int): Int {
            return position
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items.get(position)
            holder.itemView.setOnClickListener{
                startActivity(Intent())
            }
        }


        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        }
    }


}
