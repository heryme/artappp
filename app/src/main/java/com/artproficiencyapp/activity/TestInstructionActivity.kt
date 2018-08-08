package com.artproficiencyapp.activity

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.artproficiencyapp.R
import com.artproficiencyapp.test.activity.EmbryologyActivity
import com.flick_trade.model.Album
import kotlinx.android.synthetic.main.activity_test_instruction.*
import com.wus.adapter.TestInstructionRecyAdapter


class TestInstructionActivity : AppCompatActivity() {
    private var adapter: TestInstructionRecyAdapter? = null
    private var albumList: ArrayList<Album>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_instruction)
        videoview.setVideoURI(Uri.parse("https://archive.org/download/Popeye_forPresident/Popeye_forPresident_512kb.mp4"));
        videoview.setActivity(this);

        val bundle = intent.extras
        val s = bundle!!.getString("EXTRA_SESSION_ID")
        Log.e("EXTRA_SESSION_ID", "EXTRA_SESSION_ID" + s)

        bindAdapter()
        btnTestInstruction.setOnClickListener {
            val intent = Intent(this@TestInstructionActivity, EmbryologyActivity::class.java)
            intent.putExtra("EXTRA_SESSION_ID", s)
            startActivity(intent)
            finish()

        }
        TestInstructionBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        videoview.resize();
    }

    private fun bindAdapter() {
        rcvTestInstruction.setHasFixedSize(false);
        rcvTestInstruction.setNestedScrollingEnabled(false);
        rcvTestInstruction.setLayoutManager(LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false))
        albumList = ArrayList()
        albumList!!.add(Album("Lorem Ipsum is simply dummy text of the printing and typesetting industry"))
        albumList!!.add(Album("Lorem Ipsum has been the industry's standard dummy text ever since the 1500s"))
        albumList!!.add(Album("There are many variations of passages of Lorem Ipsum available"))
        albumList!!.add(Album("The standard chunk of Lorem Ipsum used since the 1500s is reproduced below for those interested"))
        albumList!!.add(Album("handful of model sentence structures, to generate Lorem Ipsum which looks reasonable"))
        val adapter = TestInstructionRecyAdapter(this@TestInstructionActivity, albumList!!)
        rcvTestInstruction.setAdapter(adapter)

    }


}
