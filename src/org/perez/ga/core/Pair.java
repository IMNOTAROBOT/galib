/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.perez.ga.core;

/**
 *
 * @author faguilarr
 */
public class Pair<K, V>
{
    public K first;
    public V second;
    
    public Pair()
    {
    }
    

    @Override
    public boolean equals(Object o)
    {
        if(o instanceof Pair) {
            Pair p = (Pair)o;
            return first.equals(p.first) && second.equals(p.second);
        }
        return false;
    }
    
    @Override
    public String toString()
    {
        return first + " " + second;
    }
}
