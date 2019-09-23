/*
 * @author: Alexandru Mocanu
 * Matricola 813322
 */

package dfa;

import static java.lang.Character.isDigit;
import static java.lang.Character.isLetter;

public class es1_2 {
    private static boolean scan(String s) {
        int state = 0; //stato iniziale
        int i = 0;
        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);
            switch (state) {
                case 0:
                    if (ch == '_')
                        state = 1;
                    else if (isLetter(ch))
                        state = 2;
                    else
                        state = -1;
                    break;
                case 1:
                    if (ch == '_')
                        state = 1;
                    else if (isLetter(ch) || isDigit(ch))
                        state = 2;
                    else
                        state = -1;
                    break;
                case 2:
                    if (isLetter(ch) || isDigit(ch) || ch == '_')
                        state = 2;
                    else
                        state = -1;
                    break;
            }
        }
        return state == 2; //stato accettato
    }

    public static void main(String[] args) {
        //System.out.println(scan(args[0]) ? "OK" : "NOPE"); //decommentare per inserimento manuale

        String[] test = {
                "0A", //NOPE
                "_A1", //OK
                "___", //NOPE
                "A_", //OK
                "A1", //OK
                "_", //NOPE
                "A0" //OK
        };

        for (String s : test) {
            System.out.print(s + " -> ");
            System.out.println(scan(s) ? "OK" : "NOPE");
        }
    }
}
