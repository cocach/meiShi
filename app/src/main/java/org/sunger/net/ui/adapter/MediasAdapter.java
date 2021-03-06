package org.sunger.net.ui.adapter;

import android.app.Activity;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.image.ImageInfo;

import org.sunger.net.app.AppUtils;
import org.sunger.net.entity.MediaEntity;
import org.sunger.net.ui.widget.AvatarView;
import org.sunger.net.ui.widget.ShadowImageView;
import org.sunger.net.ui.widget.refresh.BaseLoadMoreRecyclerAdapter;
import org.sunger.net.widget.TextImageView;

import sunger.org.demo.R;

/**
 * Created by sunger on 2015/10/28.
 */
public class MediasAdapter extends BaseLoadMoreRecyclerAdapter<MediaEntity, MediasAdapter.ViewHolder> {
    private OnItemClickListener onItemClickListener;
    private Activity mActivity;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_video_list_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * support  for StaggeredGridLayoutManager to load
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FooterViewHolder) {
            StaggeredGridLayoutManager.LayoutParams clp = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            if (clp != null)
                clp.setFullSpan(true);
        }
        super.onBindViewHolder(holder, position);
    }

    @Override
    public void onBindItemViewHolder(final ViewHolder holder, final int position) {
        MediaEntity entity = getItem(position);
        holder.mImageViewCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(getItem(position));
            }
        });
        holder.mTextViewLikesCount.setTextImageStart(18, R.mipmap.ic_thumb_up_gray_18dp, " " + entity.getLikes_count());
        holder.mTextViewRecommendCaption.setText(entity.getCaption());

        String url = entity.getCover_pic().replace("!thumb320", "");
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setControllerListener(new BaseControllerListener<ImageInfo>() {
                    @Override
                    public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                        float aspectRatio = (float)imageInfo.getWidth() / (float)imageInfo.getHeight();
                        holder.mImageViewCover.setAspectRatio(aspectRatio);
                    }
                })
                .setUri(Uri.parse(url))
                .build();
        holder.mImageViewCover.setController(controller);
        AppUtils.loadSmallUserAvata(getItem(position).getUser(), holder.mImageViewAvatar);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final AvatarView mImageViewAvatar;
        public final ShadowImageView mImageViewCover;
        public final TextImageView mTextViewLikesCount;
        public final TextView mTextViewRecommendCaption;

        public ViewHolder(View view) {
            super(view);
            mImageViewAvatar = (AvatarView) view.findViewById(R.id.imageView_avatar);
            mImageViewCover = (ShadowImageView) view.findViewById(R.id.imageview_cover_pic);
            mTextViewLikesCount = (TextImageView) view.findViewById(R.id.textView_likes_count);
            mTextViewRecommendCaption = (TextView) view.findViewById(R.id.textView_recommend_caption);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(MediaEntity entity);
    }
}
