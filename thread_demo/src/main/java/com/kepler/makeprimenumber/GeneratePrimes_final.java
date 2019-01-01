package com.kepler.makeprimenumber;

public class GeneratePrimes_final {
    private static boolean[] crossedOut;
    private static int[] result;

    public static int[] generatePrimes(int maxValues){
        if(maxValues < 2){
            return new int[0];
        }else{
            uncrossIntegersUpTo(maxValues);
            crossOutMultiples();
            putUncrossedIntegersIntoResult();
            return result;
        }
    }

    private static void uncrossIntegersUpTo(int maxValues){
        crossedOut = new boolean[maxValues + 1];
        for(int i = 2; i < crossedOut.length; i ++){
            crossedOut[i] = false;
        }
    }

    private static void crossOutMultiples(){
        int limit = 
    }
}
