package server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Converter {
    public static String byteArrayToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(Converter.byteToHex(b));

        }
        return sb.toString();
    }

    public static byte[] hexToByteArray(String string) {
        String[] hexArray = stringToHexArray(string);
        int size = hexArray.length;
        byte[] bytes = new byte[size];
        for (int i = 0; i < size; i++) {
            bytes[i] = Converter.hexToByte(hexArray[i]);
        }
        return bytes;
    }

    public static String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits);
    }

    public static String[] stringToHexArray(String s) {
        return s.split("(?<=\\G..)",0);
    }

    public static byte hexToByte(String hexString) {
        int firstDigit = toDigit(hexString.charAt(0));
        int secondDigit = toDigit(hexString.charAt(1));
        return (byte) ((firstDigit << 4) + secondDigit);
    }

    private static int toDigit(char hexChar) {
        int digit = Character.digit(hexChar, 16);
        if(digit == -1) {
            throw new IllegalArgumentException(
                    "Invalid Hexadecimal Character: "+ hexChar);
        }
        return digit;
    }

    public static void printBytes(byte[] bytes) {
        for (byte b : bytes) {
            String s1 = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            System.out.print(b+" ");
        }
        System.out.println("");
    }

    public static byte[] concatBytes(byte[] a, byte[] b) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write( a );
        outputStream.write( b );
        return outputStream.toByteArray();
    }
}
