package com.fbvideodownloader.facebookvideosaver;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class BrowseBtnFragment extends Fragment {

    public Button browseBtn;
    boolean doubleBackToExitPressedOnce = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.browse_btn, container, false);

            browseBtn = (Button) view.findViewById(R.id.button6);

            browseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    FragmentTransaction fr = getFragmentManager().beginTransaction();
                    fr.replace(R.id.browse_btn_frame, new facebook());
                    fr.commit();

                    browseBtn.setVisibility(View.GONE);

                }
            });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener
                    if (doubleBackToExitPressedOnce) {
                        getActivity().finish();
                    }
                    doubleBackToExitPressedOnce = true;
                    Toast.makeText(getContext(), "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            doubleBackToExitPressedOnce = false;
                        }
                    }, 2000);
                    return true;
                }
                return false;
            }
        });

    }
}
