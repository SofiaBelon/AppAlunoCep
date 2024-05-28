package com.example.atividade5ac2.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.atividade5ac2.R;
import com.example.atividade5ac2.api.AlunoService;
import com.example.atividade5ac2.api.ApiCep;
import com.example.atividade5ac2.api.ApiClient;
import com.example.atividade5ac2.api.CepService;
import com.example.atividade5ac2.model.Aluno;
import com.example.atividade5ac2.model.Cep;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlunoActivity extends AppCompatActivity {

    Button btnSalvar;
    Button btnBuscarCep;
    AlunoService apiService;
    TextView txtRaAluno, txtNomeAluno, txtCep, txtLogradouro, txtComplemento, txtBairro, txtCidade, txtUf;
    int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aluno);
        btnSalvar = (Button) findViewById(R.id.btnSalvar);
        btnBuscarCep = (Button) findViewById(R.id.btnCep);
        apiService = ApiClient.getAlunoService();
        txtRaAluno = findViewById(R.id.txtRaAluno);
        txtNomeAluno = findViewById(R.id.txtNomeAluno);
        txtCep = findViewById(R.id.txtCep);
        txtLogradouro = findViewById(R.id.txtLogradouro);
        txtComplemento = findViewById(R.id.txtComplemento);
        txtBairro = findViewById(R.id.txtBairro);
        txtCidade = findViewById(R.id.txtCidade);
        txtUf = findViewById(R.id.txtUf);

        id = getIntent().getIntExtra("id", 0);

        btnBuscarCep.setOnClickListener(v -> {
            buscarEnderecoPorCep(txtCep.getText().toString());
        });

        if (id > 0) {
            apiService.getAlunoPorId(id).enqueue(new Callback<Aluno>() {
                @Override
                public void onResponse(Call<Aluno> call, Response<Aluno> response) {
                    if (response.isSuccessful()) {
                        txtRaAluno.setText(response.body().getRa());
                        txtNomeAluno.setText(response.body().getNome());
                        txtCep.setText(response.body().getCep());
                        txtLogradouro.setText(response.body().getLogradouro());
                        txtComplemento.setText(response.body().getComplemento());
                        txtBairro.setText(response.body().getBairro());
                        txtCidade.setText(response.body().getCidade());
                        txtUf.setText(response.body().getUf());
                    }
                }

                @Override
                public void onFailure(Call<Aluno> call, Throwable t) {
                    Log.e("Obter aluno", "Erro ao obter aluno");
                }
            });


        }


        btnSalvar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view){
                Aluno aluno = new Aluno();
                aluno.setRa(Integer.parseInt(txtRaAluno.getText().toString()));
                aluno.setNome(txtNomeAluno.getText().toString());
                aluno.setCep(txtCep.getText().toString());
                aluno.setLogradouro(txtLogradouro.getText().toString());
                aluno.setComplemento(txtComplemento.getText().toString());
                aluno.setBairro(txtBairro.getText().toString());
                aluno.setCidade(txtCidade.getText().toString());
                aluno.setUf(txtUf.getText().toString());
                if (id == 0)
                    inserirAluno(aluno);
                else {
                    aluno.setId(id);
                    editarAluno(aluno);
                }
            }
        });

    }
    private void buscarEnderecoPorCep(String cep) {
        CepService cepService = ApiCep.getCepService();
        Call<Cep> call = cepService.getEnderecoByCep(cep);

        call.enqueue(new Callback<Cep>() {
            @Override
            public void onResponse(Call<Cep> call, Response<Cep> response) {
                if (response.isSuccessful()) {
                    Cep cep = response.body();
                    if (cep != null) {
                        txtLogradouro.setText(cep.getLogradouro());
                        txtBairro.setText(cep.getBairro());
                        txtCidade.setText(cep.getLocalidade());
                        txtUf.setText(cep.getUf());
                    } else {
                        Toast.makeText(AlunoActivity.this, "Endereço não encontrado", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AlunoActivity.this, "Erro na resposta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Cep> call, Throwable t) {
                Toast.makeText(AlunoActivity.this, "Falha na requisição", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void inserirAluno(Aluno aluno) {
        Call<Aluno> call = apiService.postAluno(aluno);
        call.enqueue(new Callback<Aluno>() {
            @Override
            public void onResponse(Call<Aluno> call, Response<Aluno> response) {
                if (response.isSuccessful()) {
                    // A solicitação foi bem-sucedida
                    Aluno createdPost = response.body();
                    Toast.makeText(AlunoActivity.this, "Inserido com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // A solicitação falhou
                    Log.e("Inserir", "Erro ao criar: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Aluno> call, Throwable t) {
                // Ocorreu um erro ao fazer a solicitação
                Log.e("Inserir", "Erro ao criar: " + t.getMessage());
            }
        });
    }
    private void editarAluno(Aluno aluno){
        Call<Aluno> call = apiService.putAluno(id, aluno);
        call.enqueue(new Callback<Aluno>() {
            @Override
            public void onResponse(Call<Aluno> call, Response<Aluno> response) {
                if (response.isSuccessful()) {
                    // A solicitação foi bem-sucedida
                    Aluno createdPost = response.body();
                    Toast.makeText(AlunoActivity.this, "Editado com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // A solicitação falhou
                    Log.e("Editar", "Erro ao editar: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Aluno> call, Throwable t) {
                // Ocorreu um erro ao fazer a solicitação
                Log.e("Editar", "Erro ao editar: " + t.getMessage());
            }
        });
    }
}
