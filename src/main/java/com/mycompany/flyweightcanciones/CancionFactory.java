/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.flyweightcanciones;

import java.util.ArrayList;


/**
 *
 * @author macbook
 */
public class CancionFactory {
    //busca, crea y reutiliza 
    private ArrayList<Cancion> pool = new ArrayList<>();
 
    // REGIÓN CRÍTICA: "buscar y crear si no existe" debe hacerlo completo un solo
    // hilo a la vez; si no, dos hilos podrían crear la misma canción dos veces.
    // Mecanismo: synchronized
    public synchronized Cancion obtener(int id, String titulo, String artista,
                                        String album, int duracion) {
        for (int i = 0; i < pool.size(); i++) {
            if (pool.get(i).getId() == id) {
                return pool.get(i);              // ya existe: se reutiliza
            }
        }
        Cancion nueva = new Cancion(id, titulo, artista, album, duracion);
        pool.add(nueva);                         // no existe: se crea y se registra
        return nueva;
    }
 
    public synchronized Cancion buscarPorId(int id) {
        for (int i = 0; i < pool.size(); i++) {
            if (pool.get(i).getId() == id) {
                return pool.get(i);
            }
        }
        return null;
    }
 
    // Cantidad de objetos Fly creados
    public synchronized int totalInstancias() {
        return pool.size();
    }
}

