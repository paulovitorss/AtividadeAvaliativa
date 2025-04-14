package br.com.unemat.paulo.atividadeavaliativa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import br.com.unemat.paulo.atividadeavaliativa.controller.UsuarioController;
import br.com.unemat.paulo.atividadeavaliativa.view.CadastroActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextSenha;
    private UsuarioController usuarioController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar componentes da view
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextSenha = findViewById(R.id.editTextSenha);
        Button btnEntrar = findViewById(R.id.btnEntrar);
        TextView txtCadastrar = findViewById(R.id.txtCadastrar);

        // Inicializar controller (parte da lógica)
        usuarioController = new UsuarioController(this);

        // Login
        btnEntrar.setOnClickListener(v -> realizarLogin());

        // Navegar para tela de cadastro
        txtCadastrar.setOnClickListener(v -> {
            startActivity(new Intent(this, CadastroActivity.class));
        });
    }

    private void realizarLogin() {
        String login = editTextEmail.getText().toString().trim();
        String senha = editTextSenha.getText().toString().trim();

        if (login.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar se é admin
        if (usuarioController.validarLoginAdmin(login, senha)) {
            startActivity(new Intent(this, AdminActivity.class));
            finish();
            return;
        }

        // Verificar se é usuário comum (login via email)
        if (usuarioController.validarLoginUsuario(login, senha)) {
            startActivity(new Intent(this, CidadaoActivity.class));
            finish();
            return;
        }

        // Verificar se é login via matrícula (aluno)
        if (usuarioController.validarLoginMatricula(login, senha)) {
            startActivity(new Intent(this, CidadaoActivity.class));
            finish();
            return;
        }

        Toast.makeText(this, "Email ou senha inválidos", Toast.LENGTH_SHORT).show();
    }
}