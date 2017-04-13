package com.gcteam.yandextranslate.bookmarks;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gcteam.yandextranslate.R;
import com.gcteam.yandextranslate.domain.History;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by turist on 12.04.2017.
 */

public class BookmarksAdapter extends RecyclerView.Adapter<BookmarkViewHolder>
        implements Consumer<List<History>> {

    private List<History> items;

    BookmarksAdapter(Context context, List<History> items) {
        this.items = items;

        Resources resources = context.getResources();
        BookmarkViewHolder.setColors(
                resources.getColor(R.color.colorPrimaryDark),
                resources.getColor(R.color.grey));
    }

    @Override
    public BookmarkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BookmarkViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(BookmarkViewHolder holder, int position) {
        History item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void accept(List<History> histories) throws Exception {
        items = histories;
        notifyDataSetChanged();
    }
}