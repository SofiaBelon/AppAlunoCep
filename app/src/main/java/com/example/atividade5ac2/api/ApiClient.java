package com.example.atividade5ac2.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
    public class ApiClient {
        private static final String BASE_URL = "https://65e128e0d3db23f7624a76cd.mockapi.io/api/v1/";

        private static Retrofit retrofit = null;

        public static Retrofit getClient() {
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit;
        }
        public static AlunoService getAlunoService() {
            return getClient().create(AlunoService.class);
        }


    }
