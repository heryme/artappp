package com.artproficiencyapp.test.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.artproficiencyapp.R
import com.artproficiencyapp.activity.DashBordActivityDIreStu
import com.artproficiencyapp.activity.TestInstructionActivity
import com.artproficiencyapp.extension.goActivity
import com.artproficiencyapp.test.fragment.TestMenuFragment
import kotlinx.android.synthetic.main.activity_test_finish.*

class TestFinishActivity : AppCompatActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_finish)
        btnResultOk.setOnClickListener(this)
    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }

    override fun onClick(p0: View?) {
        when (p0) {
            btnResultOk -> {
//                val intent = Intent(this, DashBordActivityDIreStu::class.java)
//                val bundle = Bundle()
//                bundle.putString("postion","2"  )
//                intent.putExtras(bundle)
//                startActivity(intent)
                finish()


            }
        }
    }
}
