package es.uniovi.eii.bestfood;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

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
        private final TextView puntuacion;
        private ImageView imagen;

        public ComidaViewHolder(View itemView) {
            super(itemView);
            this.nombre = itemView.findViewById(R.id.nombrecomida);
            this.puntuacion = itemView.findViewById(R.id.puntuacion);
            // this.imagen = itemView.findViewById(R.id.imagen);
        }

        public void bindUser(final Comida comida, final OnItemClickListener listener) {
            nombre.setText(comida.getNombre());
            puntuacion.setText(new StringBuilder().append(comida.getPuntuacion()).append(puntuacion.getContext().getString(R.string.puntos)).toString());

            // Picasso.get().load(comida.getUrlCaratula()).into(imagen);

            itemView.setOnClickListener(view -> {
                listener.onItemClick(comida);
            });
        }
    }
}
