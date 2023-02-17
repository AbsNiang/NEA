package com.example.demo.General;

import javafx.scene.control.Alert;

public class Repository {

    // each time it splits, go into a new mergesort, once a pair is reached, go out and complete the mergesort
    public static void mergeSort(String[] inputArray) {
        int arraySize = inputArray.length;
        int middle = arraySize / 2;
        if (arraySize < 2) {//already sorted if it has only one value
            return;
        }
        String[] leftHalf = new String[middle];
        String[] rightHalf = new String[arraySize - middle];
        System.arraycopy(inputArray, 0, leftHalf, 0, middle);
        if (arraySize - middle >= 0) System.arraycopy(inputArray, middle, rightHalf, 0, arraySize - middle);
        mergeSort(leftHalf);
        mergeSort(rightHalf);
        merge(leftHalf, rightHalf, inputArray);
    }

    //parentArray will be the array that both sortedLeft/Right will come from
    public static void merge(String[] sortedLeft, String[] sortedRight, String[] parentArray) {
        //i is smallest value in sortedLeft, j is smallest value in sortedRight, k is next position in parentArray
        int i = 0;
        int j = 0;
        int k = 0;
        while (i < sortedLeft.length && j < sortedRight.length) {
            if (LeftStringIsSmallerThanRight(sortedLeft[i], sortedRight[j])) {//if left is alphabetically before right
                parentArray[k] = sortedLeft[i];
                k++;
                i++;
            } else {//if right is alphabetically before left as they can't be equal strings
                parentArray[k] = sortedRight[j];
                k++;
                j++;
            }
        }
        //once one of the arrays has been fully put into the parent array
        while (i < sortedLeft.length) {//if left array hasn't been 'completed'
            parentArray[k] = sortedLeft[i];
            k++;
            i++;
        }
        while (j < sortedRight.length) {//if right array hasn't been 'completed'
            parentArray[k] = sortedRight[j];
            k++;
            j++;
        }
    }

    private static boolean LeftStringIsSmallerThanRight(String s1, String s2) {
        int comparison = s1.compareToIgnoreCase(s2);
        return comparison < 0;//less than 0 - s1 is before in alphabet, greater - s2 is before
    }

    //splits array into two each time depending on if the alphabetical order is bigger or smaller.
    public static boolean binarySearch(String emailAddress, String[] emailArray) {
        int arraySize = emailArray.length;
        if (arraySize > 1) {
            int middle = arraySize / 2;
            String currentMidpoint = emailArray[middle];
            if (emailAddress.equalsIgnoreCase(currentMidpoint)) {//string found
                return true;
            } else if (LeftStringIsSmallerThanRight(emailAddress, currentMidpoint)) {//string is in left half
                String[] leftHalf = new String[middle];
                System.arraycopy(emailArray, 0, leftHalf, 0, middle);
                return binarySearch(emailAddress, leftHalf);
            } else {//string is in right half
                String[] rightHalf = new String[arraySize - middle];
                System.arraycopy(emailArray, middle, rightHalf, 0, arraySize - middle);
                return binarySearch(emailAddress, rightHalf);
            }
        } else {
            boolean stringFound = emailAddress.equalsIgnoreCase(emailArray[0]);
            System.out.println("Found? = " + stringFound);
            return stringFound;
        }
    }

    public static void giveAlert(String content, String alertType) {//error, information, or confirmation
        Alert alert;
        switch (alertType) {
            case "error" -> alert = new Alert(Alert.AlertType.ERROR);
            case "information" -> alert = new Alert(Alert.AlertType.INFORMATION);
            case "confirmation" -> alert = new Alert(Alert.AlertType.CONFIRMATION);
            default -> throw new IllegalStateException("Unexpected value: " + alertType);
        }
        alert.setContentText(content);
        alert.show();
    }
}
