//package org.example;
//
//import java.util.Arrays;
//
//public class Algorithms {
//    // Quick Sort Implementation
//    public static void quickSort(int[] arr, int begin, int end) {
//        if (begin < end) {
//            int partitionIndex = partition(arr, begin, end);
//            quickSort(arr, begin, partitionIndex-1);
//            quickSort(arr, partitionIndex+1, end);
//        }
//    }
//
//    private static int partition(int[] arr, int begin, int end) {
//        int pivot = arr[end];
//        int i = (begin - 1);
//        for (int j = begin; j < end; j++) {
//            if (arr[j] <= pivot) {
//                i++;
//                int swapTemp = arr[i];
//                arr[i] = arr[j];
//                arr[j] = swapTemp;
//            }
//        }
//        int swapTemp = arr[i+1];
//        arr[i+1] = arr[end];
//        arr[end] = swapTemp;
//        return i+1;
//    }
//
//    // Merge Sort Implementation
//    public static void mergeSort(int[] array, int left, int right) {
//        if (left < right) {
//            int middle = (left + right) / 2;
//            mergeSort(array, left, middle);
//            mergeSort(array, middle + 1, right);
//            merge(array, left, middle, right);
//        }
//    }
//
//    private static void merge(int[] array, int left, int middle, int right) {
//        int[] leftArray = Arrays.copyOfRange(array, left, middle + 1);
//        int[] rightArray = Arrays.copyOfRange(array, middle + 1, right + 1);
//        int i = 0, j = 0;
//        int k = left;
//        while (i < leftArray.length && j < rightArray.length) {
//            if (leftArray[i] <= rightArray[j]) {
//                array[k] = leftArray[i];
//                i++;
//            } else {
//                array[k] = rightArray[j];
//                j++;
//            }
//            k++;
//        }
//        while (i < leftArray.length) {
//            array[k] = leftArray[i];
//            i++;
//            k++;
//        }
//        while (j < rightArray.length) {
//            array[k] = rightArray[j];
//            j++;
//            k++;
//        }
//    }
//
//    // Binary Search Implementation
//    public static int binarySearch(int[] arr, int element) {
//        int low = 0;
//        int high = arr.length - 1;
//        while (low <= high) {
//            int mid = low + (high - low) / 2;
//            if (arr[mid] == element) {
//                return mid;
//            } else if (arr[mid] < element) {
//                low = mid + 1;
//            } else {
//                high = mid - 1;
//            }
//        }
//        return -1; // Element not found
//    }
//
//    // Linear Search Implementation
//    public static int linearSearch(int[] arr, int element) {
//        for (int i = 0; i < arr.length; i++) {
//            if (arr[i] == element) {
//                return i;
//            }
//        }
//        return -1; // Element not found
//    }
//}
