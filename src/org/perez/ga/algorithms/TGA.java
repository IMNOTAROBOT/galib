/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.perez.ga.algorithms;

import java.util.Comparator;
import java.util.Random;
import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import org.perez.ga.core.Genotipo;
import org.perez.ga.core.GenotipoComparator;
import org.perez.ga.core.IFitness;
import org.perez.ga.core.Mode;
import org.perez.ga.core.Poblacion;

/**
 * Elitist Canonical Genetic Algorithm
 * - Linear offset scaling
 * - Sorted roulette selection
 * @author Fer
 */
public class TGA 
{
    /** Crossver probability */
    private double P_c;    
    /** Mutation probability */
    private double P_m;
    /** Population size */
    private int M;
    /** Number of generations */
    private int G;
    /** String (Genotipe) length */
    private int L;
    
    private Random rnd;
    private IFitness func;
    private GenotipoComparator comp;
    
    private int verboseLvl;
    private Genotipo best;
        
    public TGA(File f)
    {
        Properties p = null;
        try {
            p = new Properties();
            p.load(new FileInputStream(f.getAbsoluteFile()));
            P_c = Double.valueOf(p.getProperty("P_c"));
            P_m = Double.valueOf(p.getProperty("P_m"));
            M = Integer.valueOf(p.getProperty("M"));
            G = Integer.valueOf(p.getProperty("G"));
            L = Integer.valueOf(p.getProperty("L"));
            func = (IFitness)Class.forName(p.getProperty("Func")).newInstance();
            comp = new GenotipoComparator(func, Mode.valueOf(p.getProperty("Mode")));
            if(p.containsKey("Verbose")) {
                this.verboseLvl = Integer.valueOf(p.getProperty("Verbose"));}
            if(p.containsKey("Seed")) {
                this.rnd = new Random(Integer.valueOf(p.getProperty("Seed")));} 
            else {
                rnd = new Random();}
        } catch(Exception e) {
            System.err.println("Error al leer los parametros TGA");
            System.err.println(e);
            System.exit(1);
        }
    }    

    public Genotipo TGA()
    {
        Poblacion actual;
        Poblacion nva;
        double probs[];
        Genotipo res[] = new Genotipo[2];
        Genotipo g_x, g_y;
        
        
        actual = new Poblacion(M, L, rnd); //1. Generate a random population       
        //best = actual.getBest(func);
        best = actual.getIndividuo(rnd.nextInt(M));//2. Select randomly and individual from the population
        System.out.println("G,Best,Fitness");
        //0. Make k <- 1
        for(int k = 1; k<=G; k++) { //4. If k = G return best and stop(done)
            if(this.verboseLvl>0) {
                System.out.println(k +", " +func.getFenotipo(best) +", " +func.evalua(best)); }
            
            probs = escalaPoblacion(actual); //5. Selection
            //6. Crossover
            nva = new Poblacion(actual.getSize());
            int hechos = 0;
            while(hechos < M) { //   for i=1 to n step 2
                g_x = actual.getIndividuo( this.escogeRuleta(probs) ); //Randomly select two individuals(i_x, i_y) with
                g_y = actual.getIndividuo( this.escogeRuleta(probs) ); //probabilities PS_x and PS_y, respect
                if(toss(P_c)) { //generate rand and if ro<=P_c do
                    res = this.crossover(g_x, g_y); //do crossover
                }
                else {
                    res[0] = (Genotipo)g_x.clone();
                    res[1] = (Genotipo)g_y.clone();
                }
                this.mutate(res[0]); //7. Mutation
                nva.addIndividuo(res[0]);
                hechos++;
                if(hechos<M) {
                    this.mutate(res[1]); //7. Mutation
                    nva.addIndividuo(res[1]);
                    hechos++;
                }
            }
            Genotipo b = actual.getBest(func);
            actual = nva;
            if( func.evalua(best) < func.evalua(b) ) //max
                best = b;

            int idx = 0;
            b = actual.getIndividuo(0);
            for(int i=0; i<actual.getSize(); i++) {
                if(b.evalua(func) > actual.getIndividuo(i).evalua(func)) {
                    b = actual.getIndividuo(i);
                    idx = i;
                }
            }
            actual.setIndividuo(idx, b);
        }
        
        return best;
    }
    
    
    
    double[] escalaPoblacion(Poblacion p)
    {
        //Make F = sum fitness x_i (done)
        //for i=1 to n Select I(i) with probability PS_i (done)
        //its the probability
        int tam = p.getSize();
        double[] vals = new double[tam];
        double prom = 0.0;
        double v;
        double minv = Double.MAX_VALUE;
        double F = 0.0;
        //3. Evaluate
        for(int i=0; i<tam; i++) {
            v = p.getIndividuo(i).evalua(func);
            //   get the data for offset linear scaling
            minv = Math.min(v, minv);
            prom += Math.abs(v);
            vals[i] = v;
        }
        prom /= this.M;
        //   F_i = Phi(i), F, probs
        for(int i=0; i<tam; i++) {
            v = vals[i] + prom + minv;
            p.getIndividuo(i).setFitnessValue(v);
            F += v;
        }
        p.sort(comp); //desc for max, asc for min
        for(int i=0; i<tam; i++) { //for i=1 to n PS_i = f(x_i)/F (done)
            vals[i] = p.getIndividuo(i).getFitnessValue();
            vals[i] /= F;
        }
        
        return vals;
    }
    
    /**
     * Executes a toss
     * @param p Probability of HEAD
     * @return True if HEAD, false else
     */
    protected boolean toss(double p)
    {
        return rnd.nextDouble() <= p;
    }
    
    /**
     * CAMBIA al genotipo dado como parametro
     * @param g El Genotipo a mutar
     */
    protected void mutate(Genotipo g)
    {
        int tam = g.getSize();
        for(int i=0; i<tam; i++) { //for j=1 to L
            if(toss(this.P_m)) { //if rand ro<=P_m make bit_j = !bit_j 
                g.setGen(i, !g.getGen(i));
            }
        }
    }
    
    /**
     * Given a vector or probabilities, select
     * proportionalitty a index
     * @param probs An array with the probabilites
     * @return and index corresponding to the individual
     * select via roulette
     */
    int escogeRuleta(double probs[])
    {
        double acum = 0;
        int res = 0;
        double tope = rnd.nextDouble();
        while(res<probs.length && acum < tope) {
            acum += probs[res];
            res++;
        }
        if(res==probs.length)
            res--;
        return res;
    }
    
    /**
     * Generate two new individuals with 1 point 
     * crossover (H1X)
     * @param a First parent
     * @param b Second parent
     * @return And array with 2 elements with the 
     * parents gen
     */
    protected Genotipo[] crossover(Genotipo a, Genotipo b)
    {
        Genotipo arr[] = new Genotipo[2];
        arr[0] = new Genotipo(this.L);
        arr[1] = new Genotipo(this.L);
        //Randomly select a locus l(pto) of the chromosome
        int pto = (int)(rnd.nextDouble()*this.L);
        //System.out.println("Corte: " +pto);
        int i;
        //Pick the leftmost L-l bits of I(x) and the rightmost l bits of I(Y)
        //and concatenate them to form the new chromosome of I(X)
        for(i=0; i<pto; i++) {
            arr[0].setGen(i, b.getGen(i));
            arr[1].setGen(i, a.getGen(i));
        }
        for(; i<this.L; i++) {
            arr[0].setGen(i, a.getGen(i));
            arr[1].setGen(i, b.getGen(i));
        }
        
        return arr;
    }
    
    /**
     * Calculates the Offset linear scaling given by
     * Phi = f(x_i) + |mean| +|min|
     * @param v The actual value
     * @param mean Mean of the fitness function of all indiviuals
     * @param min Min value of the fitness function of all indiviuals
     * @return A double with Phi
     */
    private double offsetLinealScaling(double v, double mean, double min)
    {
        return v + mean+ Math.abs(min);
    }

}
