package com.mriyang.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mriyang.model.NomorAntrianModel;
import com.mriyang.model.data_anggota;
import com.mriyang.myapplication.NomorAntrianActivity;
import com.mriyang.myapplication.R;
import com.mriyang.myapplication.RiwayatDaftarActivity;

import java.util.ArrayList;

public class Riwayat_Adapter extends RecyclerView.Adapter<Riwayat_Adapter.ViewHolder> {

    private ArrayList<Message> messages;
    // Deklarasi Variabel
    private ArrayList<NomorAntrianModel> riwayat;
    //    private ArrayList<uploadinfo> listUpload;
    private Context context;




    //Membuat Interface
    public interface dataListener{
        void onDeleteData(data_anggota data,int position);
    }

    //Deklarasi Objek dari Interface
    RiwayatDaftarActivity listener;

    public Riwayat_Adapter(ArrayList riwayat,Context context){
        this.riwayat = riwayat;
        this.context = context;
//        this.listUpload = listUpload;
        listener = (RiwayatDaftarActivity) context;
    }
    @NonNull
    @Override
    public Riwayat_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // membuat view untuk menyimpan dan memasang layout yang akan di simpan pada recycleview
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_design_riwayat,parent,false);
        return new ViewHolder(V);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // mengambil nilai atau value yang terdapat pada reclycle view
        final String Nomor = riwayat.get(position).getNomor();
        final String Nama = riwayat.get(position).getNama();
        final String Poli = riwayat.get(position).getPoli();
        final String Riwayat = riwayat.get(position).getWaktu();
        final String Penjamin = riwayat.get(position).getPenjamin();
        final String Periksa = riwayat.get(position).getJenisperiksa();



        // Memasuukan Nilai ke dalam Value
        holder.Waktu.setText("Hari, Tanggal : "+Riwayat);
        holder.Nama.setText("Nama : " + Nama);
        holder.Poli.setText("Poliklinik : "+ Poli);
        holder.ListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Intent lanjut = new Intent(v.getContext(), NomorAntrianActivity.class);
                lanjut.putExtra("poli", Poli);
                lanjut.putExtra("waktu", Riwayat);
                lanjut.putExtra("nomor", Nomor);
                lanjut.putExtra("nama", Nama);
                lanjut.putExtra("penjamin", Penjamin);
                lanjut.putExtra("jenisPeriksa", Periksa);
                context.startActivity(lanjut);

            }
        });
    }



    @Override
    public int getItemCount() {
        return riwayat.size();
    }

    // Viewholder di gunakan untuk menyimpan referensi dari view_view
    public class ViewHolder extends RecyclerView.ViewHolder {
        //inisiasi widget

        private TextView Waktu,Nama,Poli;
        private LinearLayout ListItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //menginisiasi vierw view yang terpasang pada layout
            Waktu = itemView.findViewById(R.id.tv_waktu);
            Nama = itemView.findViewById(com.mriyang.myapplication.R.id.tv_nama);
            Poli = itemView.findViewById(R.id.tv_poli);
            ListItem = itemView.findViewById(com.mriyang.myapplication.R.id.list_item);
        }
    }


}

