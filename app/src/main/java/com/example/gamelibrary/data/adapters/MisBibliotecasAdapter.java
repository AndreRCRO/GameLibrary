package com.example.gamelibrary.data.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gamelibrary.R;
import com.example.gamelibrary.data.modelos.Biblioteca;
import com.example.gamelibrary.data.modelos.Juego;

import java.util.List;

public class MisBibliotecasAdapter extends RecyclerView.Adapter<MisBibliotecasAdapter.MisBibliotecasViewHolder>{
    private final List<Biblioteca> listaBibliotecas;
    private final List<Integer> bibliotecasSeleccionadas;
    private final Context context;

    public interface OnBibliotecaCheckedChangeListener {
        void onCheckedChanged(Biblioteca biblioteca, boolean isChecked);
    }

    private OnBibliotecaCheckedChangeListener listener;

    public void setOnBibliotecaCheckedChangeListener(OnBibliotecaCheckedChangeListener listener) {
        this.listener = listener;
    }

    public MisBibliotecasAdapter(Context context, List<Biblioteca> listaBibliotecas, List<Integer> bibliotecasSeleccionadas) {
        this.context = context;
        this.listaBibliotecas = listaBibliotecas;
        this.bibliotecasSeleccionadas = bibliotecasSeleccionadas;
    }
    @NonNull
    @Override
    public MisBibliotecasAdapter.MisBibliotecasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dialog, parent, false);
        return new MisBibliotecasAdapter.MisBibliotecasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MisBibliotecasViewHolder holder, int position) {
        Biblioteca biblioteca = listaBibliotecas.get(position);
        holder.titulo.setText(biblioteca.getTitulo());

        holder.checkbox.setOnCheckedChangeListener(null);
        holder.checkbox.setChecked(bibliotecasSeleccionadas.contains(biblioteca.getId()));

        holder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (listener != null) {
                listener.onCheckedChanged(biblioteca, isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaBibliotecas.size();
    }
    public static class MisBibliotecasViewHolder extends RecyclerView.ViewHolder {
        TextView titulo;
        CheckBox checkbox;

        public MisBibliotecasViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.tv_biblioteca_titulo);
            checkbox = itemView.findViewById(R.id.cb_select_item);
        }
    }
}
