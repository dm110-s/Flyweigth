/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.flyweightcanciones;

/**
 *
 * @author macbook
 */
public class Cancion {
    //hilo
    
   //objeto compartido 
    private final int id;
    private final String titulo;
    private final String artista;
    private final String album;
    private final int duracion; // en segundos
 
    public Cancion(int id, String titulo, String artista, String album, int duracion) {
        this.id = id;
        this.titulo = titulo;
        this.artista = artista;
        this.album = album;
        this.duracion = duracion;
    }
 
    public int getId() {
        return id;
    }
 
    // true si el texto aparece en el título, artista o álbum
    public boolean coincide(String texto) {
        String t = texto.toLowerCase();
        return titulo.toLowerCase().contains(t)/// todo en minusculas 
            || artista.toLowerCase().contains(t)
            || album.toLowerCase().contains(t);
    }
 
    public String toString() {
        int minutos = duracion / 60;
        int segundos = duracion % 60;
        String seg = "" + segundos;
        if (segundos < 10) {
            seg = "0" + seg;
        }
        return "#" + id + " " + titulo + " - " + artista + " [" + album + "] " + minutos + ":" + seg;
    }
}