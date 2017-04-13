package com.gcteam.yandextranslate.bookmarks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.gcteam.yandextranslate.R;
import com.gcteam.yandextranslate.domain.History;
import com.gcteam.yandextranslate.services.HistoryService;
import com.gcteam.yandextranslate.utils.RxHelpers;
import com.gcteam.yandextranslate.utils.RxKnifeFragment;
import com.gcteam.yandextranslate.utils.RxTabLayout;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by turist on 05.04.2017.
 */

public class BookmarksFragment extends RxKnifeFragment {

    @BindView(R.id.list) RecyclerView recycler_view;
    @BindView(R.id.tabs) TabLayout tab_layout;
    @BindView(R.id.search_text) EditText search_text;
    @BindView(R.id.search_clear) AppCompatImageButton search_clear;


    @OnClick(R.id.search_clear)
    void searchClear() {
        search_text.setText("");
    }

    BookmarksAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new BookmarksAdapter(getContext(), HistoryService.get().all());
    }

    BiFunction<CharSequence, Integer, List<History>> requestToHistoryService = new BiFunction<CharSequence, Integer, List<History>>() {
        @Override
        public List<History> apply(CharSequence charSequence, Integer integer) throws Exception {
            return HistoryService.get().get(charSequence.toString(), integer == 1);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = bind(inflater.inflate(R.layout.fragment_bookmarks, container, false));
        initView();

        Observable<CharSequence> textChanges = RxTextView.textChanges(search_text)
                .debounce(300, TimeUnit.MILLISECONDS);

        Observable<Integer> tabChanges = RxTabLayout.selectedChanges(tab_layout);

        save(textChanges
                .map(RxHelpers.CharsNotEmpty)
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(RxView.visibility(search_clear)));

        save(Observable.combineLatest(textChanges, tabChanges, requestToHistoryService)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(adapter));


        return view;
    }

    private void initView() {
        search_clear.setVisibility(View.GONE);

        TabLayout.Tab tab = tab_layout.newTab().setText(R.string.title_history);
        tab.select();
        tab_layout.addTab(tab);
        tab = tab_layout.newTab().setText(R.string.title_bookmarks);
        tab_layout.addTab(tab);

        recycler_view.setAdapter(adapter);
    }
}