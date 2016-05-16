package com.helio.myreelty.video_player.video_details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.helio.myreelty.R;
import com.helio.myreelty.databinding.FragmentDetailsBinding;
import com.helio.myreelty.models.HouseModel;
import com.helio.myreelty.video_player.VideoActivity;

/**
 * Created by Fedir on 24.02.2016.
 */
public class VideoDetailsFragment extends Fragment {

    public HouseModel houseModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        houseModel = ((VideoActivity)getActivity()).getHouse();
        FragmentDetailsBinding.bind(view)
                .setHouse(houseModel);
        openMapFragment();
        return view;
    }

    private void openMapFragment(){
        VideoDetailsMapFragment fragment = VideoDetailsMapFragment.newInstance(houseModel);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.details_map_container, fragment)
                .commit();
    }
}
