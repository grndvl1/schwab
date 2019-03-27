package com.matthewscorp.schwab.interview.activities

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.*
import com.afollestad.materialdialogs.MaterialDialog
import com.matthewscorp.schwab.interview.R
import com.matthewscorp.schwab.interview.data.PizzaPlaces
import com.matthewscorp.schwab.interview.data.Status
import com.matthewscorp.schwab.interview.viewmodels.PizzaViewModel
import kotlinx.android.synthetic.main.activity_scrolling.*
import kotlinx.android.synthetic.main.content_scrolling.*
import org.koin.android.viewmodel.ext.android.viewModel
import android.text.InputType
import android.widget.TextView
import com.matthewscorp.schwab.interview.utils.formatWithHtml
import android.support.v7.widget.DividerItemDecoration
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import android.widget.Toast
import com.google.android.gms.common.GooglePlayServicesUtil
import android.app.Activity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest

class ScrollingActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private val TAG: String = "ScrollActivity"
    private val pizzaSetViewModel: PizzaViewModel by viewModel()
    private lateinit var mGoogleApiClient: GoogleApiClient
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationString: String = "41.6271023,-88.2355845"
    private var mLastLocation: Location? = null
    private lateinit var mLocationRequest: LocationRequest

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        setSupportActionBar(toolbar)
        if (checkGooglePlayServices()) {
            mGoogleApiClient = GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build()
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
        }
        fab.setOnClickListener { _ ->
            MaterialDialog.Builder(this)
                .title(R.string.enter_search)
                .content(R.string.enter_search_content)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(R.string.search_for_hint, R.string.blank) { _, input ->
                    pizzaSetViewModel.init(input.toString(), locationString)
                }.show()

            if (mGoogleApiClient.isConnected()) {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        location?.let {
                            locationString = "${it.latitude},${it.longitude}"
                        } ?: kotlin.run {
                            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
                        }
                    }
            }
        }
        rv_scrolling_activity.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        // Grab the data now, DI with ViewModels
        val pizzaData = pizzaSetViewModel.getPizzaData()
        pizzaData.observe(this, Observer {resource ->
            when (resource?.status) {
                Status.LOADING -> {
                    Snackbar.make(cl_activity_scrolling_main_container, "Loading Data...", Snackbar.LENGTH_SHORT).show()
                }
                Status.ERROR -> {
                    Snackbar.make(cl_activity_scrolling_main_container, "Error in returning Results", Snackbar.LENGTH_LONG).show()
                }
                Status.SUCCESS -> {
                    resource.data?.let {
                        rv_scrolling_activity.adapter = MyPizzaAdapter(this@ScrollingActivity, it.resources)
                        pizzaData.removeObservers(this)
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        // connect to location client
        mGoogleApiClient.connect()
    }

    override fun onPause() {
        // Disconnect from location client
        if (mGoogleApiClient.isConnected) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)
            mGoogleApiClient.disconnect()
        }
        super.onPause()
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

    internal inner class MyPizzaAdapter(private val context: Context, private val items: List<PizzaPlaces>) : RecyclerView.Adapter<MyPizzaAdapter.ViewHolder>() {
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
            holder.nameView.text = item.name
            holder.distanceView.text = "Distance: 1.5mi"
            holder.addressView.text = item.Address.formattedAddress
            holder.phoneView.text = item.PhoneNumber
            holder.urlView.text = "<a href='${item.Website}'>Company Website</a>".formatWithHtml()
            holder.urlView.setOnClickListener{
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(item.Website)
                startActivity(intent)
            }
            holder.itemView.setOnClickListener{
                startActivity(DetailActivity.getStartIntent(context, item))
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var nameView: TextView = itemView.findViewById(R.id.tv_activity_scrolling_name)
            var distanceView: TextView = itemView.findViewById(R.id.tv_activity_scrolling_distance)
            var addressView: TextView = itemView.findViewById(R.id.tv_activity_scrolling_address)
            var phoneView:TextView = itemView.findViewById(R.id.tv_activity_scrolling_phone)
            var urlView: TextView = itemView.findViewById(R.id.tv_activity_scrolling_url)
        }
    }


    /// Location services methods
    @SuppressLint("MissingPermission")
    override fun onConnected(bundle: Bundle?) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        mLastLocation?.let {
            locationString = "${it.latitude},${it.longitude}"
        } ?: kotlin.run {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
        }
    }

    override fun onConnectionSuspended(error: Int) {
        Log.d(TAG, "GoogleApiClient connection suspended")
    }

    override fun onConnectionFailed(resultFailed: ConnectionResult) {
        Log.d(TAG, "GoogleApiClient connection failed")
    }

    override fun onLocationChanged(location: Location?) {
        Log.d(TAG, "Location changed $location")
        location?.let {
            locationString = "${it.latitude},${it.longitude}"
        }
    }

    private fun checkGooglePlayServices(): Boolean {

        val checkGooglePlayServices = GooglePlayServicesUtil
            .isGooglePlayServicesAvailable(this)
        if (checkGooglePlayServices != ConnectionResult.SUCCESS) {
            /*
			* google play services is missing or update is required
			*  return code could be
			* SUCCESS,
			* SERVICE_MISSING, SERVICE_VERSION_UPDATE_REQUIRED,
			* SERVICE_DISABLED, SERVICE_INVALID.
			*/
            GooglePlayServicesUtil.getErrorDialog(
                checkGooglePlayServices,
                this, 200
            ).show()

            return false
        }

        return true

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 200) {

            if (resultCode == Activity.RESULT_OK) {
                mGoogleApiClient?.let {
                    // Make sure the app is not already connected or attempting to connect
                    if (it.isConnecting.not() && it.isConnected.not()) {
                        mGoogleApiClient?.connect()
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(
                    this, "Google Play Services must be installed.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }
}
