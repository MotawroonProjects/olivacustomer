package com.apps.olivacustomer.mvvm;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.apps.olivacustomer.R;
import com.apps.olivacustomer.model.ContactUsModel;
import com.apps.olivacustomer.model.SettingDataModel;
import com.apps.olivacustomer.model.SettingModel;
import com.apps.olivacustomer.model.StatusResponse;
import com.apps.olivacustomer.remote.Api;
import com.apps.olivacustomer.share.Common;
import com.apps.olivacustomer.tags.Tags;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ContactusActivityMvvm extends AndroidViewModel {
    private Context context;


    public MutableLiveData<Boolean> send = new MutableLiveData<>();

    private CompositeDisposable disposable = new CompositeDisposable();

    public ContactusActivityMvvm(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();

    }


    public void contactus(Context context, ContactUsModel contactUsModel) {
        ProgressDialog dialog = Common.createProgressDialog(context, context.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .contactUs(contactUsModel.getName(), contactUsModel.getEmail(), contactUsModel.getTitle(), contactUsModel.getMessage())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<Response<StatusResponse>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);

                    }

                    @Override
                    public void onSuccess(@NonNull Response<StatusResponse> statusResponseResponse) {
                        dialog.dismiss();
                        if (statusResponseResponse.isSuccessful()) {
                            if (statusResponseResponse.body().getStatus() == 200) {
                                send.postValue(true);
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        dialog.dismiss();
                    }
                });


    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
