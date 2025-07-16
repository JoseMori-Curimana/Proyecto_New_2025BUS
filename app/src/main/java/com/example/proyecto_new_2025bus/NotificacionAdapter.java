package com.example.proyecto_new_2025bus;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotificacionAdapter extends  RecyclerView.Adapter<NotificacionAdapter.ViewHolder> {
    private List<NotificacionModel> lista;

    public NotificacionAdapter(List<NotificacionModel> lista) {
        this.lista = lista;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(v);
        }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificacionModel noti = lista.get(position);
        holder.titulo.setText(noti.tipo);
        holder.mensaje.setText(noti.mensaje);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, mensaje;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(android.R.id.text1);
            mensaje = itemView.findViewById(android.R.id.text2);
        }
    }
}
