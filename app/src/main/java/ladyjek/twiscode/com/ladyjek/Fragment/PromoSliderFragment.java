package ladyjek.twiscode.com.ladyjek.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;

import java.io.InputStream;

import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.R;

/**
 * Created by Unity on 01/09/2015.
 */
public class PromoSliderFragment extends Fragment {
    private static final String ARG_COLOR = "color";
    ProgressBar progressBar;
    private String mColor;

    public static PromoSliderFragment newInstance(String param1) {
        PromoSliderFragment fragment = new PromoSliderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_COLOR, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public PromoSliderFragment() {

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
        View v = inflater.inflate(R.layout.promo_slider_fragment, container, false);
        ImageView img = (ImageView) v.findViewById(R.id.imgPromo);
        //mColor = "http://ladyjek.com/popup-img/bannerladyjek-01.jpg";
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        Log.d("promo slider", mColor);


        Picasso.with(getActivity()).load(mColor).fit().into(img, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError() {
                Log.d("error load image","error");
            }
        });

/*
        new DownloadImageTask(img)
                .execute(mColor);
*/
        return v;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = "http://ladyjek.com/popup-img/bannerladyjek-01.jpg";//urls[0];
            Log.d("url promo slider", urldisplay);
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
        }
    }
}
