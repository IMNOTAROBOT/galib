/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.perez.ga.core;

import java.util.Random;
import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author Fer
 */
public class Poblacion 
    implements Cloneable
{
    /**
     * Arreglo con los individuos (Genotipos) de la poblacion
     */
    //private Genotipo[] individuos;
    private ArrayList<Genotipo> indi;

    /**
     * Inicializa el arreglo
     * @param tam 
     */
    public Poblacion(int tam)
    {
        //this.individuos = new Genotipo[tam];
        this.indi = new ArrayList<Genotipo>(tam);
    }
    
    /**
     * Inicializa en ceros
     * @param tam
     * @param numGenes 
     */
    public Poblacion(int tam, int numGenes)
    {
        this(tam);
        for(int i=0; i<tam; i++) {
            //this.individuos[i] = new Genotipo(numGenes);
            this.indi.add(new Genotipo(numGenes));
        }
    }
    
    /**
     * PUede inicializar con Random
     * @param tam
     * @param numGenes
     * @param rnd 
     */
    public Poblacion(int tam, int numGenes, Random rnd)
    {
        this(tam);
        for(int i=0; i<tam; i++) {
            //this.individuos[i] = new Genotipo(numGenes, rnd);
            this.indi.add(new Genotipo(numGenes, rnd));
        }
    }
    
        /**
     * Obtiene un individuo de la Población.
     * @param idx Posición del individuo dentro del arreglo
     * @return El individuo en la posición idx
     */
    public Genotipo getIndividuo(int idx)
    {
        //return this.individuos[idx];
        return this.indi.get(idx);
    }
    
    /**
     * Establece un individuo de la Población.
     * @param idx Posición del individuo dentro del arregl
     * @param v El individuo a ingresar
     */
    public void setIndividuo(int idx, Genotipo v)
    {
        this.indi.set(idx, v);
        //this.individuos[idx] = v;
    }
    
    /**
     * Obtiene el número de indiviudos de la Población,
     * determinado por el tamaño del arreglo.
     * @return El tamaño de la Población
     */
    public int getSize()
    {
        //return individuos.length;
        return this.indi.size();
    }
    
    public void sort(Comparator<Genotipo> comp)
    {
        Collections.sort(this.indi, comp);
    }
    
    public void shuffle(Random rnd)
    {
        Collections.shuffle(this.indi, rnd);
        /*
        //int tam = this.individuos.length;
        int tam = this.indi.size();
        int cubeta[] = new int[tam];
        int pos;
        int i = 0;
        Genotipo temp;
        while(i<tam) {
            do {
                pos = rnd.nextInt(tam);
            } while(cubeta[pos]!=0);
            cubeta[pos] = 1;
            //temp = this.individuos[i];
            temp = this.indi.get(i);
            //this.individuos[i] = this.individuos[pos];
            this.indi.set(i, this.indi.get(pos));
            //this.individuos[pos] = temp;
            this.indi.set(pos, temp);
            i++;
        }
        */
    }
    
    /**
     * Gets the individual with the maximum fitness
     * @param f
     * @return 
     */
    public Genotipo getBest(IFitness f)
    {
        Genotipo b = this.indi.get(0);
        for(int i=0; i<this.indi.size(); i++) {
            if(b.getFitness(f) < indi.get(i).getFitness(f)) {
                b = indi.get(i);
            }
        }
        return b;
    }
    
    public Genotipo getWorst(IFitness f)
    {
        Genotipo b = this.indi.get(0);
        for(int i=0; i<this.indi.size(); i++) {
            if(b.getFitness(f) > indi.get(i).getFitness(f)) {
                b = indi.get(i);
            }
        }
        return b;
    }
    
    public void addIndividuo(Genotipo g)
    {
        this.indi.add(g);
    }
    
    @Override
    public boolean equals(Object ob)
    {
        if(ob instanceof Poblacion) {
            Poblacion p = (Poblacion)ob;
            //if(this.individuos.length != p.individuos.length) {
            if(this.indi.size()!=p.indi.size()) {
                return false;
            }
            //int n = this.individuos.length;
            int n = this.indi.size();
            for(int i=0; i<n; i++) {
                //if(!this.individuos[i].equals(p.individuos[i])) {
                if(!this.indi.get(i).equals(p.indi.get(i))) {
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
        //return this.individuos.hashCode();
        return this.indi.hashCode();
    }
    
    
    @Override
    public Object clone()
    {
        //int n = this.individuos.length;
        int n = this.indi.size();
        Poblacion p = new Poblacion(n);
        for(int i=0; i<n; i++) {
            //p.individuos[i] = (Genotipo)this.individuos[i].clone();
            p.indi.add((Genotipo)this.indi.get(i).clone());
        }
        
        return p;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        //int n = this.individuos.length;
        int n = this.indi.size();
        for(int i=0; i<n; i++) {
            sb.append(this.indi.get(i)).append('\n');
            //sb.append(this.individuos[i]).append('\n');
        }
        return sb.toString();
    }
}
