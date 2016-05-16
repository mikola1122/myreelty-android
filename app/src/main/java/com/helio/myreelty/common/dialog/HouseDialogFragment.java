package com.helio.myreelty.common.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.helio.myreelty.R;
import com.helio.myreelty.common.custom_view.FontTextView;
import com.helio.myreelty.models.HouseModel;

/**
 * Created by Taras on 3/1/2016.
 */
public class HouseDialogFragment extends DialogFragment {

    private  Activity mContext;
    private static String mTitle;
    private static HouseModel mHouse;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = mContext.getLayoutInflater();
        View view = inflater.inflate(R.layout.house_dialog, null);
        ((FontTextView) view.findViewById(R.id.title_dialog)).setText(mTitle);
        builder.setView(view);
        createRecyclerView(view);
        return builder.create();
    }

    private void createRecyclerView(View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.dialog_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(new DialogListAdapter(mContext, mHouse));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    public static HouseDialogFragment newInstance( String title, HouseModel house) {
        mHouse = house;
        mTitle = title;
        return new HouseDialogFragment();
    }
}