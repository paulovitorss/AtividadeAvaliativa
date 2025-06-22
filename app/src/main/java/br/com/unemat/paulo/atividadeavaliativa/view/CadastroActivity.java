package br.com.unemat.paulo.atividadeavaliativa.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import br.com.unemat.paulo.atividadeavaliativa.ui.auth.LoginActivity;
import br.com.unemat.paulo.atividadeavaliativa.R;
import br.com.unemat.paulo.atividadeavaliativa.controller.UsuarioController;
import br.com.unemat.paulo.atividadeavaliativa.model.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private EditText editTextNome, editTextCPF, editTextEmail, editTextTelefone, editTextEndereco, editTextSenha, editTextConfirmarSenha;
    private UsuarioController usuarioController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        // Inicializar campos
        editTextNome = findViewById(R.id.editTextNome);
        editTextCPF = findViewById(R.id.editTextCPF);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextTelefone = findViewById(R.id.editTextTelefone);
        editTextEndereco = findViewById(R.id.editTextEndereco);
        editTextSenha = findViewById(R.id.editTextSenha);
        editTextConfirmarSenha = findViewById(R.id.editTextConfirmarSenha);
        Button btnCadastrar = findViewById(R.id.btnCadastrar);
        Button btnVoltar = findViewById(R.id.btnVoltar);

        usuarioController = new UsuarioController(this);

        // Botão cadastrar
        btnCadastrar.setOnClickListener(v -> {
            String nome = editTextNome.getText().toString().trim();
            String cpf = editTextCPF.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String telefone = editTextTelefone.getText().toString().trim();
            String endereco = editTextEndereco.getText().toString().trim();
            String senha = editTextSenha.getText().toString().trim();
            String confirmarSenha = editTextConfirmarSenha.getText().toString().trim();

            if (nome.isEmpty() || cpf.isEmpty() || email.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!senha.equals(confirmarSenha)) {
                Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
                return;
            }

            Usuario usuario = new Usuario(nome, cpf, email, telefone, endereco, senha);
            long resultado = usuarioController.inserirUsuario(usuario);

            if (resultado != -1) {
                Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Erro ao cadastrar. E-mail já pode estar em uso.", Toast.LENGTH_LONG).show();
            }
        });

        btnVoltar.setOnClickListener(v -> finish());
    }
}