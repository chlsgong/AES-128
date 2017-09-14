package com.hcg359;

/**
 * Created by charlesgong on 6/28/17.
 */

import java.io.IOException;


public class AES {
    public static void main(String[] args) {
        String flag = args[0];
        String keyFile = args[1];
        String inputFile = args[2];

        IOUtility ioUtility;
        int[][] key;
        int[][] matrix;
        int[][] result;

        long start;
        long finish;
        long elapsedTime;
        double throughput = 0;

        switch(flag) {
            case "e":
                // encrypt
                start = System.currentTimeMillis();

                try {
                    ioUtility = new IOUtility(inputFile, keyFile, inputFile + ".enc");
                    key = ioUtility.getKey();

                    Encryptor encryptor = new Encryptor(key);

                    while((matrix = ioUtility.getNextMatrix()) != null) {
                        encryptor.setState(matrix);
                        encryptor.encrypt();
                        result = encryptor.getState();

                        ioUtility.writeMatrix(result);
                    }

                    ioUtility.close();

                    finish = System.currentTimeMillis();
                    elapsedTime = finish - start;
                    throughput = (ioUtility.getInputFileSize() / 1000000.0) / (elapsedTime / 1000.0);
                }
                catch(IOException e) {
                    e.getLocalizedMessage();
                }

                System.out.println("Encoding throughput: " + throughput + " MB/sec");

                break;
            case "d":
                // decrypt
                start = System.currentTimeMillis();

                try {
                    ioUtility = new IOUtility(inputFile, keyFile, inputFile + ".dec");
                    key = ioUtility.getKey();

                    Decryptor decryptor = new Decryptor(key);

                    while((matrix = ioUtility.getNextMatrix()) != null) {
                        decryptor.setState(matrix);
                        decryptor.decrypt();
                        result = decryptor.getState();

                        ioUtility.writeMatrix(result);
                    }

                    ioUtility.close();

                    finish = System.currentTimeMillis();
                    elapsedTime = finish - start;
                    throughput = (ioUtility.getInputFileSize() / 1000000.0) / (elapsedTime / 1000.0);
                }
                catch(IOException e) {
                    e.getLocalizedMessage();
                }

                System.out.println("Decoding throughput: " + throughput + " MB/sec");

                break;
            default:
                break;
        }
    }

}
