/*
 * @author: Alexandru Mocanu
 * Matricola 813322
 */

package dfa;

class es1_11 {
    private static boolean scan(String s) {
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);
            switch (state) {
                case 0:
                    if(ch == '/')
                        state = 1;
                    else if (ch == '*' || ch == 'a')
                        state = 5;
                    else
                        state = -1;
                    break;
                case 1:
                    if(ch == '*')
                        state = 2;
                    else if (ch == '/' || ch == 'a')
                        state = 5;
                    else
                        state = -1;
                    break;
                case 2:
                    if (ch == 'a' || ch == '/')
                        state = 2;
                    else if(ch == '*')
                        state = 3;
                    else
                        state = -1;
                    break;
                case 3:
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
                    else if (ch == '*' || ch == 'a')
                        state = 5;
                    break;
                case 5:
                    if (ch == '/')
                        state = 1;
                    else if (ch == 'a' || ch == '*')
                        state = 5;
                    else
                        state = -1;
            }
        }
        return state == 4 || state == 5;
    }

    public static void main(String[] args){
        String[] test = {
                "aaa/****/aa",
                "aa/*a*a*/",
                "aaaa",
                "/****/",
                "/*aa*/",
                "*/a",
                "a/**/***a",
                "a/**/***/a",
                "a/**/aa/***/a",
                "aaa/*/aa", //NOPE
                "a/**//***a", //NOPE
                "aa/*aa"};  //NOPE

        for (String s : test) {
            System.out.print(s + " -> ");
            System.out.println(scan(s) ? "OK" : "NOPE");
        }
    }
}
