package com.test.app.ui

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.c1ctech.mvvmwithnetworksource.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.test.app.MyApplication
import com.test.app.R
import com.test.app.api.RetrofitService
import com.test.app.repository.MainRepository
import com.test.app.repository.MyViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var progress: ProgressDialog
    private var etPassword: TextInputEditText? = null
    private var etEmail: TextInputEditText? = null
    lateinit var viewModel: MainViewModel

    private val retrofitService = RetrofitService.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val sharedPreferences: SharedPreferences = getSharedPreferences(
            MyApplication.appContext.packageName, Context.MODE_PRIVATE
        )

        if (sharedPreferences.getBoolean("is_logged_in", false)) {
            if (sharedPreferences.getBoolean("is_checkIn_Checkout_selected", false)) {
                openCheckInCheckout()
            } else {
                openNextScreen()
            }
        }

        val textview = findViewById<Button>(R.id.btn_submit)
        etEmail = findViewById<TextInputEditText>(R.id.til_email)
        etPassword = findViewById<TextInputEditText>(R.id.til_password)

        //get viewmodel instance using ViewModelProvider.Factory
        viewModel =
            ViewModelProvider(
                this,
                MyViewModelFactory(MainRepository(retrofitService, null))
            )[MainViewModel::class.java]

        //the observer will only receive events if the owner(activity) is in active state
        //invoked when movieList data changes

        viewModel.movieList.observe(this, Observer {
//            Log.d("MainActivity", "movieList: $it")
//            adapter.setMovieList(it)
            progress.dismiss()
            if (it != null && it.success == true) {
                saveAccessToken(it.data?.accessToken, it.data?.orgSlug)
                openNextScreen()
            } else {
                hideKeyboard(textview)
                val snack = Snackbar.make(
                    textview,
                    "Something went wrong ,please try again later",
                    Snackbar.LENGTH_LONG
                )
                snack.show()
            }

        })

        //invoked when a network exception occurred
        viewModel.errorMessage.observe(this, Observer {
//            Log.d("MainActivity", "errorMessage: $it")
            progress.dismiss()
            hideKeyboard(textview)
            val snack = Snackbar.make(textview, it, Snackbar.LENGTH_LONG)
            snack.show()
        })
        viewModel.failure.observe(this, Observer {
            progress.dismiss()
            if (it == 401) {
                val sharedPreferences: SharedPreferences =
                    this.getSharedPreferences(packageName, Context.MODE_PRIVATE)
                val editor: SharedPreferences.Editor = sharedPreferences.edit()


                editor.putBoolean("is_logged_in", false)
                editor.apply()
                editor.commit()
                Toast.makeText(
                    this@MainActivity,
                    "User is not unauthorized to proceed",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })

        textview.setOnClickListener {
            hideKeyboard(textview)
            if (isValid()) {
                viewModel.getAllMovies(etEmail?.text.toString(), etPassword?.text.toString())
                showProgress()
            } else {
                if (TextUtils.isEmpty(etEmail?.text)) {
                    val snack =
                        Snackbar.make(it, "Please enter e-mail address", Snackbar.LENGTH_LONG)
                    snack.show()
                } else if (!Patterns.EMAIL_ADDRESS.matcher(etEmail?.text).matches()) {
                    val snack =
                        Snackbar.make(it, "Please enter valid e-mail address", Snackbar.LENGTH_LONG)
                    snack.show()
                } else {
                    val snack =
                        Snackbar.make(it, "Please enter Password address", Snackbar.LENGTH_LONG)
                    snack.show()
                }
            }
        }
    }

    private fun openNextScreen() {
        val intent = Intent(this@MainActivity, ChooseTimeSloActivity::class.java)
        intent.putExtra("key", "Kotlin")

        startActivity(intent)
        finish()
    }

    private fun openCheckInCheckout() {
        val sharedPreferences: SharedPreferences = getSharedPreferences(
            MyApplication.appContext.packageName, Context.MODE_PRIVATE
        )
        var checkInName = sharedPreferences.getString("check_in_name", "")
        var checkInBeaconId = sharedPreferences.getString("check_in_id", "")
        var checkOutName = sharedPreferences.getString("check_out_name", "")
        var checkOutBeaconId = sharedPreferences.getString("check_out_id", "")

        val intent = Intent(this@MainActivity, ShowTimeSlotActivity::class.java)
        intent.putExtra("key", "Kotlin")

        intent.putExtra("check_in_time", checkInName)
        intent.putExtra("check_in_time_id", let { checkInBeaconId })
        intent.putExtra("check_OUT_time_id", let { checkOutName })
        intent.putExtra("check_OUT_time", let { checkOutBeaconId })

        startActivity(intent)
        finish()
    }

    private fun saveAccessToken(it: String?, orgSlug: String?) {
        val sharedPreferences: SharedPreferences =
            this.getSharedPreferences(packageName, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        editor.putString("access_token", it)
        editor.putString("org_slug", orgSlug)
        editor.putBoolean("is_logged_in", true)
        editor.apply()
        editor.commit()
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun isValid(): Boolean {
        if (!TextUtils.isEmpty(etEmail?.text) && Patterns.EMAIL_ADDRESS.matcher(etEmail?.text)
                .matches() && !TextUtils.isEmpty(etPassword?.text)
        ) {
            return true
        }
        return false

    }

    private fun showProgress() {
        progress = ProgressDialog(this)
        progress.setTitle("Loading")
        progress.setMessage("Wait while loading...")
        progress.setCancelable(false) // disable dismiss by tapping outside of the dialog

        progress.show()

    }
}