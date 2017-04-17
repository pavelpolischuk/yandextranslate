package com.gcteam.yandextranslate.info;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.gcteam.yandextranslate.R;

import java.util.ArrayList;

public class InfoFragment extends Fragment {

    private AcknowledgmentsAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<Acknowledgment> ac = new ArrayList<>();

        ac.add(new Acknowledgment(getString(R.string.rxandroid_name),
                getString(R.string.rxandroid_license)));
        ac.add(new Acknowledgment(getString(R.string.rxjava_name),
                getString(R.string.rxjava_license)));
        ac.add(new Acknowledgment(getString(R.string.rxbinding_name),
                getString(R.string.rxbinding_license)));
        ac.add(new Acknowledgment(getString(R.string.retrofit_name),
                getString(R.string.retrofit_license)));
        ac.add(new Acknowledgment(getString(R.string.butterknife_name),
                getString(R.string.butterknife_license)));
        ac.add(new Acknowledgment(getString(R.string.activeandroid_name),
                getString(R.string.activeandroid_license)));
        ac.add(new Acknowledgment(getString(R.string.circlebutton_name),
                getString(R.string.circlebutton_license)));
        ac.add(new Acknowledgment(getString(R.string.androidlibs_name),
                getString(R.string.androidlibs_license)));

        adapter = new AcknowledgmentsAdapter(getContext(), ac);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_info, container, false);

        ListView list = (ListView)view.findViewById(R.id.acknowledgments_list);
        list.setAdapter(adapter);

        return view;
    }
}