/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.perez.ga;

import java.io.File;
import org.perez.ga.algorithms.SGA;

/**
 *
 * @author sdist
 */
public class Runner 
{
    public static void main(String args[])
    {
        if(args.length<2) {
            System.out.println("GA library");
            System.out.println("Usage:");
            System.out.println("   java -jar GAlib.jar <Algorithm> <input-file>");
            System.out.println("Lists of algorithms:");
            System.out.println(" - SGA: Simple Genetic Algorithm");
        }
        else {
            if(args[0].equals("SGA")) {
                SGA al = new SGA(new File(args[1]));
                al.SGA();
            }
            else {
                System.out.println("Incorrect algorithm");
            }
        }
        
    }
}
