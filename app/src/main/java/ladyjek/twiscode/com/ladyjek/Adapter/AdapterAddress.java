package ladyjek.twiscode.com.ladyjek.Adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;

import ladyjek.twiscode.com.ladyjek.Model.ModelPlace;
import ladyjek.twiscode.com.ladyjek.Utilities.GoogleAPIManager;

/**
 * Created by ModelUser on 8/5/2015.
 */
public class AdapterAddress extends ArrayAdapter<ModelPlace> implements Filterable {
    private ArrayList resultList;
    private static final String TAG = "PlaceArrayAdapter";
    private GoogleApiClient mGoogleApiClient;
    private AutocompleteFilter mPlaceFilter;
    private LatLngBounds mBounds;
    private ArrayList<ModelPlace> mResultList;

    public AdapterAddress(Context context, int resource) {
        super(context, resource);
    }


    @Override
    public int getCount() {
        return mResultList.size();
    }

    @Override
    public ModelPlace getItem(int index) {
        return mResultList.get(index);
    }



    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    // Retrieve the autocomplete results.
                    mResultList = GoogleAPIManager.autocomplete(constraint.toString());
                    // Assign the data to the FilterResults
                    filterResults.values = mResultList;
                    filterResults.count = mResultList.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

}
