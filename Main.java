import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("Choose operation:\n1 - Enrypt\n2 - Decrypt");
            int function = Integer.parseInt(reader.readLine());
            byte[] message1 = Files.readAllBytes(Paths.get("E:\\КиОКИ\\2kioki\\2.txt"));
            String message2 = "";
            for (int i = 0; i < message1.length; i++) {
                message2 += Integer.toBinaryString(message1[i]);
                message2 += " ";
            }

            Scanner scanner = new Scanner(new File("en2.txt")).useDelimiter(" ");
            String message3 = scanner.nextLine();
            scanner.close();

            System.out.println("Enter common key:");
            Integer key = Integer.parseInt(reader.readLine());

            if (function == 1) {
                System.out.println("\nYour crypt: " + encrypt(key, message2));
                FileWriter writer = new FileWriter("en2.txt");
                writer.write(encrypt(key, message2));
                writer.flush();
            } else if (function == 2) {
                System.out.println("\nYour crypt: " + decrypt(key, message3));
            }
            System.out.println("----------------------");
        }
    }

    private static String encrypt(Integer key, String text) {
        String encrypt = "";
        String[] keys = generateKeys(key).split(" ");
        char[][] keybites = new char[keys.length][8];
        for (int i = 0; i < keys.length; i++) {
            keybites[i] = keys[i].toCharArray();
        }
        String[] message = text.split(" ");
        char[][] bites = new char[message.length][8];

        for (int i = 0; i < message.length; i++) {
            if (message[i].length() < 8) {
                while (message[i].length() < 8)
                    message[i] = "0" + message[i];
            }
        }

        char[][] aims = new char[message.length][8];
        for (int i = 0; i < message.length; i++) {
            aims[i] = message[i].toCharArray();
        }

        for (int i = 0; i < message.length; i++) {
            bites[i] = new char[]{aims[i][1], aims[i][5], aims[i][2], aims[i][0], aims[i][3], aims[i][7], aims[i][4], aims[i][6]};
        }

        char[][] pastRound1 = new char[message.length][8];
        for (int i = 0; i < message.length; i++) {
            pastRound1[i] = round(bites[i], keybites[0]);
        }

        char[][] punkt3 = new char[message.length][8];
        for (int i = 0; i < message.length; i++) {
            punkt3[i] = new char[]{pastRound1[i][4], pastRound1[i][5], pastRound1[i][6], pastRound1[i][7], pastRound1[i][0], pastRound1[i][1], pastRound1[i][2], pastRound1[i][3]};
        }

        char[][] pastRound2 = new char[message.length][8];
        for (int i = 0; i < message.length; i++) {
            pastRound2[i] = round(punkt3[i], keybites[1]);
        }

        char[][] punkt5 = new char[message.length][8];
        for (int i = 0; i < message.length; i++) {
            punkt5[i] = new char[]{pastRound2[i][3], pastRound2[i][0], pastRound2[i][2], pastRound2[i][4], pastRound2[i][6], pastRound2[i][1], pastRound2[i][7], pastRound2[i][5]};
        }

        for (int i = 0; i < message.length; i++) {
            for (int j = 0; j < 8; j++) {
                encrypt += punkt5[i][j];
            }
            encrypt += " ";
        }
        return encrypt;
    }

    private static String decrypt(Integer key, String text) {
        String decrypt = "";
        String[] keys = generateKeys(key).split(" ");
        char[][] keybites = new char[keys.length][8];
        for (int i = 0; i < keys.length; i++) {
            keybites[i] = keys[i].toCharArray();
        }
        String[] message = text.split(" ");
        char[][] bites = new char[message.length][8];

        for (int i = 0; i < message.length; i++) {
            if (message[i].length() < 8) {
                while (message[i].length() < 8)
                    message[i] = "0" + message[i];
            }
        }

        char[][] aims = new char[message.length][8];
        for (int i = 0; i < message.length; i++) {
            aims[i] = message[i].toCharArray();
        }

        for (int i = 0; i < message.length; i++) {
            bites[i] = new char[]{aims[i][1], aims[i][5], aims[i][2], aims[i][0], aims[i][3], aims[i][7], aims[i][4], aims[i][6]};
        }
        char[][] pastRound1 = new char[message.length][8];
        for (int i = 0; i < message.length; i++) {
            pastRound1[i] = round(bites[i], keybites[1]);
        }

        char[][] punkt3 = new char[message.length][8];
        for (int i = 0; i < message.length; i++) {
            punkt3[i] = new char[]{pastRound1[i][4], pastRound1[i][5], pastRound1[i][6], pastRound1[i][7], pastRound1[i][0], pastRound1[i][1], pastRound1[i][2], pastRound1[i][3]};
        }

        char[][] pastRound2 = new char[message.length][8];
        for (int i = 0; i < message.length; i++) {
            pastRound2[i] = round(punkt3[i], keybites[0]);
        }

        char[][] punkt5 = new char[message.length][8];
        for (int i = 0; i < message.length; i++) {
            punkt5[i] = new char[]{pastRound2[i][3], pastRound2[i][0], pastRound2[i][2], pastRound2[i][4], pastRound2[i][6], pastRound2[i][1], pastRound2[i][7], pastRound2[i][5]};
        }

        for (int i = 0; i < message.length; i++) {
            for (int j = 0; j < 8; j++) {
                decrypt += punkt5[i][j];
            }
            decrypt += " ";
        }
        String[] mess = decrypt.split(" ");
        decrypt = "";
        for (int i = 0; i < mess.length; i++) {
            int b = Integer.parseInt(mess[i], 2);
            char c = (char) b;
            decrypt += c;
        }

        return decrypt;
    }

    private static String generateKeys(Integer key) {
        String strKey = key.toString();
        char[] left1 = new char[5];
        char[] right1 = new char[5];
        char[] massKey = strKey.toCharArray();
        char[] newMassKey = {massKey[2], massKey[4], massKey[1], massKey[6], massKey[3], massKey[9], massKey[0], massKey[8], massKey[7], massKey[5]};

        for (int i = 0; i < 5; i++)
            left1[i] = newMassKey[i];

        for (int i = 0; i < 5; i++)
            right1[i] = newMassKey[i + 5];

        char[] left2 = {left1[1], left1[2], left1[3], left1[4], left1[0]};
        char[] right2 = {right1[1], right1[2], right1[3], right1[4], right1[0]};
        char[] key1 = {right2[0], left2[2], right2[1], left2[3], right2[2], left2[4], right2[4], right2[3]};
        char[] left3 = {left1[3], left1[4], left1[0], left1[1], left1[2]};
        char[] right3 = {right1[3], right1[4], right1[0], right1[1], right1[2]};
        char[] key2 = {right3[0], left3[2], right3[1], left3[3], right3[2], left3[4], right3[4], right3[3]};

        String keys = "";
        for (int i = 0; i < key1.length; i++) {
            keys += key1[i];
        }
        keys += " ";
        for (int i = 0; i < key2.length; i++) {
            keys += key2[i];
        }
        return keys;
    }

    private static char[] round(char[] bites, char[] keyI) {
        char[] left1 = new char[4];
        char[] right1 = new char[4];
        char[] left4 = new char[4];
        char[] right4 = new char[4];
        for (int i = 0; i < 4; i++) {
            left1[i] = bites[i];
            right1[i] = bites[i + 4];
        }
        char[] right2 = {right1[3], right1[0], right1[1], right1[2], right1[1], right1[2], right1[3], right1[0]};
        char[] xor = new char[8];
        for (int i = 0; i < 8; i++) {
            if (keyI[i] == right2[i])
                xor[i] = '0';
            else
                xor[i] = '1';
        }

        for (int i = 0; i < 4; i++) {
            left4[i] = xor[i];
            right4[i] = xor[i + 4];
        }

        String l14 = left4[0] + "" + left4[3];
        String l23 = left4[1] + "" + left4[2];
        String r14 = right4[0] + "" + right4[3];
        String r23 = right4[1] + "" + right4[2];

        int row1 = findNum1(l14);
        int col1 = findNum1(l23);
        int row2 = findNum1(r14);
        int col2 = findNum1(r23);
        char[][] s1 = {{'1', '0', '3', '2'}, {'3', '2', '1', '0'}, {'0', '2', '1', '3'}, {'3', '1', '3', '2'}};
        char[][] s2 = {{'0', '1', '2', '3'}, {'2', '0', '1', '3'}, {'3', '0', '1', '0'}, {'2', '1', '0', '3'}};

        char leftNum = s1[row1][col1];
        char rightNum = s2[row2][col2];
        char[] newLeft = findNum2(leftNum);
        char[] newRight = findNum2(rightNum);
        char[] punkt5 = {newLeft[1], newRight[1], newRight[0], newLeft[0]};
        char[] xor6 = new char[4];
        for (int i = 0; i < 4; i++) {
            if (left1[i] == punkt5[i])
                xor6[i] = '0';
            else
                xor6[i] = '1';
        }

        return new char[]{xor6[0], xor6[1], xor6[2], xor6[3], right1[0], right1[1], right1[2], right1[3]};
    }

    public static int findNum1(String s) {
        int num = 0;
        switch (s) {
            case "11":
                num = 3;
                break;
            case "10":
                num = 2;
                break;
            case "01":
                num = 1;
                break;
            case "00":
                num = 0;
                break;
        }
        return num;
    }

    public static char[] findNum2(char s) {
        char[] nums = new char[2];
        switch (s) {
            case '3':
                nums = "11".toCharArray();
                break;
            case '2':
                nums = "10".toCharArray();
                break;
            case '1':
                nums = "01".toCharArray();
                break;
            case '0':
                nums = "00".toCharArray();
                break;
        }
        return nums;
    }
}
