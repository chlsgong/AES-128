package com.hcg359;

/**
 * Created by charlesgong on 6/30/17.
 */

public class Decryptor extends Key {
    private int[][] state;
    private int round;
    private MixColumn mixer;

    public Decryptor(int[][] key) {
        roundKeys = new int[11][4][4];
        roundKeys[0] = key;
        mixer = new MixColumn();

        expandKey();
    }

    void setState(int[][] plaintext) {
        state = plaintext;
        round = Constant.MAX_ROUNDS - 1;
    }

    void decrypt() {
        for(; round >= 0; round--) {
            switch(round) {
                case 0:
                    invAddRoundKey();
                    break;
                case 10:
                    invAddRoundKey();
                    invShiftRows();
                    invSubBytes();
                    break;
                default:
                    invAddRoundKey();
                    invMixColumns();
                    invShiftRows();
                    invSubBytes();
                    break;
            }
        }
    }

    int[][] getState() {
        return state;
    }

    private void invAddRoundKey() {
        for(int c = 0; c < 4; c++) {
            for(int r = 0; r < 4; r++) {
                state[c][r] = state[c][r] ^ roundKeys[round][c][r];
            }
        }
    }

    private void invSubBytes() {
        for(int c = 0; c < 4; c++) {
            for(int r = 0; r < 4; r++) {
                state[c][r] = getInvSubByte(state[c][r]);
            }
        }
    }

    private void invShiftRows() {
        for(int r = 0; r < 4; r++) {
            for(int c = 0; c < r; c++) {
                int[] shiftedRow = {state[3][r], state[0][r], state[1][r], state[2][r]};
                state[0][r] = shiftedRow[0];
                state[1][r] = shiftedRow[1];
                state[2][r] = shiftedRow[2];
                state[3][r] = shiftedRow[3];
            }
        }
    }

    private void invMixColumns() {
        for(int i = 0; i < 4; i++) {
            state = mixer.invMixColumn2(state, i);
        }
    }

    private int getInvSubByte(int hexByte) {
        int row = (hexByte & 0xF0) >>> 4;
        int col = hexByte & 0x0F;

        int sByte = Constant.INVSBOX[row][col];

        return sByte;
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
