package com.helio.myreelty.video_player.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Parcelable;

import com.helio.myreelty.R;
import com.helio.myreelty.models.HouseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicolas on 06.04.2016.
 */
public class Share {

    public static void shareReview(Context mContext, HouseModel mHouse) {
        String id = String.valueOf(mHouse.getId());
        String[] app = {"facebook", "gmail", "com.google.android.apps.plus"};
        try {
            List<Intent> targetedShareIntents = new ArrayList<Intent>();
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");
            List<ResolveInfo> resInfo = mContext.getPackageManager().queryIntentActivities(share, 0);
            if (!resInfo.isEmpty()) {
                for (ResolveInfo info : resInfo) {
                    for (int i = 0; i < app.length; i++) {
                        if (info.activityInfo.packageName.toLowerCase().contains(app[i])
                                || info.activityInfo.name.toLowerCase().contains(app[i])) {
                            Intent targetedShare = new Intent(android.content.Intent.ACTION_SEND);
                            targetedShare.setType("text/plain"); // put here your mime type
                            targetedShare.putExtra(Intent.EXTRA_SUBJECT, mContext.getString(R.string.share_subject)
                                    + mHouse.getFullAddress());
                            targetedShare.putExtra(Intent.EXTRA_TEXT, mContext.getString(R.string.share_url) + id);
                            targetedShare.setPackage(info.activityInfo.packageName);
                            targetedShareIntents.add(targetedShare);
                        }
                    }
                }
                Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), mContext.getString(R.string.share_title));
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));
                mContext.startActivity(chooserIntent);
            }
        } catch (Exception e) {
        }
    }
}
