package com.helio.myreelty.video_player.comments;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.ImageButton;

import com.helio.myreelty.R;
import com.helio.myreelty.common.custom_view.FontEditText;
import com.helio.myreelty.common.custom_view.FontTextView;
import com.helio.myreelty.databinding.ItemAddVideoCommentBinding;

/**
 * Created by Nicolas on 30.03.2016.
 */
public class AddVideoCommentHolder extends RecyclerView.ViewHolder {
    public ItemAddVideoCommentBinding binding;

    public AddVideoCommentHolder(View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
        binding.addVideoCommentContent.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
    }
}
