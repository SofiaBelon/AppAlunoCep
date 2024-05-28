package com.example.atividade5ac2.api;
import com.example.atividade5ac2.model.Cep;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
public interface CepService {
    @GET("{cep}/json/")
    Call<Cep> getEnderecoByCep(@Path("cep") String cep);
}
