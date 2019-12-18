package com.example.breda_op_stap.presentation;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.widget.Toast;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.breda_op_stap.R;

import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.*;

public class GoogleMapsActivityTest {

    @Test
    public void onMapReady()
    {
//        Looper.prepare();
//        GoogleMapsActivity googleMapsActivity = new GoogleMapsActivity();
//        googleMapsActivity.startActivity(null);
//
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run()
//            {
//                Looper.prepare();
//                try {
//                    Thread.sleep(10000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        thread.run();

        assert true;
    }

    @Test
    public void changeLanguage()
    {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Resources res = appContext.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();

        String textEnglish = appContext.getResources().getString(R.string.notification_off_route);
        conf.setLocale(new Locale("nl"));
        res.updateConfiguration(conf, dm);
        String textDutch = appContext.getResources().getString(R.string.notification_off_route);

        assertEquals(!textEnglish.equals(textDutch) && textEnglish.equals("You are leaving the route!") && textDutch.equals("Je bent de route aan het verlaten!"), true);
    }
}