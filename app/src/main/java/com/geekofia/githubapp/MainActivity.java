package com.geekofia.githubapp;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // mywebview is now globally declared and protected
    protected WebView  mywebview;

    // Global Instance Variable To Store Permission Code
    private static final int REQUEST_CODE_PERMISSION = 2;


    // Request Permission
    private static String[] PERMISSIONS_REQ = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    // Verify Permissions
    private static boolean verifyPermissions(Activity activity) {
        // Check if we have write permission
        int WritePermision = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (WritePermision != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_REQ,
                    REQUEST_CODE_PERMISSION
            );
            return false;
        } else {
            return true;
        }
    }
    // Main Oncreate Method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.splashScreenTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Detecting Webview Area
        mywebview = (WebView)findViewById(R.id.web1);
        // New webview client Instance
        mywebview.setWebViewClient(new WebViewClient());
        // WebSettings Instance Created
        WebSettings webSettings = mywebview.getSettings();
        // Javascript Enabled
        webSettings.setJavaScriptEnabled(true);
        // Zoom Implemented
        webSettings.setBuiltInZoomControls(true);
        // Zoom Controls Disabled
        webSettings.setDisplayZoomControls(false);
        // Home URL
        mywebview.loadUrl("https://github.com/");
        // DownloadListener Initialized
        mywebview.setDownloadListener(new DownloadListener() {
            // Creating Download Instance
            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimeType,
                                        long contentLength) {
                // Verifying If App Has The Runtime Permission
                if(verifyPermissions(MainActivity.this)){
                    DownloadManager.Request request = new DownloadManager.Request(
                            Uri.parse(url));
                    request.setMimeType(mimeType);
                    String cookies = CookieManager.getInstance().getCookie(url);
                    request.addRequestHeader("cookie", cookies);
                    request.addRequestHeader("User-Agent", userAgent);
                    request.setDescription("Downloading file...");
                    request.setTitle(URLUtil.guessFileName(url, contentDisposition,
                            mimeType));
                    request.allowScanningByMediaScanner(); request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(
                            Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(
                                    url, contentDisposition, mimeType));
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    dm.enqueue(request);
                    Toast.makeText(getApplicationContext(), "Downloading File",
                            Toast.LENGTH_LONG).show();
                }else{
                    //prompt user for permission
                }

            }});
    }
    // Going back when back button is pressed
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mywebview.canGoBack()) {
                        mywebview.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}

