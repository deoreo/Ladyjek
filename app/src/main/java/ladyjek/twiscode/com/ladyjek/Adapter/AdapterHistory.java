package ladyjek.twiscode.com.ladyjek.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.Collections;
import java.util.List;

import ladyjek.twiscode.com.ladyjek.Model.ModelOrder;
import ladyjek.twiscode.com.ladyjek.R;

/**
 * Created by Unity on 30/07/2015.
 */
public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.MyViewHolder>   {
    List<ModelOrder> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    private Activity act;

    public AdapterHistory(Context context, List<ModelOrder> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.act = (Activity) context;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_passenger_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        String nama = "",jarak="",biaya="",lokasi="";

        try {
            nama = data.get(position).getName();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        try {
            jarak = data.get(position).getDistance();
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
            lokasi = data.get(position).getTo();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        holder.nama.setText(nama);
        holder.jarak.setText(jarak);
        holder.biaya.setText(biaya);
        holder.lokasi.setText(lokasi);



    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nama,jarak,biaya,lokasi;
        public MyViewHolder(View itemView) {
            super(itemView);
            nama = (TextView) itemView.findViewById(R.id.passengerName);
            jarak= (TextView) itemView.findViewById(R.id.passengerJarak);
            biaya = (TextView) itemView.findViewById(R.id.passengerBiaya);
            lokasi = (TextView) itemView.findViewById(R.id.passengerLocation);
        }
    }

    private void SendBroadcast(String typeBroadcast,String type){
        Intent intent = new Intent(typeBroadcast);
        // add data
        intent.putExtra("message", type);
        LocalBroadcastManager.getInstance(act).sendBroadcast(intent);
    }
}
