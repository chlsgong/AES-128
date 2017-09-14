package com.hcg359;

/**
 * Created by charlesgong on 6/28/17.
 */

import java.io.*;
import java.util.HashMap;


public class IOUtility {
    private final static char[] hexChar = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private final static int[] hexInt = {0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0xA, 0xB, 0xC, 0xD, 0xE, 0xF};
    private static HashMap<Character, Integer> hexMap = initHexMap();
    private static HashMap<Character, Integer> initHexMap() {
        HashMap<Character, Integer> hexMap = new HashMap<>();
        for(int i = 0; i < hexChar.length; i++) {
            hexMap.put(hexChar[i], hexInt[i]);
        }
        return hexMap;
    }

    private BufferedReader plaintextReader;
    private BufferedReader keyReader;
    private BufferedWriter writer;
    private File inFile;

    // Constructors

    public IOUtility(String inFileName, String keyFileName, String outFileName) {
        try {
            plaintextReader = new BufferedReader(new FileReader(inFileName));
            keyReader = new BufferedReader(new FileReader(keyFileName));
            writer = new BufferedWriter(new FileWriter(outFileName));
            inFile = new File(inFileName);
        }
        catch(IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    // Public instance functions

    int[][] getNextMatrix() throws IOException {
        int[][] matrix = null;
        String line;

        while(matrix == null && (line = plaintextReader.readLine()) != null) {
            matrix = buildMatrix(line);
        }
        if(matrix != null) {
            return matrix;
        }

        return null;
    }

    int[][] getKey() throws IOException {
        String line = keyReader.readLine();
        if(line != null) {
            return buildMatrix(line);
        }

        return null;
    }

    void writeMatrix(int[][] matrix) {
        try {
            for (int c = 0; c < 4; c++) {
                for (int r = 0; r < 4; r++) {
                    String front = Integer.toHexString((matrix[c][r] & 0xF0) >>> 4).toUpperCase();
                    String end = Integer.toHexString(matrix[c][r] & 0x0F).toUpperCase();
                    writer.write(front.concat(end));
                }
            }
            writer.write("\n");
        }
        catch(IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    long getInputFileSize() {
        return inFile.length();
    }

    void close() {
        try {
            plaintextReader.close();
            keyReader.close();
            writer.close();
        }
        catch(IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    // Private helper functions

    private int[][] buildMatrix(String line) {
        char[] hexValues = line.toUpperCase().toCharArray();
        int matrixSize = Math.min(hexValues.length, 32);
        int[][] matrix = new int[4][4];

        for(int i = 0; i < matrixSize / 2; i++) {
            char head = hexValues[i*2];
            char tail = hexValues[i*2+1];

            if(!hexMap.containsKey(head) || !hexMap.containsKey(tail)) {
                return null;
            }

            int hexValue = (hexMap.get(head) << 4) ^ hexMap.get(tail);
            matrix[i/4][i%4] = hexValue;
        }
        if(matrixSize % 2 > 0) {
            char head = hexValues[matrixSize-1];
            if(!hexMap.containsKey(head)) {
                return null;
            }

            int hexValue = hexMap.get(head) << 4;
            int column = matrixSize / 8;
            int row = (matrixSize / 2) % 4;
            matrix[column][row] = hexValue;
        }

        return matrix;
    }

}
