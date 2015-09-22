package ladyjek.twiscode.com.ladyjek.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ladyjek.twiscode.com.ladyjek.Model.ModelPlace;
import ladyjek.twiscode.com.ladyjek.R;


public class AdapterSuggestion extends BaseAdapter {
    private List<ModelPlace> mSourceData, mFilterData;
    private LayoutInflater mInflater = null;
    private boolean mKeyIsEmpty = false;

    public AdapterSuggestion(FragmentActivity activity, List<ModelPlace> d) {
        try {
            mSourceData = d;
            mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (d == null || d.isEmpty()) {
                mKeyIsEmpty = true;
            }
        } catch (Exception e) {
            mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mKeyIsEmpty = true;
        }

    }

    public AdapterSuggestion(Activity activity, List<ModelPlace> d) {
        try {
            mSourceData = d;
            mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (d == null || d.isEmpty()) {
                mKeyIsEmpty = true;
            }
        } catch (Exception e) {
            mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mKeyIsEmpty = true;
        }

    }

    @Override
    public int getCount() {
        if (mKeyIsEmpty)
            return 1;
        return mSourceData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mKeyIsEmpty) {
            convertView = mInflater.inflate(R.layout.list_suggestion_empty, null);
        } else {
            ViewHolder holder;
            convertView = mInflater.inflate(R.layout.list_suggestion, null);
            holder = new ViewHolder();
            holder.txtAddress = (TextView) convertView.findViewById(R.id.txtAddress);
            holder.txtDetail = (TextView) convertView.findViewById(R.id.txtDetail);
            convertView.setTag(position);

            ModelPlace modelPlace = mSourceData.get(position);
            final String ADDRESS = modelPlace.getAddress();
            final String DETAIL = modelPlace.getAddressDetail();


            holder.txtAddress.setText(ADDRESS);
            holder.txtDetail.setText(DETAIL);


        }
        return convertView;
    }

    private static class ViewHolder {
        public TextView txtAddress;
        public TextView txtDetail;
    }


}
