package Grafo;

public class Test {
    public static void main(String[] args) {
        Grafo grafo = new Grafo();
        LectorDeGrafo lector = new LectorDeGrafo();
        lector.loadGrafo("src/main/java/Copia de red_logistica_grafo.csv",grafo); //Insertar la direccion del archivo
        System.out.println(grafo.showListaDeAdyacencia());

        Algoritmos algoritmos = new Algoritmos();
        System.out.println(algoritmos.dijkstra(grafo.getListaDeAdyacencia(),"CD-Mexicali","Hub-Norte").toString());
        System.out.println(algoritmos.prim(grafo.getListaDeAdyacencia(),"CD-Hermosillo"));
        System.out.println(algoritmos.floydWarshall(grafo.getListaDeAdyacencia()));
    }
}
