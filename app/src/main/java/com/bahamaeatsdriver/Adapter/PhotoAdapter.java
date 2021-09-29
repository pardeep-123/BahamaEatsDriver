package com.bahamaeatsdriver.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bahamaeatsdriver.R;
import com.bahamaeatsdriver.model_class.ImageVideoModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.RecyclerViewHolder> {
    Context context;
    LayoutInflater inflater;
    ArrayList<ImageVideoModel> list;


    int pos = 0;
    public PhotoAdapter(Context context, ArrayList<ImageVideoModel> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.row_photo, parent, false);
        RecyclerViewHolder viewHolder = new RecyclerViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {


        if (list.get(position).getIsAdded().equals("false")) {
            if (list.get(position).getType().equals("1")) {
                Glide.with(context).load(list.get(position).getThumbnailPath())

                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                Log.e("Exceptionimageload", e.getMessage());
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .placeholder(R.drawable.placeholder_rectangle)
                        .error(R.drawable.placeholder_rectangle)
                        .into(holder.ivProvider);

            } else {
                Glide.with(context).load(list.get(position).getImageVideoPath())

                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                Log.e("Exceptionimageload", e.getMessage());
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .placeholder(R.drawable.placeholder_rectangle)
                        .error(R.drawable.placeholder_rectangle)
                        .into(holder.ivProvider);
            }
        } else {
            if (list.get(position).getType().equals("1")) {
                Glide.with(context).load(list.get(position).getThumbnailPath())

                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                Log.e("Exceptionimageload", e.getMessage());
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .placeholder(R.drawable.placeholder_rectangle)
                        .error(R.drawable.placeholder_rectangle)
                        .into(holder.ivProvider);
            } else {
                Glide.with(context).load(list.get(position).getImageVideoPath())

                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                Log.e("Exceptionimageload", e.getMessage());
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .placeholder(R.drawable.placeholder_rectangle)
                        .error(R.drawable.placeholder_rectangle)
                        .into(holder.ivProvider);

            }

        }
        holder.iv_cross.setOnClickListener(v -> {
            list.remove(position);
            notifyDataSetChanged();
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProvider, iv_cross;


        public RecyclerViewHolder(View view) {
            super(view);
            ivProvider = view.findViewById(R.id.iv_profile);
            iv_cross = view.findViewById(R.id.iv_cross);
        }
    }

}





