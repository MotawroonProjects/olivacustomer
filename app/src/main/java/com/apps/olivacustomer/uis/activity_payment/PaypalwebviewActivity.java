package com.apps.olivacustomer.uis.activity_payment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;


import com.apps.olivacustomer.R;
import com.apps.olivacustomer.databinding.ActivityPaypalBinding;
import com.apps.olivacustomer.uis.activity_base.BaseActivity;

import java.util.Locale;

import io.paperdb.Paper;

public class PaypalwebviewActivity extends BaseActivity {
    private ActivityPaypalBinding binding;
    private String link = "";
    private String lang;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_paypal);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        initView();
    }


    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        if (getIntent().getStringExtra("url") != null) {
            link = getIntent().getStringExtra("url");
        }
        setUpWebView();
    }


    private void setUpWebView() {
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        binding.webView.getSettings().setBuiltInZoomControls(false);
        binding.webView.loadUrl(link);
        binding.webView.setWebViewClient(new WebViewClient() {
                                             @Override
                                             public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                                 super.onPageStarted(view, url, favicon);
                                             }

                                             @Override
                                             public void onPageFinished(WebView view, String url) {

                                                 if (url.contains("success") || url.contains("checkout/done") || url.contains("yes")) {
                                                     setResult(RESULT_OK);
                                                     finish();
                                                     Toast.makeText(PaypalwebviewActivity.this, getString(R.string.payment_suc), Toast.LENGTH_SHORT).show();

                                                 } else if (url.contains("no")) {
                                                     Toast.makeText(PaypalwebviewActivity.this, getString(R.string.payment_faild), Toast.LENGTH_SHORT).show();
                                                     finish();
                                                 }
                                             }

                                             @Override
                                             public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                                                 super.onReceivedError(view, request, error);
                                                 //  binding.webView.setVisibility(View.INVISIBLE);
                                             }

                                             @Override
                                             public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                                                 super.onReceivedHttpError(view, request, errorResponse);
                                                 //binding.webView.setVisibility(View.INVISIBLE);
                                             }
                                         }

        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.webView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.webView.onResume();
    }


    public void back() {
        finish();
    }
}
