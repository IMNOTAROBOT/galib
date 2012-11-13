package org.perez.ga.core;

import org.perez.ga.core.IFitness;
import java.util.Comparator;

/**
 * @author dominoFire
 */
public class GenotipoComparator 
    implements Comparator<Genotipo>
{    
    private Mode mode;
    private IFitness func;
    
    public GenotipoComparator(IFitness func)
    {
        this.mode = Mode.Maximize;
        this.func = func;
    }
    
    public GenotipoComparator(IFitness func, Mode mode) 
    {
        this.mode = mode;
        this.func = func;
    }
        
    @Override
    public int compare(Genotipo o1, Genotipo o2) 
    {
        int m = 1;
        if(mode==Mode.Maximize)  {
            m = -1;
        }
        
        double r1 = o1.evalua(func);
        double r2 = o2.evalua(func);
        double EPS = 0.000001;
        if(Math.abs(r1-r2)<=EPS) {
            return 0;
        }
        if(r1 < r2) {
            return 1 * m;
        }

        return -1 * m;
    }
}
