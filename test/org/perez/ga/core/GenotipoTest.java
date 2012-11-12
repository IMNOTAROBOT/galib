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
public class GenotipoTest {
    
    public GenotipoTest() {
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
     * Test of getGen method, of class Genotipo.
     */
    @Test
    public void testGetGen_int() {
        System.out.println("getGen");
        Genotipo instance = new Genotipo(1);
        assertEquals(false, instance.getGen(0));   
    }

    /**
     * Test of setGen method, of class Genotipo.
     */
    @Test
    public void testSetGen_int_boolean() {
        System.out.println("setGen");
        Genotipo instance = new Genotipo(5);
        instance.setGen(4, true);
        assertEquals("00001", instance.toString());
    }


    /**
     * Test of getSize method, of class Genotipo.
     */
    @Test
    public void testGetSize() {
        System.out.println("getSize");
        Genotipo instance = new Genotipo(27);
        int expResult = 27;
        int result = instance.getSize();
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class Genotipo.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Genotipo instance = new Genotipo(3);
        assertEquals("000", instance.toString());
        instance.setGen(2, true);
        assertEquals("001", instance.toString());
    }

    /**
     * Test of clone method, of class Genotipo.
     */
    @Test
    public void testClone() {
        System.out.println("clone");
        Genotipo instance = new Genotipo(10);
        Object result = instance.clone();
        assertEquals(true, instance.equals(result));
    }

    /**
     * Test of equals method, of class Genotipo.
     */
    @Test
    public void testEquals() 
    {
        System.out.println("equals");
        Genotipo g1 = new Genotipo(5);
        Genotipo g2 = new Genotipo(5);
        Genotipo g3 = new Genotipo(5);
       
        assertEquals(false, g1.equals(null)); //base
        assertEquals(false, g1.equals("hola")); //base
        
        assertEquals(true, g1.equals(g1)); //reflex
        assertEquals(true, g1.equals(g2) && g2.equals(g1)); //symmetry
        assertEquals(true, g1.equals(g2) && g2.equals(g3) && g1.equals(g3)); //transitivity
        assertEquals(true, g1.equals(g2) && g1.equals(g2) && g1.equals(g2)); //consistency
    }

    /**
     * Test of hashCode method, of class Genotipo.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        Genotipo instance = new Genotipo(10);
        System.out.println(instance.hashCode());
        instance.setGen(0, true);
        assertTrue(instance.clone().hashCode()==instance.hashCode());
        System.out.println(instance.hashCode());
        instance.setGen(9, true);
        System.out.println(instance.hashCode());
    }
}
