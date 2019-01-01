package com.kepler.makeprimenumber;

import org.junit.Test;

import java.util.*;

public class GeneratePrimes {

    public static void main(String[] args){
        int[] ints = GeneratePrimes.generatePrimes(100);
        for(int i = 0; i < ints.length; i ++){
            System.out.println(args[i]);
        }
    }

    public static int[] generatePrimes(int maxValues){
        if(maxValues >= 2){
            int s = maxValues + 1;
            boolean[] f = new boolean[s];
            int i;
            for(i = 0; i < s; i ++){
                f[i] = true;
            }
            f[0] = f[1] = false;
            int j;
            for(i = 2; i < Math.sqrt(s) + 1; i ++){
                for(j = 2 * i; j < s; j += i){
                    f[j] = false;
                }
            }
            int count = 0;
            for(i = 0; i < s; i ++){
                if(f[i]){
                    count++;
                }
            }
            int[] primes = new int[count];
            for(i = 0, j = 0;i < s; i++){
                if(f[i]){
                    primes[j++] = i;
                }
            }
            return primes;
        } else {
            return new int[0];
        }
    }
}
