/**
 * @author: Alexandru Mocanu
 * Matricola 813322
 */


public class es1_5 {

    /* PARI */
    private static boolean even(char ch) {
        return ch == '0' || ch == '2' || ch == '4' || ch == '6' || ch == '8';
    }

    /* DISPARI */
    private static boolean odd(char ch) {
        return ch == '1' || ch == '3' || ch == '5' || ch == '7' || ch == '9';
    }

    /* A..k */
    private static boolean ak(char ch){
        return (ch >= 'A' && ch <= 'K') || (ch >= 'a' && ch <= 'k');
    }

    /* L..z */
    private static boolean lz(char ch) {
        return (ch >= 'L' && ch <= 'Z') || (ch >= 'l' && ch <= 'z');
    }

    public static boolean scan(String s) {
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);
            switch (state) {
                case 0:
                    if (ak(ch))
                        state = 1;
                    else if (lz(ch))
                        state = 2;
                    else
                        state = -1;
                    break;
                case 1:
                    if(even(ch))
                        state = 4;
                    else if (odd(ch))
                        state = 5;
                    else if (Character.isLetter(ch))
                        state = 1;
                    else
                        state = -1;
                    break;
                case 2:
                    if (odd(ch))
                        state = 3;
                    else if (even(ch))
                        state = 6;
                    else if (Character.isLetter(ch))
                        state = 2;
                    else
                        state = -1;
                    break;
                case 3:         // turno 3
                    if (odd(ch))
                        state = 3;
                    else if (even(ch))
                        state = 6;
                    else
                        state = -1;
                    break;
                case 4:         // turno 2
                    if (even(ch))
                        state = 4;
                    else if (odd(ch))
                        state = 5;
                    else
                        state = -1;
                    break;
                case 5:
                    if (odd(ch))
                        state = 5;
                    else if (even(ch))
                        state = 4;
                    else
                        state = -1;
                    break;
                case 6:
                    if (even(ch))
                        state = 6;
                    else if (odd(ch))
                        state = 3;
                    else
                        state = -1;
                    break;
            }
        }
        return ((state == 3) || (state == 4));
    }

    public static void main(String[] args) {
        // System.out.println(scan(args[0]) ? "OK" : "NOPE");

        String[] test = { 
                "123456Bianchi", // NOPE
                "Bianchi123456", // OK
                "654321Rossi", // NOPE
                "Rossi654321", // OK
                "Bianchi654321", // NOPE
                "Rossi123456", // NOPE
                "654321", // NOPE
                "Rossi", // NOPE
                "Bianchi2", // OK
                "B122" // OK
        };

        for (String s : test) {
            System.out.print(s + " -> ");
            System.out.println(scan(s) ? "OK" : "NOPE");
        }

    }
}
