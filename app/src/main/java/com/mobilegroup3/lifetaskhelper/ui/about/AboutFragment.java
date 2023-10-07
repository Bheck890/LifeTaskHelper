package com.mobilegroup3.lifetaskhelper.ui.about;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.mobilegroup3.lifetaskhelper.databinding.FragmentAboutBinding;

public class AboutFragment extends Fragment {

    //private AboutViewModel aboutViewModel;
    private FragmentAboutBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAboutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /*
        aboutViewModel =
                new ViewModelProvider(this).get(AboutViewModel.class);


        final TextView textView = binding.textDeveloped;
        aboutViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
         */

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}