package com.shakir.xedin.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shakir.xedin.R;
import com.shakir.xedin.activities.InfoPage;
import com.shakir.xedin.activities.TorrentStreamer;
import com.shakir.xedin.models.TPBGET;
import com.shakir.xedin.models.YTSGET;

import java.util.ArrayList;
import java.util.List;

public class TorrentAdapter extends RecyclerView.Adapter<TorrentAdapter.ViewHolder> {
    private String type;
    private YTSGET ytsget;
    private List<TPBGET> tpbgets;
    private LayoutInflater mInflater;
    private Context mContext;

    public TorrentAdapter(Context context, String type){
        this.type = type;
        if(type.equals("TPB")){
            tpbgets = new ArrayList<>();
        }else{
            ytsget = new YTSGET();
        }
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_torrent_result,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(type.equals("TPB")){
            TPBGET tpb = tpbgets.get(position);
            holder.name.setText(tpb.getTitle());
            holder.itemView.setOnLongClickListener(v -> {
                Intent mag = new Intent(Intent.ACTION_VIEW, Uri.parse(tpb.getMagnet()));
                mag.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(mag);
                return true;
            });
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, TorrentStreamer.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("StreamUrl",tpb.getMagnet());
                mContext.startActivity(intent);
            });
        }else{
            holder.name.setText(ytsget.getTitle()[position]);
            holder.itemView.setOnLongClickListener(v -> {
                Intent mag = new Intent(Intent.ACTION_VIEW, Uri.parse(ytsget.getMagnets()[position]));
                mag.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(mag);
                return true;
            });
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, TorrentStreamer.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("StreamUrl",ytsget.getMagnets()[position]);
                mContext.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if(type.equals("YTS")){
            size = ytsget.getTitle().length;
        }else{
            size = tpbgets.size();
        }
        return size;
    }

    String getItem(int pos){
        String mag = "";
        if(type.equals("YTS")){
            mag = ytsget.getMagnets()[pos];
        }else{
            mag = tpbgets.get(pos).getMagnet();
        }
        return mag;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        View result;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            result = itemView;
            name = result.findViewById(R.id.torrentName);
        }
    }

    public void setResults(YTSGET yts){
        ytsget = yts;
        notifyDataSetChanged();
    }
    public void setResults(List<TPBGET> tpb){
        tpbgets = tpb;
        notifyDataSetChanged();
    }
}
