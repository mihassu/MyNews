package ru.mihassu.mynews.ui.preview;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import ru.mihassu.mynews.R;
import ru.mihassu.mynews.ui.main.MainAdapter;
import ru.mihassu.mynews.ui.main.MainViewModel;

public class PreviewFragment extends Fragment {

    private PreviewViewModel mViewModel;
    private PreviewAdapter adapter;
    private ProgressBar progressBar;

    public static PreviewFragment newInstance() {
        return new PreviewFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.preview_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PreviewViewModel.class);
        // TODO: Use the ViewModel
    }



}
