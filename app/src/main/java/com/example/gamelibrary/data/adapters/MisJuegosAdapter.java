package com.example.gamelibrary.data.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gamelibrary.R;
import com.example.gamelibrary.data.modelos.Juego;

import java.util.List;

public class MisJuegosAdapter extends RecyclerView.Adapter<MisJuegosAdapter.MisJuegosViewHolder> {
    private final List<Juego> listaJuegos;
    private final Context context;
    private JuegoAdapter.OnItemActionListener listener;

    public interface OnItemActionListener {
        void onItemClick(Juego item, int position);
    }

    public void setOnItemActionListener(JuegoAdapter.OnItemActionListener listener) {
        this.listener = listener;
    }

    public MisJuegosAdapter(Context context, List<Juego> listaJuegos) {
        this.context = context;
        this.listaJuegos = listaJuegos;
    }

    @NonNull
    @Override
    public MisJuegosAdapter.MisJuegosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_collections, parent, false);
        return new MisJuegosAdapter.MisJuegosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MisJuegosAdapter.MisJuegosViewHolder holder, int position) {
        Juego juego = listaJuegos.get(position);
        holder.titulo.setText(juego.getTitulo());

        Glide.with(context)
                .load(juego.getImagenUrl())
                .into(holder.imagen);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(juego, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaJuegos.size();
    }

    public static class MisJuegosViewHolder extends RecyclerView.ViewHolder {
        ImageView imagen;
        TextView titulo;

        public MisJuegosViewHolder(@NonNull View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.iv_juego_imagen);
            titulo = itemView.findViewById(R.id.tv_juego_titulo);
        }
    }
}
