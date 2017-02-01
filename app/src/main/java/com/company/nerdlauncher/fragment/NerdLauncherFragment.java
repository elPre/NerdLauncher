package com.company.nerdlauncher.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.company.nerdlauncher.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by hectorleyvavillanueva on 1/3/17.
 */
public class NerdLauncherFragment extends Fragment {

    private static final String TAG = NerdLauncherFragment.class.getSimpleName();

    RecyclerView mRecyclerView;

    public static NerdLauncherFragment newInstance() {

        Bundle args = new Bundle();
        NerdLauncherFragment fragment = new NerdLauncherFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nerd_launcher, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_nerd_launcher_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setupAdapter();
        return view;
    }

    private void setupAdapter(){
        Intent startupIntent = new Intent(Intent.ACTION_MAIN);
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(startupIntent, 0);

        Collections.sort(activities, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo a, ResolveInfo b) {
                PackageManager pm = getActivity().getPackageManager();
                return String.CASE_INSENSITIVE_ORDER.compare(
                        a.loadLabel(pm).toString(),
                        b.loadLabel(pm).toString()
                );
            }
        });

        Log.i(TAG, "found " + activities.size() + " activities");
        mRecyclerView.setAdapter(new ActivityAdapter(activities));
    }


    private class ActivityHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ResolveInfo mResolverInfo;
        private TextView mTextView;
        private ImageView mImageView;

        public ActivityHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.list_item_image_view);
            mTextView = (TextView) itemView.findViewById(R.id.list_item_text_view);
            itemView.setOnClickListener(this);
        }

        public void bindActivity(ResolveInfo resolveInfo){
            mResolverInfo = resolveInfo;
            PackageManager pm = getActivity().getPackageManager();
            String appName = mResolverInfo.loadLabel(pm).toString();
            mImageView.setImageDrawable(mResolverInfo.loadIcon(pm));
            mTextView.setText(appName);
        }

        @Override
        public void onClick(View v) {
            ActivityInfo activityInfo = mResolverInfo.activityInfo;
            Intent i = new Intent(Intent.ACTION_MAIN)
                    .setClassName(activityInfo.applicationInfo.packageName,
                            activityInfo.name)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    }

    private class ActivityAdapter extends RecyclerView.Adapter<ActivityHolder>{

        private final List<ResolveInfo> mActivities;

        private ActivityAdapter(List<ResolveInfo> mActivities) {
            this.mActivities = mActivities;
        }

        @Override
        public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item, parent, false);

            return new ActivityHolder(view);
        }

        @Override
        public void onBindViewHolder(ActivityHolder holder, int position) {
            ResolveInfo resolveInfo = mActivities.get(position);
            holder.bindActivity(resolveInfo);
        }

        @Override
        public int getItemCount() {
            return mActivities.size();
        }
    }

}
