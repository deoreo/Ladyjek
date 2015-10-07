package ladyjek.twiscode.com.ladyjek.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;

import ladyjek.twiscode.com.ladyjek.R;

/**
 * Created by Unity on 01/09/2015.
 */
public class HelpSliderFragment extends Fragment {
    private static final String ARG_COLOR = "color";
    ProgressBar progressBar;
    private String mColor;

    public static HelpSliderFragment newInstance(String param1) {
        HelpSliderFragment fragment = new HelpSliderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_COLOR, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public HelpSliderFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColor = getArguments().getString(ARG_COLOR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.help_slider_fragment, container, false);
        ImageView img = (ImageView) v.findViewById(R.id.imgPromo);

        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        Picasso.with(getActivity()).load(mColor).fit().into(img,new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                if (progressBar != null) {
                    progressBar .setVisibility(View.GONE);
                }
            }

            @Override
            public void onError() {

            }
        });

        return v;
    }
}
