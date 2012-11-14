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
    
    ///** minim = 0, maxim = 1 */
    //private int mode = 0;
    //private Comparator<Genotipo> compMin;
    //private Comparator<Genotipo> compMax;
    
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
    
    
    double[] escalaPoblacion(Poblacion actual)
    {
        //Make F = sum fitness x_i (done)
        //for i=1 to n Select I(i) with probability PS_i (done)
        //its the probability
        double[] vals = new double[actual.getSize()];
        double prom = 0.0;
        double v;
        double minv = Double.MAX_VALUE;
        double F = 0.0;
        //3. Evaluate
        //   get the data for offset linear scaling
        for(int i=0; i<actual.getSize(); i++) {
            v = actual.getIndividuo(i).evalua(func);
            minv = Math.min(v, minv);
            prom += Math.abs(v);
            vals[i] = v;
        }
        prom /= this.M;
        best = actual.getBest(func);
        //   precalculating new fitness, F, probs
        for(int i=0; i<actual.getSize(); i++) {
            actual.getIndividuo(i).
                setFitnessValue(
                    offsetLinealScaling(vals[i], prom, minv));
            //TODO: Use the comparator
            F += actual.getIndividuo(i).getFitnessValue();
        }
        actual.sort(comp);
        for(int i=0; i<actual.getSize(); i++) { //for i=1 to n PS_i = f(x_i)/F (done)
            vals[i] = actual.getIndividuo(i).getFitnessValue();
            vals[i] /= F;
        }
        
        return vals;
    }

    public Genotipo TGA()
    {
        Poblacion actual;
        Poblacion nva;
        int k;
        double probs[];
        Genotipo res[];
        Genotipo g_x, g_y;
        
        k = 1; //0. Make k <- 1
        actual = new Poblacion(this.M,this.L, rnd); //1. Generate a random population       
        best = actual.getIndividuo(rnd.nextInt(this.M));//2. Select randomly and individual from the population
        System.out.println("G,Best,Fitness");
        for(; k<=this.G; k++) { //4. If k = G return best and stop(done)
            if(this.verboseLvl>0) {
                System.out.println(k +", " +func.getFenotipo(best) +", " +func.evalua(best)); }
            
            probs = escalaPoblacion(actual); //5. Selection
            //6. Crossover
            nva = new Poblacion(actual.getSize());
            nva.addIndividuo((Genotipo)best.clone());
            for(int i=1; i<this.M; i+=2) { //   for i=1 to n step 2
                g_x = actual.getIndividuo( this.escogeRuleta(probs) ); //Randomly select two individuals(i_x, i_y) with
                g_y = actual.getIndividuo( this.escogeRuleta(probs) ); //probabilities PS_x and PS_y, respect
                if(toss(this.P_c)) { //generate rand and if ro<=P_c do
                    res = this.crossover(g_x, g_y); //do crossover
                }
                else {
                    res = new Genotipo[2];
                    res[0] = (Genotipo)g_x.clone();
                    res[1] = (Genotipo)g_y.clone();
                }
                this.mutate(res[0]); //7. Mutation
                nva.addIndividuo(res[0]);
                if(i+1<this.M) {
                    this.mutate(res[1]); //7. Mutation
                    nva.addIndividuo(res[1]);
                }
            }
            actual = nva;
        }
        
        return best;
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
        //for j=1 to L
        for(int i=0; i<tam; i++) {
            //Generate a rand 0<=ro<1
            if(toss(this.P_m)) { //if ro<=P_m make bit_j = !bit_j 
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
