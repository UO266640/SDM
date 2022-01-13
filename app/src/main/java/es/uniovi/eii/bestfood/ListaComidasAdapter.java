package es.uniovi.eii.bestfood;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import es.uniovi.eii.bestfood.modelo.Comida;

public class ListaComidasAdapter extends RecyclerView.Adapter<ListaComidasAdapter.ComidaViewHolder> {

    private final List<Comida> listaComidas;
    private final OnItemClickListener listener;

    public ListaComidasAdapter(List<Comida> listaComidas, OnItemClickListener listener) {
        this.listaComidas = listaComidas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ComidaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.linea_recycler_view_comida, parent, false);
        return new ComidaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ComidaViewHolder holder, int position) {
        Comida comida = listaComidas.get(position);
        Log.i("Lista", "Visualiza elemento: " + comida);
        holder.bindUser(comida, listener);
    }

    @Override
    public int getItemCount() {
        return listaComidas.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Comida item);
    }

    public class ComidaViewHolder extends RecyclerView.ViewHolder {
        private final TextView nombre;
        private ImageView imagen;

        public ComidaViewHolder(View itemView) {
            super(itemView);
            this.nombre = itemView.findViewById(R.id.nombrecomida);
             this.imagen = itemView.findViewById(R.id.imagen);
        }

        public void bindUser(final Comida comida, final OnItemClickListener listener) {
            nombre.setText(comida.getNombre());

             Picasso.get().load(comida.getImagen()).into(imagen);

            itemView.setOnClickListener(view -> {
                listener.onItemClick(comida);
            });
        }
    }
}
