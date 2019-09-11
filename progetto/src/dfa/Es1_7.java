package dfa;/*
 * @author Alexandru Mocanu
 */

public class Es1_7 {
    public static boolean scan(String s) {
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);
            switch (state) {
                case 0:
                    if(ch == '/')
                        state = 1;
                    else
                        state = -1;
                    break;
                case 1:
                    if(ch == '*')
                        state = 2;
                    else
                        state = -1;
                    break;
                case 2: /* stato q2 */
                    if (ch == 'a' || ch == '/')
                        state = 2;
                    else if(ch == '*')
                        state = 3;
                    else
                        state = -1;
                    break;
                case 3: /* stato q3 */
                    if (ch == '*')
                        state = 3;
                    else if (ch == 'a')
                        state = 2;
                    else if (ch == '/')
                        state = 4;
                    else
                        state = -1;
                    break;
                case 4: /* stato q4 */
                    if (ch == '/')
                        state = 2;
                    break;
            }
        }
        return state == 4;
    }

    public static void main(String[] args){
        String[] test = {"/****/",
                "/*a*a*/",
                "/*a/**/",
                "/**a///a/a**/",
                "/**/",
                "/*/*/",
                "/*/", //nope
                "/**/***/"};  //nope

        for (String s : test) {
            System.out.print(s + " -> ");
            System.out.println(scan(s) ? "OK" : "NOPE");
        }
    }
}
