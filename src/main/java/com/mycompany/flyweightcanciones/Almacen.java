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
public class Almacen {
    //catalogo y listas 
    
    private CancionFactory fabrica;
    private ArrayList<Cancion> catalogo = new ArrayList<>();           // recurso compartido
    private ArrayList<ListaReproduccion> listas = new ArrayList<>();   
 
    public Almacen(CancionFactory fabrica) {
        this.fabrica = fabrica;
    }
 
    public CancionFactory getFabrica() {
        return fabrica;
    }
 
    // catalogo
 
    // region criticwa : guarda la referencia si todavía no esta en el catalogo
    public synchronized void agregarAlCatalogo(Cancion c) {
        if (!catalogo.contains(c)) {
            catalogo.add(c);
        }
    }
 
    // Lectura del catálogo
    public synchronized Cancion consultar(int id) {
        for (int i = 0; i < catalogo.size(); i++) {
            if (catalogo.get(i).getId() == id) {
                return catalogo.get(i);
            }
        }
        return null;
    }
 
    // lee catálogo
    public synchronized ArrayList<Cancion> buscarEnCatalogo(String texto) {
        ArrayList<Cancion> resultado = new ArrayList<>();
        for (int i = 0; i < catalogo.size(); i++) {
            if (catalogo.get(i).coincide(texto)) {
                resultado.add(catalogo.get(i));
            }
        }
        return resultado;
    }
 
    // ---------------- Listas ----------------
 
    // region critica (escritura): modifica la colección de listas
    public synchronized void agregarLista(ListaReproduccion lista) {
        listas.add(lista);
    }
 
    // Lectura: cuántas listas hay
    public synchronized int numeroListas() {
        return listas.size();
    }
 
    // Lectura: la lista que está en esa posición (empezando en 0)
    public synchronized ListaReproduccion getLista(int posicion) {
        return listas.get(posicion);
    }

    
}
