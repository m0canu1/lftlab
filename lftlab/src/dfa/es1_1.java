/*
 * @author: Alexandru Mocanu
 * Matricola 813322
 */

package dfa;


import java.util.Scanner;

class es1_1 {
    private static boolean scan(String s, int choice) {
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length())
        {
            final char ch = s.charAt(i++);
            switch (state)
            {
                case 0:
                    if (ch == '0')
                        state = 1;
                    else if (ch == '1')
                        state = 0;
                    else
                        state = -1;
                    break;
                case 1:
                    if (ch == '0')
                        state = 2;
                    else if (ch == '1')
                        state = 0;
                    else
                        state = -1;
                    break;
                case 2:
                    if (ch == '0')
                        state = 3;
                    else if (ch == '1')
                        state = 0;
                    else
                        state = -1;
                    break;
                case 3:
                    if (ch == '0' || ch == '1')
                        state = 3;
                    else
                        state = -1;
                    break;
            }
        }
        if (choice == 1) //3 zeri consecutivi
            return state == 3; //boolean value
        else    //3 zeri non consecutivi
            return state != 3;
    }

    private static int menu() {
        Scanner keyboard = new Scanner(System.in);

        System.out.print("Scelta DFA: \n");
        System.out.print("1 - Tre zeri CONSECUTIVI. \n");
        System.out.print("2 - Tre zeri NON CONSECUTIVI. \n\n");

        int ret = keyboard.nextInt();
        keyboard.close();
        return ret;
    }

    public static void main(String[] args) {

        int choice = menu();

        String[] test = {
                "1000",
                "1110101",
                "110111011110000",
                "0000",
                "11110",
                "111",
                "0",
                "1",
                "01010100110001",
                "0001"
        };

        if (choice == 1 || choice == 2)
            for (String s : test) {
                System.out.print(s + " -> ");
                System.out.println(scan(s, choice) ? "OK" : "NOPE");
            }
        else
            System.out.println("\nScelta non valida!");

    }
}
