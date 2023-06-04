package com.test.app.ui

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.test.app.R
import com.test.app.adapter.LocationListAdapter
import com.test.app.adapter.LocationOutListAdapter
import com.test.app.api.RetrofitService
import com.test.app.model.Data
import com.test.app.repository.MainRepository
import com.test.app.repository.MyViewModelFactory
import com.test.app.viewmodel.ChooseLocationViewModel
import java.util.*


class ChooseTimeSloActivity : AppCompatActivity() {
    private lateinit var progress: ProgressDialog
    private lateinit var mChooseInObject: Data
    private lateinit var mChooseOutObject: Data
    private var mLocationList: MutableList<Data>? = null
    private var mLocationListLocationOut: MutableList<Data>? = null

    private var mSpinnerCheckOut: AppCompatSpinner? = null
    private var mSpinnerCheckIn: AppCompatSpinner? = null

    //    private lateinit var mList: Array<String>
    private lateinit var viewModel: ChooseLocationViewModel

    private val retrofitService = RetrofitService.getInstance()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_time_slo)

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mChooseInObject = Data()
        mChooseOutObject = Data()
        mChooseInObject.name = "Check in to item location"
        mChooseOutObject.name = "Checkout of item location"

        viewModel =
            ViewModelProvider(
                this,
                MyViewModelFactory(MainRepository(retrofitService, null))
            )[ChooseLocationViewModel::class.java]


        viewModel.failure.observe(this) {

            if (it == 401) {


                val sharedPreferences: SharedPreferences = this.getSharedPreferences(
                    packageName,
                    Context.MODE_PRIVATE
                )
                val editor: SharedPreferences.Editor = sharedPreferences.edit()


                editor.putBoolean("is_logged_in", false)
                editor.apply()
                editor.commit()
                Toast.makeText(
                    this@ChooseTimeSloActivity,
                    "User is not unauthorized to proceed",
                    Toast.LENGTH_SHORT
                ).show()

                val intent = Intent(this@ChooseTimeSloActivity, MainActivity::class.java)
//            intent.putExtra("key", "Kotlin")
                startActivity(intent)
                finish()
            }

        }
        viewModel.movieList.observe(this, Observer {
            Log.d("MainActivity", "movieList: $it")
//            adapter.setMovieList(it)
            progress.dismiss()



            if (it == null) {
                val snack = mSpinnerCheckIn?.let { it1 ->
                    Snackbar.make(
                        it1,
                        "Something went wrong please try later",
                        Snackbar.LENGTH_LONG
                    )
                }
                snack?.show()
                return@Observer
            }

            mLocationList = mutableListOf()
            mLocationListLocationOut = mutableListOf()
            for (item in it as MutableList<Data>) {
                mLocationList!!.add(item)
                mLocationListLocationOut!!.add(item)
            }


            var list = mLocationList as ArrayList<Data>
            var newList = ArrayList<Data>()
            for (i in list) {
                if (!i.name.isNullOrEmpty()) {
                    newList.add(i)
                }
            }
            Collections.sort(newList, Comparator<Data> { obj1, obj2 ->
                // ## Ascending order
                obj1.name!!.toString().toLowerCase()!!
                    .compareTo("" + obj2.name.toString().toLowerCase()!!)
            })

            if (!newList?.contains(mChooseInObject)!!) {
                newList?.add(0, mChooseInObject)
            }
            mLocationList = newList
            val mAdapter = LocationListAdapter(this, newList)
            mSpinnerCheckIn?.adapter = mAdapter


            val listOut = mLocationListLocationOut as ArrayList<Data>
            var newListOut = ArrayList<Data>()
            for (i in listOut) {
                if (!i.name.isNullOrEmpty()) {
                    newListOut.add(i)
                }
            }
            Collections.sort(newListOut, Comparator<Data> { obj1, obj2 ->
                // ## Ascending order
//                obj1.name!!.compareTo("" + obj2.name!!)
                obj1.name!!.toString().toLowerCase()!!
                    .compareTo("" + obj2.name.toString().toLowerCase()!!)
            })
            if (!newListOut?.contains(mChooseOutObject)!!) {
                newListOut?.add(0, mChooseOutObject)
            }
            mLocationListLocationOut = newListOut
            val mAdapterOut = LocationOutListAdapter(this, newListOut)
            mSpinnerCheckOut?.adapter = mAdapterOut
        })

        viewModel.getAllMovies()
        showProgress()

        //invoked when a network exception occurred
        viewModel.errorMessage.observe(this, Observer {
            Log.d("MainActivity", "errorMessage: $it")
            progress.dismiss()
        })


        mSpinnerCheckIn = findViewById(R.id.acp_check_in)
        mSpinnerCheckOut = findViewById(R.id.acp_check_out)
        val btnSubmit = findViewById<Button>(R.id.btn_submit)



        btnSubmit.setOnClickListener(View.OnClickListener {
            if (mSpinnerCheckIn!!.selectedItemPosition == mSpinnerCheckOut!!.selectedItemPosition){
                Toast.makeText(
                    this@ChooseTimeSloActivity,
                    "Checkin and checkout can't be same",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }

            if (mSpinnerCheckIn?.selectedItemPosition!! > 0 && mSpinnerCheckOut?.selectedItemPosition!! > 0) {
                val intent = Intent(this@ChooseTimeSloActivity, ShowTimeSlotActivity::class.java)
                saveLocation(mLocationList)
                intent.putExtra("check_in_time",
                    mSpinnerCheckIn?.selectedItemPosition?.let { mLocationList?.get(it)?.name })
                intent.putExtra("check_in_time_id",
                    mSpinnerCheckIn?.selectedItemPosition?.let { mLocationList?.get(it)?.beaconId })
                intent.putExtra("check_OUT_time_id",
                    mSpinnerCheckOut?.selectedItemPosition?.let { mLocationListLocationOut?.get(it)?.beaconId })
                intent.putExtra("check_OUT_time",
                    mSpinnerCheckOut?.selectedItemPosition?.let { mLocationListLocationOut?.get(it)?.name })
                startActivity(intent)
                finish()
            } else {
                if (mSpinnerCheckIn?.selectedItemPosition!! <= 0) {
                    val snack =
                        Snackbar.make(it, "Please Choose Check in location", Snackbar.LENGTH_LONG)
                    snack.show()
                } else {
                    val snack =
                        Snackbar.make(it, "Please Choose Checkout location", Snackbar.LENGTH_LONG)
                    snack.show()
                }
            }
        })

        mSpinnerCheckOut?.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }

    }

    @SuppressLint("SuspiciousIndentation")
    private fun saveLocation(it: MutableList<Data>?) {
        val sharedPreferences: SharedPreferences =
            this.getSharedPreferences(packageName, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        mSpinnerCheckIn?.selectedItemPosition?.let {
            editor.putString("check_in_name", mLocationList?.get(it)?.name)
        }
        mSpinnerCheckIn?.selectedItemPosition?.let {
            editor.putString("check_in_id", mLocationList?.get(it)?.beaconId)
        }
        mSpinnerCheckOut?.selectedItemPosition?.let {
            editor.putString("check_out_name", mLocationList?.get(it)?.name)
        }
        mSpinnerCheckOut?.selectedItemPosition?.let {
            editor.putString("check_out_id", mLocationList?.get(it)?.beaconId)
        }
        editor.putBoolean("is_checkIn_Checkout_selected", true)
        editor.apply()
        editor.commit()
    }

    private fun showProgress() {
        progress = ProgressDialog(this)
        progress.setTitle("Loading")
        progress.setMessage("Wait while loading...")
        progress.setCancelable(false) // disable dismiss by tapping outside of the dialog

        progress.show()

    }
}