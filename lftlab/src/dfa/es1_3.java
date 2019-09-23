/*
 * @author: Alexandru Mocanu
 * Matricola 813322
 */

package dfa;

class es1_3 {

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

    private static boolean scan(String s) {
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);
            switch (state) {
                case 0: // stato iniziale q0
                    if(even(ch))
                        state = 1;
                    else if (odd(ch))
                        state = 2;
                    else
                        state = -1;
                    break;
                case 1: // stato q1
                    if(even(ch))
                        state = 1;
                    else if (odd(ch))
                        state = 2;
                    else if(ak(ch)) // A..k
                        state = 3;
                    else
                        state = -1;
                    break;
                case 2: // stato q2
                    if(even(ch))
                        state = 1;
                    else if (odd(ch))
                        state = 2;
                    else if(lz(ch)) // L..z
                        state = 4;
                    else
                        state = -1;
                    break;
                case 3: // stato q3
                    if (Character.isLetter(ch)) // A..z
                        state = 3;
                    else
                        state = -1;
                    break;
                case 4:	// stato q4
                    if (Character.isLetter(ch)) // A..z
                        state = 4;
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
                "123456Bianchi", // OK
                "654321Rossi", // OK
                "654321Bianchi", // NOPE
                "123456Rossi", // NOPE
                "654321", // NOPE
                "Rossi", // NOPE
                "2Bianchi", // OK
                "122B" // OK
        };

        for (String s : test) {
            System.out.print(s + " -> ");
            System.out.println(scan(s) ? "OK" : "NOPE");
        }
    }
}
