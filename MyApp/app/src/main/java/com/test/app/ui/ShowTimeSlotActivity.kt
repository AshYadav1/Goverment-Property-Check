package com.test.app.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.test.app.R
import com.test.app.adapter.UserListAdapter
import com.test.app.api.RetrofitService
import com.test.app.model.User
import com.test.app.repository.MainRepository
import com.test.app.repository.MyViewModelFactory
import com.test.app.viewmodel.ShowLocationDataViewModel
import java.util.Calendar


class ShowTimeSlotActivity : AppCompatActivity() {
    private var ivBack: ImageView? = null
    private lateinit var mChooseLocationTime: String
    private var mCheckInOutId: String? = null
    private var mCheckInTimeId: String? = null
    private var isPermitted: Boolean = false
    private var btnSubmit: Button? = null
    private lateinit var mCheckOutCard: CardView
    private lateinit var mCheckinCard: CardView
    private lateinit var logout: Button
    private lateinit var progress: ProgressDialog
    private var mLocationList: MutableList<User>? = null
    private lateinit var mCheckOutTimeTextView: TextView
    private lateinit var mCheckInTimeTextView: TextView
    private lateinit var mChooseInObject: User
    private var mCheckOutTime: String? = ""
    private var mCheckInTime: String? = ""
    private lateinit var mSpinnerPeople: AppCompatSpinner
    private lateinit var mList: Array<String>
    private lateinit var viewModel: ShowLocationDataViewModel

    private val retrofitService = RetrofitService.getInstance()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_time_slot)

        mCheckInTime = intent.getStringExtra("check_in_time")
        mCheckInTimeId = intent.getStringExtra("check_in_time_id")
        mCheckInOutId = intent.getStringExtra("check_OUT_time_id")
        mCheckOutTime = intent.getStringExtra("check_OUT_time")

        viewModel =
            ViewModelProvider(
                this,
                MyViewModelFactory(MainRepository(retrofitService,null))
            )[ShowLocationDataViewModel::class.java]


        mChooseInObject = User()
        mChooseInObject.firstName = "Choose"
        mChooseInObject.lastName = "People"
        viewModel.getAllMovies()
        showProgress()

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
                    this@ShowTimeSlotActivity,
                    "User is not unauthorized to proceed",
                    Toast.LENGTH_SHORT
                ).show()

                val intent = Intent(this@ShowTimeSlotActivity, MainActivity::class.java)
//            intent.putExtra("key", "Kotlin")
                startActivity(intent)
                finish()
            }

        }

        viewModel.movieList.observe(this, Observer { it ->
            Log.d("MainActivity", "movieList: $it")
//            adapter.setMovieList(it)

            Log.d("MainActivity", "movieList: $it")
//            adapter.setMovieList(it)
            progress.dismiss()

            mLocationList = it as MutableList<User>?
            mLocationList?.sortBy {
                it.firstName
            }

            if (mLocationList == null) {
                val snack = mSpinnerPeople.let { it1 ->
                    Snackbar.make(
                        it1,
                        "Something went wrong please try later",
                        Snackbar.LENGTH_LONG
                    )
                }
                snack.show()
                return@Observer
            }

            if (!mLocationList?.contains(mChooseInObject)!!) {
                mLocationList?.add(0, mChooseInObject)
            }
            val mAdapter = UserListAdapter(this, mLocationList as ArrayList<User>)


            mSpinnerPeople.adapter = mAdapter
        })


        viewModel.errorMessage.observe(this, Observer {
            Log.d("MainActivity", "errorMessage: $it")
        })

        mSpinnerPeople = findViewById(R.id.acp_people)
        mCheckinCard = findViewById(R.id.cv_check_in)
        mCheckOutCard = findViewById(R.id.cv_check_out)
        mCheckInTimeTextView = findViewById(R.id.tv_check_in)
        mCheckOutTimeTextView = findViewById(R.id.tv_check_out)
        mCheckInTimeTextView.text = mCheckInTime
        mCheckOutTimeTextView.text = mCheckOutTime
        btnSubmit = findViewById<Button>(R.id.btn_submit)

        logout = findViewById<Button>(R.id.log_out)
        ivBack = findViewById<ImageView>(R.id.iv_back)
        ivBack?.visibility=View.GONE
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        mCheckinCard.setOnClickListener(View.OnClickListener {
            mCheckOutCard.visibility = View.GONE
           mChooseLocationTime= Calendar.getInstance().timeInMillis.toString()
            mSpinnerPeople.visibility = View.VISIBLE
            btnSubmit?.visibility = View.VISIBLE

        })
        mCheckOutCard.setOnClickListener(View.OnClickListener {
            mCheckinCard.visibility = View.GONE
            mChooseLocationTime= Calendar.getInstance().timeInMillis.toString()

            mSpinnerPeople.visibility = View.VISIBLE
            btnSubmit?.visibility = View.VISIBLE
        })

        logout.setOnClickListener {
            val sharedPreferences: SharedPreferences = this.getSharedPreferences(
                packageName,
                Context.MODE_PRIVATE
            )
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.clear()
            editor.commit()
            val intent = Intent(this@ShowTimeSlotActivity, MainActivity::class.java)
            startActivity(intent)
        }

        btnSubmit?.setOnClickListener(View.OnClickListener { it ->

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                checkPermissionForCameraUse()
            }
            else {
                if (mSpinnerPeople.selectedItemPosition > 0) {
                    val intent = Intent(this@ShowTimeSlotActivity, QrScannerActivity::class.java)
                    intent.putExtra(
                        "user",
                        mLocationList?.get(mSpinnerPeople.selectedItemPosition)?.lastName + " " + mLocationList?.get(
                            mSpinnerPeople.selectedItemPosition
                        )?.lastName
                    )
                    intent.putExtra(
                        "user_id",
                        mLocationList?.get(mSpinnerPeople.selectedItemPosition)?.personId
                    )
                    intent.putExtra(
                        "user_time",
                        Calendar.getInstance().timeInMillis.toString()
                    )
                    intent.putExtra(
                        "location_choose_time",
                       mChooseLocationTime
                    )

                    intent.putExtra(
                        "selected_type",
                        getSelectedType()
                    )

                    intent.putExtra(
                        "check_in_time",
                        mCheckInTime
                    )
                    intent.putExtra("check_in_time_id",mCheckInTimeId
                      )
                    intent.putExtra("check_OUT_time_id",mCheckInOutId
                       )
                    intent.putExtra(
                        "check_OUT_time",
                        mCheckOutTime
                    )
                    startActivity(intent)
//                    finish()
                } else {
                    val snack =
                        Snackbar.make(it, "Please Choose User from list ", Snackbar.LENGTH_LONG)
                    snack.show()
                }
            }



        })

        mSpinnerPeople.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

    private fun getSelectedType(): Int? {
       return  if(mCheckinCard.visibility==View.VISIBLE)
       {
           1
       }
         else{
             2
       }

    }

    private fun checkPermissionForCameraUse() {

        requestPermissions(arrayOf(Manifest.permission.CAMERA), 101)


    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode === 101) {
            for (i in grantResults.indices) {
                val permission = permissions[i]
                isPermitted = grantResults[i] === PackageManager.PERMISSION_GRANTED
                if (grantResults[i] === PackageManager.PERMISSION_DENIED) {
                    // user rejected the permission
                    val showRationale = shouldShowRequestPermissionRationale(permission)
                    if (!showRationale) {
                        val snack = mSpinnerPeople.let { it1 ->
                            Snackbar.make(
                                it1,
                                "Please allow camera permission for use the device ",
                                Snackbar.LENGTH_LONG
                            )
                        }
                        snack?.show()
                        //execute when 'never Ask Again' tick and permission dialog not show
                    } else {
                        val snack = mSpinnerPeople.let { it1 ->
                            Snackbar.make(
                                it1,
                                "Please allow camera permission for use the device ",
                                Snackbar.LENGTH_LONG
                            )
                        }
                        snack?.show()
//                        if (openDialogOnce) {
//                            alertView()
//                        }
                    }
                }
            }
            if (isPermitted) {
                btnSubmit?.performClick()
            }
        }

    }

    private fun showProgress() {
        progress = ProgressDialog(this)
        progress.setTitle("Loading")
        progress.setMessage("Wait while loading...")
        progress.setCancelable(false) // disable dismiss by tapping outside of the dialog

        progress.show()

    }


}