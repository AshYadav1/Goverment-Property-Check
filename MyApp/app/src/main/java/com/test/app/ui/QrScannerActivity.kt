package com.test.app.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.wifi.WifiManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.budiyev.android.codescanner.*
import com.google.android.material.snackbar.Snackbar
import com.test.app.R
import com.test.app.adapter.DataShowAdapter
import com.test.app.api.RetrofitService
import com.test.app.model.QrResponse
import com.test.app.repository.MainRepository
import com.test.app.repository.MyViewModelFactory
import com.test.app.viewmodel.QrCodeInfoGetViewModel
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*


class QrScannerActivity : AppCompatActivity() {


    private var logout: Button? = null
    private var ivBack: ImageView? = null

    //    private lateinit var viewModelFireBase: QrCodeForFirebase
    private var mLocationChooseTime: String? = null
    private var mUserChoosedtime: String? = null

    //    private lateinit var viewModelForFirbase: QrCodeForFirebase
    private var mSelectedType: Int = 0
    private var mSelectedUserId: String? = null
    private var mCheckInOutId: String? = null
    private var mCheckInTimeId: String? = null
    private lateinit var mAdapter: DataShowAdapter
    private var mRecyclerView: RecyclerView? = null
    private var mUserName: String? = null
    private var mCheckOutTime: String? = null
    private var mCheckInTime: String? = null
    private var tvCount: TextView? = null
    private lateinit var mListOfSelectedUrl: MutableSet<String>
    private lateinit var mListOfData: MutableList<QrResponse>
    private lateinit var codeScanner: CodeScanner

    private lateinit var viewModel: QrCodeInfoGetViewModel

    private val retrofitService = RetrofitService.getInstance()
    private val retrofitServiceFirebase = RetrofitService.getInstanceForFirebase()
    private val retrofitServiceMoveItem = RetrofitService.getInstanceForMoveItem()

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_scanner)
//        mutableListOf<Data>()
        mListOfSelectedUrl = mutableSetOf()

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mCheckInTime = intent.getStringExtra("check_in_time")
        mUserChoosedtime = intent.getStringExtra("user_time")
        mLocationChooseTime = intent.getStringExtra("location_choose_time")
        mCheckOutTime = intent.getStringExtra("check_OUT_time")
        mUserName = intent.getStringExtra("user")
        mCheckInTimeId = intent.getStringExtra("check_in_time_id")
        mCheckInOutId = intent.getStringExtra("check_OUT_time_id")
        mSelectedUserId = intent.getStringExtra("user_id")//getStringExtra("user_id")
        mSelectedType = intent.getIntExtra("selected_type", 0)
        logout = findViewById<Button>(R.id.log_out)
        viewModel =
            ViewModelProvider(
                this,
                MyViewModelFactory(
                    MainRepository(
                        retrofitService,
                        retrofitServiceFirebase,
                        retrofitServiceMoveItem
                    )
                )
            )[QrCodeInfoGetViewModel::class.java]
//        viewModelFireBase =
//            ViewModelProvider(
//                this,
//                MyViewModelFactory(MainRepository(retrofitService,retrofitServiceFirebase,retrofitServiceMoveItem))
//            )[QrCodeForFirebase::class.java]


        val mId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

        val wifiMgr = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiMgr.connectionInfo

//        val ip: String = Formatter.formatIpAddress(wifiMgr.getConnectionInfo().getIpAddress())
        val MAC_ADDRESS = wifiInfo.macAddress

        var uniqueId = UUID.randomUUID().toString();
        val ipAddress = getLocalIpAddress()


        viewModel.movieList.observe(this) {
            it.data?.let { it1 ->
                it1.time = Calendar.getInstance().timeInMillis.toString()
                mListOfData.add(it1)
            }
            codeScanner.startPreview()
            mAdapter.notifyItemChanged(mListOfData.size)
        }

        viewModel.errorMessage.observe(this) {
            Toast.makeText(this@QrScannerActivity, it, Toast.LENGTH_LONG).show()
            val intent = Intent(this@QrScannerActivity, ShowTimeSlotActivity::class.java)
            intent.putExtra(
                "check_in_time",
                mCheckInTime
            )
            intent.putExtra(
                "check_in_time_id",
                mCheckInTimeId
            )
            intent.putExtra(
                "check_OUT_time_id",
                mCheckInOutId
            )
            intent.putExtra(
                "check_OUT_time",
                mCheckInOutId
            )
            intent.putExtra("check_OUT_time", mCheckOutTime)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)

        }
        viewModel.doneObserver.observe(this) {
            val intent = Intent(this@QrScannerActivity, ShowTimeSlotActivity::class.java)

            intent.putExtra(
                "check_in_time",
                mCheckInTime
            )
            intent.putExtra(
                "check_in_time_id",
                mCheckInTimeId
            )
            intent.putExtra(
                "check_OUT_time_id",
                mCheckInOutId
            )
            intent.putExtra(
                "check_OUT_time",
                mCheckOutTime
            )
            intent.putExtra("check_OUT_time", mCheckOutTime)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        }


        viewModel.showPopUp.observe(this) {
            if (true){
                showPopUp()
            }
        }


        val textview = findViewById<Button>(R.id.btn_submit)
        mRecyclerView = findViewById(R.id.rc_data)
        mListOfData = mutableListOf()
        ivBack = findViewById<ImageView>(R.id.iv_back)
        ivBack?.visibility = View.VISIBLE
        ivBack?.setOnClickListener {
            onBackPressed()
        }

        mRecyclerView?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true)
        mAdapter = DataShowAdapter(this, mListOfData)
        mRecyclerView?.adapter = mAdapter
        textview.setOnClickListener {


            if (mListOfData.size > 0) {
                viewModel.hirFirebaseId(
                    uniqueId,
                    MAC_ADDRESS,
                    ipAddress,
                    mId,
                    mSelectedType,
                    mCheckInTimeId,
                    mCheckInOutId,
                    mUserName,
                    mSelectedUserId,
                    mUserChoosedtime,
                    mLocationChooseTime,
                    mListOfData
                )
            } else {
                val snack = textview?.let { it1 ->
                    Snackbar.make(
                        it1,
                        "Please scan at-least on code ",
                        Snackbar.LENGTH_LONG
                    )
                }
                snack?.show()
            }

//            val intent = Intent(this@QrScannerActivity, ChooseTimeSloActivity::class.java)
//
//            startActivity(intent)
//             finish()
        }

        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)
        tvCount = findViewById(R.id.tv_count)
        logout?.setOnClickListener {
            val sharedPreferences: SharedPreferences = this.getSharedPreferences(
                packageName,
                Context.MODE_PRIVATE
            )
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.clear()
            editor.commit()
            val intent = Intent(this@QrScannerActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent)
        }

        codeScanner = CodeScanner(this, scannerView)

        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not


        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                if (!mListOfSelectedUrl.contains(it.text)) {
                    val baseUrl = it.text.substring(it.text.lastIndexOf("/") + 1, it.text.length)
                    if (it.text.length >= 32) {
                        it.text
                        viewModel.getDataFromText(baseUrl)
                        mListOfSelectedUrl.add(it.text)
                    } else {
                        Toast.makeText(
                            this@QrScannerActivity,
                            "This Code is not valid",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@QrScannerActivity,
                        "This QR Code Already Scanned",
                        Toast.LENGTH_LONG
                    ).show()
                    codeScanner.startPreview()
                }
                tvCount?.text = "Items ".plus(mListOfSelectedUrl.size)
            }
        }

        viewModel.failure.observe(this) {

            if (it == 401) {

                val sharedPreferences: SharedPreferences = this.getSharedPreferences(
                    packageName,
                    Context.MODE_PRIVATE
                )
                val editor: SharedPreferences.Editor = sharedPreferences.edit()


                editor.putBoolean("is_logged_in", false)

                editor.apply()

                Toast.makeText(
                    this@QrScannerActivity,
                    "User is not unauthorized to proceed",
                    Toast.LENGTH_SHORT
                ).show()

                val intent = Intent(this@QrScannerActivity, MainActivity::class.java)
//            intent.putExtra("key", "Kotlin")
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent)

                finish()
            }

        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(
                    this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }


    private fun showPopUp() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_layout)
        val yesBtn = dialog.findViewById(R.id.btn_yes) as Button
        yesBtn.setOnClickListener {
            finish()
            dialog.dismiss()
        }

        dialog.show()

    }



    fun getIPAddress(useIPv4: Boolean): String? {
        try {
            val interfaces: List<NetworkInterface> =
                Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                val addrs: List<InetAddress> = Collections.list(intf.inetAddresses)
                for (addr in addrs) {
                    if (!addr.isLoopbackAddress) {
                        val sAddr = addr.hostAddress.uppercase(Locale.getDefault())
                        val isIPv4: Boolean = false
                        if (useIPv4) {
                            if (isIPv4) return sAddr
                        } else {
                            if (!isIPv4) {
                                val delim = sAddr.indexOf('%') // drop ip6 port suffix
                                return if (delim < 0) sAddr else sAddr.substring(0, delim)
                            }
                        }
                    }
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun getLocalIpAddress(): String? {


        try {
            val en = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val enumIpAddress = en.nextElement().inetAddresses
                while (enumIpAddress.hasMoreElements()) {
                    val inetAddress = enumIpAddress.nextElement()
                    if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                        return inetAddress.hostAddress
                    }
                }
            }
        } catch (ex: SocketException) {
            ex.printStackTrace()
        }
        return null
    }

//    fun getLocalIpAddress(): String? {
//        try {
//            val en = NetworkInterface.getNetworkInterfaces()
//            while (en.hasMoreElements()) {
//                val intf = en.nextElement()
//                val enumIpAddr = intf.inetAddresses
//                while (enumIpAddr.hasMoreElements()) {
//                    val inetAddress = enumIpAddr.nextElement()
//                    if (!inetAddress.isLoopbackAddress) {
//                        val ip: String = Formatter.formatIpAddress(inetAddress.hashCode())
//                        Log.i(TAG, "***** IP=$ip")
//                        return ip
//                    }
//                }
//            }
//        } catch (ex: SocketException) {
//            Log.e(TAG, ex.toString())
//        }
//        return null
//    }


    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
}