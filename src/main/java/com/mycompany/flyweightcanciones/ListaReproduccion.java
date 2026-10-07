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
public class ListaReproduccion {
    // guarda las referencias 
    //class
      private static class EntradaLista {
        Cancion cancion;  // referencia compartida fly
        int orden;        // extrínseco: orden de inserción en la  lista
 
        EntradaLista(Cancion cancion, int orden) {
            this.cancion = cancion;
            this.orden = orden;
        }
    }
 
    private String nombre;
    private ArrayList<EntradaLista> entradas = new ArrayList<>();
    private int siguienteOrden = 1;
 
    public ListaReproduccion(String nombre) {
        this.nombre = nombre;
    }
 
    public String getNombre() {
        return nombre;
    }
 
    // ddescanso en la region critica 
    private static void dormir(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
 
    // regionncritica(escritura)
    public synchronized void agregar(Cancion c) {
        dormir(50);
        entradas.add(new EntradaLista(c, siguienteOrden));
        siguienteOrden++;
    }
 
    //  Devuelve true si la canción estaba y se quitó.
    public synchronized boolean eliminar(int id) {
        dormir(50);
        for (int i = 0; i < entradas.size(); i++) {
            if (entradas.get(i).cancion.getId() == id) {
                entradas.remove(i);
                return true;
            }
        }
        return false;
    }
 
    // Lectura
    public synchronized ArrayList<Cancion> buscar(String texto) {
        dormir(50);
        ArrayList<Cancion> resultado = new ArrayList<>();
        for (int i = 0; i < entradas.size(); i++) {
            Cancion c = entradas.get(i).cancion;
            if (c.coincide(texto)) {
                resultado.add(c);
            }
        }
        return resultado;
    }
 
    // recorre la lista y muestra cada canción
    public synchronized void mostrar() {
        System.out.println("Lista " + nombre + ":");
        for (int i = 0; i < entradas.size(); i++) {
            EntradaLista e = entradas.get(i);
            System.out.println("  posición " + (i + 1) + " (orden de inserción " + e.orden
                    + "): " + e.cancion);
        }
    }
 
    // devuelve la canción con ese id, o null si no está
    public synchronized Cancion obtener(int id) {
        for (int i = 0; i < entradas.size(); i++) {
            if (entradas.get(i).cancion.getId() == id) {
                return entradas.get(i).cancion;
            }
        }
        return null;
    }
 
    // canción en una posición (empezando en 0)
    public synchronized Cancion getCancion(int posicion) {
        return entradas.get(posicion).cancion;
    }
 
    public synchronized int tamano() {
        return entradas.size();
    }
}