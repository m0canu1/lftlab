/**
 * @author: Alexandru Mocanu
 * Matricola 813322
 */


// TODO corretto, studiare automa
public class es1_6 {
    public static boolean scan(String s){
        int state = 0; //stato iniziale q0
        int i = 0;
        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);
            switch (state) {
                case 0: //finale, 0 è considerato multiplo di 3 perché il resto è 0
                    if(ch == '0')
                        state = 0; 
                    else if (ch == '1')
                        state = 1; //perché numero (1) dà resto 1
                    else
                        state = -1;
                    break;
                case 1: // stato q1
                    if(ch == '0')
                        state = 2; //(10) dà resto 2.
                    else if (ch == '1')
                        state = 0; //(11) dà resto 0.
                    else
                        state = -1;
                    break;
                case 2: // stato q2
                    if(ch == '0')
                        state = 1; //(100) dà resto 1
                    else if (ch == 1) 
                        state = 2; //(101) dà resto 2
                    else
                        state = -1;
                    break;
            }
        }
        return state == 0;
    }

    public static void main(String[] args) {
        // System.out.println(scan(args[0]) ? "OK" : "NOPE");

        String[] test = { 
                "110",
                "1001", 
                "111", 
                "10", 
                "101", 
                "11", 
                "1100",
                "1111", 
                "10010" 
        };

        for (String s : test) {
            System.out.print(s + " = " + Integer.parseInt(s,2) + " -> ");
            System.out.println(scan(s) ? "OK" : "NOPE");
        }
    }
}
