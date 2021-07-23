package com.mriyang.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mriyang.model.Informasi;
import com.mriyang.myapplication.R;

import java.util.ArrayList;

public class informasi_adapter extends RecyclerView.Adapter<informasi_adapter.ViewHolder> {

    ArrayList<Informasi> dataGlobal;

    public informasi_adapter(ArrayList dataGlobal) {
        this.dataGlobal = dataGlobal;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mview = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_design_informasi, viewGroup, false);
        return new ViewHolder(mview);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        String nama = dataGlobal.get(position).getJudul();
        String isi = dataGlobal.get(position).getIsiinfo();

        viewHolder.judul.setText(nama);
        viewHolder.konten.setText(isi);
    }

    @Override
    public int getItemCount() {
        return dataGlobal.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView judul,konten;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            judul = itemView.findViewById(R.id.judul);
            konten = itemView.findViewById(R.id.isi);

        }
    }
}
