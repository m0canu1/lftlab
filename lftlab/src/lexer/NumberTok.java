package lexer;

public class NumberTok extends Token {
    public String lexeme = "";
    NumberTok(int tag, String s) {
        super(tag);
        lexeme = s;
    }
    public String toString() {
        return "<" + tag + ", " + lexeme + ">";
    }
}
