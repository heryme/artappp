package com.artproficiencyapp.test.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.TextView
import com.artproficiencyapp.R

import com.artproficiencyapp.activity.BaseActivity
import com.artproficiencyapp.extension.*
import com.artproficiencyapp.restapi.ApiInitialize
import com.artproficiencyapp.restapi.ApiRequest
import com.artproficiencyapp.restapi.ApiResponseInterface
import com.artproficiencyapp.restapi.ApiResponseManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_embryology.*
import kotlinx.android.synthetic.main.five_option_single_selected.*
import kotlinx.android.synthetic.main.question_seekbar.*
import kotlinx.android.synthetic.main.slider_of_percentage_and_one_button.*
import kotlinx.android.synthetic.main.slider_with_option_and_no_button.*
import kotlinx.android.synthetic.main.slider_with_option_and_one_button.*
import kotlinx.android.synthetic.main.three_of_one_button_single_selection.*
import org.json.JSONArray
import org.json.JSONObject
import com.artproficiencyapp.adapter.QuestionImagePagerAdapter
import android.support.v4.view.ViewPager
import com.artproficiencyapp.test.model.Question
import com.artproficiencyapp.receiver.ConnectivityReceiver


class EmbryologyActivity : BaseActivity(), View.OnClickListener, ApiResponseInterface {
    // private val TAG = EmbryologyReviewAdapter::class.java.name
    //  private val TAG = EmbryologyActivity::class.java.name

    private var progressBarFragmentEmbryology: ProgressBar? = null
    private var tvFragmentEmbryologyTotalCount: TextView? = null
    private var btnFragmentEmbryologyNext: Button? = null
    private var btnFragmentEmbryologyIncrement: Button? = null
    private var btnFragmentEmbryologyDecrement: Button? = null
    private var TIMER = 0L/*300000L*/
    internal var max = 100f
    internal var totalNoOfQuestion = 0
    internal var questionCounter = 0;
    internal var per = 0
    internal var incDec = 0
    private lateinit var questionModel: Question
    private var answerMap: HashMap<Int, String>? = null
    private var questionImagePagerAdapter: QuestionImagePagerAdapter? = null
    private val ImagesArray = ArrayList<String>()

    private var leftTime: Long = 0
    private var isTimerStart: Boolean = false
    private var viewPager: ViewPager? = null
    private var imageViewPagerPostion = 0;
    private var IMAGE_EDIT_MODE = 200

    //private var finishPhotoActivity: FinishPhotoEditInterface? = null

    companion object {
        lateinit var countDownTimer: CountDownTimer
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_embryology)
        setUpView()
        Initview()


        if (ConnectivityReceiver.isConnected())
            showMessage(ConnectivityReceiver.isConnected())
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        super.onNetworkConnectionChanged(isConnected)
        Log.e(TAG, "In $TAG ---- onNetworkConnectionChanged")
        try {
            if (!isConnected) {
               // stopCounter()
                countDownTimer.cancel()
                isTimerStart = false
                Log.e(TAG, " if Left time---- $leftTime")
            } else {
                Log.e(TAG, " else Left time---- $leftTime")
                //startCounter()
                // Toast("Esle",true,this)
                if (leftTime > 0) {
                    Log.e("TAG", "Left Time--->" + leftTime)
                    startTimer(leftTime)
                    leftTime = 0

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



    private fun Initview() {
        answerMap = HashMap()
        // calculateProgress()
        registerClickEvent()
        callGetQuestionAPI()
        //questionImagePagerAdapter = obje QuestionImagePagerAdapter(imageList);
    }

    var viewPagerPageChangeListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            imageViewPagerPostion = position
            questionImagePagerAdapter!!.notifyDataSetChanged()
        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {

        }

        override fun onPageScrollStateChanged(arg0: Int) {

        }
    }

    /***
     * Call Get Question API
     */
    private fun callGetQuestionAPI() {

        val questionId = intent.getStringExtra("EXTRA_SESSION_ID")
        Log.e("EXTRA_SESSION_ID", "EXTRA_SESSION_ID" + questionId)

        if (isNetWork(this)) {
            ApiRequest(this@EmbryologyActivity,
                    ApiInitialize.initialize(ApiInitialize.MAIN_URl_API).getQuestionList("Bearer " + getUserModel()!!.data.token, questionId),
                    GET_QUESTION, true, this);
        }

    }

    /**
     * Calculate Percentage Of The Progress Bar
     */
    private fun calculateProgress() {
        per += Math.ceil((max / totalNoOfQuestion).toDouble()).toInt()
        progressBarFragmentEmbryology!!.progress = per
    }

    /***
     * Set Up View
     */
    private fun setUpView() {
        // viewPagerFragmentEmbryology = view.findViewById<View>(R.id.viewPagerFragmentEmbryology) as ViewPager
        progressBarFragmentEmbryology = findViewById<ProgressBar>(R.id.progressBarFragmentEmbryology)
        btnFragmentEmbryologyNext = findViewById<Button>(R.id.btnFragmentEmbryologyNext) as Button
        btnFragmentEmbryologyIncrement = findViewById<Button>(R.id.btnFragmentEmbryologyIncrement)
        btnFragmentEmbryologyDecrement = findViewById<Button>(R.id.btnFragmentEmbryologyDecrement)
        tvFragmentEmbryologyTotalCount = findViewById<TextView>(R.id.tvFragmentEmbryologyTotalCount)

    }

    /**
     * Register Button Click
     */
    private fun registerClickEvent() {
        btnFragmentEmbryologyNext!!.setOnClickListener(this)
        btnFragmentEmbryologyIncrement!!.setOnClickListener(this)
        btnFragmentEmbryologyDecrement!!.setOnClickListener(this)
//        btnFragmentEmbryologyNext!!.setOnClickListener(this)
        btnEmbrylogyActivityEdit.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view) {
            btnFragmentEmbryologyNext -> {
                /* if (radioGroupEmbryologyQuestionSeekBarFeedback.getCheckedRadioButtonId() == -1) {
                     Toast("Select at least one option", true, activity)
                 } else {*/
//                incDec = 0
//                tvFragmentEmbryologyTotalCount!!.text = incDec.toString()
//                nextQuestion();
                resetComponent()
                // }
            }
            btnFragmentEmbryologyIncrement -> {
                incDec = incDec + 1
                tvFragmentEmbryologyTotalCount!!.text = incDec.toString()
                btnFragmentEmbryologyDecrement!!.setEnabled(false);
            }
            btnFragmentEmbryologyDecrement -> if (incDec > 0) {
                incDec = incDec - 1
                tvFragmentEmbryologyTotalCount!!.text = incDec.toString()
                btnFragmentEmbryologyIncrement!!.setEnabled(false);

            }
            btnEmbrylogyActivityEdit -> {

                val intent = Intent(this@EmbryologyActivity, PhotoEditorActivity::class.java)
                val bundle = Bundle()
                bundle.putString("Edit_Image", ImagesArray[imageViewPagerPostion])
                intent.putExtras(bundle)
                startActivityForResult(intent, IMAGE_EDIT_MODE)
                countDownTimer.cancel()
                LoggE("TAG", "Left Time Osm----->" + leftTime )


//                Toast("image ====  "+ImagesArray[imageViewPagerPostion],true,this)
//                Log.e("","Imageeee==>> "+ImagesArray[imageViewPagerPostion])
                // ImagesArray[imageViewPagerPostion]
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((resultCode == Activity.RESULT_OK || resultCode != Activity.RESULT_CANCELED) && requestCode == IMAGE_EDIT_MODE) {
            if (data != null) {
                LoggE(TAG, "In onActivityResult code method./....." + data.getStringExtra("imagePath"))
                ImagesArray.set(imageViewPagerPostion, data.getStringExtra("imagePath"))
                questionImagePagerAdapter!!.notifyDataSetChanged()
                Toast("Yes",true,this)
                startTimer(leftTime)
                leftTime = 0
            }
        }else{
            startTimer(leftTime)
            leftTime = 0
        }

    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {
            GET_QUESTION -> {
                questionModel = apiResponseManager.response as Question

                if (questionModel.status_code == STATUS_CODE) {
                    setUpExam(true);
                }
            }
        }
    }

    /**
     * Set Option Radio Button six selection radio button
     */
    private fun radioButtonOptionTitleSixoptionSigleSelection(optionList: MutableList<String>) {
        if (optionList.size > 0) {
            LoggE("TAG", "Sizzee->" + optionList.size)
            rbEmbryologyQuestionSeekBarFeedbackOne.setText(optionList.get(0))
            rbEmbryologyQuestionSeekBarFeedbackTwo.setText(optionList.get(1))
            rbEmbryologyQuestionSeekBarFeedbackThree.setText(optionList.get(2))
            rbEmbryologyQuestionSeekBarFeedbackFour.setText(optionList.get(3))
            rbEmbryologyQuestionSeekBarFeedbackFive.setText(optionList.get(4))
            rbEmbryologyQuestionSeekBarFeedbackSix.setText(optionList.get(5))
        }
    }

    /**
     * Set Option Radio Button THREE_OPTION_SINGLE_SELECTED
     */
    private fun radioButtonOptionTitleThreeOptionSigleSelection(optionList: MutableList<String>) {
        if (optionList.size > 0) {
            LoggE("TAG", "Sizzee->" + optionList.size)
            rbEmbryologyQuestionThreeOfOneSingleSelectionOtherButtonOne.setText(optionList.get(0))
            rbEmbryologyQuestionThreeOfOneSingleSelectionOtherButtonTwo.setText(optionList.get(1))
            rbEmbryologyQuestionThreeOfOneSingleSelectionOtherButtonThree.setText(optionList.get(2))
            rbEmbryologyQuestionThreeOfOneSingleSelectionOtherButtonOther.setText(optionList.get(3))
        }
    }

    /**
     * Set Option Radio Button FIVE_OPTION_SINGLE_SELECTED
     */
    private fun radioButtonOptionTitleFiveOptionSigleSelection(optionList: MutableList<String>) {
        if (optionList.size > 0) {
            LoggE("TAG", "Sizzee->" + optionList.size)
            rbEmbryologyQuestionFiveOptionSingleSelectedOne.setText(optionList.get(0))
            rbEmbryologyQuestionFiveOptionSingleSelectedTwo.setText(optionList.get(1))
            rbEmbryologyQuestionFiveOptionSingleSelectedThree.setText(optionList.get(2))
            rbEmbryologyQuestionFiveOptionSingleSelectedFour.setText(optionList.get(3))
            rbrbEmbryologyQuestionFiveOptionSingleSelectedFive.setText(optionList.get(4))
        }
    }

    /**
     * Set Option Radio Button SLIDER_WITH_OPTION_AND_NO_BUTTON
     */
    private fun radioButtonOptionTitleSliderWithOptionAndNoButton(optionList: MutableList<String>) {
        if (optionList.size > 0) {
            LoggE("TAG", "Sizzee->" + optionList.size)

            if (optionList.size == 4) {
                rbEmbryologyQuestionSliderWithOptionAndNoButtonButtonOne.setText(optionList.get(0))
                rbEmbryologyQuestionSliderWithOptionAndNoButtonButtonTwo.setText(optionList.get(1))
                rbEmbryologyQuestionSliderWithOptionAndNoButtonButtonThree.setText(optionList.get(2))
                rbEmbryologyQuestionSliderWithOptionAndNoButtonButtonOther.setText(optionList.get(3))
            } else {
                rbEmbryologyQuestionSliderWithOptionAndNoButtonButtonOne.setText(optionList.get(0))
                rbEmbryologyQuestionSliderWithOptionAndNoButtonButtonTwo.setText(optionList.get(1))
                rbEmbryologyQuestionSliderWithOptionAndNoButtonButtonThree.setText(optionList.get(2))
                rbEmbryologyQuestionSliderWithOptionAndNoButtonButtonOther.visibility = View.GONE
            }
        }
    }

    /**
     * Set Option Radio Button SLIDER_WITH_OPTION_AND_ONE_BUTTON
     */
    private fun radioButtonOptionTitleSliderWithOptionAndOneButton(optionList: MutableList<String>) {
        if (optionList.size > 0) {
            LoggE("TAG", "Sizzee->" + optionList.size)
            rbEmbryologyQuestionSliderWithOptionWithOneButtonOne.setText(optionList.get(0))
            rbEmbryologyQuestionSliderWithOptionWithOneButtonTwo.setText(optionList.get(1))
            rbEmbryologyQuestionSliderWithOptionWithOneButtonThree.setText(optionList.get(2))
            rbEmbryologyQuestionSliderWithOptionWithOneButtonFour.setText(optionList.get(3))
            rbrbEmbryologyQuestionSliderWithOptionWithOneButtonFive.setText(optionList.get(4))


        }
    }

    /**
     * Set Option Radio Button SLIDER_OF_PERCENTAGE_WITH_ONE_BUTTON
     */
    private fun radioButtonOptionTitleSliderOfPercentageAndOneButton(optionList: MutableList<String>) {
        if (optionList.size > 0) {
            LoggE("TAG", "Sizzee->" + optionList.size)
            rbEmbryologyQuestionSliderSliderOfPercentageWithOneButtonOne.setText(optionList.get(0))
            rbEmbryologyQuestionSliderSliderOfPercentageWithOneButtonTwo.setText(optionList.get(1))
            rbEmbryologyQuestionSliderSliderOfPercentageWithOneButtonThree.setText(optionList.get(2))
        }
    }

    /**
     * Set Question Image
     */
    private fun setQuestionImage(url: String) {
        Log.e(javaClass.simpleName, "Image url is ------ $url")
        Glide.with(this@EmbryologyActivity).load(url)
                .thumbnail(0.5f)
                .into(ivFragmentEmbryologyQuestionImage)
    }

    /**
     * Go to next question
     */
    private fun nextQuestion() {
        LoggE("TAG", "Counter-->" + questionCounter)
        //questi        onCounter++
        if (questionCounter < totalNoOfQuestion) {
            calculateProgress()
            countDownTimer.cancel()
            isTimerStart = false
            crateAnswerList()
            questionCounter++
            if (questionCounter < totalNoOfQuestion) {
                setUpExam(false);
            } else {
                // calculateProgress()
                //crateAnswerList()
                createJsonAnswer()
                val intent = Intent(this@EmbryologyActivity, TestFinishActivity::class.java)
                startActivity(intent)
                finish()
                resetComponent()
                countDownTimer.cancel()
                isTimerStart = false
            }


        } /*else {
            calculateProgress()
            //crateAnswerList()
            val intent = Intent(this@EmbryologyActivity, TestFinishActivity::class.java)
            startActivity(intent)
            resetComponent()
            countDownTimer.cancel()
        }*/
    }

    /***
     * Set Question Timer
     */
    private fun startTimer(timer: Long) {

        Log.e(javaClass.simpleName, "Timer for this question is --- $timer")
        val time = timer * 1000
        countDownTimer = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tvTimeCounter.text = String.format("%02d:%02d", millisUntilFinished / 60000, (millisUntilFinished % 60000 / 1000))
                leftTime = millisUntilFinished / 1000

                LoggE("TAG","OnTick---->" + leftTime)
            }
            override fun onFinish() {
                tvTimeCounter.text = "00:00"
                btnFragmentEmbryologyNext!!.performClick()


            }
        }.start()
        isTimerStart = true
    }


    /**
     * Set up exam
     */
    private fun setUpExam(isCalculate: Boolean) {
        if (questionModel.status_code == STATUS_CODE) {
            totalNoOfQuestion = questionModel.data.size

            if (totalNoOfQuestion == 0) {

                notAvailable.visibility = View.VISIBLE
                finish.visibility = View.VISIBLE
                finish.setOnClickListener {
                    finish()
                }
                mainViewFragmentEmbryology.visibility = View.GONE

            } else {
                if (isCalculate) {
                    calculateProgress()
                }
                Log.d(TAG, "Question List Size" + questionModel.data.get(questionCounter).extra.size)
                //Set Question
                tvFragmentEmbryologyQuestionTitle!!.text = questionModel.data.get(questionCounter).name
                //Set Question Image
                setQuestionImage(questionModel.data.get(questionCounter).image_url.get(0))

// .replace("http://139.59.24.105/artcompass/public/uploads/",
//                        "http://139.59.24.105/artcompass/public/uploads/"))
                //Show Full View
                mainViewFragmentEmbryology.visibility = View.VISIBLE
                //Show Hide View According To Question Type
                if (questionModel.data.get(questionCounter).type.equals("INCREMENT_DECREMENT")) {
                    //Set Question Time according to api

                    startTimer(questionModel.data.get(questionCounter).qseconds.toLong())

                    questionSliderWithOptionFragmentEmbryology.visibility = View.GONE
                    increamentDecrementFragmentEmbryology.visibility = View.VISIBLE
                    three_option_single_selection.visibility = View.GONE
                    fiveOptionSingleSelection.visibility = View.GONE
                    sliderWithOptionWithNoButton.visibility = View.GONE
                    sliderWithOptionWithOneButton.visibility = View.GONE
                    sliderOfPercentageWithOneButton.visibility = View.GONE
                } else if (questionModel.data.get(questionCounter).type.equals("SIX_OPTION_SINGLE_SELECTED")) {
                    //Set Question Time according to api
                    if (questionModel.data.get(0).extra.size > 0) {
                        var optionList: MutableList<String> = ArrayList()
                        for (item in questionModel.data.get(questionCounter).extra) {
                            if (item.type.equals("OPTION")) {
                                optionList.add(item.value)
                            }
                        }
                        //Set Option Ratio Button
                        radioButtonOptionTitleSixoptionSigleSelection(optionList)
                    }
                    startTimer(questionModel.data.get(questionCounter).qseconds.toLong())
                    questionSliderWithOptionFragmentEmbryology.visibility = View.VISIBLE
                    increamentDecrementFragmentEmbryology.visibility = View.GONE
                    three_option_single_selection.visibility = View.GONE
                    fiveOptionSingleSelection.visibility = View.GONE
                    sliderWithOptionWithNoButton.visibility = View.GONE
                    sliderWithOptionWithOneButton.visibility = View.GONE
                    sliderOfPercentageWithOneButton.visibility = View.GONE
                } else if (questionModel.data.get(questionCounter).type.equals("SLIDER_WITH_OPTION_AND_NO_BUTTON")) {


                    if (questionModel.data.get(0).extra.size > 0) {
                        var optionList: MutableList<String> = ArrayList()
                        for (item in questionModel.data.get(questionCounter).extra) {
                            if (item.type.equals("OPTION") || item.type.equals("BUTTON")) {
                                optionList.add(item.value)
                            }
                        }
                        //Set Option Ratio Button
                        radioButtonOptionTitleSliderWithOptionAndNoButton(optionList)
                    }
                    startTimer(questionModel.data.get(questionCounter).qseconds.toLong())
                    questionSliderWithOptionFragmentEmbryology.visibility = View.GONE
                    increamentDecrementFragmentEmbryology.visibility = View.GONE
                    three_option_single_selection.visibility = View.GONE
                    fiveOptionSingleSelection.visibility = View.GONE
                    sliderWithOptionWithNoButton.visibility = View.VISIBLE
                    sliderWithOptionWithOneButton.visibility = View.GONE
                    sliderOfPercentageWithOneButton.visibility = View.GONE
                } else if (questionModel.data.get(questionCounter).type.equals("SLIDER_WITH_OPTION_AND_ONE_BUTTON")) {
                    if (questionModel.data.get(0).extra.size > 0) {
                        var optionList: MutableList<String> = ArrayList()
                        for (item in questionModel.data.get(questionCounter).extra) {
                            if (item.type.equals("OPTION") || item.type.equals("BUTTON")) {
                                optionList.add(item.value)
                            }
                        }
                        //Set Option Ratio Button
                        radioButtonOptionTitleSliderWithOptionAndOneButton(optionList)
                    }
                    startTimer(questionModel.data.get(questionCounter).qseconds.toLong())
                    questionSliderWithOptionFragmentEmbryology.visibility = View.GONE
                    increamentDecrementFragmentEmbryology.visibility = View.GONE
                    three_option_single_selection.visibility = View.GONE
                    fiveOptionSingleSelection.visibility = View.GONE
                    sliderWithOptionWithNoButton.visibility = View.GONE
                    sliderWithOptionWithOneButton.visibility = View.VISIBLE
                    sliderOfPercentageWithOneButton.visibility = View.GONE
                } else if (questionModel.data.get(questionCounter).type.equals("THREE_OF_ONE_BUTTON_SINGLE_SELECTION")) {
                    if (questionModel.data.get(0).extra.size > 0) {
                        var optionList: MutableList<String> = ArrayList()
                        for (item in questionModel.data.get(questionCounter).extra) {
                            if (item.type.equals("OPTION") || item.type.equals("BUTTON")) {
                                optionList.add(item.value)
                            }
                        }
                        //Set Option Ratio Button
                        radioButtonOptionTitleThreeOptionSigleSelection(optionList)
                    }
                    startTimer(questionModel.data.get(questionCounter).qseconds.toLong())
                    questionSliderWithOptionFragmentEmbryology.visibility = View.GONE
                    increamentDecrementFragmentEmbryology.visibility = View.GONE
                    three_option_single_selection.visibility = View.VISIBLE
                    fiveOptionSingleSelection.visibility = View.GONE
                    sliderWithOptionWithNoButton.visibility = View.GONE
                    sliderWithOptionWithOneButton.visibility = View.GONE
                    sliderOfPercentageWithOneButton.visibility = View.GONE
                } else if (questionModel.data.get(questionCounter).type.equals("SLIDER_OF_PERCENTAGE_WITH_ONE_BUTTON")) {

                    if (questionModel.data.get(0).extra.size > 0) {
                        var optionList: MutableList<String> = ArrayList()
                        for (item in questionModel.data.get(questionCounter).extra) {
                            if (item.type.equals("OPTION") || item.type.equals("BUTTON")) {
                                optionList.add(item.value)
                            }
                        }
                        //Set Option Ratio Button
                        radioButtonOptionTitleSliderOfPercentageAndOneButton(optionList)
                    }
                    startTimer(questionModel.data.get(questionCounter).qseconds.toLong())
                    questionSliderWithOptionFragmentEmbryology.visibility = View.GONE
                    increamentDecrementFragmentEmbryology.visibility = View.GONE
                    three_option_single_selection.visibility = View.GONE
                    fiveOptionSingleSelection.visibility = View.GONE
                    sliderWithOptionWithNoButton.visibility = View.GONE
                    sliderWithOptionWithOneButton.visibility = View.GONE
                    sliderOfPercentageWithOneButton.visibility = View.VISIBLE
                } else if (questionModel.data.get(questionCounter).type.equals("FIVE_OPTION_SINGLE_SELECTED")) {
                    if (questionModel.data.get(0).extra.size > 0) {
                        var optionList: MutableList<String> = ArrayList()
                        for (item in questionModel.data.get(questionCounter).extra) {
                            if (item.type.equals("OPTION")) {
                                optionList.add(item.value)
                            }
                        }
                        //Set Option Ratio Button
                        radioButtonOptionTitleFiveOptionSigleSelection(optionList)
                    }
                    startTimer(questionModel.data.get(questionCounter).qseconds.toLong())
                    questionSliderWithOptionFragmentEmbryology.visibility = View.GONE
                    increamentDecrementFragmentEmbryology.visibility = View.GONE
                    three_option_single_selection.visibility = View.GONE
                    fiveOptionSingleSelection.visibility = View.VISIBLE
                    sliderWithOptionWithNoButton.visibility = View.GONE
                    sliderWithOptionWithOneButton.visibility = View.GONE
                    sliderOfPercentageWithOneButton.visibility = View.GONE
                } else if (questionModel.data.get(questionCounter).type.equals("MULTIPLE_IMAGE")) {
                    startTimer(questionModel.data.get(questionCounter).qseconds.toLong())
                    for (i in 0 until questionModel.data.get(questionCounter).extra.size) {
                        ImagesArray.add(questionModel.data.get(questionCounter).extra[i].value)

                    }
                    questionImagePagerAdapter = QuestionImagePagerAdapter(this, ImagesArray);
                    viewPagerFragmentEmbryology.setAdapter(questionImagePagerAdapter);
                    viewPagerFragmentEmbryology.addOnPageChangeListener(viewPagerPageChangeListener);
                    viewPagerFragmentEmbryology.visibility = View.VISIBLE
                    ivFragmentEmbryologyQuestionImage.visibility = View.GONE
                    btnEmbrylogyActivityEdit.visibility = View.VISIBLE


                }
            }
        }
    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }

    /**
     * Reset Radio Button And TextView Counter Value
     */
    private fun resetComponent() {
        nextQuestion();
        radioGroupEmbryologyQuestionSeekBarFeedback.clearCheck()
        radioGroupEmbryologyQuestionThreeOfOneSingleSelectionOtherButton.clearCheck()
        radioGroupEmbryologyQuestionFiveOptionSingleSelected.clearCheck()
        radioGroupEmbryologySliderWithOptionAndNoButton.clearCheck()
        radioGroupEmbryologyQuestionSliderWithOptionWithOneButton.clearCheck()
        radioGroupEmbryologyQuestionSliderOfPercentageWithOneButton.clearCheck()
        incDec = 0
        tvFragmentEmbryologyTotalCount!!.text = incDec.toString()
    }

    /**
     * Get Question Answer Selected By User
     */
    private fun crateAnswerList() {
        //six option one selection
        val selectedRadioButton = radioGroupEmbryologyQuestionSeekBarFeedback.getCheckedRadioButtonId()
        val radioButton = findViewById<View>(selectedRadioButton) as? RadioButton
        val selectedRadioButtonSixOptionAns = radioButton?.text.toString()

        //three option one selection
        val threeSelectedRadioButton = radioGroupEmbryologyQuestionThreeOfOneSingleSelectionOtherButton.getCheckedRadioButtonId()
        val threeRadioButton = findViewById<View>(threeSelectedRadioButton) as? RadioButton
        val selectedRadioButtonThreeOptionAns = threeRadioButton?.text.toString()


        //five option one selection
        val fiveSelectedRadioButton = radioGroupEmbryologyQuestionFiveOptionSingleSelected.getCheckedRadioButtonId()
        val fiveRadioButton = findViewById<View>(fiveSelectedRadioButton) as? RadioButton
        val selectedRadioButtonFiveOptionAns = fiveRadioButton?.text.toString()


        //slider with option with no button
        val sliderWithNoButtonSelectedRadioButton = radioGroupEmbryologySliderWithOptionAndNoButton.getCheckedRadioButtonId()
        val sliderNoButtonRadioButton = findViewById<View>(sliderWithNoButtonSelectedRadioButton) as? RadioButton
        val selectedRadioButtonSliderNoButton = sliderNoButtonRadioButton?.text.toString()


        //slider with option with no button
        val sliderWithOneButtonSelectedRadioButton = radioGroupEmbryologyQuestionSliderWithOptionWithOneButton.getCheckedRadioButtonId()
        val sliderOneButtonRadioButton = findViewById<View>(sliderWithOneButtonSelectedRadioButton) as? RadioButton
        val selectedRadioButtonSliderOneButton = sliderOneButtonRadioButton?.text.toString()

        //slider slider of persentage with no button
        val sliderOfpercentageButtonSelectedRadioButton = radioGroupEmbryologyQuestionSliderOfPercentageWithOneButton.getCheckedRadioButtonId()
        val sliderPercentageButtonRadioButton = findViewById<View>(sliderOfpercentageButtonSelectedRadioButton) as? RadioButton
        val selectedRadioButtonSliderOfPercentageButton = sliderPercentageButtonRadioButton?.text.toString()



        LoggE("TAG", "Selected Ans-->" + tvFragmentEmbryologyTotalCount?.text.toString())
        LoggE("TAG", "Radio Six Selected Ans-->" + selectedRadioButtonSixOptionAns)
        LoggE("TAG", "Radio three Selected Ans-->" + selectedRadioButtonThreeOptionAns)
        LoggE("TAG", "Radio five Selected Ans-->" + selectedRadioButtonFiveOptionAns)
        LoggE("TAG", "Radio slider with option no button-->" + selectedRadioButtonSliderNoButton)
        LoggE("TAG", "Radio slider with option one button-->" + selectedRadioButtonSliderOneButton)
        LoggE("TAG", "Radio slider with option of percentage button-->" + selectedRadioButtonSliderOfPercentageButton)


        if (questionModel.data.get(questionCounter).type.equals("SIX_OPTION_SINGLE_SELECTED")) {
            // radioGroupEmbryologyQuestionSeekBarFeedback.getCheckedRadioButtonId() == -1
            answerMap?.put(questionModel.data[questionCounter].id, selectedRadioButtonSixOptionAns)
        } else if (questionModel.data.get(questionCounter).type.equals("INCREMENT_DECREMENT")) {
            answerMap?.put(questionModel.data[questionCounter].id, tvFragmentEmbryologyTotalCount?.text.toString())
        } else if (questionModel.data.get(questionCounter).type.equals("THREE_OF_ONE_BUTTON_SINGLE_SELECTION")) {
            answerMap?.put(questionModel.data[questionCounter].id, selectedRadioButtonThreeOptionAns)
        } else if (questionModel.data.get(questionCounter).type.equals("FIVE_OPTION_SINGLE_SELECTED")) {
            answerMap?.put(questionModel.data[questionCounter].id, selectedRadioButtonFiveOptionAns)
        } else if (questionModel.data.get(questionCounter).type.equals("SLIDER_OF_PERCENTAGE_WITH_ONE_BUTTON")) {
            answerMap?.put(questionModel.data[questionCounter].id, selectedRadioButtonSliderOfPercentageButton)
        } else if (questionModel.data.get(questionCounter).type.equals("SLIDER_WITH_OPTION_AND_ONE_BUTTON")) {
            answerMap?.put(questionModel.data[questionCounter].id, selectedRadioButtonSliderOneButton)
        } else if (questionModel.data.get(questionCounter).type.equals("SLIDER_WITH_OPTION_AND_NO_BUTTON")) {
            answerMap?.put(questionModel.data[questionCounter].id, selectedRadioButtonSliderNoButton)
        }
        LoggE("TAG", "questionCounter-->" + questionCounter)
        LoggE("TAG", "Hash Map Size-->" + answerMap?.size)
        LoggE("TAG", "Hash Map Data-->" + answerMap?.toString())


    }


    /**
     * Create Json For Answer For Send To Server
     */
    private fun createJsonAnswer() {
        val jsonObject = JSONObject()
        val jsonArray = JSONArray()
        for (key in answerMap?.keys!!) {
            val value = answerMap!!.get(key) as String
            LoggE("TAG", "key--> $key" + "Value: $value")
            var jsonObjectIn = JSONObject()
            jsonObjectIn.put("qId", key)
            jsonObjectIn.put("ans", value)
            jsonArray.put(jsonObjectIn)
        }
        jsonObject.put("data", jsonArray)
        LoggE("TAG", "Json Array-->" + jsonObject.toString())
    }

}
