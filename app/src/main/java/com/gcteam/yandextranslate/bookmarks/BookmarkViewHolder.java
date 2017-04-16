package com.gcteam.yandextranslate.bookmarks;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gcteam.yandextranslate.R;
import com.gcteam.yandextranslate.domain.History;

/**
 * Created by turist on 12.04.2017.
 */

class BookmarkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private AppCompatImageView bookmark_image;
    private TextView source;
    private TextView translation;
    private TextView direction;

    private OnClickListener clickListener;

    private static int bookmarkColor = 0xe0c155;
    private static int notBookmarkColor = 0x757575;

    interface OnClickListener {
        void onClick(int position);
    }

    BookmarkViewHolder(View itemView, OnClickListener clickListener) {
        super(itemView);

        this.clickListener = clickListener;
        itemView.setOnClickListener(this);

        bookmark_image = (AppCompatImageView)itemView.findViewById(R.id.bookmark_image);
        source = (TextView)itemView.findViewById(R.id.source);
        translation = (TextView)itemView.findViewById(R.id.translation);
        direction = (TextView)itemView.findViewById(R.id.direction);
    }

    static void setColors(int bookmarkColor, int notBookmarkColor) {
        BookmarkViewHolder.bookmarkColor = bookmarkColor;
        BookmarkViewHolder.notBookmarkColor = notBookmarkColor;
    }

    void bind(History history) {
        source.setText(history.sourceText);
        translation.setText(history.translation);
        direction.setText(history.direction);
        bookmark_image.setColorFilter(history.isBookmark ? bookmarkColor : notBookmarkColor);
    }

    @Override
    public void onClick(View v) {
        if(clickListener != null) {
            clickListener.onClick(getAdapterPosition());
        }
    }
}
