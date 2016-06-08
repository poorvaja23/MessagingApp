package com.google.samples.apps.friendlyping.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.samples.apps.friendlyping.AnalyticsHelper;
import com.google.samples.apps.friendlyping.R;
import com.google.samples.apps.friendlyping.constants.PingerKeys;
import com.google.samples.apps.friendlyping.constants.RegistrationConstants;
import com.google.samples.apps.friendlyping.fragment.FriendlyPingFragment;
import com.google.samples.apps.friendlyping.gcm.GcmAction;
import com.google.samples.apps.friendlyping.model.TrackingEvent;
import com.google.samples.apps.friendlyping.util.FriendlyPingUtil;
import com.google.samples.apps.friendlyping.model.Pinger;

import java.io.IOException;

/**
 * Created by SC-NV-LQ on 6/8/2016.
 */
public class CPUOCActivity extends AppCompatActivity
{
    private String mRegistrationToken;
    private String sharedPreferences;
    private Button oc1button;
    private Button oc2button;
    private Button ocOffbutton;
    private SharedPreferences mDefaultSharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpu_oc);
        oc1button = (Button) findViewById(R.id.OC1button);
        oc2button = (Button) findViewById(R.id.OC2button);
        ocOffbutton = (Button) findViewById(R.id.OCoffbutton);

        oc1button.setOnClickListener(oc1AppliedHandler);
        oc2button.setOnClickListener(oc2AppliedHandler);
        ocOffbutton.setOnClickListener(ocOffAppliedHandler);
        Intent intent = getIntent();
        mRegistrationToken = intent.getStringExtra("token");
        sharedPreferences = intent.getStringExtra("shared");
        mDefaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    private View.OnClickListener oc1AppliedHandler = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            ApplyProfile(1);
        }
    };

    private void ApplyProfile(int profileID)
    {
        final Context context = getApplicationContext();
        Bundle data = new Bundle();
        data.putString(PingerKeys.ACTION, GcmAction.APPLY_PROFILE);   //key/value pairs to be send- value always string
        data.putString(PingerKeys.PROFILE, String.valueOf(profileID));
        data.putString(PingerKeys.TO, mRegistrationToken);
        data.putString(PingerKeys.SENDER,sharedPreferences);
        try {
            //send(String to, String msgId, Bundle data)
            GoogleCloudMessaging.getInstance(context)
                    .send(FriendlyPingUtil.getServerUrl(getApplicationContext()),
                            String.valueOf(System.currentTimeMillis()), data);
            AnalyticsHelper.send(context, TrackingEvent.PING_SENT);
        } catch (IOException e) {
        }
    }

    private View.OnClickListener oc2AppliedHandler = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            ApplyProfile(2);
        }
    };

    private View.OnClickListener ocOffAppliedHandler = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            ApplyProfile(0);
        }
    };

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
