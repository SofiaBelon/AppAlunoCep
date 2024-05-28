package com.example.atividade5ac2.api;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class ApiCep {
    private static final String BASEURL = "https://viacep.com.br/ws/";
    private static Retrofit retrofit = null;
    public static Retrofit getCep() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASEURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public static CepService getCepService() {
        return getCep().create(CepService.class);
    }
}
