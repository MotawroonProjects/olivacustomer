package com.apps.olivacustomer.uis.activity_notification;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.apps.olivacustomer.R;
import com.apps.olivacustomer.adapter.NotificationAdapter;
import com.apps.olivacustomer.databinding.ActivityHomeBinding;
import com.apps.olivacustomer.databinding.ActivityNotificationBinding;
import com.apps.olivacustomer.mvvm.ActivityNotificationMvvm;
import com.apps.olivacustomer.uis.activity_base.BaseActivity;

public class NotificationActivity extends BaseActivity {

    private ActivityNotificationBinding binding;
    private NotificationAdapter adapter;
    private ActivityNotificationMvvm activityNotificationMvvm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification);
        initView();


    }


    private void initView() {
        activityNotificationMvvm = ViewModelProviders.of(this).get(ActivityNotificationMvvm.class);
        activityNotificationMvvm.getIsLoading().observe(this, loading -> {
            binding.swipeRefresh.setRefreshing(loading);

        });
        activityNotificationMvvm.getNotification().observe(this, list -> {
            if (list.size() > 0) {
                adapter.updateList(list);
                binding.cardNoData.setVisibility(View.GONE);
            } else {
                binding.cardNoData.setVisibility(View.VISIBLE);

            }
        });
        setUpToolbar(binding.toolbar, getString(R.string.notifications), R.color.white, R.color.black);
        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationAdapter(this);
        binding.recView.setAdapter(adapter);
        activityNotificationMvvm.getNotifications(getUserModel());

    }

}