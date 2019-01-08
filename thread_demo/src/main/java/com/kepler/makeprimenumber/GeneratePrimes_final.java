package com.kepler.makeprimenumber;

public class GeneratePrimes_final {
    private static boolean[] crossedOut;
    private static int[] result;

    public static void main(String[] args){
        int[] ints = generatePrimes(100);
        for(int i = 0; i < ints.length; i ++){
            System.out.println(ints[i]);
        }
    }

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
        int limit = determineIterationLimit();
        for(int i = 2; i <= limit; i ++){
            if(notCrossed(i)){
                crossOutMultiplesOf(i);
            }
        }
    }

    public static int determineIterationLimit(){
        double iterationLimit = Math.sqrt(crossedOut.length);
        return (int) iterationLimit;
    }

    private static void crossOutMultiplesOf(int i){
        for(int multiple = 2 * i; multiple < crossedOut.length; multiple += i){
            crossedOut[multiple] = true;
        }
    }

    private static boolean notCrossed(int i){
        return crossedOut[i] == false;
    }

    private static void putUncrossedIntegersIntoResult() {
        result = new int[numberofUncrossedIntegers()];
        for(int j = 0, i = 2; i < crossedOut.length; i ++){
            if(notCrossed(i)){
                result[j++] = i;
            }
        }
    }

    private static int numberofUncrossedIntegers(){
        int count = 0;
        for(int i = 2; i < crossedOut.length; i ++){
            if(notCrossed(i)){
                count ++;
            }
        }
        return count++;
    }
}
