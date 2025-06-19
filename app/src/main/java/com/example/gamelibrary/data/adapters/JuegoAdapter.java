package com.example.gamelibrary.data.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gamelibrary.R;
import com.example.gamelibrary.data.modelos.Juego;


import java.util.List;

public class JuegoAdapter extends RecyclerView.Adapter<JuegoAdapter.JuegoViewHolder> {
    private List<Juego> listaJuegos;
    private Context context;

    public JuegoAdapter(Context context, List<Juego> listaJuegos) {
        this.context = context;
        this.listaJuegos = listaJuegos;
    }

    @Override
    public JuegoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_biblioteca, parent, false);
        return new JuegoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(JuegoViewHolder holder, int position) {
        Juego juego = listaJuegos.get(position);
        holder.titulo.setText(juego.getTitulo());

        Glide.with(context)
                .load(juego.getImagenUrl())
                .into(holder.imagen);
    }

    @Override
    public int getItemCount() {
        return listaJuegos.size();
    }

    public static class JuegoViewHolder extends RecyclerView.ViewHolder {
        ImageView imagen;
        TextView titulo;

        public JuegoViewHolder(View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.iv_juego_imagen);
            titulo = itemView.findViewById(R.id.tv_juego_titulo);
        }
    }
}
