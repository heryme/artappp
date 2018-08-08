package com.artproficiencyapp.test.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Typeface
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView


import com.ahmedadeltito.photoeditorsdk.BrushDrawingView
import com.ahmedadeltito.photoeditorsdk.OnPhotoEditorSDKListener
import com.ahmedadeltito.photoeditorsdk.PhotoEditorSDK
import com.ahmedadeltito.photoeditorsdk.ViewType
import com.artproficiencyapp.R
import com.artproficiencyapp.activity.BaseActivity
import com.artproficiencyapp.test.adapter.ColorPickerAdapter
import com.artproficiencyapp.test.widget.SlidingUpPanelLayout
import com.bumptech.glide.Glide
import com.viewpagerindicator.PageIndicator

import java.text.SimpleDateFormat
import java.util.*

class PhotoEditorActivity : BaseActivity(), View.OnClickListener, OnPhotoEditorSDKListener {


    //private val TAG = "PhotoEditorActivity"
    private var parentImageRelativeLayout: RelativeLayout? = null
    private var drawingViewColorPickerRecyclerView: RecyclerView? = null
    private var undoTextView: TextView? = null
    private var undoTextTextView: TextView? = null
    private var doneDrawingTextView: TextView? = null
    private var eraseDrawingTextView: TextView? = null
    private var mLayout: SlidingUpPanelLayout? = null
    private var emojiFont: Typeface? = null
    private var topShadow: View? = null
    private var topShadowRelativeLayout: RelativeLayout? = null
    private var bottomShadow: View? = null
    private var bottomShadowRelativeLayout: RelativeLayout? = null
    private var colorPickerColors: ArrayList<Int>? = null
    private var colorCodeTextView = -1
    private var photoEditorSDK: PhotoEditorSDK? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_editor)



        //  val selectedImagePath = intent.extras!!.getString("Edit_Image")
        val bundle = intent.extras
        val selectedImagePath = bundle!!.getString("Edit_Image")
//        val options = BitmapFactory.Options()
//        options.inSampleSize = 1
//        val bitmap = BitmapFactory.decodeFile(selectedImagePath, options)

        val newFont = Typeface.createFromAsset(assets, "Eventtus-Icons.ttf")
        emojiFont = Typeface.createFromAsset(assets, "emojione-android.ttf")

        val brushDrawingView = findViewById<View>(R.id.drawing_view) as BrushDrawingView
        drawingViewColorPickerRecyclerView = findViewById<View>(R.id.drawing_view_color_picker_recycler_view) as RecyclerView
        parentImageRelativeLayout = findViewById<View>(R.id.parent_image_rl) as RelativeLayout
        val closeTextView = findViewById<View>(R.id.close_tv) as TextView
        val addTextView = findViewById<View>(R.id.add_text_tv) as TextView
        val addPencil = findViewById<View>(R.id.add_pencil_tv) as TextView
        val deleteRelativeLayout = findViewById<View>(R.id.delete_rl) as RelativeLayout
        val deleteTextView = findViewById<View>(R.id.delete_tv) as TextView
        val addImageEmojiTextView = findViewById<View>(R.id.add_image_emoji_tv) as TextView
        val saveTextView = findViewById<View>(R.id.save_tv) as TextView
        val saveTextTextView = findViewById<View>(R.id.save_text_tv) as TextView
        undoTextView = findViewById<View>(R.id.undo_tv) as TextView
        undoTextTextView = findViewById<View>(R.id.undo_text_tv) as TextView
        doneDrawingTextView = findViewById<View>(R.id.done_drawing_tv) as TextView
        eraseDrawingTextView = findViewById<View>(R.id.erase_drawing_tv) as TextView
        val clearAllTextView = findViewById<View>(R.id.clear_all_tv) as TextView
        val clearAllTextTextView = findViewById<View>(R.id.clear_all_text_tv) as TextView
        val goToNextTextView = findViewById<View>(R.id.go_to_next_screen_tv) as TextView
        val photoEditImageView = findViewById<View>(R.id.photo_edit_iv) as ImageView
        mLayout = findViewById<View>(R.id.sliding_layout) as SlidingUpPanelLayout
        topShadow = findViewById(R.id.top_shadow)
        topShadowRelativeLayout = findViewById<View>(R.id.top_parent_rl) as RelativeLayout
        bottomShadow = findViewById(R.id.bottom_shadow)
        bottomShadowRelativeLayout = findViewById<View>(R.id.bottom_parent_rl) as RelativeLayout

        //ViewPager pager = (ViewPager) findViewById(R.id.image_emoji_view_pager);
        val indicator = findViewById<View>(R.id.image_emoji_indicator) as PageIndicator

        //  photoEditImageView.setImageBitmap(bitmap)
        Glide.with(this).load(selectedImagePath)
                .thumbnail(0.5f)
                .into(photoEditImageView);

        closeTextView.typeface = newFont
        addTextView.typeface = newFont
        addPencil.typeface = newFont
        addImageEmojiTextView.typeface = newFont
        saveTextView.typeface = newFont
        undoTextView!!.typeface = newFont
        clearAllTextView.typeface = newFont
        goToNextTextView.typeface = newFont
        deleteTextView.typeface = newFont

        //        final List<Fragment> fragmentsList = new ArrayList<>();
        //        fragmentsList.add(new ImageFragment());
        //        fragmentsList.add(new EmojiFragment());

        //        PreviewSlidePagerAdapter adapter = new PreviewSlidePagerAdapter(getSupportFragmentManager(), fragmentsList);
        //        pager.setAdapter(adapter);
        //        pager.setOffscreenPageLimit(5);
        //        indicator.setViewPager(pager);

        photoEditorSDK = PhotoEditorSDK.PhotoEditorSDKBuilder(this@PhotoEditorActivity)
                .parentView(parentImageRelativeLayout) // add parent image view
                .childView(photoEditImageView) // add the desired image view
                .deleteView(deleteRelativeLayout) // add the deleted view that will appear during the movement of the views
                .brushDrawingView(brushDrawingView) // add the brush drawing view that is responsible for drawing on the image view
                .buildPhotoEditorSDK() // build photo editor sdk
        photoEditorSDK!!.setOnPhotoEditorSDKListener(this)
        //
        //        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
        //
        //            @Override
        //            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //
        //            }
        //
        //            @Override
        //            public void onPageSelected(int position) {
        //                if (position == 0)
        //                    mLayout.setScrollableView(((ImageFragment) fragmentsList.get(position)).imageRecyclerView);
        //                else if (position == 1)
        //                    mLayout.setScrollableView(((EmojiFragment) fragmentsList.get(position)).emojiRecyclerView);
        //            }
        //
        //            @Override
        //            public void onPageScrollStateChanged(int state) {
        //
        //            }
        //        });

        closeTextView.setOnClickListener(this)
        addImageEmojiTextView.setOnClickListener(this)
        addTextView.setOnClickListener(this)
        addPencil.setOnClickListener(this)
        saveTextView.setOnClickListener(this)
        saveTextTextView.setOnClickListener(this)
        undoTextView!!.setOnClickListener(this)
        undoTextTextView!!.setOnClickListener(this)
        doneDrawingTextView!!.setOnClickListener(this)
        eraseDrawingTextView!!.setOnClickListener(this)
        clearAllTextView.setOnClickListener(this)
        clearAllTextTextView.setOnClickListener(this)
        goToNextTextView.setOnClickListener(this)

        colorPickerColors = ArrayList()
        colorPickerColors!!.add(resources.getColor(R.color.black))
        colorPickerColors!!.add(resources.getColor(R.color.blue_color_picker))
        colorPickerColors!!.add(resources.getColor(R.color.brown_color_picker))
        colorPickerColors!!.add(resources.getColor(R.color.green_color_picker))
        colorPickerColors!!.add(resources.getColor(R.color.orange_color_picker))
        colorPickerColors!!.add(resources.getColor(R.color.red_color_picker))
        colorPickerColors!!.add(resources.getColor(R.color.red_orange_color_picker))
        colorPickerColors!!.add(resources.getColor(R.color.sky_blue_color_picker))
        colorPickerColors!!.add(resources.getColor(R.color.violet_color_picker))
        colorPickerColors!!.add(resources.getColor(R.color.white))
        colorPickerColors!!.add(resources.getColor(R.color.yellow_color_picker))
        colorPickerColors!!.add(resources.getColor(R.color.yellow_green_color_picker))

        object : CountDownTimer(500, 100) {

            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                // mLayout.setScrollableView(((ImageFragment) fragmentsList.get(0)).imageRecyclerView);
            }

        }.start()
    }






    private fun stringIsNotEmpty(string: String?): Boolean {
        if (string != null && string != "null") {
            if (string.trim { it <= ' ' } != "") {
                return true
            }
        }
        return false
    }

    fun addEmoji(emojiName: String) {
        photoEditorSDK!!.addEmoji(emojiName, emojiFont)
        if (mLayout != null)
            mLayout!!.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
    }

    fun addImage(image: Bitmap) {
        photoEditorSDK!!.addImage(image)
        if (mLayout != null)
            mLayout!!.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
    }

    private fun addText(text: String, colorCodeTextView: Int) {
        photoEditorSDK!!.addText(text, colorCodeTextView)
    }

    private fun clearAllViews() {
        photoEditorSDK!!.clearAllViews()
    }

    private fun undoViews() {
        photoEditorSDK!!.viewUndo()
    }

    private fun eraseDrawing() {
        photoEditorSDK!!.brushEraser()
    }

    private fun openAddTextPopupWindow(text: String, colorCode: Int) {
        colorCodeTextView = colorCode
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val addTextPopupWindowRootView = inflater.inflate(R.layout.add_text_popup_window, null)
        val addTextEditText = addTextPopupWindowRootView.findViewById<View>(R.id.add_text_edit_text) as EditText
        val addTextDoneTextView = addTextPopupWindowRootView.findViewById<View>(R.id.add_text_done_tv) as TextView
        val addTextColorPickerRecyclerView = addTextPopupWindowRootView.findViewById<View>(R.id.add_text_color_picker_recycler_view) as RecyclerView
        val layoutManager = LinearLayoutManager(this@PhotoEditorActivity, LinearLayoutManager.HORIZONTAL, false)
        addTextColorPickerRecyclerView.layoutManager = layoutManager
        addTextColorPickerRecyclerView.setHasFixedSize(true)
        val colorPickerAdapter = ColorPickerAdapter(this@PhotoEditorActivity, colorPickerColors!!)
        colorPickerAdapter.setOnColorPickerClickListener { colorCode ->
            addTextEditText.setTextColor(colorCode)
            colorCodeTextView = colorCode
        }
        addTextColorPickerRecyclerView.adapter = colorPickerAdapter
        if (stringIsNotEmpty(text)) {
            addTextEditText.setText(text)
            addTextEditText.setTextColor(if (colorCode == -1) resources.getColor(R.color.white) else colorCode)
        }
        val pop = PopupWindow(this@PhotoEditorActivity)
        pop.contentView = addTextPopupWindowRootView
        pop.width = LinearLayout.LayoutParams.MATCH_PARENT
        pop.height = LinearLayout.LayoutParams.MATCH_PARENT
        pop.isFocusable = true
        pop.setBackgroundDrawable(null)
        pop.showAtLocation(addTextPopupWindowRootView, Gravity.TOP, 0, 0)
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        addTextDoneTextView.setOnClickListener { view ->
            addText(addTextEditText.text.toString(), colorCodeTextView)
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            pop.dismiss()
        }
    }

    private fun updateView(visibility: Int) {
        topShadow!!.visibility = visibility
        topShadowRelativeLayout!!.visibility = visibility
        bottomShadow!!.visibility = visibility
        bottomShadowRelativeLayout!!.visibility = visibility
    }



    private fun updateBrushDrawingView(brushDrawingMode: Boolean) {
        photoEditorSDK!!.setBrushDrawingMode(brushDrawingMode)
        if (brushDrawingMode) {
            updateView(View.GONE)
            drawingViewColorPickerRecyclerView!!.visibility = View.VISIBLE
            doneDrawingTextView!!.visibility = View.VISIBLE
            eraseDrawingTextView!!.visibility = View.VISIBLE
            val layoutManager = LinearLayoutManager(this@PhotoEditorActivity, LinearLayoutManager.HORIZONTAL, false)
            drawingViewColorPickerRecyclerView!!.layoutManager = layoutManager
            drawingViewColorPickerRecyclerView!!.setHasFixedSize(true)
            val colorPickerAdapter = ColorPickerAdapter(this@PhotoEditorActivity, colorPickerColors!!)
            colorPickerAdapter.setOnColorPickerClickListener { colorCode -> photoEditorSDK!!.brushColor = colorCode }
            drawingViewColorPickerRecyclerView!!.adapter = colorPickerAdapter
        } else {
            updateView(View.VISIBLE)
            drawingViewColorPickerRecyclerView!!.visibility = View.GONE
            doneDrawingTextView!!.visibility = View.GONE
            eraseDrawingTextView!!.visibility = View.GONE
        }
    }

    private fun returnBackWithSavedImage() {
        updateView(View.GONE)
        val layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
        parentImageRelativeLayout!!.layoutParams = layoutParams

//        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//        val imageName = "IMG_$timeStamp.jpg"
//        val returnIntent = Intent()
//        returnIntent.putExtra("imagePath", photoEditorSDK!!.saveImage("ART APP", imageName))
//        Log.e(TAG, "Image Name==>" + photoEditorSDK!!.saveImage("ART APP", imageName))
//        setResult(Activity.RESULT_OK, returnIntent)

        object : CountDownTimer(1000, 500) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val imageName = "IMG_$timeStamp.jpg"
                val returnIntent = Intent()
                returnIntent.putExtra("imagePath", photoEditorSDK!!.saveImage("ART APP", imageName))
                Log.e(TAG,"Image Name==>"+photoEditorSDK!!.saveImage("ART APP", imageName))
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
        }.start()
    }

    override fun onClick(v: View) {
        if (v.id == R.id.close_tv) {
            setResult(Activity.RESULT_CANCELED)
            onBackPressed()
        } else if (v.id == R.id.add_image_emoji_tv) {
            mLayout!!.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
        } else if (v.id == R.id.add_text_tv) {
            openAddTextPopupWindow("", -1)
        } else if (v.id == R.id.add_pencil_tv) {
            updateBrushDrawingView(true)
        } else if (v.id == R.id.done_drawing_tv) {
            updateBrushDrawingView(false)
        } else if (v.id == R.id.save_tv || v.id == R.id.save_text_tv) {
            returnBackWithSavedImage()
        } else if (v.id == R.id.clear_all_tv || v.id == R.id.clear_all_text_tv) {
            clearAllViews()
        } else if (v.id == R.id.undo_text_tv || v.id == R.id.undo_tv) {
            undoViews()
        } else if (v.id == R.id.erase_drawing_tv) {
            eraseDrawing()
        } else if (v.id == R.id.go_to_next_screen_tv) {
            returnBackWithSavedImage()
        }
    }

    override fun onEditTextChangeListener(text: String, colorCode: Int) {
        openAddTextPopupWindow(text, colorCode)
    }

    override fun onAddViewListener(viewType: ViewType, numberOfAddedViews: Int) {
        if (numberOfAddedViews > 0) {
            undoTextView!!.visibility = View.VISIBLE
            undoTextTextView!!.visibility = View.VISIBLE
        }
        when (viewType) {
            ViewType.BRUSH_DRAWING -> Log.i("BRUSH_DRAWING", "onAddViewListener")
            ViewType.EMOJI -> Log.i("EMOJI", "onAddViewListener")
            ViewType.IMAGE -> Log.i("IMAGE", "onAddViewListener")
            ViewType.TEXT -> Log.i("TEXT", "onAddViewListener")
        }
    }

    override fun onRemoveViewListener(numberOfAddedViews: Int) {
        Log.i(TAG, "onRemoveViewListener")
        if (numberOfAddedViews == 0) {
            undoTextView!!.visibility = View.GONE
            undoTextTextView!!.visibility = View.GONE
        }
    }

    override fun onStartViewChangeListener(viewType: ViewType) {
        when (viewType) {
            ViewType.BRUSH_DRAWING -> Log.i("BRUSH_DRAWING", "onStartViewChangeListener")
            ViewType.EMOJI -> Log.i("EMOJI", "onStartViewChangeListener")
            ViewType.IMAGE -> Log.i("IMAGE", "onStartViewChangeListener")
            ViewType.TEXT -> Log.i("TEXT", "onStartViewChangeListener")
        }
    }

    override fun onStopViewChangeListener(viewType: ViewType) {
        when (viewType) {
            ViewType.BRUSH_DRAWING -> Log.i("BRUSH_DRAWING", "onStopViewChangeListener")
            ViewType.EMOJI -> Log.i("EMOJI", "onStopViewChangeListener")
            ViewType.IMAGE -> Log.i("IMAGE", "onStopViewChangeListener")
            ViewType.TEXT -> Log.i("TEXT", "onStopViewChangeListener")
        }
    }

    //    private class PreviewSlidePagerAdapter extends FragmentStatePagerAdapter {
    //        private List<Fragment> mFragments;
    //
    //        PreviewSlidePagerAdapter(FragmentManager fm, List<Fragment> fragments) {
    //            super(fm);
    //            mFragments = fragments;
    //        }
    //
    //        @Override
    //        public Fragment getItem(int position) {
    //            if (mFragments == null) {
    //                return (null);
    //            }
    //            return mFragments.get(position);
    //        }
    //
    //        @Override
    //        public int getCount() {
    //            return 2;
    //        }
    //    }
}
