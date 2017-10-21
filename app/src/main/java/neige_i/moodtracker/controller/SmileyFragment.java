package neige_i.moodtracker.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import neige_i.moodtracker.R;

/**
 * Created by neige-i on 21/10/2017.
 */

public class SmileyFragment extends Fragment {

    public static SmileyFragment newInstance(int smileyResource) {
        Bundle args = new Bundle();
        args.putInt("smileyResource", smileyResource);

        SmileyFragment fragment = new SmileyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup relativeLayout = (ViewGroup) inflater.inflate(R.layout.fragment_smiley, container, false);
        ((ImageView) relativeLayout.findViewById(R.id.smiley_img)).setImageResource(getArguments().getInt("smileyResource"));
        return relativeLayout;
    }
}
