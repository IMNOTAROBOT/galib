/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.perez.ga.algorithms;

import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.perez.ga.core.Genotipo;
import org.perez.ga.core.Poblacion;

/**
 *
 * @author Fer
 */
public class TGATest 
{
    TGA algo;
    
    public TGATest() 
    {
        algo = new TGA(new File("test.tga"));
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

        /**
     * Test of escogeRuleta method, of class TGA.
     */
    @Test
    public void testEscogeRuleta() {
        System.out.println("escogeRuleta");
        double[] probs = null;
        TGA instance = null;
        int expResult = 0;
        int result = instance.escogeRuleta(probs);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
    @Test
    public void testGeneraRuleta()
    {
        System.out.println("escalaPoblacion");
        Poblacion p = new Poblacion(10, 10);
        
        p.setIndividuo(0, new Genotipo("1111111111"));
        p.setIndividuo(1, new Genotipo("0000011111"));
        System.out.println(p);
        algo.generaRuleta(p);
        System.out.println(p);
    }
    
    /**
     * Test of escalaPoblacion method, of class TGA.
     */
    @Test
    public void testEscalaPoblacion() {
        System.out.println("escalaPoblacion");
        Poblacion p = new Poblacion(10, 10);
        
        p.setIndividuo(0, new Genotipo("1111111111"));
        p.setIndividuo(1, new Genotipo("0000011111"));
        System.out.println(p);
        algo.escalaPoblacion(p);
        System.out.println(p);
    }

    /**
     * Test of toss method, of class TGA.
     */
    @Test
    public void testToss() {
        System.out.println("toss");
        int s = 0;
        for(int i=0; i<10000; i++)
            if(algo.toss(0.5))
                s++;
        assertEquals(0.5, (double)s/10000, 0.500);
    }

    /**
     * Test of mutate method, of class TGA.
     */
    @Test
    public void testMutate() {
        System.out.println("mutate");
        Genotipo g = new Genotipo("0000000000");
        System.out.println(g);
        algo.mutate(g);
        System.out.println(g);
        algo.mutate(g);
        System.out.println(g);
        algo.mutate(g);
        System.out.println(g);
        algo.mutate(g);
        System.out.println(g);
    }

    /**
     * Test of crossover method, of class TGA.
     */
    @Test
    public void testCrossover() 
    {
        Genotipo a, b, result[];
        System.out.println("crossover");
        a = new Genotipo("0000000000");
        b = new Genotipo("1111111111");
        result = algo.crossover(a, b);
        System.out.println(result[0]);
        System.out.println(result[1]);
        result = algo.crossover(a, b);
        System.out.println(result[0]);
        System.out.println(result[1]);
        result = algo.crossover(a, b);
        System.out.println(result[0]);
        System.out.println(result[1]);
    }
}
