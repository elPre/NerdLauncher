package com.company.nerdlauncher.activity;

import android.support.v4.app.Fragment;
import com.company.nerdlauncher.fragment.NerdLauncherFragment;

public class NerdLauncherActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return NerdLauncherFragment.newInstance();
    }
}
