package com.sptech.SistemasDistribuidos.Entities;

import java.util.ArrayList;
import java.util.List;
import java.util.ListResourceBundle;

public class CharacterData {
    private String nome;
    private String apelidos;
    private String lar;
    private List<String> idade;
    private String afiliacao;
    private List<String> cargos;
    private String hobby;
    private String raca;
    private String genero;
    private String altura;
    private String corCabelo;
    private String corOlhos;
    private String tipoJogador;
    private List<String> armas;
    private String designer;
    private String capturaDeMovimento;
    private List<String> dubladorJap;
    private List<String> dubladorEng;
    private List<String> aparicoes;

    public CharacterData(String nome, String apelidos, String lar, List<String> idade, String afiliacao,
                         List<String> cargos, String hobby, String raca, String genero, String altura,
                         String corCabelo, String corOlhos, String tipoJogador, List<String> armas,
                         String designer, String capturaDeMovimento, List<String> dubladorJap,
                         List<String> dubladorEng, List<String> aparicoes) {
        this.nome = nome;
        this.apelidos = apelidos;
        this.lar = lar;
        this.idade = idade;
        this.afiliacao = afiliacao;
        this.cargos = cargos;
        this.hobby = hobby;
        this.raca = raca;
        this.genero = genero;
        this.altura = altura;
        this.corCabelo = corCabelo;
        this.corOlhos = corOlhos;
        this.tipoJogador = tipoJogador;
        this.armas = armas;
        this.designer = designer;
        this.capturaDeMovimento = capturaDeMovimento;
        this.dubladorJap = dubladorJap;
        this.dubladorEng = dubladorEng;
        this.aparicoes = aparicoes;
    }

    public CharacterData() {
        this.idade = new ArrayList<>();
        this.cargos = new ArrayList<>();
        this.armas = new ArrayList<>();
        this.dubladorJap = new ArrayList<>();
        this.dubladorEng = new ArrayList<>();
        this.aparicoes = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getApelidos() {
        return apelidos;
    }

    public void setApelidos(String apelidos) {
        this.apelidos = apelidos;
    }

    public String getLar() {
        return lar;
    }

    public void setLar(String lar) {
        this.lar = lar;
    }

    public List<String> getIdade() {
        return idade;
    }

    public void setIdade(List<String> idade) {
        this.idade = idade;
    }

    public String getAfiliacao() {
        return afiliacao;
    }

    public void setAfiliacao(String afiliacao) {
        this.afiliacao = afiliacao;
    }

    public List<String> getCargos() {
        return cargos;
    }

    public void setCargos(List<String> cargos) {
        this.cargos = cargos;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getAltura() {
        return altura;
    }

    public void setAltura(String altura) {
        this.altura = altura;
    }

    public String getCorCabelo() {
        return corCabelo;
    }

    public void setCorCabelo(String corCabelo) {
        this.corCabelo = corCabelo;
    }

    public String getCorOlhos() {
        return corOlhos;
    }

    public void setCorOlhos(String corOlhos) {
        this.corOlhos = corOlhos;
    }

    public String getTipoJogador() {
        return tipoJogador;
    }

    public void setTipoJogador(String tipoJogador) {
        this.tipoJogador = tipoJogador;
    }

    public List<String> getArmas() {
        return armas;
    }

    public void setArmas(List<String> armas) {
        this.armas = armas;
    }

    public String getDesigner() {
        return designer;
    }

    public void setDesigner(String designer) {
        this.designer = designer;
    }

    public String getCapturaDeMovimento() {
        return capturaDeMovimento;
    }

    public void setCapturaDeMovimento(String capturaDeMovimento) {
        this.capturaDeMovimento = capturaDeMovimento;
    }

    public List<String> getDubladorJap() {
        return dubladorJap;
    }

    public void setDubladorJap(List<String> dubladorJap) {
        this.dubladorJap = dubladorJap;
    }

    public List<String> getDubladorEng() {
        return dubladorEng;
    }

    public void setDubladorEng(List<String> dubladorEng) {
        this.dubladorEng = dubladorEng;
    }

    public List<String> getAparicoes() {
        return aparicoes;
    }

    public void setAparicoes(List<String> aparicoes) {
        this.aparicoes = aparicoes;
    }
}
