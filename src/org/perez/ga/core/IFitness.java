/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.perez.ga.core;

import org.perez.ga.core.Genotipo;

/**
 *
 * @author Fer
 */
public interface IFitness 
{
    public double evalua(Genotipo g);
}
