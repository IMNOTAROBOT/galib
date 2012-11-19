package org.perez.ga.core;

import java.util.Random;
import java.math.BigInteger;

/**
 * Represnta un individuo utilizado en un 
 * Algoritmo Genético Simple
 * @author Fer
 */
public class Genotipo
    implements Cloneable
{
    /**
     * Tamaño en bits de un entero
     */
    private static final int BITSM = 32;
    
    /**
    * Cada bit del entero es un gen del individuo
    */
    private int genes[];
    
    /**
     * Número de genes del genotipo
     */
    private int numGenes;
    
    /**
     * Medida de bondad
     */
    private double fitness;
    
    public Genotipo(String gen)
    {
        this(gen.length());
        char v;
        for(int i=0; i<gen.length(); i++) {
            v = gen.charAt(i);
            if(v=='1') {
                this.setGen(i, true);
            }
            else if(v=='0') {
                this.setGen(i, false);
            }
        }
    }
    
    /**
     * Constructor default
     */
    public Genotipo(int numGenes)
    {
        if(numGenes<=0) {
            System.err.println("Error: El numero de genes debe ser mayor a 0");
            System.exit(1);
        }
        if(numGenes%BITSM==0) {
            this.genes = new int[numGenes/BITSM];
        }
        else {
            this.genes = new int[numGenes/BITSM + 1];
        }
        
        this.numGenes = numGenes;
        this.fitness = Double.NaN; //TODO: Choose a better value
    }
    
    /**
     * Constructor. Con posibilidad de generar
     * un genotipo de manera aleatoriamente
     * @param rnd Objeto random con el que se generan
     * los bits aleatorios. Use null si no quiere
     * inicializar el genotipo
     */
    public Genotipo(int numGenes, Random rnd)
    {
        this(numGenes);
        if(rnd!=null) {
            for(int i=0; i<this.numGenes; i++) {
                this.setGen(i, rnd.nextBoolean());
            }
        }
    }
    
    /**
     * Obtiene un bit del genoma
     * @param pos Posición del bit a obtener en el 
     *      rango [0, numGenes-1]
     * @return true si el bit=1 o false si bit=0
     */
    public boolean getGen(int pos)
    {
        if(pos<0 || pos>=numGenes) {
            System.out.println("Error en Genotipo: Indice fuera de los limites");
            System.exit(1);
        }
        
        return this.getGen(pos/BITSM, pos%BITSM);        
    }
    
    /**
     * Establece un bit en el genoma
     * @param pos Posición del bit a establecer en el 
     *      rango [0, 59]
     * @param valor true si el bit=1 o false si bit=0
     */
    public void setGen(int pos, boolean valor)
    {
        if(pos<0 || pos>=numGenes) {
            System.out.println("Error en Genotipo: Indice fuera de los limites");
            System.exit(1);
        }
        this.setGen(pos/BITSM, pos%BITSM, valor);
    }
    
    /**
    * @param ind Indice del arreglo
    * @param pos Posicion a obtener desde el bit
    *       mas significativo
    * @param valor True=1 o False=0 para el bit
    */
    private void setGen(int ind, int pos, boolean valor)
    {
        int mask;
        if(valor) {
            mask = 1 << (BITSM - 1 - pos);
            genes[ind] = genes[ind] | mask;
        }
        else {
            mask = 1 << (BITSM - 1 - pos);
            mask = ~mask; //deberia funcionar
            genes[ind] = genes[ind] & mask;
        }
    }
    
    /**
    * @param ind 0 para x, 1, para y
    * @param pos Posicion a obtener desde el bit
    *       mas significativo
    * [0] = signo
    * [1, 5] = entero
    * [6, 29] = decimal
    */
    private boolean getGen(int ind, int pos)
    {
        int mask = 1 << (BITSM - 1 - pos);
        
        return (genes[ind] & mask)!=0;
    }
    
    /**
     * Obtiene el tamaño del genotipo
     * @return el tamaño del genotipo
     */
    public int getSize()
    {
        return numGenes;
    }
    
    /**
     * Representación en cadena de un Genotipo
     * @return String de la forma "10101001.."
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<numGenes;i++) {
            sb.append(getGen(i)? '1':'0');
        }
        sb.append(" f=").append(fitness);
        sb.append(" b=").append(this.numGenes);
                
        return sb.toString();
    }
        
    @Override
    public Object clone()
    {
        try {
            return super.clone();
        } catch (Exception ex) {
            System.err.println("ERROR al clonar: ");
            System.err.println(ex);
            System.exit(1);
            return null;
        }
    }
    
    @Override
    public boolean equals(Object ob)
    {
        if(ob instanceof Genotipo) {
            Genotipo g = (Genotipo)ob;
            if(g.numGenes!=this.numGenes) {
                return false;
            }
            for(int i=0; i<this.numGenes; i++) {
                if(this.getGen(i)!=g.getGen(i)) {
                    return false;
                }
            }   
            return true;
        }
        
        return false;
    }
    
    @Override
    public int hashCode()
    {
        String s = this.toString();
        BigInteger bi = new BigInteger(s, 2);

        return bi.hashCode();
    }
    
    public double getFitness(IFitness f)
    {
        return f.evalua(this);
    }
    
    public void setFitnessValue(double value)
    {
        this.fitness = value;
    }
    
    public double getFitnessValue()
    {
        return this.fitness;
    }
}