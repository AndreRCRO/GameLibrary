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
import com.example.gamelibrary.data.AppDatabase;
import com.example.gamelibrary.data.Dao.BibliotecaDao;
import com.example.gamelibrary.data.modelos.Biblioteca;
import com.example.gamelibrary.data.modelos.Juego;

import java.util.List;

public class BibliotecaAdapter extends RecyclerView.Adapter<BibliotecaAdapter.BibliotecaViewHolder>{

    private final List<Biblioteca> listaBibliotecas;
    private final Context context;
    private OnItemActionListener listener;
    private BibliotecaDao bibliotecaDao;

    public interface OnItemActionListener {
        void onItemClick(Biblioteca item, int position);
    }

    public void setOnItemActionListener(OnItemActionListener listener) {
        this.listener = listener;
    }

    public BibliotecaAdapter(Context context, List<Biblioteca> listaBibliotecas) {
        this.context = context;
        this.listaBibliotecas = listaBibliotecas;
        AppDatabase db = AppDatabase.getInstance(context);
        this.bibliotecaDao = db.bibliotecaDao();
    }

    @NonNull
    @Override
    public BibliotecaAdapter.BibliotecaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_carpeta_biblioteca, parent, false);
        return new BibliotecaAdapter.BibliotecaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BibliotecaAdapter.BibliotecaViewHolder holder, int position) {
        Biblioteca biblioteca = listaBibliotecas.get(position);
        holder.titulo.setText(biblioteca.getTitulo());
        holder.elementos.setText(String.valueOf(biblioteca.getElementos()));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(biblioteca, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaBibliotecas.size();
    }

    public static class BibliotecaViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, elementos;

        public BibliotecaViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.tv_biblioteca_nombre);
            elementos = itemView.findViewById(R.id.tv_biblioteca_contador);
        }
    }
}
