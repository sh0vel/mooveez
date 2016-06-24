package com.app.shovonh.mooveez;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.app.shovonh.mooveez.Receivers.ReleaseNotifications;

public class NotificationHandler extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Uri uri = Uri.parse("http://www.google.com/#q=Showtimes for " + getIntent().getStringExtra(ReleaseNotifications.BUNDLE_ID_TITLE));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
        finish();
        System.exit(0);
    }
}
