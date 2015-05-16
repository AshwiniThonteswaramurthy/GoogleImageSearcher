package com.android.ashwini.googleimagesearcher.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.ashwini.googleimagesearcher.R;
import com.android.ashwini.googleimagesearcher.helpers.SearchFilter;

public class SettingsDialog extends DialogFragment implements TextView.OnEditorActionListener {
    private Spinner spImageSize;
    private Spinner spColorFilter;
    private Spinner spImageType;
    private EditText etSiteFilter;
    private SearchFilter searchFilter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.settings, null, false);
        searchFilter = SearchFilter.getInstance();

        getDialog().setTitle("Advanced Filters");
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        spImageSize = (Spinner) view.findViewById(R.id.spImageSize);
        spImageType = (Spinner) view.findViewById(R.id.spImageType);
        spColorFilter = (Spinner) view.findViewById(R.id.spColorFilter);
        etSiteFilter = (EditText) view.findViewById(R.id.etSiteFilter);

        reloadSavedSettings();

        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Button btnSave = (Button) view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get all the properties
                if (etSiteFilter.getText() != null) {
                    String as_sitesearch = String.valueOf(etSiteFilter.getText());
                    searchFilter.put(SearchFilter.IMAGE_SEARCH_SITE_KEY, as_sitesearch);
                }
                String imgcolor = String.valueOf(spColorFilter.getSelectedItem());
                String imgsz = String.valueOf(spImageSize.getSelectedItem());
                String imgtype = String.valueOf(spImageType.getSelectedItem());
                searchFilter.put(SearchFilter.IMAGE_COLOR_KEY, imgcolor);
                searchFilter.put(SearchFilter.IMAGE_SIZE_KEY, imgsz);
                searchFilter.put(SearchFilter.IMAGE_TYPE_KEY, imgtype);
                OnSettingsSaveListener listener = (OnSettingsSaveListener) getActivity();
                listener.onSave(searchFilter);
                dismiss();
            }

        });
        return view;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            OnSettingsSaveListener listner = (OnSettingsSaveListener) getActivity();
            listner.onSave(searchFilter);
            dismiss();
            return true;
        }
        return false;
    }

    public interface OnSettingsSaveListener {
        void onSave(SearchFilter searchFilter);
    }

    public void reloadSavedSettings() {
        String imgType = searchFilter.get(SearchFilter.IMAGE_TYPE_KEY);
        spImageType.setSelection(getIndexFromSpinner(spImageType, imgType));
        String imgColor = searchFilter.get(SearchFilter.IMAGE_COLOR_KEY);
        spColorFilter.setSelection(getIndexFromSpinner(spColorFilter, imgColor));
        String imgSize = searchFilter.get(SearchFilter.IMAGE_SIZE_KEY);
        spImageSize.setSelection(getIndexFromSpinner(spImageSize, imgSize));
        etSiteFilter.setText(searchFilter.get(SearchFilter.IMAGE_SEARCH_SITE_KEY));

    }

    private int getIndexFromSpinner(Spinner spinner, String valueToBeSet) {
        Adapter adapter = spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equals(valueToBeSet)) {
                return i;
            }
        }
        return -1;
    }
}
