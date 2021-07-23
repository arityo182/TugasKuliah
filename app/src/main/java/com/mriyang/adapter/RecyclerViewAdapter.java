package com.mriyang.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mriyang.myapplication.ActivityUpdate;
import com.mriyang.myapplication.ListDataActivity;
import com.mriyang.model.data_anggota;

import java.util.ArrayList;

// Class adapter ini mengatur data yang akan di tampilkan
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<Message> messages;
    // Deklarasi Variabel
    private ArrayList<data_anggota> listAnggota;
//    private ArrayList<uploadinfo> listUpload;
    private Context context;

    //Membuat Interface
    public interface dataListener{
        void onDeleteData(data_anggota data,int position);
    }

    //Deklarasi Objek dari Interface
    dataListener listener;

    public RecyclerViewAdapter(ArrayList listAnggota,Context context){
        this.listAnggota = listAnggota;
        this.context = context;
//        this.listUpload = listUpload;
        listener = (ListDataActivity) context;
    }
    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // membuat view untuk menyimpan dan memasang layout yang akan di simpan pada recycleview
        View V = LayoutInflater.from(parent.getContext()).inflate(com.mriyang.myapplication.R.layout.view_design,parent,false);
        return new ViewHolder(V);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // mengambil nilai atau value yang terdapat pada reclycle view
        final String KTP = listAnggota.get(position).getNo_ktp();
        final String Nama = listAnggota.get(position).getNama();
        final String Hubungan = listAnggota.get(position).getHubungan();
        final String goldar  = listAnggota.get(position).getGoldar();
        final String gender = listAnggota.get(position).getGender();
        final String date = listAnggota.get(position).getDate();
        final String nmrhp = listAnggota.get(position).getNmrhp();
        final String beratbadan = listAnggota.get(position).getBrtbadan();
        final String Alamat = listAnggota.get(position).getAlamat();


        //Image View

        Glide.with(context).load(listAnggota.get(position).getImageURL()).into(holder.imageView);

        // Memasuukan Nilai ke dalam Value
        holder.KTP.setText("KTP : "+KTP);
        holder.Nama.setText("Nama : " + Nama);
        holder.Hubungan.setText("Hubungan : "+ Hubungan);
        holder.goldar.setText("Golongan Darah : "+goldar);
        holder.gender.setText("Jenis Kelamin : "+gender);
        holder.date.setText("Tanggal Lahir : "+date);
        holder.nmrhp.setText("Nomor Hp : " +nmrhp);
        holder.beratbadan.setText("Berat Badan : " +beratbadan);
        holder.alamat.setText("Alamat : " +Alamat);
        holder.ListItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final String[] action = {"Update","Delete"};
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setItems(action, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        switch (i){
                            case 0:
                                /* Berpindah activity dari halaman layout updatedata
                                dan mengambil data pada list mahasiswa, berdasarkan
                                posisinya untuk diambil ke activity selanjutnya
                                 */
                                Bundle bundle = new Bundle();
                                bundle.putString("dataKtp",listAnggota.get(position).getNo_ktp());
                                bundle.putString("dataNama",listAnggota.get(position).getNama());
                                bundle.putString("dataHubungan",listAnggota.get(position).getHubungan());
                                bundle.putString("dataGoldar",listAnggota.get(position).getGoldar());
                                bundle.putString("dataGender",listAnggota.get(position).getGender());
                                bundle.putString("dataDate",listAnggota.get(position).getDate());
                                bundle.putString("dataNmrhp",listAnggota.get(position).getNmrhp());
                                bundle.putString("dataBeratb",listAnggota.get(position).getBrtbadan());
                                bundle.putString("dataAlamat",listAnggota.get(position).getAlamat());
                                bundle.putString("dataFoto",listAnggota.get(position).getImageURL());
                                bundle.putString("getPrimaryKey",listAnggota.get(position).getKey());
                                Intent intent = new Intent(v.getContext(), ActivityUpdate.class);
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                                break;

                            case 1:
                                listener.onDeleteData(listAnggota.get(position),position);
                                break;
                        }
                    }
                });
                alert.create();
                alert.show();
                return true;
            }
        });
    }



    @Override
    public int getItemCount() {
        return listAnggota.size();
    }

    // Viewholder di gunakan untuk menyimpan referensi dari view_view
    public class ViewHolder extends RecyclerView.ViewHolder {
        //inisiasi widget

        private TextView KTP,Nama,Hubungan,status,goldar,gender,date,nmrhp,beratbadan,alamat;
        private LinearLayout ListItem;
        private ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //menginisiasi vierw view yang terpasang pada layout
            KTP = itemView.findViewById(com.mriyang.myapplication.R.id.ktp);
            Nama = itemView.findViewById(com.mriyang.myapplication.R.id.nama);
            Hubungan = itemView.findViewById(com.mriyang.myapplication.R.id.hubungan);
            goldar = itemView.findViewById(com.mriyang.myapplication.R.id.goldar);
            gender = itemView.findViewById(com.mriyang.myapplication.R.id.gender);
            date = itemView.findViewById(com.mriyang.myapplication.R.id.tgl_lhr);
            nmrhp = itemView.findViewById(com.mriyang.myapplication.R.id.nmor_hp);
            beratbadan = itemView.findViewById(com.mriyang.myapplication.R.id.berat_badan);
            alamat = itemView.findViewById(com.mriyang.myapplication.R.id.alamat_umah);
            imageView = itemView.findViewById(com.mriyang.myapplication.R.id.viewimage);

            ListItem = itemView.findViewById(com.mriyang.myapplication.R.id.list_item);
        }
    }


}
