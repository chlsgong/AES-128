package com.hcg359;

/**
 * Created by charlesgong on 6/28/17.
 */

public class Encryptor extends Key {
    private int[][] state;
    private int round;
    private MixColumn mixer;

    public Encryptor(int[][] key) {
        roundKeys = new int[11][4][4];
        roundKeys[0] = key;
        mixer = new MixColumn();

        expandKey();
    }

    void setState(int[][] plaintext) {
        state = plaintext;
        round = 0;
    }

    void encrypt() {
        for(; round < Constant.MAX_ROUNDS; round++) {
            switch(round) {
                case 0:
                    addRoundKey();
                    break;
                case 10:
                    subBytes();
                    shiftRows();
                    addRoundKey();
                    break;
                default:
                    subBytes();
                    shiftRows();
                    mixColumns();
                    addRoundKey();
                    break;
            }
        }
    }

    int[][] getState() {
        return state;
    }

    private void addRoundKey() {
        for(int c = 0; c < 4; c++) {
            for(int r = 0; r < 4; r++) {
                state[c][r] = state[c][r] ^ roundKeys[round][c][r];
            }
        }
    }

    private void subBytes() {
        for(int c = 0; c < 4; c++) {
            for(int r = 0; r < 4; r++) {
                state[c][r] = getSubByte(state[c][r]);
            }
        }
    }

    private void shiftRows() {
        for(int r = 0; r < 4; r++) {
            for(int c = 0; c < r; c++) {
                int[] shiftedRow = {state[1][r], state[2][r], state[3][r], state[0][r]};
                state[0][r] = shiftedRow[0];
                state[1][r] = shiftedRow[1];
                state[2][r] = shiftedRow[2];
                state[3][r] = shiftedRow[3];
            }
        }
    }

    private void mixColumns() {
        for(int i = 0; i < 4; i++) {
            state = mixer.mixColumn2(state, i);
        }
    }

    private void printState() {
        for(int c = 0; c < 4; c++) {
            for(int r = 0; r < 4; r++) {
                String front = Integer.toHexString((state[c][r] & 0xF0) >>> 4).toUpperCase();
                String end = Integer.toHexString(state[c][r] & 0x0F).toUpperCase();
                System.out.print(front.concat(end));
            }
        }
        System.out.println();
    }

}
