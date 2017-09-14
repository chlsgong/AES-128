package com.hcg359;

/**
 * Created by charlesgong on 6/30/17.
 */

public class Key {
    protected int[][][] roundKeys;

    protected void expandKey() {
        for(int i = 0; i < 10; i++) {
            int[][] currentKey = roundKeys[i];
            int[][] nextKey = roundKeys[i+1];

            int[] column = rotWord(currentKey);
            for(int r = 0; r < 4; r++) {
                column[r] = getSubByte(column[r]);
                nextKey[0][r] = currentKey[0][r] ^ column[r] ^ Constant.RCON[i][r];
            }
            for(int c = 1; c < 4; c++) {
                for(int r = 0; r < 4; r++) {
                    nextKey[c][r] = currentKey[c][r] ^ nextKey[c-1][r];
                }
            }
        }
    }

    protected int getSubByte(int hexByte) {
        int row = (hexByte & 0xF0) >>> 4;
        int col = hexByte & 0x0F;

        int sByte = Constant.SBOX[row][col];

        return sByte;
    }

    private int[] rotWord(int[][] key) {
        int[] column = {key[3][1], key[3][2], key[3][3], key[3][0]};
        return column;
    }

    private void printKey(int[][] key) {
        for(int c = 0; c < 4; c++) {
            for(int r = 0; r < 4; r++) {
                String front = Integer.toHexString((key[c][r] & 0xF0) >>> 4).toUpperCase();
                String end = Integer.toHexString(key[c][r] & 0x0F).toUpperCase();
                System.out.print(front.concat(end));
            }
        }
        System.out.println();
    }
}
