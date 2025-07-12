package com.example.proyecto_new_2025bus;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.ViewHolder> {

    private List<HistorialItem> historialList;

    public HistorialAdapter(List<HistorialItem> historialList) {
        this.historialList = historialList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitulo, txtFecha, txtDetalle;

        public ViewHolder(View view) {
            super(view);
            txtTitulo = view.findViewById(R.id.txtTitulo);
            txtFecha = view.findViewById(R.id.txtFecha);
            txtDetalle = view.findViewById(R.id.txtDetalle);
        }
    }

    @Override
    public HistorialAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_historial_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HistorialItem item = historialList.get(position);
        holder.txtTitulo.setText(item.getTitulo());
        holder.txtFecha.setText(item.getFecha());
        holder.txtDetalle.setText(item.getDetalle());
    }

    @Override
    public int getItemCount() {
        return historialList.size();
    }
}
