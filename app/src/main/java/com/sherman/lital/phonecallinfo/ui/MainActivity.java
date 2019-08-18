package com.sherman.lital.phonecallinfo.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.sherman.lital.phonecallinfo.R;
import com.sherman.lital.phonecallinfo.model.ContactPhone;
import com.sherman.lital.phonecallinfo.model.PhoneCallInfo;
import com.sherman.lital.phonecallinfo.viewModel.PhoneCallInfoViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 11;
    public static final String CONTACT_PHONE_LIST = "contactPhoneList";
    public static final String PHONE_CALL_INFO = "phoneCallInfo";

    private String[] permissions = {Manifest.permission.READ_CONTACTS, Manifest.permission
            .READ_CALL_LOG};

    private PhoneCallInfoViewModel viewModel;
    private Observer<List<ContactPhone>> contactPhoneObserver = null;
    private Observer<PhoneCallInfo> callTypeInfoObserver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = ViewModelProviders.of(this).get(PhoneCallInfoViewModel.class);

        contactPhoneObserver = new Observer<List<ContactPhone>>() {
            @Override
            public void onChanged(@Nullable List<ContactPhone> contactPhoneList) {
                if (contactPhoneList != null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(CONTACT_PHONE_LIST, (Serializable) contactPhoneList);
                    addContactPhoneListFragment(bundle);
                }
            }
        };

        callTypeInfoObserver = new Observer<PhoneCallInfo>() {
            @Override
            public void onChanged(@Nullable PhoneCallInfo phoneCallInfo) {
                if (phoneCallInfo != null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PHONE_CALL_INFO, phoneCallInfo);
                    addPhoneCallInfoFragment(bundle);
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.no_calls), Toast.LENGTH_SHORT).show();
                }
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (arePermissionsEnabled()) {
                viewModel.getContactPhoneList().observe(this, contactPhoneObserver);
            } else {
                requestMultiplePermissions();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean arePermissionsEnabled() {
        for (String permission : permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestMultiplePermissions() {
        List<String> remainingPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                remainingPermissions.add(permission);
            }
        }
        requestPermissions(remainingPermissions.toArray(new String[remainingPermissions.size()]), REQUEST_CODE);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    if (shouldShowRequestPermissionRationale(permissions[i])) {
                        showPermissionRationale();
                    }
                    return;
                }
                viewModel.getContactPhoneList().observe(this, contactPhoneObserver);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showPermissionRationale() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.permission_denied))
                .setMessage(getString(R.string.permission_rationale))
                .setCancelable(false)
                .setNegativeButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .setPositiveButton(getString(R.string.allow), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestMultiplePermissions();
                        dialog.dismiss();
                    }
                }).show();
    }

    //PhoneCallInfoFragment will call this method when contactPhone item pressed
    public void initPhoneCallInfoObserver() {
        viewModel.getPhoneCallInfo().observe(this, callTypeInfoObserver);
    }

    public void addContactPhoneListFragment(Bundle bundle) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.container, ContactPhoneListFragment.newInstance(bundle))
                .addToBackStack(ContactPhoneListFragment.class.getSimpleName()).commit();
    }

    public void addPhoneCallInfoFragment(Bundle bundle) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.container, PhoneCallInfoFragment.newInstance(bundle))
                .addToBackStack(PhoneCallInfoFragment.class.getSimpleName()).commit();
    }

    @Override
    public void onBackPressed() {
        int entries = getSupportFragmentManager().getBackStackEntryCount();
        if (entries <= 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
