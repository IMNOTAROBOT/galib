package org.perez.ga.core;

/**
 *
 * @author sdist
 */
public class Number 
{
    /**
     * Fixed point interpretation, left to right
     * @param g Bits source
     * @param ini Initial pos [0, g.size() - 1]
     * @param fin Final pos [0, g.size() - 1]
     * @param ent Number of integers
     * @param dec Number of decimals
     * @return A number gith te interpretation
     */
    public static double getNumber(Genotipo g, int ent, int dec, int offset)
    {
        if(g==null) {
            System.err.println("g no puede ser nulo");
            System.exit(1);
        }
        if(offset>=g.getSize()) {
            System.err.println("Cheque ini");
            System.exit(1);
        }
        if(ent<0 || dec<0 || (ent+dec+1)>(g.getSize()-offset)) {
            System.err.println("Cheque ent y dec");
            System.exit(1);
        }
        
        int sig = 1;
        if(g.getGen(offset)) {
            sig = -1;
        }
        //horner rule
        int pent = 0;
        for(int i=offset+1; i<offset+1+ent; i++) {
            pent = pent*2 + (g.getGen(i)? 1: 0);
        }
        double pdec = 0.0;
        double dos = 0.5;
        for(int i=offset+1+ent; i<offset+1+ent+dec; i++) {
            pdec += (g.getGen(i)? dos: 0);
            dos /= 2.0;
        }
        
        return sig*(pent + pdec);
    }
    
    public static double getNumber(Genotipo g, int ent, int dec)
    {
        return getNumber(g, ent, dec, 0);
    }
    
    public static double getNumber(Genotipo g)
    {
        return getNumber(g,g.getSize()-1, 0, 0);
    }
}
