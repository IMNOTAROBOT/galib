/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.perez.ga.examples;

import org.perez.ga.core.Genotipo;
import org.perez.ga.core.IFitness;
import org.perez.ga.core.Number;

/**
 *
 * @author sdist
 */
public class Tarea 
    implements IFitness
{
     /**
     * Función a optimizar
     * @param x X
     * @param y Y
     * @return  6*x + 9*y;
     */
    public static double f(double x, double y)
    {
        return 6*x + 9*y;
    }
    
    /**
     * Restrición 1 (2*x + 3*y <= 12.0)
     * @param x X
     * @param y Y
     * @return true si 2*x + 3*y <= 12.0
     */
    public static boolean r1(double x, double y)
    {
        return 2*x + 3*y <= 12.0;
    }

    /**
     * Restricción 2 (4*x + y <= 8.0)
     * @param x X
     * @param y Y
     * @return true si 4*x + y <= 8.0
     */
    public static boolean r2(double x, double y)
    {
        return 4*x + y <= 8.0;
    }
    
    /**
     * Restricción 3 (x>=0.0)
     * @param x X
     * @return true si x>=0.0
     */
    public static boolean r3(double x)
    {
        return x>=0.0;
    }
    
    
    @Override
    public double evalua(Genotipo g) 
    {
        double x = Number.getNumber(g, 5, 24);
        double y = Number.getNumber(g, 5, 24, 30);
        int r = 0;
        if(r1(x, y))
            r++;
        if(r2(x, y))
            r++;
        if(r3(x))
            r++;
        if(r3(y))
            r++;
        
        if(r==4)
            return f(x, y);
        
        return -1000000 + 1.0/4.0*(double)r*1000000;
    }    
    
    @Override
    public Object getFenotipo(Genotipo g)
    {
        Punto2D v = new Punto2D();
        v.x = Number.getNumber(g, 5, 24);
        v.y = Number.getNumber(g, 5, 24, 30);
        return v;
    }
}

class Punto2D
{
    double x, y;
    
    @Override
    public String toString()
    {
        return "(" + x +", " +y +")";
    }
}