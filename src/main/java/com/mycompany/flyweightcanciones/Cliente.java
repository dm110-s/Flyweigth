/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.flyweightcanciones;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author macbook
 */
public class Cliente extends Thread {
    
    // Tipos de operación que puede hacer el hilo en run()
    public static final int NINGUNA = 0;           // sin hilo
    public static final int BUSCAR = 1;            // buscar un texto en una lista
    public static final int AGREGAR = 2;           // agregar una canción a una lista
    public static final int ELIMINAR = 3;          // eliminar una canción de una lista
    public static final int CONSULTAR_FABRICA = 4; // preguntar cuántos Flyweights hay
    public static final int PEDIR_CANCIONES = 5;   // pedir canciones directo a la fábrica
 
    private Almacen almacen;         // recurso compartido todos lo usan
    private int tipo;
    private ListaReproduccion lista; // lista sobre la que trabaja (BUSCAR, AGREGAR, ELIMINAR)
    private String texto;            // texto a buscar
    private int idCancion;           // canción a agregar o eliminar
    private int repeticiones;        // cuántas veces repite la operación
    private Cancion[] destino;       // donde guarda lo que recibe (PEDIR_CANCIONES)
    private int eliminadasOk = 0;    // cuántas eliminaciones sí lograron quitar la canción
 
    
    public Cliente(String nombre, Almacen almacen, int tipo, ListaReproduccion lista,
                   String texto, int idCancion, int repeticiones) {
        super(nombre); // nombre del hilo
        this.almacen = almacen;
        this.tipo = tipo;
        this.lista = lista;
        this.texto = texto;
        this.idCancion = idCancion;
        this.repeticiones = repeticiones;
    }
 
    public void setDestino(Cancion[] destino) {
        this.destino = destino;
    }
 
    public int getEliminadasOk() {
        return eliminadasOk;
    }
 
    
    // operaciobes 
   
 
    // Registra una canción: la fábrica la crea si no existe si existe la reutiliza 
    public void registrar(int id, String titulo, String artista, String album, int duracion) {
        Cancion c = almacen.getFabrica().obtener(id, titulo, artista, album, duracion);
        almacen.agregarAlCatalogo(c);
    }
 
    // Carga canciones desde un archivo. Cada línea: id;titulo;artista;album;duracion
    public int cargarDesdeArchivo(String ruta) {
        int cargadas = 0;
        try {
            BufferedReader lector = new BufferedReader(new FileReader(ruta));
            String linea = lector.readLine();
            while (linea != null) {
                if (linea.length() > 0) {
                    String[] partes = linea.split(";");
                    registrar(Integer.parseInt(partes[0]), partes[1], partes[2],
                              partes[3], Integer.parseInt(partes[4]));
                    cargadas++;
                }
                linea = lector.readLine();
            }
            lector.close();
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
        return cargadas;
    }
 
    public ListaReproduccion crearLista(String nombre) {
        ListaReproduccion nueva = new ListaReproduccion(nombre);
        almacen.agregarLista(nueva);
        return nueva;
    }
 
    // Busca la canción en el catálogo y guarda su REFERENCIA en la lista
    public boolean agregar(ListaReproduccion destinoLista, int id) {
        Cancion c = almacen.consultar(id);
        if (c == null) {
            return false;
        }
        destinoLista.agregar(c);
        return true;
    }
 
    public boolean eliminar(ListaReproduccion origenLista, int id) {
        return origenLista.eliminar(id);
    }
 
    public ArrayList<Cancion> buscar(ListaReproduccion en, String textoBuscado) {
        return en.buscar(textoBuscado);
    }
 
    public ArrayList<Cancion> buscarEnCatalogo(String textoBuscado) {
        return almacen.buscarEnCatalogo(textoBuscado);
    }
 
    public void mostrar(ListaReproduccion l) {
        l.mostrar();
    }
 
    // Suma de las canciones que hay en todas las listas
    public int totalReferencias() {
        int total = 0;
        for (int i = 0; i < almacen.numeroListas(); i++) {
            total = total + almacen.getLista(i).tamano();
        }
        return total;
    }
 
    // Cuenta objetos Cancion distintos que aparecen en las listas
    public int cancionesDiferentes() {
        ArrayList<Cancion> distintas = new ArrayList<>();
        for (int i = 0; i < almacen.numeroListas(); i++) {
            ListaReproduccion l = almacen.getLista(i);
            for (int j = 0; j < l.tamano(); j++) {
                Cancion c = l.getCancion(j);
                if (!distintas.contains(c)) {
                    distintas.add(c);
                }
            }
        }
        return distintas.size();
    }
 
    // Imprime las tres cifras de evidencia del patrón Flyweight
    public void mostrarEvidencia() {
        System.out.println("  Total de referencias a canciones : " + totalReferencias());
        System.out.println("  Canciones diferentes en listas   : " + cancionesDiferentes());
        System.out.println("  Objetos Flyweight creados        : " + almacen.getFabrica().totalInstancias());
    }
 
    
    //start
    
    public void run() {
 
        if (tipo == BUSCAR) {
            System.out.println(getName() + ": inicia buscar '" + texto + "' en Lista " + lista.getNombre());
            int encontradas = buscar(lista, texto).size();
            System.out.println(getName() + ": termina, " + encontradas + " resultado(s)");
 
        } else if (tipo == AGREGAR) {
            System.out.println(getName() + ": inicia agregar #" + idCancion + " a Lista " + lista.getNombre()
                    + " (" + repeticiones + " vez/veces)");
            for (int i = 0; i < repeticiones; i++) {
                agregar(lista, idCancion);
            }
            System.out.println(getName() + ": termina, Lista " + lista.getNombre() + " tiene "
                    + lista.tamano() + " canciones");
 
        } else if (tipo == ELIMINAR) {
            System.out.println(getName() + ": inicia eliminar #" + idCancion + " de Lista " + lista.getNombre()
                    + " (" + repeticiones + " intento(s))");
            for (int i = 0; i < repeticiones; i++) {
                if (eliminar(lista, idCancion)) {
                    eliminadasOk++;
                }
            }
            System.out.println(getName() + ": termina, eliminó " + eliminadasOk + " vez/veces");
 
        } else if (tipo == CONSULTAR_FABRICA) {
            System.out.println(getName() + ": inicia consultar la fábrica");
            System.out.println(getName() + ": termina, "
                    + almacen.getFabrica().totalInstancias() + " Flyweights creados");
 
        } else if (tipo == PEDIR_CANCIONES) {
            // Pide 5 canciones a la fábrica varias veces y guarda la última referencia recibida
            for (int r = 0; r < repeticiones; r++) {
                for (int i = 0; i < 5; i++) {
                    destino[i] = almacen.getFabrica().obtener(i + 1, "Titulo " + (i + 1),
                                                              "Artista", "Album", 200);
                }
            }
        }
    }
}


    
