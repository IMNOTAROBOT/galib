package org.perez.ga.core;

import org.perez.ga.core.Genotipo;

/**
 * @author Fer
 */
public interface IFitness 
{
    public double evalua(Genotipo g);
    
    public Object getFenotipo(Genotipo g);
}
