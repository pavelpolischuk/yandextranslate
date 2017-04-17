package com.gcteam.yandextranslate.bookmarks;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.gcteam.yandextranslate.R;
import com.gcteam.yandextranslate.domain.History;
import com.gcteam.yandextranslate.services.HistoryService;

/**
 * Created by turist on 16.04.2017.
 */

public class BookmarkDialog {

    public static void show(Context context, History item) {
        Dialog dialog = new Dialog(context);

        dialog.setTitle(R.string.bookmark_actions);
        dialog.setContentView(R.layout.dialog_bookmark);

        Listener listener = new Listener(dialog, item);
        dialog.findViewById(R.id.bookmark).setOnClickListener(listener);
        dialog.findViewById(R.id.delete).setOnClickListener(listener);
        dialog.findViewById(R.id.cancel).setOnClickListener(listener);

        if(!item.isBookmark) {
            ((TextView)dialog.findViewById(R.id.bookmark_text)).setText(R.string.add_to_bookmarks);
        }

        dialog.show();
    }

    private static class Listener implements View.OnClickListener {

        private Dialog dialog;
        private History item;

        Listener(Dialog dialog, History item) {
            this.dialog = dialog;
            this.item = item;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bookmark:
                    item.isBookmark = !item.isBookmark;
                    HistoryService.get().save(item);
                    break;
                case R.id.delete:
                    HistoryService.get().remove(item);
                    break;
                case R.id.cancel:
                    break;
            }

            dialog.dismiss();
        }
    }
}
