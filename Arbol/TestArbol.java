package Arbol;

import java.util.ArrayList;

public class TestArbol {
    public static void main(String[] args) {
        LectorDeArbol lector = new LectorDeArbol();
        ArbolBalanceado<Data> arbol = new ArbolBalanceado();

        lector.loadArbol("src/main/java/Copia de inventario_avl.csv",arbol);

        System.out.println(arbol.mostrarInorden());
        System.out.println(arbol.buscar(new Data(1099,new ArrayList<>())));
        System.out.println(arbol.eliminar(new Data(1099,new ArrayList<>())));
        System.out.println(arbol.mostrarInorden());
        System.out.println(arbol.mostrarPreorden());
        System.out.println(arbol.mostrarPostorden());
    }
}
