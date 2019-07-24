package com.zadorovskyi.cars.rent.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Solution {

    private static int arraySize = 99999999;

    public static void main(String[] args) {

        Integer[] arrayToSort = fillRandomly();
        Integer[] arrayToSort2 = arrayToSort.clone();
        List<Integer> listToSort = Arrays.asList(arrayToSort.clone());

//        simpleSort(arrayToSort);
        listSort(listToSort);
        recursiveSort(arrayToSort2);
    }

    private static Integer[] fillRandomly() {
        Integer[] data = new Integer[arraySize];
        for (int i = 0; i < data.length; i++) {
            data[i] = new Random().nextInt();
        }
        return data;
    }

    private static void listSort(List<Integer> data) {
        long startTime = System.currentTimeMillis();
        data.sort(Comparator.comparingInt(o -> o));
        log.info("List sort. Execution time is={}", System.currentTimeMillis() - startTime);
        //        for (int i : data) {
        //            System.out.print(i + " ");
        //        }
    }

    private static void simpleSort(Integer[] data) {
        long startTime = System.currentTimeMillis();
        Arrays.sort(data);
        log.info("Simple sort. Execution time is={}", System.currentTimeMillis() - startTime);
        //        for (int i : data) {
        //            System.out.print(i + " ");
        //        }
    }

    private static void recursiveSort(Integer[] data) {
        QuickSortAction quickSortAction = new QuickSortAction(data);
        ForkJoinPool pool = new ForkJoinPool();
        long startTime = System.currentTimeMillis();
        pool.invoke(quickSortAction);
        log.info("Recursive sort. Execution time is={}", System.currentTimeMillis() - startTime);
        //        for (int i : data) {
        //            System.out.print(i + " ");
        //        }
    }

    public static Integer[] sort(Integer[] passed, int start, int end) {
        if (end >= start) {
            int partition = partition(passed, start, end);
            Integer[] partialSorted = sort(passed, start, partition - 1);
            sort(partialSorted, partition + 1, end);
        }
        return passed;
    }

    private static int partition(Integer[] array, int begin, int end) {
        int pivot = array[end];
        int i = (begin - 1);

        for (int j = begin; j < end; j++) {
            if (array[j] <= pivot) {
                i++;

                int swapTemp = array[i];
                array[i] = array[j];
                array[j] = swapTemp;
            }
        }

        int swapTemp = array[i + 1];
        array[i + 1] = array[end];
        array[end] = swapTemp;

        return i + 1;
    }

    static class QuickSortAction extends RecursiveAction {

        private Integer[] data;

        private int leftBound;

        private int rightBound;

        public QuickSortAction(Integer[] data) {
            this.data = data;
            leftBound = 0;
            rightBound = data.length - 1;
        }

        public QuickSortAction(Integer[] data, int leftBound, int rightBound) {
            this.data = data;
            this.leftBound = leftBound;
            this.rightBound = rightBound;
        }

        @Override protected void compute() {
            if (rightBound >= leftBound) {
                int partition = partition(data, leftBound, rightBound);
                invokeAll(new QuickSortAction(data, leftBound, partition - 1),
                    new QuickSortAction(data, partition + 1, rightBound));
            }
        }

        private int partition(Integer[] array, int begin, int end) {
            int pivot = array[end];
            int i = (begin - 1);

            for (int j = begin; j < end; j++) {
                if (array[j] <= pivot) {
                    i++;

                    int swapTemp = array[i];
                    array[i] = array[j];
                    array[j] = swapTemp;
                }
            }

            int swapTemp = array[i + 1];
            array[i + 1] = array[end];
            array[end] = swapTemp;

            return i + 1;
        }
    }
}