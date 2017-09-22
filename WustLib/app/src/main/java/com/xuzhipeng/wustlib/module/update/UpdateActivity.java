package com.xuzhipeng.wustlib.module.update;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xuzhipeng.wustlib.R;
import com.xuzhipeng.wustlib.base.BaseActivity;
import com.xuzhipeng.wustlib.common.util.HttpUtil;

import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Response;

public class UpdateActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "UpdateActivity";

    public static Intent newIntent(Context context) {
        return new Intent(context,UpdateActivity.class);
    }

    private TextView mVersionTv;
    private Button mUpdateBtn;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_update;
    }

    @Override
    protected void initView() {
        mVersionTv = (TextView) findViewById(R.id.version_tv);
        mUpdateBtn = (Button) findViewById(R.id.update_btn);
        mUpdateBtn.setOnClickListener(this);
    }

    @Override
    protected void setView() {
        setToolbar(R.string.app_info);
        mVersionTv.setText(UpdateUtil.getVersionName(this));
    }

    @Override
    protected void initData() {
        check();
    }

    /**
     *  检测是否有新版本
     */
    private void check(){
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                Response response = HttpUtil.sendOkHttp(UpdateUtil.URL_UPDATE);

                if(response.isSuccessful()){
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    int severVersion = jsonObject.optInt("versionCode");
                    int  clientVersion =  UpdateUtil.getVersionCode(UpdateActivity.this);
                    e.onNext(severVersion > clientVersion);
                    e.onComplete();
                }
            }
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull Boolean aBoolean) {
                if(aBoolean){
                    mUpdateBtn.setText(R.string.update);
                }else{
                    mUpdateBtn.setText(R.string.no_update);
                    mUpdateBtn.setEnabled(false);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "updateError: ",e);
            }

            @Override
            public void onComplete() {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_btn:
                UpdateUtil.update(this);
                break;
        }
    }


}
