package com.artproficiencyapp.fragment.student


import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v13.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.artproficiencyapp.R
import com.artproficiencyapp.common.PermissionHandler
import com.artproficiencyapp.fragment.commanfragment.BaseFragment
import com.artproficiencyapp.utils.Validator
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_edit_profile.*


class StudentEditProfileFragment : BaseFragment(), View.OnClickListener {
    private var resultUri: Uri? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        setTitle("Edit Profile")
        setToolbar(false)
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Initview()
    }

    private fun Initview() {
        etStuUpdateName.setText(getUserModel()!!.data.name)
        etStuUpdateEmail.setText(getUserModel()!!.data.email)
        etstuUpdatePhoNum.setText(getUserModel()!!.data.phone_number)
        btnStuUpdate.setOnClickListener(this)
        imgProfileViewStu.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {
        when (p0) {
            btnStuUpdate -> {
                if (IsValidInput()) {
                    showMsgDialog("Under development......")
                }
            }
            imgProfileViewStu -> {
                ShowProfileImageDialog()
            }

        }
    }

    private fun ShowProfileImageDialog() {
        val builder = AlertDialog.Builder(activity, android.R.style.Theme_Material_Light_Dialog_Alert)
        builder.setTitle(getString(R.string.edit_profile_msg))
        builder.setPositiveButton("Pick") { dialogInterface, i ->
            if (PermissionHandler.checkCameraPermission(activity) && PermissionHandler.checkReadExternalStoragePermission(activity)) {
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
                CropImage.startPickImageActivity(activity)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            else -> {
            }
        }
    }

    private fun requestCameraPermission(fromCamera: Boolean) {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
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
        Log.e(javaClass.simpleName, "In onActivityResult method")
        if (resultCode != Activity.RESULT_CANCELED) {
            when (requestCode) {
                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE // Crop
                -> {
                      val result = CropImage.getActivityResult(data)
                    try {
                        if (resultCode == AppCompatActivity.RESULT_OK) {
                            resultUri = result.uri
                            //                            imgPath = resultUri.toString();
                            Log.e("", "Path - " + resultUri!!.toString())
                            //                            uploadImage();
                            imgProfileViewStu!!.setImageURI(Uri.parse(resultUri!!.toString()))
                        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                            val error = result.error
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE // Gallery request
                -> try {
                    val selectedImageUri = CropImage.getPickImageResultUri(activity, data)
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
                .setActivityMenuIconColor(ContextCompat.getColor(activity, R.color.color_app_base))
                .setGuidelinesColor(ContextCompat.getColor(activity, R.color.color_app_base))
                .setScaleType(CropImageView.ScaleType.FIT_CENTER)
                .setFixAspectRatio(true)
                .setBorderCornerColor(ContextCompat.getColor(activity, R.color.color_app_base))
                .setBorderLineColor(ContextCompat.getColor(activity, R.color.color_app_base))
                .start(activity)
    }



    private fun IsValidInput(): Boolean {
        if (TextUtils.isEmpty(etStuUpdateName!!.text.toString().trim { it <= ' ' })) {
            showSnackBar(etStuUpdateName!!, getString(R.string.error_name))
            return false
        } else if (TextUtils.isEmpty(etStuUpdateEmail!!.text.toString().trim { it <= ' ' })) {
            showSnackBar(etStuUpdateEmail!!, getString(R.string.error_email))
            return false
        } else if (TextUtils.isEmpty(etstuUpdatePhoNum!!.text.toString())) {
            showSnackBar(etstuUpdatePhoNum!!, getString(R.string.error_ph_num))
            return false
        } else if (!Validator.validateEmail(etStuUpdateEmail!!.text.toString().trim { it <= ' ' })) {
            showSnackBar(etStuUpdateEmail!!, getString(R.string.error_valid_email))
            return false
        } else if (etstuUpdatePhoNum!!.text.toString().length < 10) {
            showSnackBar(etstuUpdatePhoNum!!, getString(R.string.error_short_ph_num))
            return false
        }
        return true
    }

}
