package com.artproficiencyapp.activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v13.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.artproficiencyapp.R
import com.artproficiencyapp.common.PermissionHandler
import com.artproficiencyapp.utils.Validator
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

import org.json.JSONArray
import org.json.JSONException

import java.io.File
import java.util.ArrayList

import butterknife.BindView
import butterknife.ButterKnife
import com.artproficiencyapp.common.PermissionHandler.saveImage
import com.artproficiencyapp.extension.*


import com.artproficiencyapp.model.CountryCodeModel
import com.artproficiencyapp.model.LevelModel
import com.artproficiencyapp.model.ReportModel
import com.artproficiencyapp.restapi.ApiInitialize
import com.artproficiencyapp.restapi.ApiRequest
import com.artproficiencyapp.restapi.ApiResponseInterface
import com.artproficiencyapp.restapi.ApiResponseManager
import kotlinx.android.synthetic.main.activity_update_profile.*
import kotlinx.android.synthetic.main.fragment_director_signup.*
import kotlinx.android.synthetic.main.parent_toolbar.*
import java.io.IOException

/**
 * Created by admin on 21-May-18.
 */

class EditProfileActivity : BaseActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener, ApiResponseInterface {
    private lateinit var reportmodel: ReportModel
    private var report: String = ""
    private val reportList = ArrayList<String>()
    var path = ""
    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {
            REPORT_AGENCY -> {
                reportmodel = apiResponseManager.response as ReportModel
                when (reportmodel.status_code) {
                    STATUS_CODE -> {
                        (0 until reportmodel.data.size!!).mapTo(reportList) { reportmodel?.data!![it]?.name!! }
                        reportList.add(0, getString(R.string.report_agenciess))
                        LoggE(TAG, "countryList==>${reportList.size}")
                        setReportAgency(reportList)
                    }
                }
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when (p0) {

//            spDirEditCountryCode -> {
//                Log.e(TAG, "Country code selected is - " + countryCode!![p2]._mCountryName)
//            }
            spDirSignUpLevelSelection -> {
                report = reportList[p2]
                LoggE(TAG, "country====>$report")
            }
        }
    }


    private val mProfileImg: File? = null
    private val CAMERA_MODE_SELECTION = 101
    private var resultUri: Uri? = null
    private val countryCode = ArrayList<CountryCodeModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)
        ButterKnife.bind(this)

        InitViews()
        SetClickListener()
        ApiRequest(this, ApiInitialize.initialize(ApiInitialize.MAIN_URl_API).reportAgency("Bearer " + getUserModel()!!.data.token), REPORT_AGENCY, true, this)
        val email = sessionManager[getString(R.string.user_email), ""]
        val password = sessionManager[getString(R.string.user_password), ""]

        Log.e(TAG, "Edit Email value==  " + email)
        Log.e(TAG, "  Edit password value==  " + password)
    }

    private fun SetClickListener() {
        img_toolbar_back!!.setOnClickListener(this)
        btnDirUpdate!!.setOnClickListener(this)
        imgProfileView!!.setOnClickListener(this)
    }

    private fun InitViews() {
        tv_toolbar_title!!.text = getString(R.string.edit_profile_title)
        etDirUpdateName.setText(getUserModel()!!.data.name)
        etDirUpdateDesignation.setText(getUserModel()!!.data.designation)
        etDirUpdateEmail.setText(getUserModel()!!.data.email)
        etDirUpdatePhoNum.setText(getUserModel()!!.data.phone_number)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        goActivity<DirectorProfileActivity>()
        finish()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnDirUpdate -> if (IsValidInput()) {
                if (isNetWork(this)) {
                    showMsgDialog("Under development......")
                    sessionManager.put(getString(R.string.is_user_registered), false)
                    goActivity<DashBordActivityDIreStu>()
                    finish()
                }

            }
            R.id.imgProfileView -> {
                ShowProfileImageDialog()
            }
            R.id.img_toolbar_back -> {
                if (sessionManager.get(getString(R.string.is_user_registered), false)) {
                    sessionManager.put(getString(R.string.is_user_registered), false)
                    goActivity<DirectorProfileActivity>()
                } else {
                    goActivity<DirectorProfileActivity>()
                    finish()
                }
            }
        }
    }

    private fun ShowProfileImageDialog() {
        val builder = AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert)
        builder.setTitle(getString(R.string.edit_profile_msg))
        builder.setPositiveButton("Pick") { dialogInterface, i ->
            if (PermissionHandler.checkCameraPermission(this@EditProfileActivity) && PermissionHandler.checkReadExternalStoragePermission(this@EditProfileActivity)) {
                captureImageInitialization(0) // Camera
            } else {
                requestCameraPermission(true)
            }
        }
        builder.setNegativeButton("Cancel") { dialogInterface, i -> dialogInterface.dismiss() }
        builder.setCancelable(true)

        val mDialog = builder.create()
        mDialog.show()
    }

    private fun captureImageInitialization(item: Int) {
        when (item) {
            0 //Camera
            -> try {
                CropImage.startPickImageActivity(this@EditProfileActivity)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            else -> {
            }
        }
    }

    private fun setReportAgency(regionList: MutableList<String>?) {
        LoggE(TAG, "=levelList=>${regionList?.size}")
        spDirUpdateReportAgency.onItemSelectedListener = this
        var regionAdapter = object : ArrayAdapter<String>(this@EditProfileActivity, R.layout.row_spinner_membership, regionList) {
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
        spDirUpdateReportAgency.adapter = regionAdapter
    }

    private fun requestCameraPermission(fromCamera: Boolean) {
        ActivityCompat.requestPermissions(this@EditProfileActivity, arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> if (grantResults.size > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Camera Granted", "Permission Granted, Now you can access camera.")
                    captureImageInitialization(0)
                }
            } else {
                showMsgDialog("Permissions Denied, Please allow all permissions..")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_CANCELED) {
            when (requestCode) {
                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE // Crop
                -> {
                    val result = CropImage.getActivityResult(data)


                    try {
                        if (resultCode == RESULT_OK) {
                            resultUri = result.uri
                            val bit: Bitmap = BitmapFactory.decodeStream(this.contentResolver.openInputStream(resultUri), null, null)
                            path = saveImage(bit, this)
                            //             imgPath = resultUri.toString();
                            Log.e(TAG, "Path - " + path)
                            Log.e(TAG, "Path - " + resultUri.toString())
                            //                            uploadImage();
                            imgProfileView!!.setImageURI(Uri.parse(resultUri!!.toString()))
                            Log.e("", "Image PAthhhhhh==>" + path)

                        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                            val error = result.error
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE // Gallery request
                -> try {
                    val selectedImageUri = CropImage.getPickImageResultUri(this, data)
                    startCropImageActivity(selectedImageUri)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun startCropImageActivity(imageUri: Uri?) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setActivityMenuIconColor(ContextCompat.getColor(this@EditProfileActivity, R.color.color_app_base))
                .setGuidelinesColor(ContextCompat.getColor(this@EditProfileActivity, R.color.color_app_base))
                .setScaleType(CropImageView.ScaleType.FIT_CENTER)
                .setFixAspectRatio(true)
                .setBorderCornerColor(ContextCompat.getColor(this@EditProfileActivity, R.color.color_app_base))
                .setBorderLineColor(ContextCompat.getColor(this@EditProfileActivity, R.color.color_app_base))
                .start(this)
    }

    private fun IsValidInput(): Boolean {
        if (TextUtils.isEmpty(etDirUpdateName!!.text.toString().trim { it <= ' ' })) {
            showSnackBar(etDirUpdateName!!, getString(R.string.error_name))
            return false
        } else if (TextUtils.isEmpty(etDirUpdateDesignation!!.text.toString().trim { it <= ' ' })) {
            showSnackBar(etDirUpdateDesignation!!, getString(R.string.error_designation))
            return false
        } else if (TextUtils.isEmpty(etDirUpdateEmail!!.text.toString().trim { it <= ' ' })) {
            showSnackBar(etDirUpdateEmail!!, getString(R.string.error_email))
            return false
        } else if (TextUtils.isEmpty(etDirUpdatePhoNum!!.text.toString())) {
            showSnackBar(etDirUpdatePhoNum!!, getString(R.string.error_ph_num))
            return false
        } else if (!Validator.validateEmail(etDirUpdateEmail!!.text.toString().trim { it <= ' ' })) {
            showSnackBar(etDirUpdateEmail!!, getString(R.string.error_valid_email))
            return false
        } else if (etDirUpdatePhoNum!!.text.toString().length < 10) {
            showSnackBar(etDirUpdatePhoNum!!, getString(R.string.error_short_ph_num))
            return false
        } else if (spDirUpdateReportAgency.getSelectedItem().toString().trim().equals("Report Agencies")) {
            showSnackBar(etDirUpdatePhoNum!!, getString(R.string.error_report_selection))
            return false
        }
        return true
    }


}