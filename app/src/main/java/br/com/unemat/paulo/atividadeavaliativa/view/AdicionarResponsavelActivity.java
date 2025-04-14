package br.com.unemat.paulo.atividadeavaliativa.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import br.com.unemat.paulo.atividadeavaliativa.R;
import br.com.unemat.paulo.atividadeavaliativa.controller.ResponsavelController;
import br.com.unemat.paulo.atividadeavaliativa.model.Responsavel;

public class AdicionarResponsavelActivity extends AppCompatActivity {

    private EditText editTextNome, editTextCPF, editTextEmail, editTextTelefone,
            editTextEndereco, editTextSenha, editTextConfirmarSenha;

    private ResponsavelController responsavelController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_responsavel);

        editTextNome = findViewById(R.id.editTextNome);
        editTextCPF = findViewById(R.id.editTextCPF);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextTelefone = findViewById(R.id.editTextTelefone);
        editTextEndereco = findViewById(R.id.editTextEndereco);
        editTextSenha = findViewById(R.id.editTextSenha);
        editTextConfirmarSenha = findViewById(R.id.editTextConfirmarSenha);

        Button btnCadastrar = findViewById(R.id.btnCadastrar);
        Button btnVoltar = findViewById(R.id.btnVoltar);

        responsavelController = new ResponsavelController(this);

        btnCadastrar.setOnClickListener(v -> cadastrarResponsavel());
        btnVoltar.setOnClickListener(v -> finish());
    }

    private void cadastrarResponsavel() {
        String nome = editTextNome.getText().toString().trim();
        String cpf = editTextCPF.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String telefone = editTextTelefone.getText().toString().trim();
        String endereco = editTextEndereco.getText().toString().trim();
        String senha = editTextSenha.getText().toString().trim();
        String confirmarSenha = editTextConfirmarSenha.getText().toString().trim();

        if (nome.isEmpty() || cpf.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()) {
            Toast.makeText(this, "Preencha os campos obrigatórios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!senha.equals(confirmarSenha)) {
            Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
            return;
        }

        Responsavel responsavel = new Responsavel(nome, cpf, email, telefone, endereco, senha);
        long resultado = responsavelController.cadastrarResponsavel(responsavel);

        if (resultado != -1) {
            Toast.makeText(this, "Responsável cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erro ao cadastrar. Verifique se o CPF ou e-mail já existem.", Toast.LENGTH_LONG).show();
        }
    }
}