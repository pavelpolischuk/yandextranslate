package com.gcteam.yandextranslate.info;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gcteam.yandextranslate.R;

import java.util.ArrayList;

class AcknowledgmentsAdapter extends ArrayAdapter<Acknowledgment> {

    AcknowledgmentsAdapter(Context context, ArrayList<Acknowledgment> items) {
        super(context, R.layout.acknowledgment_item, items);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Acknowledgment item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.acknowledgment_item, null);
        }

        if(item != null) {
            ((TextView) convertView.findViewById(R.id.title_text)).setText(item.getTitle());
            ((TextView) convertView.findViewById(R.id.content_text)).setText(item.getContent());
        }

        return convertView;
    }
}
