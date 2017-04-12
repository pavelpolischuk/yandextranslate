package com.gcteam.yandextranslate.translate;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.gcteam.yandextranslate.R;
import com.gcteam.yandextranslate.domain.Language;

/**
 * Created by turist on 09.04.2017.
 */

public class SelectLanguageDialog extends DialogFragment {

    private final static String LANGUAGES_NAMES_KEY = "languages_names_list";
    private final static String LANGUAGES_CODES_KEY = "languages_codes_list";

    private Callback callback;
    private int code;

    interface Callback {
        void onSelect(Language language, int code);
    }

    public static SelectLanguageDialog create(Language[] languages, Callback callback, int code) {
        SelectLanguageDialog dialog = new SelectLanguageDialog();

        Bundle arg = new Bundle();
        String[] codes = new String[languages.length];
        CharSequence[] names = new CharSequence[languages.length];

        for (int i = 0; i < languages.length; ++i) {
            codes[i] = languages[i].code;
            names[i] = languages[i].name;
        }

        arg.putStringArray(LANGUAGES_CODES_KEY, codes);
        arg.putCharSequenceArray(LANGUAGES_NAMES_KEY, names);
        dialog.setArguments(arg);
        dialog.callback = callback;
        dialog.code = code;

        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String[] codes = getArguments().getStringArray(LANGUAGES_CODES_KEY);
        final CharSequence[] names = getArguments().getCharSequenceArray(LANGUAGES_NAMES_KEY);

        AlertDialog.Builder builder =
                new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.select_language);
        builder.setItems(names, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.onSelect(new Language(codes[which], names[which].toString()), code);
            }});

        builder.setNegativeButton(android.R.string.cancel, null);
        return builder.create();
    }
}