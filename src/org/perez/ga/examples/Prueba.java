package org.perez.ga.examples;

import org.perez.ga.core.Genotipo;
import org.perez.ga.core.IFitness;
import org.perez.ga.core.Number;

public class Prueba 
    implements IFitness
{
    @Override
    public double evalua(Genotipo g) 
    {
        double x = new Numero(g).num;
        
        return -x*x +6*x + 9;
    }

    @Override
    public Object getFenotipo(Genotipo g) 
    {
        return new Numero(g);
    }
}


class Numero
{
    double num;
    
    Numero(Genotipo g)    
    {
        num = Number.getNumber(g, 3, 6);
    }
    
    @Override
    public String toString()
    {
        return Double.toString(num);
    }
}
