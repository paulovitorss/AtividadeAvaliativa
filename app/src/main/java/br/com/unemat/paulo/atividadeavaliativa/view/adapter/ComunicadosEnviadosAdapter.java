package br.com.unemat.paulo.atividadeavaliativa.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.unemat.paulo.atividadeavaliativa.R;
import br.com.unemat.paulo.atividadeavaliativa.controller.ComunicadoController;
import br.com.unemat.paulo.atividadeavaliativa.model.Comunicado;

public class ComunicadosEnviadosAdapter extends RecyclerView.Adapter<ComunicadosEnviadosAdapter.ComunicadoViewHolder> {

    private final List<Comunicado> comunicadosList;
    private final Context context;
    private final ComunicadoController controller;

    public ComunicadosEnviadosAdapter(Context context, List<Comunicado> comunicadosList) {
        this.context = context;
        this.comunicadosList = comunicadosList;
        this.controller = new ComunicadoController(context);
    }

    @NonNull
    @Override
    public ComunicadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comunicado_enviado, parent, false);
        return new ComunicadoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComunicadoViewHolder holder, int position) {
        Comunicado item = comunicadosList.get(position);
        holder.txtTitulo.setText(item.getTitulo());
        holder.txtConteudo.setText(item.getConteudo());
        holder.txtData.setText("Data: " + item.getData());
        holder.txtDestinatario.setText("Para: " + item.getDestinatario());

        holder.btnRemover.setOnClickListener(v -> {
            controller.removerComunicado(item.getId());
            comunicadosList.remove(holder.getAdapterPosition());
            notifyItemRemoved(holder.getAdapterPosition());
            Toast.makeText(context, "Comunicado removido", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return comunicadosList != null ? comunicadosList.size() : 0;
    }

    static class ComunicadoViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitulo, txtConteudo, txtData, txtDestinatario;
        Button btnRemover;

        public ComunicadoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTituloComunicado);
            txtConteudo = itemView.findViewById(R.id.txtConteudoComunicado);
            txtData = itemView.findViewById(R.id.txtDataComunicado);
            txtDestinatario = itemView.findViewById(R.id.txtDestinatarioComunicado);
            btnRemover = itemView.findViewById(R.id.btnRemover);
        }
    }
}