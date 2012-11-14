/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.perez.ga.core;

import org.perez.ga.core.Genotipo;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Fer
 */
public class NumberTest {
    
    public NumberTest() {
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
    
    @Test
    public void testGetNumber_general() {
        System.out.println("getNumber_general");
        Genotipo g;
        
        g = new Genotipo("00001101101");
        System.out.println(Number.getNumber(g, 4, 3, 3));
        assertEquals(13.625, Number.getNumber(g, 4, 3, 3), 0.0001);
        g = new Genotipo("11101101");
        System.out.println(Number.getNumber(g, 4, 3, 0));
        assertEquals(-13.625, Number.getNumber(g, 4, 3, 0), 0.0001);
        g = new Genotipo("0000011101111");
        System.out.println(Number.getNumber(g, 4, 3, 5));
        assertEquals(-13.875, Number.getNumber(g, 4, 3, 5), 0.0001);        
    }

    @Test
    public void testGetNumber_decimal() {
        System.out.println("getNumber_decimal");
        Genotipo g;
        
        g = new Genotipo("01101101");
        System.out.println(Number.getNumber(g, 4, 3));
        assertEquals(13.625, Number.getNumber(g, 4, 3), 0.0001);
        g = new Genotipo("11101101");
        System.out.println(Number.getNumber(g, 4, 3));
        assertEquals(-13.625, Number.getNumber(g, 4, 3), 0.0001);
        g = new Genotipo("11101111");
        System.out.println(Number.getNumber(g, 4, 3));
        assertEquals(-13.875, Number.getNumber(g, 4, 3), 0.0001);
    }
    
    @Test
    public void testGetNumber() {
        System.out.println("getNumber");
        Genotipo g;
        
        g = new Genotipo("011");
        System.out.println(Number.getNumber(g));
        assertEquals(Number.getNumber(g), 3.0, 0.00001);
        g = new Genotipo("111");
        System.out.println(Number.getNumber(g));
        assertEquals(Number.getNumber(g), -3.0, 0.00001);
        g = new Genotipo("101");
        System.out.println(Number.getNumber(g));
        assertEquals(Number.getNumber(g), -1.0, 0.00001);
        g = new Genotipo("100");
        System.out.println(Number.getNumber(g));
        assertEquals(Number.getNumber(g), 0.0, 0.00001);
        g = new Genotipo("000");
        System.out.println(Number.getNumber(g));
        assertEquals(Number.getNumber(g), 0.0, 0.00001);
        g = new Genotipo("01010");
        System.out.println(Number.getNumber(g));
        assertEquals(Number.getNumber(g), 10.0, 0.00001);
    }
}
