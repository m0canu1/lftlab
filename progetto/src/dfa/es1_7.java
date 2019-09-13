/**
 * @author: Alexandru Mocanu
 * Matricola 813322
 */
package dfa;

// TODO corretto
public class es1_7 {
    public static boolean scan(String s) {
        int state = 0; // stato iniziale q0
        int i = 0;
        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);
            switch (state) {
            case 0: 
                if (ch == 'a')
                    state = 3;
                else if (ch == 'b')
                    state = 1; 
                else
                    state = -1;
                break;
            case 1: 
                if (ch == 'a')
                    state = 3; 
                else if (ch == 'b')
                    state = 2; 
                else
                    state = -1;
                break;
            case 2:
                if (ch == 'a')
                    state = 3;
                else if (ch == 'b')
                    state = -1; 
                else
                    state = -1;
                break;
            }
        }
        return state == 3;
    }

    public static void main(String[] args) {
        // System.out.println(scan(args[0]) ? "OK" : "NOPE");

        String[] test = { 
            "abb", //OK
            "bbaba", //OK
            "baaaaaaa", //OK
            "aaaaaaaa", //OK
            "a", //OK
            "ba", //OK
            "bba", //OK
            "aa", //OK
            "abbbbb", //OK
            "babbbbbb", //OK
            "aaabbbbb", //OK
            "bbabbbbb", //OK
            "bbbaaaaa", //NOPE
            "ababbbbb", //OK
            "caabbbb", // NOPE
            "b" //NOPE
        };

        for (String s : test) {
            System.out.print(s + " -> ");
            System.out.println(scan(s) ? "OK" : "NOPE");
        }
    }
}
