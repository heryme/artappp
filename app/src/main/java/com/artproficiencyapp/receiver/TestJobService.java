package com.artproficiencyapp.receiver;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.artproficiencyapp.common.JobShe;

public class TestJobService extends JobService {
    private static final String TAG = "SyncService";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onStartJob(JobParameters params) {
        Intent service = new Intent(getApplicationContext(), ConnectivityReceiver.class);
        getApplicationContext().startService(service);
        JobShe.scheduleJob(getApplicationContext()); // reschedule the job
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }

}
