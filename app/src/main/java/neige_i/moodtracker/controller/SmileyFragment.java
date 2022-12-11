package neige_i.moodtracker.controller;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import neige_i.moodtracker.R;

/**
 * This fragment is used in a ViewPager.
 * It contains an ImageView that shows a smiley.
 * To modify the smiley, just change the image resource of the ImageView.
 */
public class SmileyFragment extends Fragment {
    // ---------------------------------------     CLASS VARIABLES     --------------------------------------

    /**
     * Constant for the key of the bundle argument.
     */
    private static final String BUNDLE_ARGS_SMILEY_ID = "BUNDLE_ARGS_SMILEY_ID";

    // -------------------------------------     OVERRIDDEN METHODS     -------------------------------------

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout from the XML file
        ViewGroup relativeLayout = (ViewGroup) inflater.inflate(R.layout.fragment_smiley, container, false);

        // Set the resource according to the bundle arguments
        ((ImageView) relativeLayout.findViewById(R.id.smiley_img)).setImageResource(getArguments().getInt(BUNDLE_ARGS_SMILEY_ID));

        return relativeLayout;
    }

    // ---------------------------------------     STATIC METHODS     ---------------------------------------

    /**
     * Creates a new instance of this class with the specified resource ID.
     * This method allows displaying the appropriate smiley according to specified argument.
     * @param smileyResource the ID of the drawable to show.
     * @return an instance of this class with a drawable ID put into bundle arguments.
     */
    public static SmileyFragment newInstance(int smileyResource) {
        Bundle args = new Bundle();
        args.putInt(BUNDLE_ARGS_SMILEY_ID, smileyResource);

        SmileyFragment fragment = new SmileyFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
