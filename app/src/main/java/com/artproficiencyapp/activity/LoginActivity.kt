package com.artproficiencyapp.activity

import android.Manifest
import android.annotation.TargetApi
import android.app.AlertDialog
import android.app.Dialog
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.hardware.fingerprint.FingerprintManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import android.support.annotation.RequiresApi
import android.support.v13.app.ActivityCompat
import android.support.v4.app.ActivityCompat.finishAffinity
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.content.ContextCompat
import android.support.v4.content.PermissionChecker
import android.text.SpannableString
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.StyleSpan
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import com.artproficiencyapp.BuildConfig
import com.artproficiencyapp.R
import com.artproficiencyapp.R.string.email
import com.artproficiencyapp.R.string.password
import com.artproficiencyapp.common.FingerprintHandler
import com.artproficiencyapp.common.RegularTextview
import com.artproficiencyapp.common.RregularEditext
import com.artproficiencyapp.extension.*
import com.artproficiencyapp.model.FingerScanModel
import com.artproficiencyapp.restapi.ApiInitialize
import com.artproficiencyapp.restapi.ApiRequest
import com.artproficiencyapp.restapi.ApiResponseInterface
import com.artproficiencyapp.restapi.ApiResponseManager
import com.artproficiencyapp.utils.Validator
import com.facebook.*
import com.facebook.internal.ImageRequest
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.IOException
import java.security.*
import java.security.cert.CertificateException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey


class LoginActivity : BaseActivity(), View.OnClickListener, ApiResponseInterface, FingerScanModel.TargetValueChagned, android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback {

    private lateinit var dialog: Dialog
    private var keyStore: KeyStore? = null
    // Variable used for storing the key in the Android Keystore container
    private val KEY_NAME = BuildConfig.APPLICATION_ID
    private var cipher: Cipher? = null
    private var isPasswordVisible = false
    private var isConfirmPasswordVisible = false
    private var callbackManager: CallbackManager? = null
    private var profilePics: String = ""
    private var facebook_id: String = ""
    private var facebook_email_user: String = ""

    private lateinit var dialogFbEmail: Dialog
    private val permissionList = arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        FingerScanModel.getInstance().setListener(this)
        inItView()
        InitView()
        CheckPermission()
        facebook_id = sessionManager.get("facebook_key", "")
        Log.e(TAG, "Create facebook id" + facebook_id)

        facebook_email_user = sessionManager.get("facebook_email", "")
        Log.e(TAG, "Create facebook email" + facebook_email_user)

        val spannableString = SpannableString(getString(R.string.signup_Msg))
        spannableString.setSpan(StyleSpan(Typeface.BOLD), spannableString.length - 7, spannableString.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        txtSignUp.text = spannableString
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun inItView() {
        loginSocial(this@LoginActivity)
        btnLogin.setOnClickListener(this)
        txtForgotpassword.setOnClickListener(this)
        txtSignUp.setOnClickListener(this)
        ivFacebookLogin.setOnClickListener(this)

        /*val facebook_id = sessionManager.get("facebook_key", "")
        Log.e(TAG, "sessionmanager value id ==  " + facebook_id)*/

        val email = sessionManager[getString(R.string.user_email), ""]
        val password = sessionManager[getString(R.string.user_password), ""]

        Log.e(TAG, "Email value==  " + email)
        Log.e(TAG, "password value==  " + password)
        // SetupFingerScanner()


        /*if (!TextUtils.isEmpty(sessionManager[getString(R.string.user_email), ""]) && !TextUtils.isEmpty(sessionManager[getString(R.string.user_password), ""])) {
            Log.e("", "Finger print scane in andorid ")
            SetupFingerScanner()
        }*/

        if (sessionManager.get(getString(R.string.key_is_finger_enabled), false)) {
            SetupFingerScanner()
        }
    }

    private fun InitView() {

        edtPassword!!.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= edtPassword!!.right - edtPassword!!.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
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

    }

    private fun makePassowrdVisible(_mVisible: Boolean) {
        if (_mVisible) {
            isPasswordVisible = false
            edtPassword!!.transformationMethod = HideReturnsTransformationMethod.getInstance()
        } else {
            isPasswordVisible = true

            edtPassword!!.transformationMethod = PasswordTransformationMethod.getInstance()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun SetupFingerScanner() {
        // Initializing both Android Keyguard Manager and Fingerprint Manager
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        val fingerprintManager = getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager


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
        } else {
            // Checks whether fingerprint permission is set on manifest
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
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
                            val helper = FingerprintHandler(this)
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


    private fun IsValidInput(): Boolean {
        if (TextUtils.isEmpty(edtUsernameEmail!!.text.toString().trim { it <= ' ' })) {
            showSnackBar(edtUsernameEmail, getString(R.string.toast_email))
            return false
        } else if (!Validator.validateEmail(edtUsernameEmail!!.text.toString().trim { it <= ' ' })) {
            showSnackBar(edtUsernameEmail, getString(R.string.error_valid_email))
            return false
        } else if (TextUtils.isEmpty(edtPassword!!.text.toString())) {
            showSnackBar(edtPassword, getString(R.string.toast_password))
            return false
        }
        return true
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


    override fun onClick(p0: View?) {
        when (p0) {
            btnLogin -> {
                if (IsValidInput()) {
                    hideSoftKeyboard(this)
                    if (isNetWork(this)) {
                        ApiRequest(this@LoginActivity, ApiInitialize.initialize(ApiInitialize.MAIN_URl_API).userLogin("0", edtUsernameEmail.value,
                                edtPassword.value), LOGIN, true, this)
                    }
                }
            }

            txtForgotpassword -> {
                goActivity<ForgetPasswordActivity>()
                finish()
            }

            txtSignUp -> {
                sessionManager.put("facebook_name", "")
                sessionManager.put("facebook_email", "")
                sessionManager.put("facebook_profile_uri", "")
                goActivity<SignUpActivity>()
                finish()
            }

            ivFacebookLogin -> {
                //showMsgDialog("Under development")
                connectWithFaceBook()
            }
        }
    }


    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {
            LOGIN -> {
                val model = apiResponseManager.response as ResponseBody
                val responseValue = model.string()
                LoggE(TAG, "response LOGIN:-$responseValue")
                val response = JSONObject(responseValue)

                if (response.optInt("status_code") == STATUS_CODE) {
                    sessionManager.put(getString(R.string.user_data), responseValue)
                    sessionManager.put(getString(R.string.user_email), edtUsernameEmail.text.toString())
                    sessionManager.put(getString(R.string.user_password), edtPassword.text.toString())
                    LoggE(TAG, "Token==>${getUserModel()?.data!!.token}")
                    if (response.optJSONObject("data").optString("user_type").equals("DIRECTOR", true)) {
                        if (response.optJSONObject("data").optString("is_email_verified") == "1") {
                            sessionManager.put(getString(R.string.is_login), true)
                            //When User First Time Login After The Sign Up Then Redirect To The DirectorProfileActivity
                            if (sessionManager.get(getString(R.string.is_user_registered), false)) {
                                //sessionManager.put(getString(R.string.is_user_registered),false)
                                goActivity<DirectorProfileActivity>()
                                finish()
                            } else {
                                goActivity<DashBordActivityDIreStu>()
                                finish()
                            }


                            /*goActivity<DashBordActivityDIreStu>()
                            finish()*/
                        } else {
                            showMsgDialog(getString(R.string.error_email_not_verified))
                        }
                    } else if (response.optJSONObject("data").optString("user_type").equals("STUDENT", true)) {
                        if (response.optJSONObject("data").optString("is_email_verified") == "1" &&
                                response.optJSONObject("data").optString("is_otp_verified") == "1" &&
                                response.optJSONObject("data").optString("is_director_verified") == "1") {
                            sessionManager.put(getString(R.string.is_login), true)
                            this.goActivity<DashBordActivityDIreStu>()
                            finish()
                        } else if (response.optJSONObject("data").optString("is_email_verified") == "0" &&
                                response.optJSONObject("data").optString("is_otp_verified") == "0" &&
                                response.optJSONObject("data").optString("is_director_verified") == "1") {
                            showMsgDialog(getString(R.string.error_email_not_verified))
                        } else if (response.optJSONObject("data").optString("is_email_verified") == "1" &&
                                response.optJSONObject("data").optString("is_otp_verified") == "1" &&
                                response.optJSONObject("data").optString("is_director_verified") == "0") {
                            sessionManager.put(getString(R.string.is_login), true)
                            this.goActivity<DashBordActivityDIreStu>()
                            finish()
                        } else if (response.optJSONObject("data").optString("is_email_verified") == "1" &&
                                response.optJSONObject("data").optString("is_otp_verified") == "0" &&
                                response.optJSONObject("data").optString("is_director_verified") == "0") {
                            showMsgDialog(getString(R.string.error_otp_not_verified))
                            goActivity<VerificationActivity>()
                            finish()
                        } else {
                            showMsgDialog(response.optString("message"))
                        }/* else if (response.optJSONObject("data").optString("is_email_verified") == "1" &&
                                response.optJSONObject("data").optString("is_otp_verified") == "1" &&
                                response.optJSONObject("data").optString("is_director_verified") == "0") {
                            showSnackBar(edtUsernameEmail, getString(R.string.error_director_not_verified))
                        }*/

                        /**
                         * Uncomment above code when director verification pending status should be considered */
                    }
                } else /*if(response.optInt("status_code") == 422)*/ {
                    LoggE(TAG, "In error code else part.....")
                    showSnackBar(edtUsernameEmail, response.optString(MESSAGE))
                }
//                Toast(response.optString(MESSAGE), false, this)
            }
            FACEBOOK_LOGIN -> {
                val model = apiResponseManager.response as ResponseBody
                val responseValue = model.string()
                LoggE(TAG, "response FB LOGIN:-$responseValue")
                val response = JSONObject(responseValue)

                if (response.optInt("status_code") == STATUS_CODE) {

                    if (response.optInt("is_register") == 3) {
                        //  Toast("Facebook Login", true, this)
                        Log.e(TAG, "Facebook is regidter==> 3")
                        sessionManager.put(getString(R.string.user_data), responseValue)
                        sessionManager.put(getString(R.string.user_email), facebook_id)
                        sessionManager.put(getString(R.string.user_password), facebook_email_user)
                        LoggE(TAG, "Token==>${getUserModel()?.data!!.token}")
                        if (response.optJSONObject("data").optString("user_type").equals("DIRECTOR", true)) {
                            if (response.optJSONObject("data").optString("is_email_verified") == "1") {
                                sessionManager.put(getString(R.string.is_login), true)
                                sessionManager.put("isFristProfileActivityLaunch", true)
                                goActivity<DashBordActivityDIreStu>()
                                finish()
                            } else {
                                showSnackBar(edtUsernameEmail, getString(R.string.error_email_not_verified))
                            }
                        } else if (response.optJSONObject("data").optString("user_type").equals("STUDENT", true)) {
                            if (response.optJSONObject("data").optString("is_email_verified") == "1" &&
                                    response.optJSONObject("data").optString("is_otp_verified") == "1" &&
                                    response.optJSONObject("data").optString("is_director_verified") == "1") {
                                sessionManager.put(getString(R.string.is_login), true)
                                this.goActivity<DashBordActivityDIreStu>()
                                finish()
                            } else if (response.optJSONObject("data").optString("is_email_verified") == "0" &&
                                    response.optJSONObject("data").optString("is_otp_verified") == "0" &&
                                    response.optJSONObject("data").optString("is_director_verified") == "1") {
                                showSnackBar(edtUsernameEmail, getString(R.string.error_email_not_verified))
                            } else if (response.optJSONObject("data").optString("is_email_verified") == "1" &&
                                    response.optJSONObject("data").optString("is_otp_verified") == "1" &&
                                    response.optJSONObject("data").optString("is_director_verified") == "0") {
                                sessionManager.put(getString(R.string.is_login), true)
                                this.goActivity<DashBordActivityDIreStu>()
                                finish()
                            } else if (response.optJSONObject("data").optString("is_email_verified") == "1" &&
                                    response.optJSONObject("data").optString("is_otp_verified") == "0" &&
                                    response.optJSONObject("data").optString("is_director_verified") == "0") {
                                showMsgDialog(getString(R.string.error_otp_not_verified))
                                goActivity<VerificationActivity>()
                                finish()
//                              startActivity(Intent(this, VerificationActivity::class.java)
//                                      .apply {
//                                          putExtra("director", response.optJSONObject("data").optString("director")) //todo remove this one when twillio starts work
//                                          putExtra("id", response.optJSONObject("data").optString("id"))
//                                      })
//                              finish()
                            }
                        }
                    } else if (response.optInt("is_register") == 1) {
                        Log.e(TAG, "Facebook is regidter==> 1")
                        goActivity<SignUpActivity>()

                    } else if (response.optInt("is_register") == 2) {
                        Log.e(TAG, "Facebook is regidter==> 2")
                        showSnackBar(edtUsernameEmail, "Facebook Id already registered with other email")
                    }
                    /* else if (response.optJSONObject("data").optString("is_email_verified") == "1" &&
                        response.optJSONObject("data").optString("is_otp_verified") == "1" &&
                        response.optJSONObject("data").optString("is_director_verified") == "0") {
                    showSnackBar(edtUsernameEmail, getString(R.string.error_director_not_verified))
                }*/

                    /**
                     * Uncomment above code when director verification pending status should be considered */

                } else /*if(response.optInt("status_code") == 422)*/ {
                    LoggE(TAG, "In error code else part.....")
                    showSnackBar(edtUsernameEmail, response.optString(MESSAGE))
                }
//                Toast(response.optString(MESSAGE), false, this)
            }
            FINGER -> {

                val model = apiResponseManager.response as ResponseBody
                val responseValue = model.string()
                LoggE(TAG, "response FINGER:-$responseValue")
                val response = JSONObject(responseValue)

                if (response.optInt("status_code") == STATUS_CODE) {
                    sessionManager.put(getString(R.string.user_data), responseValue)
                    val Fbid = sessionManager[facebook_id, ""]
                    val email = sessionManager[facebook_email_user, ""]
                    sessionManager.put("facebook_key", Fbid)
                    sessionManager.put("facebook_email", email)
                    LoggE(TAG, "Token==>${getUserModel()?.data!!.token}")
                    if (response.optJSONObject("data").optString("user_type").equals("DIRECTOR", true)) {
                        if (response.optJSONObject("data").optString("is_email_verified") == "1") {
                            sessionManager.put(getString(R.string.is_login), true)
                            goActivity<DashBordActivityDIreStu>()
                            finish()
                        } else {
                            if (dialog != null)
                                dialog.dismiss()
                            showMsgDialog(getString(R.string.error_email_not_verified))
                        }
                    } else if (response.optJSONObject("data").optString("user_type").equals("STUDENT", true)) {
                        if (response.optJSONObject("data").optString("is_email_verified") == "1" &&
                                response.optJSONObject("data").optString("is_otp_verified") == "1" &&
                                response.optJSONObject("data").optString("is_director_verified") == "1") {
                            sessionManager.put(getString(R.string.is_login), true)
                            if (dialog != null)
                                dialog.dismiss()
                            this.goActivity<DashBordActivityDIreStu>()
                            finish()
                        } else if (response.optJSONObject("data").optString("is_email_verified") == "1" &&
                                response.optJSONObject("data").optString("is_otp_verified") == "1" &&
                                response.optJSONObject("data").optString("is_director_verified") == "0") {
                            sessionManager.put(getString(R.string.is_login), true)
                            this.goActivity<DashBordActivityDIreStu>()
                            finish()
                        } else if (response.optJSONObject("data").optString("is_email_verified") == "0" &&
                                response.optJSONObject("data").optString("is_otp_verified") == "1" &&
                                response.optJSONObject("data").optString("is_director_verified") == "1") {
                            if (dialog != null)
                                dialog.dismiss()
                            showMsgDialog(getString(R.string.error_email_not_verified))
                        } else if (response.optJSONObject("data").optString("is_email_verified") == "1" &&
                                response.optJSONObject("data").optString("is_otp_verified") == "0" &&
                                response.optJSONObject("data").optString("is_director_verified") == "0") {
                            if (dialog != null)
                                dialog.dismiss()
                            showMsgDialog(getString(R.string.error_otp_not_verified))
                            goActivity<VerificationActivity>()
                            finish()
                            /*startActivity(Intent(this, VerificationActivity::class.java)
                                    .apply {
                                        putExtra("director", response.optJSONObject("data").optString("director")) //todo remove this one when twillio starts work
                                        putExtra("id", response.optJSONObject("data").optString("id"))
                                    })*/
                            finish()
                        } else {
                            showMsgDialog(response.optString("message"))
                        }/* else if (response.optJSONObject("data").optString("is_email_verified") == "1" &&
                                response.optJSONObject("data").optString("is_otp_verified") == "1" &&
                                response.optJSONObject("data").optString("is_director_verified") == "0") {
                            showSnackBar(edtUsernameEmail, getString(R.string.error_director_not_verified))
                        }*/

                        /**
                         * Uncomment above code when director verification pending status should be considered */
                    }
                } else /*if(response.optInt("status_code") == 422)*/ {
                    LoggE(TAG, "In error code else part.....")

                    showSnackBar(edtUsernameEmail, response.optString(MESSAGE))
                }
            }
        }
    }

    private fun ShowFingerScanDialog() {
        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_finger_scan)

        val imgDismiss = dialog.findViewById<View>(R.id.imgDialogCancel) as ImageView
        imgDismiss.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    override fun targetChanged() {
        if (FingerScanModel.getInstance().state) {
            if (sessionManager.get(getString(R.string.key_is_finger_enabled), false)) {
                Log.e(TAG, "User has enabled finger setting......")
                if (!TextUtils.isEmpty(sessionManager[getString(R.string.user_email), ""]) && !TextUtils.isEmpty(sessionManager[getString(R.string.user_password), ""])) {
//                dialog.dismiss()

                    ApiRequest(this@LoginActivity, ApiInitialize.initialize(ApiInitialize.MAIN_URl_API).userLogin("0",
                            sessionManager[getString(R.string.user_email), ""], sessionManager[getString(R.string.user_password), ""]), FINGER, true,
                            this)

                    val email = sessionManager[getString(R.string.user_email), ""]
                    val password = sessionManager[getString(R.string.user_password), ""]


                    sessionManager.put(getString(R.string.user_email), email)
                    sessionManager.put(getString(R.string.user_password), password)

                } else if (!TextUtils.isEmpty(facebook_id) && !TextUtils.isEmpty(facebook_email_user)) {
                    ApiRequest(this@LoginActivity, ApiInitialize.initialize(ApiInitialize.MAIN_URl_API).facebookuserLogin("1", facebook_id,
                            "", facebook_email_user), FACEBOOK_LOGIN, true, this)


                    val Fbid = sessionManager[facebook_id, ""]
                    val FbEmail = sessionManager[facebook_email_user, ""]


                    sessionManager.put("facebook_key", Fbid)
                    sessionManager.put("facebook_email", FbEmail)
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    override fun getFacebookData(response: GraphResponse) {
        super.getFacebookData(response)
        val jsnObj = response.jsonObject
        LoggE("email==>", jsnObj.optString("email"))
        LoggE("faceBookId==>", jsnObj.optString("id"))
        LoggE("name==>", jsnObj.optString("first_name"))
        LoggE("Last  name==>", jsnObj.optString("name"))
        LoggE("number==>", jsnObj.optString("employee_number"))
        LoggE(TAG, "==> FACEBOOK  Login")
        LoggE(TAG, "=>Facebook Profile Uri ==>${ImageRequest.getProfilePictureUri(jsnObj.optString("id"), 100, 100).toString()}")
        profilePics = "${ImageRequest.getProfilePictureUri(jsnObj.optString("id"), 100, 100).toString()}.jpg"
        /// Toast(" datatata", true, this)
        sessionManager.put("facebook_key", jsnObj.optString("id"))
        sessionManager.put("facebook_name", jsnObj.optString("name"))
        sessionManager.put("facebook_email", jsnObj.optString("email"))
        sessionManager.put("facebook_profile_uri", profilePics)

        // val faceboo_id = sessionManager.put("facebook_key", jsnObj.optString("id"))
        //  val facebook_email = sessionManager.put("facebook_email", jsnObj.optString("email"))
        //  LoggE(TAG,"facebook email"+facebook_email)
        facebook_email_user = sessionManager.get("facebook_email", "")
        Log.e(TAG, "Create facebook email condition" + facebook_email_user)

        facebook_id = sessionManager.get("facebook_key", "")
        Log.e(TAG, "Create facebook id" + facebook_id)

        if (facebook_email_user != null) {

            if (isNetWork(this)) {
                ApiRequest(this@LoginActivity, ApiInitialize.initialize(ApiInitialize.MAIN_URl_API).facebookuserLogin("1", facebook_id,
                        "", facebook_email_user), FACEBOOK_LOGIN, true, this)

            }

        } else {
            customdialog()
            Log.e(TAG, "Emil nulll")

        }


//        val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
//        intent.putExtra("flag", true)
//        startActivity(intent)


        //ApiRequest(this@LoginActivity, ApiInitialize.initialize(ApiInitialize.MAIN_URl_AUTH).loginSocial(FACEBOOK_LOGIN, jsnObj.optString("id")), LOGIN, true, this)

    }

    private fun customdialog() {
        dialog = Dialog(this, android.R.style.Theme_Translucent_NoTitleBar)
        dialog.setContentView(R.layout.item_facebook_email_dialog)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(false)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH)
        dialog.show()
        val facebookEmail = dialog.findViewById<RregularEditext>(R.id.edFacebookEmail)
        val facebookSubmit = dialog.findViewById<Button>(R.id.edFacebookSubmit)

        facebookSubmit.setOnClickListener {


            if (TextUtils.isEmpty(facebookEmail!!.text.toString().trim { it <= ' ' })) {
                showSnackBar(facebookEmail, getString(R.string.toast_email))

            } else if (!Validator.validateEmail(facebookEmail!!.text.toString().trim { it <= ' ' })) {
                showSnackBar(facebookEmail, getString(R.string.error_valid_email))
            } else {
                Log.e(TAG, "Facebook enter email=> " + facebookEmail.value)
                ApiRequest(this@LoginActivity, ApiInitialize.initialize(ApiInitialize.MAIN_URl_API).facebookuserLogin("1", facebook_id,
                        "", facebookEmail.value), FACEBOOK_LOGIN, true, this)
                dialog.dismiss()
            }
        }
    }

    private fun checkPermission() {


        if (ContextCompat.checkSelfPermission(this@LoginActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this@LoginActivity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

                && ContextCompat.checkSelfPermission(this@LoginActivity, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this@LoginActivity, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
        ) {
//
        } else {
            ActivityCompat.requestPermissions(this@LoginActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS), 100)
        }
    }

    fun CheckPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this@LoginActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this@LoginActivity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this@LoginActivity, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this@LoginActivity, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
            Log.e(javaClass.simpleName, "Permission allowed.....")
        } else {
            android.support.v4.app.ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 100) {
            if (grantResults.size > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_DENIED || grantResults[1] == PackageManager.PERMISSION_DENIED ||
                        grantResults[2] == PackageManager.PERMISSION_DENIED) {
                    // user rejected the permission
                    val showRationaleCamera = android.support.v4.app.ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
                    val showRationaleWrite = android.support.v4.app.ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    val showReadExcternalStoreage = android.support.v4.app.ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    val showFinelocation = android.support.v4.app.ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    val showCoreLocation = android.support.v4.app.ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    val showreceivcesms = android.support.v4.app.ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)
                    val showreadsms = android.support.v4.app.ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)
                    //                    boolean showRationaleRead = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE);

                    if (!showRationaleCamera || !showReadExcternalStoreage || !showRationaleWrite || !showFinelocation || !showCoreLocation || !showreceivcesms || !showreadsms) {

                        val builder = AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog)
                        builder.setTitle("Permission required")
                        builder.setMessage("To use this application you have to allow all of these permissions. Change the permissions setting " + "from app setting menu.")
                        builder.setPositiveButton("Change setting") { dialogInterface, i ->

                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts("package", packageName, null)
                            intent.data = uri
                            intent.addCategory(Intent.CATEGORY_DEFAULT)
                            startActivityForResult(intent, 1000)

                            //                            ActivityCompat.requestPermissions(Permission.this, new String[]{Manifest.permission.CAMERA,
                            //                            Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                            dialogInterface.dismiss()
                        }

                        builder.setNegativeButton("Cancel") { dialogInterface, i ->
                            finish()
                            dialogInterface.dismiss()
                        }

                        builder.create()
                        val dialog = builder.create()
                        dialog.setCancelable(false)
                        dialog.show()
                    } else {

                        val builder = AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog)
                        builder.setTitle("Permission required")
                        builder.setMessage("To use this application you have to allow all of these permissions. Change the permissions setting " + "from app setting menu.")
                        builder.setPositiveButton("Allow") { dialogInterface, i ->
                            android.support.v4.app.ActivityCompat.requestPermissions(this@LoginActivity, arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
                            dialogInterface.dismiss()
                        }

                        builder.setNegativeButton("Deny") { dialogInterface, i ->
                            finish()
                            dialogInterface.dismiss()
                        }

                        builder.create()
                        val dialog = builder.create()
                        dialog.setCancelable(false)
                        dialog.show()
                    }
                } else {
                    Log.e(javaClass.simpleName, "Permission allowed.....")
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e(javaClass.simpleName, "In onactivityresult method....")
        for (permission in permissionList) {
            if (PermissionChecker.checkSelfPermission(applicationContext, permission) == PermissionChecker.PERMISSION_DENIED || PermissionChecker.checkSelfPermission(applicationContext, permission) == PermissionChecker.PERMISSION_DENIED_APP_OP) {
                android.support.v4.app.ActivityCompat.requestPermissions(this@LoginActivity, arrayOf(permission), 100)
            }
        }
    }
}