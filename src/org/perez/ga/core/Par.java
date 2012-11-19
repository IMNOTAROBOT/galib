package org.perez.ga.core;

/**
 * Clase auxiliar para la ruleta. Contiene un valor que tiene el
 * indice del lugar que ocupa en la poblacion un 
 * individuo y otro valor con su respectivo
 * fitness. Los arreglos de esta clase se orden
 * por medio del valor del fitness de manera descendente
 * @author Fer
 */
public class Par
    implements Comparable<Par>
{
    /**
     * Indice 
     */
    public int idx;
    
    /**
     * Valor del fitness
     */
    public double valor;

    /**
     * Compara dos objetos Par
     * @param o Objeto a comparar
     * @return La comparación de acuerdo al valor del fitness:
     *      0 si o y este objeto son iguales,
     *      1 si éste es menor que o
     *      -1 en otro caso
     */
    @Override
    public int compareTo(Par o) 
    {
        if(Math.abs(this.valor - o.valor)<0.000001)
            return 0;
        if(this.valor < o.valor)
            return 1;
        else 
            return -1;            
    }
    
    /**
     * Representación en cadena de un Par
     * @return String de la forma "(" + idx +", " +valor +")"
     */
    @Override
    public String toString()
    {
        return "(" + idx +", " +valor +")";
    }
}
