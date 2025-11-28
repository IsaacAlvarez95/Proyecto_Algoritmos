package Grafo;

import java.util.*;

public class Algoritmos {

    //METODO DE DIJKSTRA

    /**
     * Obtiene una lista que contiene todos los vertices necesarios para llegar al destino.
     * @param grafo Grafo que se verificara.
     * @param origen Vertice de donde se origina la ruta.
     * @param destino Vertice de destino.
     * @return Lista de vertices visitados desde el origen  hasta el final.
     */
    public ArrayList<String> dijkstra(HashMap<String, ArrayList<Arista>> grafo, String origen, String destino){
        /**
         * Se inician los valores necesarios. Las distancias son infinitas.
         */
        PriorityQueue<NodoDistancia> queue = new PriorityQueue<>();
        HashMap<String, Double> dist = new HashMap<>();
        HashMap<String, String> padres = new HashMap<>();
        for (String key : grafo.keySet()) {
            dist.put(key, Double.POSITIVE_INFINITY);
            padres.put(key, null);
        } //Se inicializan los valores, @dist contiene las distancias de cada vertice al inicio, empezando como
        //infinito
        //Cada vertice tiene como padre un nulo, se encuentran en el hash padres
        /**
         * La distancia del origen es 0.
         */
        dist.put(origen, 0.0);
        queue.add(new NodoDistancia(origen, 0)); //El vertice de inicio tendra una distancia de 0 y
                                                        //Se agreaga a la cola de prioridad
        /**
         * Se visitan de una a una las aristas que se van insertando
         * dentro de la cola de prioridad hasta encontrar el vertice de destino.
         */
        while (!queue.isEmpty()){ //Hasta que no encuentre mas rutas
            NodoDistancia u = queue.poll(); //Se toma la ultima arista en base a la prioridad.

            if(u.getNombre().equals(destino)){ //Se encontro el destino
                /**
                 * Se regresa la lista necesaria para recrear la ruta.
                 */
                return reconstruirRuta(padres,origen,destino);
            }else{
                for(Arista a: grafo.get(u.getNombre())){ //Para cada arista del vertice, se identifica la distancia
                                                        //Entre el padre y el hijo
                    double nuevaDistancia = u.getDistancia() + a.getCosto();
                    if (dist.get(a.getDestino()) > nuevaDistancia){ //Si la distancia es mayor que la nueva distancia
                                                                    //Es decir, el costo mas la distancia al nodo de inicio
                                                                    //Quiere decir que no se ha explorado o hay una ruta
                                                                    //menor
                        dist.put(a.getDestino(), nuevaDistancia);
                        padres.put(a.getDestino(), u.getNombre());

                        queue.add(new NodoDistancia(a.getDestino(), nuevaDistancia)); //Se agrega la arista a la cola de prioridad
                    }
                }
            }
        }
        /**
         * Si no se encuentra se regresa una lista vacia.
         */
        return new ArrayList<>();
    }

    /**
     * Reconstruye la lista de vertices visitados en base a los padres, el origen y destino.
     * @param padres Los padres de los vertices visitados.
     * @param origen El inicio de la ruta.
     * @param destino El final de la ruta.
     * @return La ruta en si.
     */
    private ArrayList<String> reconstruirRuta(HashMap<String,String> padres, String origen, String destino){
        ArrayList<String> ruta = new ArrayList<>();
        String actual = destino;
        while(actual != null){
            ruta.addFirst(actual);
            actual = padres.get(actual);
        }
        return ruta;
    }

    /**
     * Clase que contiene las distancias recorridas en nodos.
     */
    public class NodoDistancia implements Comparable<NodoDistancia>{
        private String nombre;
        private double distancia;
        public NodoDistancia(String nombre,double distancia){
            this.nombre=nombre;
            this.distancia=distancia;
        }
        public String getNombre() {
            return nombre;
        }
        public double getDistancia() {
            return distancia;
        }
        @Override
        public int compareTo(NodoDistancia o) {
            return Double.compare(distancia,o.distancia);
        }
    }

    //METODO DE PRIM

    /**
     * Metodo de prim regresa el arbol minimo de expansion dentro de un grafo, es decir,
     * regresar el grafo sin ciclos y con las aristas de menor costo.
     * @param grafo Grafo original.
     * @param inicio De donde se empezara a construir el arbol.
     * @return Arbol de expansion minima.
     */
    public Grafo prim(HashMap<String, ArrayList<Arista>> grafo, String inicio){
        /**
         * Valores necesarios para el metodo.
         */
        HashMap<String, Double> vertice = new HashMap<>(); //Igual que el anterior
        HashMap<String, String> padres = new HashMap<>(); //Inicia los valores
        //Vertice contiene el vertice destino y el costo.
        PriorityQueue<NodoDistancia> queue = new PriorityQueue<>();
        for(String u: grafo.keySet()){
            vertice.put(u, Double.POSITIVE_INFINITY);
            padres.put(u, null);
            queue.add(new NodoDistancia(u, Double.POSITIVE_INFINITY));
        }

        /**
         * Retorna el grafo vacio si el vertice de inicio no existe.
         */
        if(!vertice.containsKey(inicio)){ //Si no se encuentra el vertice, regresa el grafo vacio
            return new Grafo();
        }

        /**
         * El vertice de inicio tiene una distancia de 0.
         */
        vertice.put(inicio, 0.0); //Se modifica el vertice de donde se encontrara el arbol de expansion
        queue.remove(new NodoDistancia(inicio,Double.POSITIVE_INFINITY)); //Minima, es decir, el arbol con menor
        queue.add(new NodoDistancia(inicio,0.0));               //Costo

        /**
         * Se visitan todas las aristas, por lo que se busca el camino mas corto entre estas.
         */
        while(!queue.isEmpty()){ //Se explora toda arista posible
            NodoDistancia u = queue.poll(); //se retira la arista con mayor prioridad
            if(grafo.containsKey(u.getNombre())){
                for(Arista a: grafo.get(u.getNombre())){ //Para cada arista del vertice
                    if(vertice.containsKey(a.getDestino()) && a.getCosto() < vertice.get(a.getDestino())){
                        //Si el costo es menor al que ya tenia el vertice anteriormente, se modifica su
                        //vertice padre y costo.
                        padres.put(a.getDestino(), u.getNombre());
                        vertice.put(a.getDestino(), a.getCosto());

                        queue.remove(new NodoDistancia(a.getDestino(), u.getDistancia()));
                        queue.add(new NodoDistancia(a.getDestino(), a.getCosto()));
                    }
                }
            }
        }
        /**
         * Se construye el grafo.
         */
        return construirGrafo(grafo,padres);
    }

    /**
     * Se reconstruye el grafo en base al grafo original y los padres de los vertices.
     * @param grafoOriginal Grafo original.
     * @param padres Padres de los vertices visitados
     * @return Arbol de expansion minima
     */
    private Grafo construirGrafo(HashMap<String, ArrayList<Arista>> grafoOriginal, HashMap<String,String> padres){
        Grafo grafo = new Grafo();
        for(String u: grafoOriginal.keySet()){
            grafo.addVertice(u);
        }
        for(String u: padres.keySet()){
            String v = padres.get(u);
            if(v != null && grafoOriginal.containsKey(v)){
                for(Arista a: grafoOriginal.get(v)){
                    if(a.getDestino().equals(u)){
                        grafo.addArista(v,a.getDestino(),a.getCosto(),a.getTipo());
                        break;
                    }
                }
            }
        }

        return grafo;
    }

    //METODO DE FLOYD-WARSHALL

    /**
     * Genera una matriz en forma de Hashmap, cuyo hashmap contiene tanto el nombre del vertice, junto con
     * todas las distancias/costos necesarios para llegar a otros vertices (De no haber ninguna, el costo sera
     * infinito)
     * @param grafo Grafo de donde se obtendra la matriz.
     * @return La matriz de distancias necesarias.
     */
    public HashMap<String,HashMap<String,Double>> floydWarshall(HashMap<String, ArrayList<Arista>> grafo){
        /**
         * Valores iniciales iniciados.
         */
        HashMap<String,Integer> verticesIndex = new HashMap<>();
        //Contiene el index que contiene cada vertice
        ArrayList<String> vertices = new ArrayList<>();
        //Arraylist que solo contiene los keyset, es decir, los vertices
        int index = 0;
        for(String u: grafo.keySet()){
            if(!verticesIndex.containsKey(u))vertices.add(u);
            verticesIndex.putIfAbsent(u, index++);
        } //Cada vertice tiene un valor asignado
        double[][] dist = new double[vertices.size()][vertices.size()]; //Matriz de adyacencia o conectividad total

        /**
         * Por cada vertice, se verifican todas las aristas del grafo hasta encontrar
         * la ruta mas corta.
         * Si el destino es igual al origen, el resultado es 0, si por el contrario,
         * no hay ninguna ruta posible, sera infinito.
         */
        for(int i = 0; i < vertices.size(); i++){ //PARA CADA VERTICE TANTO EN X COMO EN Y DENTRO DE LA MATRIZ
            for(int j = 0; j < vertices.size(); j++){
                String u = vertices.get(i); //Se obtiene el key del vertice de cual se buscara la distancia
                String v = vertices.get(j); //hacia el vertice en cuestion
                if(i == j) dist[i][j] = 0.0; //Si son el mismo, la distancia es 0.
                else dist[i][j] = Double.POSITIVE_INFINITY; //De otro modo, sera infinito hasta que se encuentre la distancia correspondiente.
                if(grafo.containsKey(u)){ //Si el grafo contiene el keyset del vertice destino
                    for(Arista a: grafo.get(u)){ //Para cada arista del vertice u.
                        if(a.getDestino().equals(v)){ //A la distancia se le actualiza el costo si ya se encontro el destino.
                            dist[i][j] = a.getCosto();
                            break;
                        }
                    }
                }
            }
        }

        /**
         * Se calculan las distancias finales.
         */
        for(int k = 0; k < vertices.size(); k++){
            for(int i = 0; i < vertices.size(); i++){
                for(int j = 0; j < vertices.size(); j++){
                    //Es complicado de explicar, ya que se encuentra en la diapositiva, pero se usa la formula
                    //empleada, pero lo que se entiende es que se calculan las distancias entre los vertices utilizando
                    //los datos de la matriz
                    if(dist[i][k] != Double.POSITIVE_INFINITY && dist[k][j] != Double.POSITIVE_INFINITY){
                        if(dist[i][j] > dist[i][k] + dist[k][j]){
                            dist[i][j] = dist[i][k] + dist[k][j];
                        }
                    }
                }
            }
        }

        /**
         * Se recrea la matriz en forma de un hashmap que contiene un hashmap dentro,
         * que contiene la informacion de los vertices y su distancia minima.
         */
        HashMap<String,HashMap<String,Double>> matriz = new HashMap<>();
        for(int i = 0; i < vertices.size(); i++){
            String u = vertices.get(i);
            HashMap<String,Double> distancias = new HashMap();
            for(int j = 0; j < vertices.size(); j++){
                String destino = vertices.get(j);
                double distancia = dist[i][j];
                distancias.put(destino, distancia);
            }
            matriz.put(u,distancias);
        }
        return matriz;

    }
}
