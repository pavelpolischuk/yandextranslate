package com.gcteam.yandextranslate.bookmarks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
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
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import at.markushi.ui.CircleButton;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function3;
import io.reactivex.schedulers.Schedulers;

/**
 * Fragment to show and manage history and bookmarks
 *
 * Created by turist on 05.04.2017.
 */
public class BookmarksFragment extends RxKnifeFragment
        implements BookmarksAdapter.OnItemClickListener {

    @BindView(R.id.list) RecyclerView recycler_view;
    @BindView(R.id.tabs) TabLayout tab_layout;
    @BindView(R.id.search_text) EditText search_text;
    @BindView(R.id.search_clear) CircleButton search_clear;


    @OnClick(R.id.search_clear)
    void searchClear() {
        search_text.setText("");
    }

    BookmarksAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new BookmarksAdapter(getContext(), HistoryService.get().get(false), this);
    }

    Function3<CharSequence, Integer, Integer, List<History>> requestToHistoryService = new Function3<CharSequence, Integer, Integer, List<History>>() {
        @Override
        public List<History> apply(CharSequence charSequence, Integer integer, Integer v) throws Exception {
            return HistoryService.get().get(charSequence.toString(), integer == 1);
        }
    };

    Consumer<Integer> tabSelectionToSearchHint = new Consumer<Integer>() {
        @Override
        public void accept(Integer position) throws Exception {
            search_text.setHint(position == 1 ? R.string.bookmarks_search_text : R.string.history_search_text);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = bind(inflater.inflate(R.layout.fragment_bookmarks, container, false));
        initView();
        
        Observable<CharSequence> textChanges = RxTextView.textChanges(search_text);
        Observable<Integer> tabChanges = RxTabLayout.selectedChanges(tab_layout);
        Observable<Integer> modelUpdates = HistoryService.get().updates().startWith(0);

        save(tabChanges.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(tabSelectionToSearchHint));

        save(RxHelpers.subscribeViewVisibleOnNotEmptyText(textChanges, search_clear));

        save(Observable.combineLatest(textChanges.debounce(300, TimeUnit.MILLISECONDS),
                                      tabChanges,
                                      modelUpdates, requestToHistoryService)
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

    @Override
    public void onClick(History item) {
        BookmarkDialog.show(getContext(), item);
    }
}