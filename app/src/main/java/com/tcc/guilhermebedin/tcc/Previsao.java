package com.tcc.guilhermebedin.tcc;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Guilherme Bedin on 09/05/2016.
 */
public class Previsao implements Serializable{
    private String local;
    private double temperatura_maxima;
    private double temperatura_minima;
    private double precipitacao;
    private double pressao;
    private Date data;

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public double getTemperatura_maxima() {
        return temperatura_maxima;
    }

    public void setTemperatura_maxima(double temperatura_maxima) {
        this.temperatura_maxima = temperatura_maxima;
    }

    public double getTemperatura_minima() {
        return temperatura_minima;
    }

    public void setTemperatura_minima(double temperatura_minima) {
        this.temperatura_minima = temperatura_minima;
    }

    public double getPrecipitacao() {
        return precipitacao;
    }

    public void setPrecipitacao(double precipitacao) {
        this.precipitacao = precipitacao;
    }

    public double getPressao() {
        return pressao;
    }

    public void setPressao(double pressao) {
        this.pressao = pressao;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
