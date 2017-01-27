package com.hackerrank.java2darray;

import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Java_2d_array {
    public static void main(String[] args) {
        /*Scanner in = new Scanner(System.in);
        int arr[][] = new int[6][6];
        int maxSum = Integer.MIN_VALUE;
        for(int i=0; i < 6; i++){
            for(int j=0; j < 6; j++){
                arr[i][j] = in.nextInt();
            }
        }*/
        int n = 5;
        int arr[] = {1, -2, 4, -5, 1};

        int result = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                int sum = 0;
                for (int k = i; k <= j; k++) {
                    sum += arr[k];
                    //if (k > i) sum += arr[j];
                }
                if (sum < 0) result++;
            }
        }
        System.out.print(result);
    }
}
