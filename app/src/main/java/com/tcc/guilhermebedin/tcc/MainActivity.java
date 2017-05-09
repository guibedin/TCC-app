package com.tcc.guilhermebedin.tcc;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    // Definiçao de variaveis
    private TextView txtPrevisao;
    private TextView txtData;
    private TextView txtTempMaxima;
    private TextView txtTempMinima;
    private TextView txtTempMedia;
    private TextView txtPrecipitacao;
    private Button btnGetPrevisao;
    private Button btnVoltar;
    private DatePicker datePicker;

    private int dia;
    private int mes;
    private int ano;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtPrevisao = (TextView) findViewById(R.id.txtPrevisao);
        txtData = (TextView) findViewById(R.id.txtData);
        txtTempMaxima = (TextView) findViewById(R.id.txtTempMaxima);
        txtTempMinima = (TextView) findViewById(R.id.txtTempMinima);
        txtTempMedia = (TextView) findViewById(R.id.txtTempMedia);
        txtPrecipitacao = (TextView) findViewById(R.id.txtPrecipitacao);
        btnGetPrevisao = (Button) findViewById(R.id.btnGetPrevisao);
        btnVoltar = (Button) findViewById(R.id.btnVoltar);
        datePicker = (DatePicker) findViewById(R.id.datePicker);

        // Evento do botao responsavel por fazer a requisicao GET ao web service
        btnGetPrevisao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pega data do datepicker para enviar ao web service
                dia = datePicker.getDayOfMonth();
                mes = datePicker.getMonth() + 1;
                ano = datePicker.getYear();
                Log.i("DATE", "" + dia + mes + ano);
                lerPrevisaoJSON();
            }
        });

        btnVoltar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                datePicker.setVisibility(View.VISIBLE);
                btnGetPrevisao.setVisibility(View.VISIBLE);
                //txtPrevisao.setVisibility(View.VISIBLE);
                txtPrevisao.setText("Selecione uma data:");
                btnVoltar.setVisibility(View.INVISIBLE);
                txtData.setVisibility(View.INVISIBLE);
                txtTempMaxima.setVisibility(View.INVISIBLE);
                txtTempMinima.setVisibility(View.INVISIBLE);
                txtTempMedia.setVisibility(View.INVISIBLE);
                txtPrecipitacao.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * Prepara requisicao para web service com a data desejada
     * @return Previsao feita pelo web service, no formato JSON
     */
    public String lerPrevisaoJSON() {
       try{
           //URL url = new URL("http://api.openweathermap.org/data/2.5/weather?id=3467865&APPID=d615f6c884a103b652b5edeb8558067c");
           //URL url = new URL("http://172.20.10.6:8080/TCC/jaxrs/previsao?data="
           // "/" + mes + "/" + ano);
           URL url = new URL("http://192.168.0.108:8080/TCC/jaxrs/previsao?data="
                   + ano + "/" + mes + "/" + dia);
           new ConsomeJSON().execute(url);
       }catch (Exception ex) {
           ex.printStackTrace(System.out);
       }
        return null;
    }

    /**
     * Classe responsavel por gerar uma task assincrona, utilizada para comunicacao com o
     * Web Service
     */
    private class ConsomeJSON extends AsyncTask<URL, Integer, String> {

        @Override
        protected String doInBackground(URL... params) {
            HttpURLConnection conexao = null;
            InputStream is = null;

            // Define parametros de conexao e conecta no web service
            try {
                conexao = (HttpURLConnection) params[0].openConnection();
                conexao.setRequestMethod("GET");
                conexao.setDoInput(true);
                conexao.setDoOutput(false);
                conexao.connect();
                int status = conexao.getResponseCode();

                Log.i("METHOD", conexao.getRequestMethod());
                Log.i("STATUS", "" + status);
                switch (status) {
                    case 200:
                    case 201:
                        // Le resposta do web service
                        StringBuffer buffer = new StringBuffer();
                        is = conexao.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        String line = null;
                        while ((line = br.readLine()) != null)
                            buffer.append(line);

                        is.close();
                        conexao.disconnect();
                        Log.i("RESULTADO", buffer.toString());
                        return buffer.toString();
                }
            }
            catch(Exception e) {
                e.printStackTrace(System.out);
            }
            finally {
                try { is.close(); } catch(Throwable t) {}
                try { conexao.disconnect(); } catch(Throwable t) {}
            }
            return null;
        }

        /**
         * Metodo executado apos o fim da conexao com Web Service, onde é feita a leitura
         * do resultado
         */
        protected void onPostExecute(String result) {
            if(result != null){
                //txtPrevisao.setVisibility(View.INVISIBLE);
                try{
                    JSONObject jResult = new JSONObject(result);

                    datePicker.setVisibility(View.INVISIBLE);
                    btnGetPrevisao.setVisibility(View.INVISIBLE);

                    btnVoltar.setVisibility(View.VISIBLE);
                    txtData.setVisibility(View.VISIBLE);
                    txtTempMaxima.setVisibility(View.VISIBLE);
                    txtTempMinima.setVisibility(View.VISIBLE);
                    txtTempMedia.setVisibility(View.VISIBLE);
                    txtPrecipitacao.setVisibility(View.VISIBLE);

                    txtPrevisao.setText("Previsão Realizada:\n");
                    txtData.setText("Data: " + jResult.getString("data"));
                    txtTempMaxima.setText(String.format("Temperatura Máxima: %.2f", jResult.getDouble("temperatura_maxima")) + " °C");
                    txtTempMinima.setText(String.format("Temperatura Minima: %.2f", jResult.getDouble("temperatura_minima")) + " °C");
                    txtTempMedia.setText(String.format("Temperatura Média: %.2f", jResult.getDouble("temperatura_media")) + " °C");
                    txtPrecipitacao.setText(String.format("Precipitação: %.2f", jResult.getDouble("precipitacao")) + " mm");
                }catch(Exception e){
                    e.printStackTrace();
                }
            }else{
                datePicker.setVisibility(View.INVISIBLE);
                btnGetPrevisao.setVisibility(View.INVISIBLE);

                btnVoltar.setVisibility(View.VISIBLE);

                txtPrevisao.setText("Servidor offline, impossível realizar previsão!");
            }
        }
    }
}