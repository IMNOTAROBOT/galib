/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.perez.ga.core;

import org.perez.ga.core.Poblacion;
import org.perez.ga.core.Genotipo;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Random;

/**
 *
 * @author Fer
 */
public class PoblacionTest {
    
    public PoblacionTest() {
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
     * Test of getIndividuo method, of class Poblacion.
     */
    @Test
    public void testGetIndividuo() {
        System.out.println("getIndividuo");
        Poblacion instance = new Poblacion(25, 5);
        Genotipo result = instance.getIndividuo(0);
        assertEquals("00000", result.toString());
    }

    /**
     * Test of setIndividuo method, of class Poblacion.
     */
    @Test
    public void testSetIndividuo() {
        System.out.println("setIndividuo");
        Poblacion instance = new Poblacion(25, 5);
        Genotipo otro = new Genotipo(5);
        instance.setIndividuo(3, otro);
        Genotipo result = instance.getIndividuo(3);
        assertEquals(otro, result);
    }

    /**
     * Test of getSize method, of class Poblacion.
     */
    @Test
    public void testGetSize() {
        System.out.println("getSize");
        Poblacion instance = new Poblacion(20, 3);
        int expResult = 20;
        int result = instance.getSize();
        assertEquals(expResult, result);
    }

    /**
     * Test of hashCode method, of class Poblacion.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        Poblacion instance = new Poblacion(20, 20);
       
        assertEquals(instance.hashCode(), instance.clone().hashCode());
    }

    /**
     * Test of clone method, of class Poblacion.
     */
    @Test
    public void testClone() {
        System.out.println("clone");
        Poblacion instance = new Poblacion(10, 10);
        Object result = instance.clone();
        assertEquals(instance.equals(result), true);
    }

    /**
     * Test of toString method, of class Poblacion.
     */
    @Test
    public void testToString() 
    {
        System.out.println("toString");
        java.util.Random rnd = new java.util.Random();
        Poblacion p = new Poblacion(10, 10, rnd);
        System.out.println(p);
    }
}
