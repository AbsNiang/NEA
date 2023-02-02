package com.example.demo;

public class Repository { //use SQL COUNT the number of users, make an array which gets inputted into the array
    // each time it splits, go into a new mergesort, once a pair is reached, go out and complete the mergesort
    public static String[] mergeSort(String[] inputArray) { //can replace manual array copy with System.arraycopy()
        int arraySize = inputArray.length;
        int leftPointer = 0;
        int rightPointer = arraySize - 1;
        int middle = arraySize / 2;
        if (arraySize > 1) {
            String[] leftHalf = new String[middle];
            for (int i = 0; i < middle; i++) {
                leftHalf[i] = inputArray[i];
            }
            String[] rightHalf = new String[arraySize - middle];
            for (int i = 0; i < (arraySize - middle); i++) {
                rightHalf[i] = inputArray[i + middle];
            }
            leftHalf = mergeSort(leftHalf);
            rightHalf = mergeSort(rightHalf);
            int pos = 0;
            for (String email : leftHalf) {
                inputArray[pos] = email;
                pos++;
            }
            for (String email : rightHalf) {
                inputArray[pos] = email;
                pos++;
            }
        }
        //sort the list you are left with

        return inputArray;
    }

    //parentArray will be the array that both sortedLeft/Right will come from
    public static String[] merge(String[] sortedLeft, String[] sortedRight, String parentArray) {
        //i is smallest value in sortedLeft, j is smallest value in sortedRight, k is next position in parentArray
        int i = 0;
        int j = 0;
        int k = 0;
        while (i < sortedLeft.length - 1 && j < sortedRight.length - 1) {
            if (compareString(sortedLeft[i], sortedRight[j])) {

            }
        }
    }

    //splits array into two each time depending on if the alphabetical order is bigger or smaller.
    public static void binarySearch(String[] emailArray) {

    }

    private static boolean compareString(String s1, String s2) {
        boolean s1Smaller; //refers to if s1 is alphabetically before s2

    }
}
