package br.com.unemat.paulo.atividadeavaliativa;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.unemat.paulo.atividadeavaliativa.db.dao.ResponsavelDAO;
import br.com.unemat.paulo.atividadeavaliativa.model.Responsavel;

public class EnviarComunicadosActivity extends AppCompatActivity {
    private Spinner spinnerDestinatario;
    private EditText editTextTituloComunicado;
    private EditText editTextConteudoComunicado;

    private List<String> destinatarios;
    private List<ComunicadoEnviadoItem> comunicadosList;
    private ComunicadosEnviadosAdapter adapter;
    private ResponsavelDAO responsavelDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_comunicados);

        // Inicializar componentes
        TextView txtTitulo = findViewById(R.id.txtTitulo);
        spinnerDestinatario = findViewById(R.id.spinnerDestinatario);
        editTextTituloComunicado = findViewById(R.id.editTextTituloComunicado);
        editTextConteudoComunicado = findViewById(R.id.editTextConteudoComunicado);
        Button btnEnviarComunicado = findViewById(R.id.btnEnviarComunicado);
        RecyclerView recyclerViewComunicados = findViewById(R.id.recyclerViewComunicados);
        Button btnVoltar = findViewById(R.id.btnVoltar);

        // Configurar título
        txtTitulo.setText("Enviar Comunicados");
        carregarDestinatariosDoBanco();

        // Configurar spinner
        ArrayAdapter<String> destinatariosAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, destinatarios);
        destinatariosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDestinatario.setAdapter(destinatariosAdapter);

        comunicadosList = new ArrayList<>();

        // Configurar RecyclerView
        adapter = new ComunicadosEnviadosAdapter(comunicadosList);
        recyclerViewComunicados.setAdapter(adapter);
        recyclerViewComunicados.setLayoutManager(new LinearLayoutManager(this));

        // Configurar listener para o botão de enviar comunicado
        btnEnviarComunicado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarComunicado();
            }
        });

        // Configurar listener para o botão de voltar
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void carregarDestinatariosDoBanco() {
        responsavelDAO = new ResponsavelDAO(this);
        List<Responsavel> lista = responsavelDAO.getResponsaveis();
        destinatarios = new ArrayList<>();

        // Adiciona uma opção geral
        destinatarios.add("Todos os Responsáveis");

        for (Responsavel r : lista) {
            destinatarios.add(r.getNome());
        }
    }

    private void enviarComunicado() {
        String destinatario = spinnerDestinatario.getSelectedItem().toString();
        String titulo = editTextTituloComunicado.getText().toString().trim();
        String conteudo = editTextConteudoComunicado.getText().toString().trim();

        if (titulo.isEmpty() || conteudo.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obter data atual formatada
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dataAtual = sdf.format(new Date());

        // Adicionar novo comunicado à lista
        comunicadosList.add(0, new ComunicadoEnviadoItem(titulo, conteudo, dataAtual, destinatario));

        // Atualizar RecyclerView
        adapter.notifyDataSetChanged();

        // Limpar campos
        editTextTituloComunicado.setText("");
        editTextConteudoComunicado.setText("");

        Toast.makeText(this, "Comunicado enviado com sucesso!", Toast.LENGTH_SHORT).show();
    }

    // Classe modelo para os itens de comunicado enviado
    public static class ComunicadoEnviadoItem {
        private String titulo;
        private String conteudo;
        private String data;
        private String destinatario;

        public ComunicadoEnviadoItem(String titulo, String conteudo, String data, String destinatario) {
            this.titulo = titulo;
            this.conteudo = conteudo;
            this.data = data;
            this.destinatario = destinatario;
        }

        public String getTitulo() {
            return titulo;
        }

        public String getConteudo() {
            return conteudo;
        }

        public String getData() {
            return data;
        }

        public String getDestinatario() {
            return destinatario;
        }
    }

    // Adapter para o RecyclerView de comunicados enviados
    public class ComunicadosEnviadosAdapter extends RecyclerView.Adapter<ComunicadosEnviadosAdapter.ComunicadoViewHolder> {
        private List<ComunicadoEnviadoItem> comunicadosList;

        public ComunicadosEnviadosAdapter(List<ComunicadoEnviadoItem> comunicadosList) {
            this.comunicadosList = comunicadosList;
        }

        @Override
        public ComunicadoViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_comunicado_enviado, parent, false);
            return new ComunicadoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ComunicadoViewHolder holder, int position) {
            ComunicadoEnviadoItem item = comunicadosList.get(position);
            holder.txtTitulo.setText(item.getTitulo());
            holder.txtConteudo.setText(item.getConteudo());
            holder.txtData.setText("Data: " + item.getData());
            holder.txtDestinatario.setText("Para: " + item.getDestinatario());

            // Configurar botão de remover
            holder.btnRemover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = holder.getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        comunicadosList.remove(adapterPosition);
                        notifyItemRemoved(adapterPosition);
                        Toast.makeText(EnviarComunicadosActivity.this, "Comunicado removido", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return comunicadosList.size();
        }

        public class ComunicadoViewHolder extends RecyclerView.ViewHolder {
            TextView txtTitulo, txtConteudo, txtData, txtDestinatario;
            Button btnRemover;

            public ComunicadoViewHolder(View itemView) {
                super(itemView);
                txtTitulo = itemView.findViewById(R.id.txtTituloComunicado);
                txtConteudo = itemView.findViewById(R.id.txtConteudoComunicado);
                txtData = itemView.findViewById(R.id.txtDataComunicado);
                txtDestinatario = itemView.findViewById(R.id.txtDestinatarioComunicado);
                btnRemover = itemView.findViewById(R.id.btnRemover);
            }
        }
    }
}
