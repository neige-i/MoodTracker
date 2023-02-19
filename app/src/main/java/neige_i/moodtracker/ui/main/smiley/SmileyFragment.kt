package neige_i.moodtracker.ui.main.smiley

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import neige_i.moodtracker.R

class SmileyFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout from the XML file
        val relativeLayout = inflater.inflate(R.layout.fragment_smiley, container, false) as ViewGroup

        // Set the resource according to the bundle arguments
        (relativeLayout.findViewById<View>(R.id.smiley_img) as ImageView).setImageResource(requireArguments().getInt(BUNDLE_ARGS_SMILEY_ID))
        return relativeLayout
    }

    companion object {
        private const val BUNDLE_ARGS_SMILEY_ID = "BUNDLE_ARGS_SMILEY_ID"
        fun newInstance(smileyResource: Int): SmileyFragment {
            val args = Bundle()
            args.putInt(BUNDLE_ARGS_SMILEY_ID, smileyResource)
            val fragment = SmileyFragment()
            fragment.arguments = args
            return fragment
        }
    }
}