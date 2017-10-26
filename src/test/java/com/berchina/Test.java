package com.berchina;

import java.util.Scanner;

public class Test {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println(scanner.nextInt());
        }
    }

}
