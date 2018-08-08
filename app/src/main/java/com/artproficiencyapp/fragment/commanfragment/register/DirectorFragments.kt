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
import com.artproficiencyapp.model.FingerScanModel
import com.artproficiencyapp.model.LevelModel
import com.artproficiencyapp.restapi.ApiInitialize
import com.artproficiencyapp.restapi.ApiRequest
import com.artproficiencyapp.restapi.ApiResponseInterface
import com.artproficiencyapp.restapi.ApiResponseManager
import com.artproficiencyapp.utils.Validator
import com.facebook.internal.FacebookRequestErrorClassification.KEY_NAME
import kotlinx.android.synthetic.main.fragment_director_signup.*

import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.security.*
import java.security.cert.CertificateException
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey


/**
 * Created by admin on 22-May-18.
 */
class DirectorFragments : BaseFragment(), View.OnClickListener, AdapterView.OnItemSelectedListener, ApiResponseInterface, FingerScanModel.TargetValueChagned {

    private val TAG = javaClass.simpleName
    private var mDirectorView: View? = null
    private var isPasswordVisible = false
    private var isConfirmPasswordVisible = false
    private var level: String = ""
    private var member: String = ""
    private val levelList = ArrayList<String>()
    private val memberList = ArrayList<String>()
    private lateinit var levelModel: LevelModel
    private var mSelectedLevel = ""
    private var mSelectedMembership = ""
    private var levelSelection: MutableList<String>? = null
    private var membershipSelection: MutableList<String>? = null
    private val countryCode = ArrayList<CountryCodeModel>()
    private var facebook_id: String = ""
    private lateinit var dialog: Dialog
    private var keyStore: KeyStore? = null
    //private lateinit var dirModel: LevelModel
    // Variable used for storing the key in the Android Keystore container
    private var cipher: Cipher? = null

    private var LevelId: String = ""
    private var MemberId: String = ""

    companion object {
        fun newInstance(): DirectorFragments {
            val args = Bundle()
            val fragment = DirectorFragments()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_director_signup, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        FingerScanModel.getInstance().setListener(this)

        InitView()
        SetclickListener()
        FacebookLoginDataGet()
        facebook_id = sessionManager.get("facebook_key", "")
        Log.e(TAG, "Create facebook id" + facebook_id)




        ApiRequest(activity, ApiInitialize.initialize(ApiInitialize.MAIN_URl_API).levelMember(""), LEVEL_MEMBER, true, this)
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
            etDirSignUpName.setText(facebook_name)
            etDirSignUpEmail.setText(facebook_email)
            hidePassword()

        } else {
            etDirSignUpName.setText("")
            etDirSignUpEmail.setText("")

        }
    }


    private fun SetclickListener() {
        btnSignupDir.setOnClickListener(this)
        tvDirSignUpMsg.setOnClickListener(this)
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
            spDirSignupCountryCode.adapter = adapter
            spDirSignupCountryCode.onItemSelectedListener = this
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

    private fun InitView() {
        etDirSignUpPassword!!.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= etDirSignUpPassword!!.right - etDirSignUpPassword!!.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
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

        etDirSignUpConfirmPassword!!.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= etDirSignUpConfirmPassword!!.right - etDirSignUpConfirmPassword!!.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
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
        tvDirSignUpMsg!!.text = spannableString




        setCountryCode()
    }

    private fun setLevelSelection(regionList: MutableList<String>?) {
        LoggE(TAG, "=levelList=>${regionList?.size}")
        spDirSignUpLevelSelection.onItemSelectedListener = this
        var regionAdapter = object : ArrayAdapter<String>(activity, R.layout.row_spinner_membership, regionList) {
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
        spDirSignUpLevelSelection.adapter = regionAdapter
        regionAdapter

    }

    private fun setMemberSelection(regionList: MutableList<String>?) {
        LoggE(TAG, "=memberList=>${regionList?.size}")
        spDirSignUpMembershipSociety.onItemSelectedListener = this
        var regionAdapter = object : ArrayAdapter<String>(activity, R.layout.row_spinner_membership, regionList) {
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
        spDirSignUpMembershipSociety.adapter = regionAdapter


    }


    private fun makePassowrdVisible(_mVisible: Boolean) {
        if (_mVisible) {
            isPasswordVisible = false
            etDirSignUpPassword!!.transformationMethod = HideReturnsTransformationMethod.getInstance()
        } else {
            isPasswordVisible = true
            etDirSignUpPassword!!.transformationMethod = PasswordTransformationMethod.getInstance()
        }
    }

    private fun makeConfirmPassowrdVisible(_mVisible: Boolean) {
        if (_mVisible) {
            isConfirmPasswordVisible = false
            etDirSignUpConfirmPassword!!.transformationMethod = HideReturnsTransformationMethod.getInstance()
        } else {
            isConfirmPasswordVisible = true
            etDirSignUpConfirmPassword!!.transformationMethod = PasswordTransformationMethod.getInstance()
        }
    }

    private fun IsValidInput(): Boolean {
        if (TextUtils.isEmpty(etDirSignUpName!!.text.toString().trim { it <= ' ' })) {
            showSnackBar(etDirSignUpName, getString(R.string.error_name))
            return false
        } else if (TextUtils.isEmpty(etDirSignUpEmail!!.text.toString().trim { it <= ' ' })) {
            showSnackBar(etDirSignUpEmail, getString(R.string.error_email))
            return false
        } else if (TextUtils.isEmpty(etDirSignUpPhoneNum!!.text.toString())) {
            showSnackBar(etDirSignUpPhoneNum, getString(R.string.error_ph_num))
            return false
        } else if (TextUtils.isEmpty(etDirSignUpDesignation!!.text.toString().trim { it <= ' ' })) {
            showSnackBar(etDirSignUpDesignation, getString(R.string.error_designation))
            return false
        } else if (TextUtils.isEmpty(etDirSignUpPassword!!.text.toString())) {
            showSnackBar(etDirSignUpPassword, getString(R.string.error_pwd))
            return false
        } else if (TextUtils.isEmpty(etDirSignUpConfirmPassword!!.text.toString())) {
            showSnackBar(etDirSignUpConfirmPassword, getString(R.string.error_confirm_pwd))
            return false
        } else if (!Validator.validateEmail(etDirSignUpEmail!!.text.toString().trim { it <= ' ' })) {
            showSnackBar(etDirSignUpEmail, getString(R.string.error_valid_email))
            return false
        } else if (etDirSignUpPhoneNum!!.text.toString().length < 10) {
            showSnackBar(etDirSignUpPhoneNum, getString(R.string.error_short_ph_num))
            return false
        } else if (etDirSignUpConfirmPassword!!.text.toString() != etDirSignUpPassword!!.text.toString()) {
            showSnackBar(etDirSignUpConfirmPassword, getString(R.string.error_pwd_diff))
            return false
        } else if (spDirSignUpLevelSelection.getSelectedItem().toString().trim().equals("Level selection")) {
            showSnackBar(spDirSignUpLevelSelection, getString(R.string.error_level_selection))
            return false
        } else if (spDirSignUpMembershipSociety.getSelectedItem().toString().trim().equals("MemberShip society selection")) {
            showSnackBar(spDirSignUpLevelSelection, getString(R.string.error_membership_selection))
            return false
        } else if (etDirSignUpPassword.text.toString().trim().length < 6) {
            showSnackBar(etDirSignUpPassword, getString(R.string.error_pwd_msg))
            return false
        } else if (etDirSignUpConfirmPassword.text.toString().trim().length < 6) {
            showSnackBar(etDirSignUpConfirmPassword, getString(R.string.error_pwd_msg))
            return false
        } else if (!Validator.isValidPassword(etDirSignUpPassword.text.toString().trim())) {
            showSnackBar(etDirSignUpPassword, getString(R.string.error_pwd_invalid_msg))
            return false
        } else if (!Validator.isValidPassword(etDirSignUpConfirmPassword.text.toString().trim())) {
            showSnackBar(etDirSignUpConfirmPassword, getString(R.string.error_pwd_invalid_msg))
            return false
        }
        return true
    }

    private fun IsValidInputFacebookLogin(): Boolean {
        if (TextUtils.isEmpty(etDirSignUpName!!.text.toString().trim { it <= ' ' })) {
            showSnackBar(etDirSignUpName, getString(R.string.error_name))
            return false
        } else if (TextUtils.isEmpty(etDirSignUpEmail!!.text.toString().trim { it <= ' ' })) {
            showSnackBar(etDirSignUpEmail, getString(R.string.error_email))
            return false
        } else if (TextUtils.isEmpty(etDirSignUpPhoneNum!!.text.toString())) {
            showSnackBar(etDirSignUpPhoneNum, getString(R.string.error_ph_num))
            return false
        } else if (TextUtils.isEmpty(etDirSignUpDesignation!!.text.toString().trim { it <= ' ' })) {
            showSnackBar(etDirSignUpDesignation, getString(R.string.error_designation))
            return false

        } else if (!Validator.validateEmail(etDirSignUpEmail!!.text.toString().trim { it <= ' ' })) {
            showSnackBar(etDirSignUpEmail, getString(R.string.error_valid_email))
            return false
        } else if (etDirSignUpPhoneNum!!.text.toString().length < 10) {
            showSnackBar(etDirSignUpPhoneNum, getString(R.string.error_short_ph_num))
            return false
        } else if (spDirSignUpLevelSelection.getSelectedItem().toString().trim().equals("Level selection")) {
            showSnackBar(spDirSignUpLevelSelection, getString(R.string.error_level_selection))
            return false
        } else if (spDirSignUpMembershipSociety.getSelectedItem().toString().trim().equals("MemberShip society selection")) {
            showSnackBar(spDirSignUpLevelSelection, getString(R.string.error_membership_selection))
            return false
        }
        return true
    }

    override fun onClick(p0: View?) {
        when (p0) {
            btnSignupDir -> {

                if (!TextUtils.isEmpty(facebook_id)) {

                    if (IsValidInputFacebookLogin()) {
                        // showMsgDialog("Data will be add ")
                        if (isNetWork(activity)) {
                            ApiRequest(activity, ApiInitialize.initialize(ApiInitialize.MAIN_URl_API).registerDirector(etDirSignUpName!!.text.toString(),
                                    etDirSignUpEmail!!.text.toString(), countryCode.get(spDirSignupCountryCode.selectedItemPosition)._mCountryCode.plus(etDirSignUpPhoneNum!!.text.toString()),
                                    "DIRECTOR", etDirSignUpDesignation!!.text.toString(), MemberId, LevelId, etDirSignUpConfirmPassword!!.text.toString(),
                                    "ANDROID", "123456789", "Ahmedabad", "23.0000000", "75.0000000", "1",
                                    facebook_id), DIRECTOR_SIGNUP, true, this@DirectorFragments)
                        }


                    }

                } else {
                    if (IsValidInput()) {
                        if (isNetWork(activity)) {
                            ApiRequest(activity, ApiInitialize.initialize(ApiInitialize.MAIN_URl_API).registerDirector(etDirSignUpName!!.text.toString(),
                                    etDirSignUpEmail!!.text.toString(), countryCode.get(spDirSignupCountryCode.selectedItemPosition)._mCountryCode.plus(etDirSignUpPhoneNum!!.text.toString()),
                                    "DIRECTOR", etDirSignUpDesignation!!.text.toString(), MemberId, LevelId, etDirSignUpConfirmPassword!!.text.toString(),
                                    "ANDROID", "123456789", "Ahmedabad", "23.0000000", "75.0000000", "0",
                                    etDirSignUpEmail!!.text.toString()), DIRECTOR_SIGNUP, true, this@DirectorFragments)
                        }

                    }
                }
            }
            tvDirSignUpMsg -> {
                activity.goActivity<LoginActivity>()
            }
        }
    }

    override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
        when (adapterView) {

            spDirSignUpLevelSelection -> {
                level = levelList[i]
                LoggE(TAG, "country====>$level")

                (0 until levelModel.data?.level.size)
                        .filter { levelModel.data!!.level[it]?.name!!.contains(levelList[i]) }
                        .forEach {
                            LevelId = levelModel.data.level[it]?.id.toString()
                            LoggE(TAG, "==LevelId=>$LevelId")
                        }

            }

            spDirSignUpMembershipSociety -> {
                member = memberList[i]
                LoggE(TAG, "country====>$member")

                (0 until levelModel.data?.membership_societies.size)
                        .filter { levelModel.data!!.membership_societies[it]?.name!!.contains(memberList[i]) }
                        .forEach {
                            MemberId = levelModel.data.membership_societies[it]?.id.toString()
                            LoggE(TAG, "==MemberId=>$MemberId")
                        }


            }

            spDirSignupCountryCode -> {
                Log.e(TAG, "Country code selected is - " + countryCode!![i]._mCountryName)
            }
        }
    }

    private fun hidePassword() {
        etDirSignUpPassword.gone()
        etDirSignUpConfirmPassword.gone()
        etDirPasswordText.gone()
    }

    override fun onNothingSelected(adapterView: AdapterView<*>) {

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {
            DIRECTOR_SIGNUP -> {

                val regDirModel = apiResponseManager.response as ResponseBody
                val registerResponse = regDirModel.string()
                val response = JSONObject(registerResponse)
                if (response.optInt("status_code") == STATUS_CODE) {
                    LoggE(TAG, "User registered------------>")
                    sessionManager.put(getString(R.string.user_email), etDirSignUpEmail.text.toString())
                    sessionManager.put(getString(R.string.user_password), etDirSignUpPassword.text.toString())

                    sessionManager.put(getString(R.string.user_reg_data), registerResponse)
                    sessionManager.put(getString(R.string.is_user_registered), true)

                    SetupFingerScanner()
                }
            }
            LEVEL_MEMBER -> {
                levelModel = apiResponseManager.response as LevelModel
                when (levelModel.status_code) {
                    STATUS_CODE -> {
                        (0 until levelModel.data?.level?.size!!).mapTo(levelList) { levelModel?.data!!.level[it]?.name }
                        levelList.add(0, getString(R.string.level_selection))
                        LoggE(TAG, "countryList==>${levelList.size}")
                        setLevelSelection(levelList)

                        (0 until levelModel.data?.membership_societies?.size!!).mapTo(memberList) { levelModel?.data!!.membership_societies[it]?.name }
                        memberList.add(0, getString(R.string.member_selection))
                        LoggE(TAG, "countryList==>${memberList.size}")
                        setMemberSelection(memberList)
                    }
                }
            }
        }
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

            keyGenerator.init(KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
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
            val key = keyStore!!.getKey(KEY_NAME, null) as SecretKey
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