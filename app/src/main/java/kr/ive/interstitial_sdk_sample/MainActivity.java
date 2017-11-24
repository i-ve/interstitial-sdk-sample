package kr.ive.interstitial_sdk_sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import kr.ive.interstitial_sdk.IveAd;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IveAd.getInstance().initialize(this);
        IveAd.getInstance().showInterstitialAd(this);
    }

    @Override
    public void onBackPressed() {
        IveAd.getInstance().showFinishAd(this);
    }
}
