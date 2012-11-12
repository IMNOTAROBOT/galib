/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.perez.ga.algorithms;

import java.util.Random;
import java.util.Comparator;
import org.perez.ga.core.Genotipo;
import org.perez.ga.core.IFitness;
import org.perez.ga.core.Poblacion;

/**
 *
 * @author Fer
 */
public class CHC 
{
    /** population size */
    int M;
    /** string length */
    int L;
    /** generation */
    int t;
    /** difference threshold */
    double d;
    /** divergence rate */
    double r;
    /** parent population P(t-1) */
    Poblacion parent;
    /** actual population P(t) */
    Poblacion actual;
    /** candidate population C(t) */
    Poblacion candidate;
    /** child population C'(t) */
    Poblacion child;
    /** runnings */
    int N;

    Random rnd;
    IFitness func;
    /** minim = 0, maxim = 1 */
    int mode = 0;
    Comparator<Genotipo> compMin;
    Comparator<Genotipo> compMax;
    
    
    void CHC()
    {
        t = 0;
        d = (double)L/4;
        //initalize P(t);
        rnd = new Random();
        parent = new Poblacion(M, L, rnd);
        compMin = this.getMinComparator();
        compMax = this.getMaxComparator();
        //evaluate structures in P(t); --on the go
        while(t<N) { //while termination condition not satisfied
            t = t + 1;
            candidate = select_r(parent);
            child = recombine(candidate);
            //evaluate structures in C'(t)
            //select_s P(t) from C'(t) and P(t-1)
            if(actual.equals(parent)) {
                d = d - 1;
                if(d<0) {
                    //diverge P(t)
                    d = r*(1-r)*L;
                }
            }
        }
    }
        
    /**
     * Selection for recombination
     * @param from
     * @return 
     */
    Poblacion select_r(Poblacion from)
    {
        Poblacion ret = (Poblacion)from.clone();
        ret.shuffle(rnd);
        
        return ret;
    }
    
    //TODO: cuando es impar?
    Poblacion recombine(Poblacion p)
    {
        Poblacion ret = new Poblacion(p.getSize());
        if(p.getSize()%2!=0) {
            System.err.println("El tamaño de la poblacion debe ser par");
            System.exit(1);
        }
        int ham;
        Genotipo uno, dos;
        for(int i=0; i<p.getSize(); i += 2) {
            uno = p.getIndividuo(i);
            dos = p.getIndividuo(i+1);
            ham = hamming(uno, dos);
            if((double)ham/2 > d) {
                //swap half the differing bits at random
                int j = 0;
                int tam = uno.getSize();
                ham /= 2;
                while(ham>0) {
                    for(;j<tam; j++) {
                        if(uno.getGen(j)!=dos.getGen(j))  {
                            break;
                        }
                    }
                    if(rnd.nextBoolean()) { //swap
                        boolean temp = uno.getGen(j);
                        uno.setGen( j, dos.getGen(j));
                        dos.setGen( j, temp);
                    }
                    j++;
                    ham--;
                }
                //TODO: A quien agrego?
            }
            else {
                //delete the pair of structuraes from C(t)
                
            }
        }
                
        return ret;
    }

    /**
     * Calculates the Hamming distance between 2 individuals
     * @param a
     * @param b
     * @return 
     */
    int hamming(Genotipo a, Genotipo b)
    {
        if(a.getSize()!=b.getSize()) {
            System.err.println("Los genotipos deben ser de igual longitud");
            System.exit(1);
        }
            
        int ret = 0;
        int tam = a.getSize();
        for(int i=0; i<tam; i++) {
            if(a.getGen(i)!=b.getGen(i)) {
                ret++;
            }
        }
        
        return ret;
    }
    
    /**
     * Selection for survival
     * @param par
     * @param child
     * @return Una nueva Poblacion
     */
    Poblacion select_s(Poblacion par, Poblacion child, Comparator<Genotipo> comp)
    {
        Poblacion ret = new Poblacion(par.getSize());
        par.sort(comp);
        child.sort(comp);
        int ic = 0, ip = 0;
        for(int i=0; i<M; i++) {
            if(comp.compare(par.getIndividuo(ip), child.getIndividuo(ic)) <= 0) {
                ret.addIndividuo(par.getIndividuo(ip));
                ip++;
            }
            else {
                ret.addIndividuo(child.getIndividuo(ic));
                ic++;
            }
        }
        
        return ret;
    }
    
    Comparator<Genotipo> getMinComparator()
    {
        return new Comparator<Genotipo>() 
        {
            @Override
            public int compare(Genotipo o1, Genotipo o2) {
                double r1 = o1.evalua(func);
                double r2 = o2.evalua(func);
                double EPS = 0.000001;
                if(Math.abs(r1-r2)<=EPS) {
                    return 0;
                }
                if(r1 < r2) {
                    return -1;
                }

                return 1;
            }         
        };
    }
    
    Comparator<Genotipo> getMaxComparator()
    {
        return new Comparator<Genotipo>() 
        {
            @Override
            public int compare(Genotipo o1, Genotipo o2) {
                double r1 = o1.evalua(func);
                double r2 = o2.evalua(func);
                double EPS = 0.000001;
                if(Math.abs(r1-r2)<=EPS) {
                    return 0;
                }
                if(r1 < r2) {
                    return 1;
                }

                return -1;
            }         
        };
    }
}
