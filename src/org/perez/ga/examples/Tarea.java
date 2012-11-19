package org.perez.ga.examples;

import org.perez.ga.core.Genotipo;
import org.perez.ga.core.IFitness;
import org.perez.ga.core.Number;

/**
 * @author sdist
 */
public class Tarea 
    implements IFitness
{   
    @Override
    public double evalua(Genotipo g) 
    {
        Punto2D v = new Punto2D(g);
        double x = v.x;
        double y = v.y;
        int r = 0;
        if(2*x + 3*y <= 12.0)
            r++;
        if(4*x + y <= 8.0)
            r++;
        if(x>=0.0)
            r++;
        if(y>=0.0)
            r++;
        
        if(r==4)
            return 6*x + 9*y;
        
        return -1000000 + 1.0/4.0*(double)r*1000000;
    }    
    
    @Override
    public Object getFenotipo(Genotipo g)
    {
        return new Punto2D(g);
    }
}

class Punto2D
{
    double x, y;
    
    public Punto2D(Genotipo g)
    {
        x = Number.getNumber(g, 5, 24);
        y = Number.getNumber(g, 5, 24, 30);        
    }
    
    @Override
    public String toString()
    {
        return "(" + x +", " +y +")";
    }
}