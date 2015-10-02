package ladyjek.twiscode.com.ladyjek.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import ladyjek.twiscode.com.ladyjek.Activity.ActivityDetail;
import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.Model.ModelHistory;
import ladyjek.twiscode.com.ladyjek.R;

/**
 * Created by Unity on 30/07/2015.
 */
public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.MyViewHolder>   {
    List<ModelHistory> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    private Activity act;

    private NumberFormat numberFormat;
    private DecimalFormat decimalFormat;
    boolean isNull = false;

    public AdapterHistory(Context context, List<ModelHistory> data, boolean isNull) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.act = (Activity) context;
        this.isNull = isNull;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        Log.d("count history",""+getItemCount());
        if(!isNull){
            view = inflater.inflate(R.layout.row_history_item, parent, false);
        }
        else {
            view = inflater.inflate(R.layout.row_history_null, parent, false);
        }
        MyViewHolder holder = new MyViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        if(!isNull){
            DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
            otherSymbols.setDecimalSeparator(',');
            otherSymbols.setGroupingSeparator('.');
            decimalFormat = new DecimalFormat();
            decimalFormat.setDecimalFormatSymbols(otherSymbols);

            final int pos = position;
            String asal = "",tgl="",biaya="",tujuan="",status="";

            try {
                asal = data.get(position).getFrom();
            }
            catch (Exception ex){
                ex.printStackTrace();
            }

            try {
                tujuan = data.get(position).getDestination();
            }
            catch (Exception ex){
                ex.printStackTrace();
            }

            try {
                biaya = data.get(position).getPrice();
            }
            catch (Exception ex){
                ex.printStackTrace();
            }

            try {
                tgl = data.get(position).getDate();
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
            try {
                status = data.get(position).getStatus();
            }
            catch (Exception ex){
                ex.printStackTrace();
            }

            String[] from = asal.split(", ");
            String[] dest = tujuan.split(", ");
            try {
                holder.asal.setText(from[0]);
            }catch(Exception e){
                holder.asal.setText(from[0]);
            }
            try {
                holder.tujuan.setText(dest[0]);
            }catch (Exception e){
                holder.tujuan.setText(dest[0]);
            }
            try {
                holder.detailAsal.setText(from[1] + ", " + from[2] + ", " + from[3] + ", " + from[4]);
            }catch(Exception e){
                holder.detailAsal.setText(from[1] + ", " + from[2] + ", " + from[3]);
            }
            try {
                holder.detailTujuan.setText(dest[1] + ", " + dest[2] + ", " + dest[3] + ", " + dest[4]);
            }catch (Exception e){
                holder.tujuan.setText(dest[1] + ", " + dest[2] + ", " + dest[3]);
            }
            holder.biaya.setText("Rp. "+decimalFormat.format(Integer.parseInt(biaya)));
            holder.status.setText(status);
            holder.tgl.setText(tgl);

            holder.row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ApplicationData.detail = data.get(position);
                    Intent i = new Intent(act, ActivityDetail.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    act.startActivity(i);
                }
            });
        }




    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView asal,tujuan,biaya,tgl,detailAsal,detailTujuan,status;
        LinearLayout row;
        public MyViewHolder(View itemView) {
            super(itemView);
            if(!isNull){
                asal = (TextView) itemView.findViewById(R.id.passengerFrom);
                tujuan = (TextView) itemView.findViewById(R.id.passengerDestination);
                detailAsal = (TextView) itemView.findViewById(R.id.passengerFromDetail);
                detailTujuan = (TextView) itemView.findViewById(R.id.passengerDestinationDetail);
                biaya = (TextView) itemView.findViewById(R.id.passengerPrice);
                tgl = (TextView) itemView.findViewById(R.id.passengerDate);
                status = (TextView) itemView.findViewById(R.id.passengerStatus);
                row = (LinearLayout)itemView.findViewById(R.id.layout_row);
            }

        }
    }

    private void SendBroadcast(String typeBroadcast,String type){
        Intent intent = new Intent(typeBroadcast);
        // add data
        intent.putExtra("message", type);
        LocalBroadcastManager.getInstance(act).sendBroadcast(intent);
    }


}
