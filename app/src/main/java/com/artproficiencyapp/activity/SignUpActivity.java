package com.artproficiencyapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.artproficiencyapp.R;
import com.artproficiencyapp.adapter.MyCommunityPagerAdapter;
import com.artproficiencyapp.fragment.commanfragment.register.DirectorFragments;
import com.artproficiencyapp.fragment.commanfragment.register.StudentFragments;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    @BindView(R.id.img_toolbar_back)
    ImageView imgToolbarBack;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.vPager_sign_up)
    ViewPager vPagerSignUp;
    @BindView(R.id.parent_toolbar)
    Toolbar parentToolbar;
    @BindView(R.id.tv_tab_director)
    TextView tvTabDirector;
    @BindView(R.id.tv_tab_student)
    TextView tvTabStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        setSupportActionBar(parentToolbar);
        tvToolbarTitle.setText(getString(R.string.signup_title));
        setupViewPager(vPagerSignUp, DirectorFragments.Companion.newInstance(), StudentFragments.Companion.newInstance());
        vPagerSignUp.addOnPageChangeListener(this);
        UpdateUI(0);
        SetClickListeners();
    }

    private void SetClickListeners() {
        tvTabStudent.setOnClickListener(this);
        tvTabDirector.setOnClickListener(this);
        imgToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                sessionManager.put("facebook_name", "");
               // sessionManager.put("facebook_email", "");
                sessionManager.put("facebook_profile_uri", "");
                finish();
            }
        });
    }

    private void UpdateUI(int pos) {
        switch (pos) {
            case 0:
                tvTabDirector.setBackgroundResource(R.drawable.ic_tab_selected);
                tvTabDirector.setTextColor(getResources().getColor(R.color.White));
                tvTabDirector.setText(getString(R.string.signup_director_title));

                tvTabStudent.setBackgroundResource(R.drawable.ic_tab_not_selected);
                tvTabStudent.setTextColor(getResources().getColor(R.color.white_alpha_50));
                tvTabStudent.setText(getString(R.string.signup_student_title));
                break;

            case 1:
                tvTabDirector.setBackgroundResource(R.drawable.ic_tab_not_selected);
                tvTabDirector.setTextColor(getResources().getColor(R.color.white_alpha_50));
                tvTabDirector.setText(getString(R.string.signup_director_title));

                tvTabStudent.setBackgroundResource(R.drawable.ic_tab_selected);
                tvTabStudent.setTextColor(getResources().getColor(R.color.White));
                tvTabStudent.setText(getString(R.string.signup_student_title));
                break;
        }
    }

    private void setupViewPager(ViewPager viewPager, DirectorFragments mCommunityFragment, StudentFragments studentFragment) {
        MyCommunityPagerAdapter adapter = new MyCommunityPagerAdapter(getFragmentManager());
        adapter.addFragment(mCommunityFragment, getString(R.string.signup_director_title));
        adapter.addFragment(studentFragment, getString(R.string.signup_student_title));
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);

        adapter.notifyDataSetChanged();
        viewPager.getAdapter().notifyDataSetChanged();

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        UpdateUI(position);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_tab_director:
                UpdateUI(0);
                vPagerSignUp.setCurrentItem(0);
                break;

            case R.id.tv_tab_student:
                UpdateUI(1);
                vPagerSignUp.setCurrentItem(1);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        sessionManager.put("facebook_name", "");
       // sessionManager.put("facebook_email", "");
        sessionManager.put("facebook_profile_uri", "");
        finish();
    }
}