package com.hc.posterccb.ui.acitivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hc.posterccb.R;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;

public class LicenseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);
        Observable.timer(5, TimeUnit.SECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        startActivity(new Intent(LicenseActivity.this, MainActivity.class));
                        finish();
                    }
                });
    }
}
