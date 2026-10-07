/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.flyweightcanciones;

/**
 *
 * @author macbook
 */
public class FlyweightCanciones {
    
    public static void main(String[] args) throws InterruptedException {
 
        Almacen almacen = new Almacen(new CancionFactory());
 
        // Cliente "administrador": NO se lanza como hilo, se llaman sus metodos 
        // Cliente(nombre, almacen, tipo, lista, texto, idCancion, repeticiones)
        Cliente admin = new Cliente("Admin", almacen, Cliente.NINGUNA, null, null, 0, 0);
 
        // ESCENARIO 1: evidencia del Flyweight 
        System.out.println("=== ESCENARIO 1: carga de archivo y evidencia Flyweight ===");
        int cargadas = admin.cargarDesdeArchivo("canciones.txt");
        System.out.println("Líneas leídas del archivo: " + cargadas + " (2 están repetidas)");
        System.out.println("Flyweights creados: " + almacen.getFabrica().totalInstancias());
 
        // Registrar una canción nueva (dos veces: la segunda reutiliza la instancia)
        admin.registrar(9, "Bad Guy", "Billie Eilish", "When We All Fall Asleep", 194);
        admin.registrar(9, "Bad Guy", "Billie Eilish", "When We All Fall Asleep", 194);
 
        ListaReproduccion listaA = admin.crearLista("A");
        ListaReproduccion listaB = admin.crearLista("B");
        ListaReproduccion listaC = admin.crearLista("C");
 
        admin.agregar(listaA, 1);
        admin.agregar(listaA, 2);
        admin.agregar(listaA, 3);
        admin.agregar(listaA, 4);
        admin.agregar(listaA, 5);
        admin.agregar(listaA, 9);
 
        admin.agregar(listaB, 1);
        admin.agregar(listaB, 3);
        admin.agregar(listaB, 5);
        admin.agregar(listaB, 6);
        admin.agregar(listaB, 7);
 
        admin.agregar(listaC, 1);
        admin.agregar(listaC, 2);
        admin.agregar(listaC, 5);
        admin.agregar(listaC, 7);
        admin.agregar(listaC, 8);
 
        System.out.println("\nLa canción #1 en las tres listas:");
        Cancion oficial = almacen.getFabrica().buscarPorId(1);
        Cancion enA = listaA.obtener(1);
        Cancion enB = listaB.obtener(1);
        Cancion enC = listaC.obtener(1);
        System.out.println("  Lista A: identityHashCode=" + System.identityHashCode(enA)
                + " | misma instancia del Factory: " + (enA == oficial));
        System.out.println("  Lista B: identityHashCode=" + System.identityHashCode(enB)
                + " | misma instancia del Factory: " + (enB == oficial));
        System.out.println("  Lista C: identityHashCode=" + System.identityHashCode(enC)
                + " | misma instancia del Factory: " + (enC == oficial));
 
        System.out.println("\nEvidencia:");
        admin.mostrarEvidencia();
 
        System.out.println();
        admin.mostrar(listaA);
        System.out.println("Buscar 'Beatles' en el catálogo: " + admin.buscarEnCatalogo("Beatles"));
        System.out.println("Eliminar #9 de la lista A: " + admin.eliminar(listaA, 9));
        System.out.println("Eliminar #9 otra vez:      " + admin.eliminar(listaA, 9));
 
        // ESCENARIO 2: cuatro hilos independiente
        System.out.println("\n=== ESCENARIO 2: cuatro hilos con operaciones independientes ===");
 
        Cliente hilo1 = new Cliente("Hilo-1", almacen, Cliente.BUSCAR, listaA, "Queen", 0, 0);
        Cliente hilo2 = new Cliente("Hilo-2", almacen, Cliente.BUSCAR, listaB, "Queen", 0, 0);
        Cliente hilo3 = new Cliente("Hilo-3", almacen, Cliente.AGREGAR, listaC, null, 4, 1);
        Cliente hilo4 = new Cliente("Hilo-4", almacen, Cliente.CONSULTAR_FABRICA, null, null, 0, 0);
 
        hilo1.start();
        hilo2.start();
        hilo3.start();
        hilo4.start();
 
        hilo1.join();
        hilo2.join();
        hilo3.join();
        hilo4.join();
 
        System.out.println("Evidencia:");
        admin.mostrarEvidencia();
 
        // ESCENARIO 3: competencia por la misma lista
        System.out.println("\n=== ESCENARIO 3: agregar y eliminar en la misma lista (A) ===");
 
        int inicial = listaA.tamano();
 
        Cliente agregador1 = new Cliente("Agregador-1", almacen, Cliente.AGREGAR, listaA, null, 1, 5);
        Cliente agregador2 = new Cliente("Agregador-2", almacen, Cliente.AGREGAR, listaA, null, 1, 5);
        Cliente eliminador1 = new Cliente("Eliminador-1", almacen, Cliente.ELIMINAR, listaA, null, 1, 5);
        Cliente eliminador2 = new Cliente("Eliminador-2", almacen, Cliente.ELIMINAR, listaA, null, 1, 5);
 
        agregador1.start();
        agregador2.start();
        eliminador1.start();
        eliminador2.start();
 
        agregador1.join();
        agregador2.join();
        eliminador1.join();
        eliminador2.join();
 
        int eliminadas = eliminador1.getEliminadasOk() + eliminador2.getEliminadasOk();
        int esperado = inicial + 10 - eliminadas;
        System.out.println("Tamaño inicial: " + inicial + " | agregadas: 10 | eliminadas con éxito: " + eliminadas);
        System.out.println("Tamaño esperado: " + esperado + " | tamaño real: " + listaA.tamano());
        if (esperado == listaA.tamano()) {
            System.out.println("RESULTADO: CONSISTENTE");
        } else {
            System.out.println("RESULTADO: INCONSISTENTE");
        }
 
        //ESCENARIO 4: el Factory usado por varios hilos
        System.out.println("\n=== ESCENARIO 4: el Factory reutiliza instancias entre hilos ===");
 
        Almacen almacen2 = new Almacen(new CancionFactory());   // catalogo  vacio
        Cancion[] v1 = new Cancion[5];
        Cancion[] v2 = new Cancion[5];
        Cancion[] v3 = new Cancion[5];
        Cancion[] v4 = new Cancion[5];
 
        Cliente f1 = new Cliente("Hilo-F1", almacen2, Cliente.PEDIR_CANCIONES, null, null, 0, 100);
        Cliente f2 = new Cliente("Hilo-F2", almacen2, Cliente.PEDIR_CANCIONES, null, null, 0, 100);
        Cliente f3 = new Cliente("Hilo-F3", almacen2, Cliente.PEDIR_CANCIONES, null, null, 0, 100);
        Cliente f4 = new Cliente("Hilo-F4", almacen2, Cliente.PEDIR_CANCIONES, null, null, 0, 100);
        f1.setDestino(v1);
        f2.setDestino(v2);
        f3.setDestino(v3);
        f4.setDestino(v4);
 
        f1.start();
        f2.start();
        f3.start();
        f4.start();
        f1.join();
        f2.join();
        f3.join();
        f4.join();
 
        boolean iguales = true;
        for (int i = 0; i < 5; i++) {
            if (v2[i] != v1[i] || v3[i] != v1[i] || v4[i] != v1[i]) {
                iguales = false;
            }
        }
        System.out.println("Solicitudes: 4 hilos x 100 x 5 canciones = 2000");
        System.out.println("Flyweights creados (esperado 5): " + almacen2.getFabrica().totalInstancias());
        System.out.println("Todos los hilos recibieron la misma instancia: " + iguales);
    }
}


