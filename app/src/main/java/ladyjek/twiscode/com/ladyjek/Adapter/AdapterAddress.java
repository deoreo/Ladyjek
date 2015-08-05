package ladyjek.twiscode.com.ladyjek.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Utilities.PlaceAPI;

/**
 * Created by User on 8/5/2015.
 */
public class AdapterAddress extends ArrayAdapter<String> implements Filterable {

    ArrayList<String> resultList;

    Context mContext;
    int mResource;
    String mTag;

    PlaceAPI mPlaceAPI = new PlaceAPI();

    public AdapterAddress(Context context, int resource, String tag ) {
        super(context, resource);
        mContext = context;
        mTag = tag;
    }

    @Override
    public int getCount() {
        // Last item will be the footer
        return resultList.size();
    }

    @Override
    public String getItem(int position) {
        return resultList.get(position);
    }
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    resultList = mPlaceAPI.autocomplete(constraint.toString());
                    filterResults.values = resultList;
                    filterResults.count = resultList.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }
        };

        return filter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        AutoCompleteTextView autocompleteTextView=null;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (position != (resultList.size() - 1))
            view = inflater.inflate(R.layout.auto_complete_list_item, null);


        if (position != (resultList.size() - 1)) {
            if(mTag.equals("FROM"))
                autocompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.txtFrom);
            else if(mTag.equals("DESTINATION"))
                autocompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.txtDestination);
            autocompleteTextView.setText(resultList.get(position));
        }

        return view;
    }


}
