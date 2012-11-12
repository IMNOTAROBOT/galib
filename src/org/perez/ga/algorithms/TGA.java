/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.perez.ga.algorithms;

import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;
import java.util.Properties;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import org.perez.ga.core.Genotipo;
import org.perez.ga.core.IFitness;
import org.perez.ga.core.Poblacion;

/**
 * Elitist Canonical Genetic Algorithm
 * - Linear offset scaling
 * 
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
    
    private int verboseLvl;
    
    ///** minim = 0, maxim = 1 */
    //private int mode = 0;
    //private Comparator<Genotipo> compMin;
    //private Comparator<Genotipo> compMax;
    
    public TGA(File f)
    {
        
        Scanner jin = null;
        Properties p = null;
        try {
            jin = new Scanner(f);
            p = new Properties();
            p.load(new FileInputStream(f.getAbsoluteFile()));
            P_c = Double.valueOf(p.getProperty("P_c"));
            P_m = Double.valueOf(p.getProperty("P_m"));
            Integer.valueOf(p.getProperty("M"));
            M = Integer.valueOf(p.getProperty("M"));
            G = Integer.valueOf(p.getProperty("G"));
            L = Integer.valueOf(p.getProperty("L"));
            func = (IFitness)Class.forName(p.getProperty("Func")).newInstance();
            if(p.containsKey("Verbose")) {
                this.verboseLvl = Integer.valueOf(p.getProperty("Verbose"));}
            else {
                this.verboseLvl = 0;}
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
        Genotipo best;
        int k;
        double F;
        double vals[] = new double[this.M];
        double probs[] = new double[this.M];
        
        //0. Make k <- 1
        k = 1;
        
        //1. Generate a random population
        actual = new Poblacion(this.M,this.L, rnd);
        
        //2. Select randomly and individual from the population
        best = actual.getIndividuo(rnd.nextInt(this.M));
        
        for(; k<this.G; k++) {
            //3. Evaluate
            //   get the data for offset linear scaling
            double prom = 0;
            double v;
            double minv = Double.MAX_VALUE;
            for(int i=0; i<this.M; i++) {
                v = actual.getIndividuo(i).evalua(func);
                minv = Math.min(v, minv);
                prom += Math.abs(v);
                vals[i] = v;
            }
            prom /= this.M;
            minv = Math.abs(minv);
            //   precalculating new fitness, F, probs
            double minv2 = Double.MAX_VALUE;
            F = 0;
            for(int i=0; i<this.M; i++) {
                actual.getIndividuo(i).
                        setFitnessValue(
                        offsetLinealScaling(vals[i], prom, minv));
                //TODO: Use the comparator
                if(actual.getIndividuo(i).getFitnessValue() < minv2) {
                    best = actual.getIndividuo(i);
                    minv2 = best.getFitnessValue();
                }
                F += actual.getIndividuo(i).getFitnessValue();
                probs[i] = actual.getIndividuo(i).getFitnessValue();
            }
            for(int i=0; i<this.M; i++) {
                probs[i] /= F;
            }

            //4. If k = G return best and stop(done)

            //5. Selection
            //Make F = sum fitness x_i (done)
            //for i=1 to n PS_i = f(x_i)/F (done)
            //for i=1 to n Select I(i) with probability PS_i (done)
            //its the probability

            //6. Crossover
            //   for i=1 to n step 2
            int i_x, i_y;
            Genotipo g_x, g_y;
            double ro;
            for(int i=0; i<this.M; i+=2) {
                //Randomly select two individuals(i_x, i_y) with
                //probabilities PS_x and PS_y, respect
                i_x = select_roulette(probs, F);
                i_y = select_roulette(probs, F);
                g_x = actual.getIndividuo(i_x);
                g_y = actual.getIndividuo(i_y);
                //generate a random number 0<=ro<1
                //if ro<=P_c do
                if(toss(this.P_c)) {
                    //do crossover
                    Genotipo res[] = this.crossover(g_x, g_y);
                    actual.setIndividuo(i, res[0]);
                    if(i+1 < this.M) { //por los impares
                        actual.setIndividuo(i+1, res[1]);
                    }
                }
            }

            //7. Mutation
            //   for i=1 to n
            for(int i=0; i<this.M; i++) {
                //Select I(i)
                // do muattion
                this.mutate(actual.getIndividuo(i));
            }
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
     * @param sum The sum of the probabilites
     * @return and index corresponding to the individual
     * select via roulette
     */
    int select_roulette(double probs[], double sum)
    {
        double acum = 0;
        int res = 0;
        double tope = rnd.nextDouble()*sum;
        while(res<probs.length && acum < tope) {
            acum += probs[res++];
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
        return v + Math.abs(mean) + Math.abs(min);
    }

}
