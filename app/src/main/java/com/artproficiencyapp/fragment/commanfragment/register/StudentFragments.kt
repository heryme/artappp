package com.artproficiencyapp.fragment.commanfragment.register

import android.Manifest
import android.annotation.TargetApi
import android.app.Dialog
import android.app.KeyguardManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import android.support.annotation.RequiresApi
import android.support.v13.app.ActivityCompat
import android.text.SpannableString
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.StyleSpan
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.artproficiencyapp.R
import com.artproficiencyapp.activity.LoginActivity
import com.artproficiencyapp.adapter.CountryCodeAdapter
import com.artproficiencyapp.common.FingerprintHandler
import com.artproficiencyapp.extension.*
import com.artproficiencyapp.fragment.commanfragment.BaseFragment
import com.artproficiencyapp.model.CountryCodeModel
import com.artproficiencyapp.model.DirectorModel
import com.artproficiencyapp.model.FingerScanModel
import com.artproficiencyapp.restapi.ApiInitialize
import com.artproficiencyapp.restapi.ApiRequest
import com.artproficiencyapp.restapi.ApiResponseInterface
import com.artproficiencyapp.restapi.ApiResponseManager
import com.artproficiencyapp.utils.Validator
import com.facebook.internal.FacebookRequestErrorClassification

import kotlinx.android.synthetic.main.fragment_student_signup.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.security.*
import java.security.cert.CertificateException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey

/**
 * Created by admin on 25-May-18.
 */
class StudentFragments : BaseFragment(), View.OnClickListener, AdapterView.OnItemSelectedListener, ApiResponseInterface, FingerScanModel.TargetValueChagned {

    private val TAG = javaClass.simpleName
    private var isPasswordVisible = false
    private var isConfirmPasswordVisible = false
    private val countryCode = ArrayList<CountryCodeModel>()
    private var mSelectedDirector: String = ""
    private val directorList = ArrayList<String>()
    private lateinit var dirModel: DirectorModel
    private var profilePics: String = ""
    private var facebook_id: String = ""
    private lateinit var dialog: Dialog
    private var keyStore: KeyStore? = null
    // Variable used for storing the key in the Android Keystore container
    private var cipher: Cipher? = null
    val REQUEST_ID_MULTIPLE_PERMISSIONS = 1

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_student_signup, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        FingerScanModel.getInstance().setListener(this)
        InitView()
        FacebookLoginDataGet()
        SetClickListener()
        GetDirectorValues()
        facebook_id = sessionManager.get("facebook_key", "")
        Log.e(TAG, "Create facebook id" + facebook_id)


    }

    private fun FacebookLoginDataGet() {
        val facebook_id = sessionManager.get("facebook_key", "")
        val facebook_name = sessionManager.get("facebook_name", "")
        val facebook_email = sessionManager.get("facebook_email", "")
        val facebook_photo = sessionManager.get("facebook_profile_uri", "")

//        val facebook_id = activity.intent.extras!!.getString("facebook_key")
//        val facebook_name = activity.intent.extras!!.getString("facebook_name")
//        val facebook_email = activity.intent.extras!!.getString("facebook_email")


        Log.e(TAG, "sessionmanager value id==  " + facebook_id)
        Log.e(TAG, "sessionmanager value name==  " + facebook_name)
        Log.e(TAG, "sessionmanager value email==  " + facebook_email)
        Log.e(TAG, "sessionmanager value photo==" + facebook_photo)
        if (!TextUtils.isEmpty(facebook_id) && !TextUtils.isEmpty(facebook_name) && !TextUtils.isEmpty(facebook_email)) {
            etStuSignUpName.setText(facebook_name)
            etStuSignUpEmail.setText(facebook_email)
            hidePassword()

        } else {
            etStuSignUpName.setText("")
            etStuSignUpName.setText("")

        }
    }

    private fun hidePassword() {
        etStuSignUpPassword.gone()
        etStuSignUpConfirmPassword.gone()
        etPasswordText.gone()
    }



    private fun GetDirectorValues() {
        ApiRequest(activity, ApiInitialize.initialize(ApiInitialize.MAIN_URl_API).getDirectors(""), GET_DIRECTORS, true,
                this@StudentFragments)
    }

    private fun SetClickListener() {
        btnSignupStu.setOnClickListener(this)
        tvStuSignUpMsg.setOnClickListener(this)
    }

    private fun InitView() {
        etStuSignUpPassword.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= etStuSignUpPassword!!.right - etStuSignUpPassword!!.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                    // your action here
                    if (isPasswordVisible)
                        makePassowrdVisible(true)
                    else
                        makePassowrdVisible(false)
                    return@OnTouchListener true
                }
            }
            false
        })

        etStuSignUpConfirmPassword.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= etStuSignUpConfirmPassword!!.right - etStuSignUpConfirmPassword!!.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                    // your action here
                    if (isConfirmPasswordVisible)
                        makeConfirmPassowrdVisible(true)
                    else
                        makeConfirmPassowrdVisible(false)
                    return@OnTouchListener true
                }
            }
            false
        })

        val spannableString = SpannableString(getString(R.string.signup_msg))
        spannableString.setSpan(StyleSpan(Typeface.BOLD), spannableString.length - 7, spannableString.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        tvStuSignUpMsg!!.text = spannableString

        setCountryCode()
    }

    private fun setCountryCode() {
        try {
            val jsonArray = JSONArray(loadJsonFromAssets())
            for (i in 0 until jsonArray.length()) {
                val jObj = jsonArray.optJSONObject(i)
                val model = CountryCodeModel()
                model._mCountryCode = jObj.optString("code")
                model._mCountryName = jObj.optString("name")
                countryCode?.add(model)
            }
            var adapter = CountryCodeAdapter(activity, R.layout.row_country_code, countryCode)
            spStuSignupCountryCode.adapter = adapter
            spStuSignupCountryCode.onItemSelectedListener = this
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadJsonFromAssets(): String? {
        var json: String? = null
        try {
            val `is` = activity.assets.open("CountryCode.json")
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            json = String(buffer)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    private fun makePassowrdVisible(_mVisible: Boolean) {
        if (_mVisible) {
            isPasswordVisible = false
            etStuSignUpPassword!!.transformationMethod = HideReturnsTransformationMethod.getInstance()
        } else {
            isPasswordVisible = true
            etStuSignUpPassword!!.transformationMethod = PasswordTransformationMethod.getInstance()
        }
    }

    private fun makeConfirmPassowrdVisible(_mVisible: Boolean) {
        if (_mVisible) {
            isConfirmPasswordVisible = false
            etStuSignUpConfirmPassword!!.transformationMethod = HideReturnsTransformationMethod.getInstance()
        } else {
            isConfirmPasswordVisible = true
            etStuSignUpConfirmPassword!!.transformationMethod = PasswordTransformationMethod.getInstance()
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onClick(view: View) {
        when (view) {
            btnSignupStu -> {
                if (!TextUtils.isEmpty(facebook_id)) {
                    if (IsValidInputFaceBiookLogin()) {
                        if (isNetWork(activity)) {
                            ApiRequest(activity, ApiInitialize.initialize(ApiInitialize.MAIN_URl_API).registerStudent(etStuSignUpName.text.toString().trim(),
                                    etStuSignUpEmail.text.toString().trim(), countryCode.get(spStuSignupCountryCode.selectedItemPosition)._mCountryCode.plus(etStuSignUpPhoneNum.text.toString().trim()),
                                    "STUDENT", mSelectedDirector, "", "ANDROID", "123456", "Ahmedabad",
                                    "23.0000000", "75.0000000", "1", facebook_id), STUDENT_SIGNUP, true, this@StudentFragments)
                              //showMsgDialog("Data will be add ")
                        }

                    }

                } else {
                    if (IsValidInput()) {

                        if (isNetWork(activity)) {
                            ApiRequest(activity, ApiInitialize.initialize(ApiInitialize.MAIN_URl_API).registerStudent(etStuSignUpName.text.toString().trim(),
                                    etStuSignUpEmail.text.toString().trim(), countryCode.get(spStuSignupCountryCode.selectedItemPosition)._mCountryCode.plus(etStuSignUpPhoneNum.text.toString().trim()),
                                    "STUDENT", mSelectedDirector, etStuSignUpConfirmPassword.text.toString().trim(), "ANDROID", "123456", "Ahmedabad",
                                    "23.0000000", "75.0000000", "0", etStuSignUpEmail.text.toString()), STUDENT_SIGNUP, true, this@StudentFragments)
                        }
                           //showMsgDialog("Yeppiiee..... Sign up as Student successful...")

                    }
                }


            }

            tvStuSignUpMsg -> {
                activity.goActivity<LoginActivity>()
            }

        }
    }

    private fun IsValidInput(): Boolean {
        if (TextUtils.isEmpty(etStuSignUpName!!.text.toString())) {
            showSnackBar(etStuSignUpName, getString(R.string.error_name))
            return false
        } else if (TextUtils.isEmpty(etStuSignUpEmail!!.text.toString().trim { it <= ' ' })) {
            showSnackBar(etStuSignUpEmail, getString(R.string.error_email))
            return false
        } else if (TextUtils.isEmpty(etStuSignUpPhoneNum!!.text.toString())) {
            showSnackBar(etStuSignUpPhoneNum, getString(R.string.error_ph_num))
            return false
        } else if (TextUtils.isEmpty(etStuSignUpPassword!!.text.toString())) {
            showSnackBar(etStuSignUpPassword, getString(R.string.error_pwd))
            return false
        } else if (TextUtils.isEmpty(etStuSignUpConfirmPassword!!.text.toString())) {
            showSnackBar(etStuSignUpConfirmPassword, getString(R.string.error_confirm_pwd))
            return false
        } else if (!Validator.validateEmail(etStuSignUpEmail!!.text.toString().trim { it <= ' ' })) {
            showSnackBar(etStuSignUpEmail, getString(R.string.error_valid_email))
            return false
        } else if (etStuSignUpPhoneNum!!.text.toString().length < 10) {
            showSnackBar(etStuSignUpPhoneNum, getString(R.string.error_short_ph_num))
            return false
        } else if (etStuSignUpConfirmPassword!!.text.toString() != etStuSignUpPassword!!.text.toString()) {
            showSnackBar(etStuSignUpConfirmPassword, getString(R.string.error_pwd_diff))
            return false
        } else if (etStuSignUpPassword!!.text.toString().trim { it <= ' ' }.length < 6) {
            showSnackBar(etStuSignUpPassword, getString(R.string.error_pwd_msg))
            return false
        } else if (etStuSignUpConfirmPassword!!.text.toString().trim { it <= ' ' }.length < 6) {
            showSnackBar(etStuSignUpConfirmPassword, getString(R.string.error_pwd_msg))
            return false
        } else if (!Validator.isValidPassword(etStuSignUpPassword.text.toString().trim())) {
            showSnackBar(etStuSignUpPassword, getString(R.string.error_pwd_invalid_msg))
            return false
        } else if (!Validator.isValidPassword(etStuSignUpConfirmPassword.text.toString().trim())) {
            showSnackBar(etStuSignUpConfirmPassword, getString(R.string.error_pwd_invalid_msg))
            return false
        } else if (TextUtils.isEmpty(mSelectedDirector)) {
            showSnackBar(spStuChooseDir, getString(R.string.error_director_not_chosen))
            return false
        }
        return true
    }

    private fun IsValidInputFaceBiookLogin(): Boolean {
        if (TextUtils.isEmpty(etStuSignUpName!!.text.toString())) {
            showSnackBar(etStuSignUpName, getString(R.string.error_name))
            return false
        } else if (TextUtils.isEmpty(etStuSignUpEmail!!.text.toString().trim { it <= ' ' })) {
            showSnackBar(etStuSignUpEmail, getString(R.string.error_email))
            return false
        } else if (TextUtils.isEmpty(etStuSignUpPhoneNum!!.text.toString())) {
            showSnackBar(etStuSignUpPhoneNum, getString(R.string.error_ph_num))
            return false
        } else if (!Validator.validateEmail(etStuSignUpEmail!!.text.toString().trim { it <= ' ' })) {
            showSnackBar(etStuSignUpEmail, getString(R.string.error_valid_email))
            return false
        } else if (etStuSignUpPhoneNum!!.text.toString().length < 10) {
            showSnackBar(etStuSignUpPhoneNum, getString(R.string.error_short_ph_num))
            return false
        } else if (TextUtils.isEmpty(mSelectedDirector)) {
            showSnackBar(spStuChooseDir, getString(R.string.error_director_not_chosen))
            return false
        }
        return true
    }

    companion object {

        fun newInstance(): StudentFragments {
            val args = Bundle()
            val fragment = StudentFragments()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
        when (adapterView.id) {
            R.id.spStuSignupCountryCode -> {
                Log.e(javaClass.simpleName, "Country code selected is - " + countryCode[i]._mCountryName)
            }

            R.id.spStuChooseDir -> {
                (0 until dirModel.data?.size!!)
                        .filter { dirModel.data!![it]?.name!!.contains(directorList[i]) }
                        .forEach {
                            mSelectedDirector = dirModel.data[it]?.id.toString()
                            LoggE(TAG, "==categoryId=>$mSelectedDirector")
                        }




                Log.e(TAG, "Selected director is -- ${directorList[spStuChooseDir.selectedItemPosition]}")
            }
        }
    }

    override fun onNothingSelected(adapterView: AdapterView<*>) {

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {
            STUDENT_SIGNUP -> {
                val regDirModel = apiResponseManager.response as ResponseBody
                val registerResponse = regDirModel.string()
                val response = JSONObject(registerResponse)

                if (response.optInt("status_code") == STATUS_CODE) {
                    LoggE(TAG, "User registered------------>")
                    sessionManager.put(getString(R.string.user_email), etStuSignUpEmail.text.toString())
                    sessionManager.put(getString(R.string.user_password), etStuSignUpPassword.text.toString())

                    sessionManager.put(getString(R.string.user_reg_data), registerResponse)
                    sessionManager.put(getString(R.string.is_user_registered), true)

                    SetupFingerScanner()

//                    activity.startActivity(Intent(activity, VerificationActivity::class.java)
//                            .apply {
//                                putExtra("director", response.optJSONObject("data").optString("director"))//todo remove this one when twillio starts work
//                                putExtra("id", response.optJSONObject("data").optString("id"))
//                            })


                }
            }

            GET_DIRECTORS -> {
                directorList.clear()
                dirModel = apiResponseManager.response as DirectorModel
                Log.e(TAG, dirModel.data[0].name)

                (0 until dirModel.data?.size!!).mapTo(directorList) { dirModel?.data!![it]?.name!! }
                directorList.add(0, getString(R.string.signup_choose_director))
                LoggE(TAG, "countryList==>${directorList.size}")
                setDirectorSelection(directorList)

                Log.e(TAG, "Director array list size -- ${directorList.size}")
            }
        }
    }

    private fun setDirectorSelection(regionList: MutableList<String>?) {
        LoggE(TAG, "=memberList=>${regionList?.size}")
        spStuChooseDir.onItemSelectedListener = this

        var regionAdapter = object : ArrayAdapter<String>(activity, R.layout.row_spinner_membership, R.id.tvRowSpinnerItemMembership, regionList) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val view = super.getDropDownView(position, convertView, parent)
                var tv = view as TextView
                if (position == 0) {
                    tv.setTextColor(Color.GRAY)
                } else {
                    tv.setTextColor(Color.BLACK)
                }
                return view
            }
        }

        regionAdapter.setDropDownViewResource(R.layout.row_spinner_membership)
        spStuChooseDir.adapter = regionAdapter
        spStuChooseDir.onItemSelectedListener = this
    }

    private fun ShowFingerScanDialog() {
        dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_finger_scan)

        val imgDismiss = dialog.findViewById<View>(R.id.imgDialogCancel) as ImageView
        imgDismiss.setOnClickListener {
            dialog.dismiss()
            activity.goActivity<LoginActivity>()
            activity.finish()
            sessionManager.put(getString(R.string.key_is_finger_enabled), false)
        }

        dialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun SetupFingerScanner() {
        // Initializing both Android Keyguard Manager and Fingerprint Manager
        val keyguardManager = activity.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        val fingerprintManager = activity.getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager

        // Check whether the device has radio_button_selected_state Fingerprint sensor.
        if (!fingerprintManager.isHardwareDetected) {
            /**
             * An error message will be displayed if the device does not contain the fingerprint hardware.
             * However if you plan to implement radio_button_selected_state default authentication method,
             * you can redirect the user to radio_button_selected_state default authentication activity from here.
             * Example:
             * Intent intent = new Intent(this, DefaultAuthenticationActivity.class);
             * startActivity(intent);
             */
            showMsgDialog("Your Device does not have radio_button_selected_state Fingerprint Sensor")
            sessionManager.put(getString(R.string.key_is_finger_enabled), false)
//            if (dialog != null) {
//                dialog.dismiss()
//            }
            activity.goActivity<LoginActivity>()
            activity.finish()
        } else {
            // Checks whether fingerprint permission is set on manifest
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                showMsgDialog("Fingerprint authentication permission not enabled")
            } else {
                // Check whether at least one fingerprint is registered
                if (!fingerprintManager.hasEnrolledFingerprints()) {
                    showMsgDialog("Register at least one fingerprint in Settings")
                } else {
                    // Checks whether lock screen security is enabled or not
                    if (!keyguardManager.isKeyguardSecure) {
                        showMsgDialog("Lock screen security not enabled in Settings")
                    } else {
                        ShowFingerScanDialog()

                        generateKey()
                        if (cipherInit()) {
                            val cryptoObject = FingerprintManager.CryptoObject(cipher)
                            val helper = FingerprintHandler(activity)
                            helper.startAuth(fingerprintManager, cryptoObject)
                        }
                    }
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val keyGenerator: KeyGenerator
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Failed to get KeyGenerator instance", e)
        } catch (e: NoSuchProviderException) {
            throw RuntimeException("Failed to get KeyGenerator instance", e)
        }

        try {
            try {
                keyStore!!.load(null)
            } catch (e: CertificateException) {
                e.printStackTrace()
            }

            keyGenerator.init(KeyGenParameterSpec.Builder(FacebookRequestErrorClassification.KEY_NAME, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build())
            keyGenerator.generateKey()
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        } catch (e: InvalidAlgorithmParameterException) {
            throw RuntimeException(e)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun cipherInit(): Boolean {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" +
                    KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Failed to get Cipher", e)
        } catch (e: NoSuchPaddingException) {
            throw RuntimeException("Failed to get Cipher", e)
        }

        try {
            keyStore!!.load(null)
            val key = keyStore!!.getKey(FacebookRequestErrorClassification.KEY_NAME, null) as SecretKey
            cipher!!.init(Cipher.ENCRYPT_MODE, key)
            return true
        } catch (e: KeyPermanentlyInvalidatedException) {
            return false
        } catch (e: KeyStoreException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: CertificateException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: UnrecoverableKeyException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: IOException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: InvalidKeyException) {
            throw RuntimeException("Failed to init Cipher", e)
        }
    }

    override fun targetChanged() {
        if (FingerScanModel.getInstance().state) {
            Log.e(TAG, "User enabled finger print setting.....")
            sessionManager.put(getString(R.string.key_is_finger_enabled), true)
//            dialog.dismiss()
            activity.goActivity<LoginActivity>()
            activity.finish()
        }
    }
}