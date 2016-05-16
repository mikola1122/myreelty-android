package com.helio.myreelty.search.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.helio.myreelty.R;
import com.helio.myreelty.search.model.MarkerItem;

/**
 * Created by Taras on 22.03.2016.
 */
public class MyRenderer extends DefaultClusterRenderer<MarkerItem> {


    private final int PADDING_HORIZONTAL = 25;
    private final int PADDING_VERTICAL_SMALL = 10;
    private final int PADDING_VERTICAL_MEDIUM = 20;
    private final int PADDING_VERTICAL_BIG = 30;
    private IconGenerator mClusterIconGenerator;
    private Context mContext;

    public MyRenderer(Context context, GoogleMap map, ClusterManager clusterManager) {
        super(context, map, clusterManager);
        mContext = context;
        initClusterItemIconGenerator(context);
    }

    private void initClusterItemIconGenerator(Context context){
        IconGenerator mIconGenerator = new IconGenerator(mContext.getApplicationContext());
        ImageView mImageView = new ImageView(context.getApplicationContext());
        mIconGenerator.setContentView(mImageView);
    }

    private void initClusterIconGenerator(){
        mClusterIconGenerator = new IconGenerator(mContext.getApplicationContext());
        LayoutInflater myInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = myInflater.inflate(R.layout.coluster_item, null, false);
        mClusterIconGenerator.setContentView(activityView);
    }

    @Override
    protected void onBeforeClusterItemRendered(MarkerItem markerItem, MarkerOptions markerOptions) {
        markerOptions.icon(BitmapDescriptorFactory.fromResource(markerItem.imageRes));
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<MarkerItem> cluster, MarkerOptions markerOptions) {
        initClusterIconGenerator();
        setClusterIcon(cluster.getSize(), markerOptions);
    }

    private void setClusterIcon(int clusterSize, MarkerOptions markerOptions){
        mClusterIconGenerator.setBackground(mContext.getResources().getDrawable(R.drawable.cluster_circle));
        changeClusterIconSize(clusterSize);
        mClusterIconGenerator.makeIcon(String.valueOf(clusterSize));
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(mClusterIconGenerator.makeIcon()));
    }

    private void changeClusterIconSize(int clusterSize){
        if (clusterSize < 10) {
            mClusterIconGenerator.setContentPadding(PADDING_HORIZONTAL, PADDING_VERTICAL_SMALL, PADDING_HORIZONTAL, PADDING_VERTICAL_SMALL);
        }else if(clusterSize < 100){
            mClusterIconGenerator.setContentPadding(PADDING_HORIZONTAL, PADDING_VERTICAL_MEDIUM, PADDING_HORIZONTAL, PADDING_VERTICAL_MEDIUM);
        }else if(clusterSize < 1000){
            mClusterIconGenerator.setContentPadding(PADDING_HORIZONTAL, PADDING_VERTICAL_BIG, PADDING_HORIZONTAL, PADDING_VERTICAL_BIG);
        }
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster cluster) {
        return cluster.getSize() > 4;
    }
}
