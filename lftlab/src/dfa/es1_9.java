package dfa;

/**
 * @author: Alexandru Mocanu
 * Matricola 813322
 */

public class es1_9 {
    public static boolean scan(String s) {
        int state = 0; // stato iniziale q0
        int i = 0;
        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);
            switch (state) {
            
            case 0:
                if (ch == 'a' || ch == 'A')
                    state = 5;
                else
                    state = 1;
                break;             
            case 1: 
                if (ch == 'l' || ch == 'L')
                    state = 2;
                else
                    state = -1;
                break;
            case 2: 
                if (ch == 'e' || ch == 'E')
                    state = 3; 
                else
                    state = -1;
                break;
            case 3:
                if (ch == 'x' || ch == 'X')                
                    state = 4;
                else
                    state = -1;
                break;
            case 5:
                if (ch == 'l' || ch == 'L')   
                    state = 6;
                else 
                    state = 2;
                break;             
            case 6:
                if (ch == 'e' || ch == 'E')
                    state = 7;
                else
                    state = 3;
                break;
            case 7:
                    state = 4;
                break;
            }
        }
        return state == 4;
    }

    public static void main(String[] args) {
        // System.out.println(scan(args[0]) ? "OK" : "NOPE");

        String[] test = { 
            "Alex", //OK
            "apex", //OK
            "al0x", //OK
            "a", //NOPE
            "ba", //NOPE
            "al0ex", //NOPE
            "ale", //NOPE
            "al x", //OK
            "ale " //OK
        };

        for (String s : test) {
            System.out.print(s + " -> ");
            System.out.println(scan(s) ? "OK" : "NOPE");
        }
    }
}
