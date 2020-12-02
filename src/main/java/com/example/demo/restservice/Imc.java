package com.example.demo.restservice;

public class Imc {

    private float fImc;
    private float fPoids;
    private float fTaille;
    private String imc;

    private static final String imcTemplate = "Votre IMC est de : %s";

    public Imc(String poids, String taille){
        fPoids = Float.parseFloat(poids);
        fTaille = Float.parseFloat(taille);

        this.fImc = fPoids / (fTaille*fTaille);
        this.imc = String.format(imcTemplate, Float.toString(this.fImc));

    }

    public String getImc() {
        return imc;
    }
    public float getFIMC() { return fImc; }
    public float getFPoids() { return fPoids; }
    public float getFTaille() { return fTaille; }




}
