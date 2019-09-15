import java.io.BufferedReader;

import com.sun.org.apache.xerces.internal.util.SymbolTable;

import Lexer.java;
import OpCode.java;
import Token.java;
import CodeGenerator.java;


public class Translator {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count = 0;

    public Translator (Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    /**
     * Legge il prossimo token e lo stampa
     */
    void move() {
        look = lex.lexical_scan(pbr);
        if (look != null) // evito di stampare "token = null"
            System.out.println("token = " + look);
    }

    void error(String s) {
        throw new Error("near line " + lex.line + ": " + s);
    }

    /**
     * @param t: è il token atteso. Se VERO, legge il prossimo token (se non è EOF).
     */
    void match(int t) {
        if (look.tag == t) {
            if (look.tag != Tag.EOF)
                move();
        } else
            error("syntax error");
    }


    public void prog() {
        //completare
        int lnext_prog = code.newLabel(); //S.next = S.newLabeL()
        statlist(lnext_prog);
        code.emitLabel(lnext_prog);
        match(Tag.EOF);
        try {
            code.toJasmin();
        } catch (IOException e) {
            System.out.println("IO error\n");
        }
        //completare
    }

    public void stat (int lnext) {
        switch (look.tag) {
            case Tag.PRINT:
                match(Tag.PRINT);
                match('(');
                expr();
                code.emit(OpCode.invokestatic,1);
                match(')');
                break;
            case Tag.READ: 
                match(Tag.READ); 
                match('('); 
                if (look.tag==Tag.ID) { 
                    int read_id_addr = st.lookupAddress(((Word)look).lexeme);
                    if (read_id_addr==-1) { 
                        read_id_addr = count; 
                        st.insert(((Word)look).lexeme,count++);
                    } 
                    match(Tag.ID); 
                    match(’)’; 
                    code.emit(OpCode.invokestatic,0);
                    code.emit(OpCode.istore,read_id_addr);
                } else
                    error("Error in grammar (stat) after read( with " + look);
                break;
                //completare
            default:
                break;
        }

    }
    
    private void bexpr(int ltrue, int lfalse) {
         // ... completare ... 
         expr(); 
         if (look == Word.eq) { 
             match(Tag.RELOP); 
             expr(); 
             // ... completare ... 
            } // ... completare ... 
    } 
    
    // ... completare ... 
        
    /**
     *
     */
    private void bexpr(int ltrue, int lfalse) { //label true, label false
        if (look.tag == '(' || look.tag == Tag.NUM || look.tag == Tag.ID) {
            expr();
            switch (look.tag) {
                case Word.lt:
                    
                    break;
                case Word.gt:

                break;
                case Word.eq:

                break;
                case Word.le:

                break;
            case Word.ne:

                break;
            case Word.ge:

                break;
                default:
                    break;
            }
            if (look.tag == Tag.RELOP) {
                match(Tag.RELOP);
                expr();
            } else
                error("Erroneous character in bexpr. Invalid boolean expression");
        } // TODO non ci va l'else?
    }


    /**
     *
     */
    private void expr() {
        if (look.tag == '(' || look.tag == Tag.NUM || look.tag == Tag.ID) {
            term();
            exprp();
        } else
            error("Erroneous character in expr. Found " + look);
    }

    private void exprp() { 
        switch(look.tag) { 
            case '+': 
                match('+'); 
                term(); 
                code.emit(OpCode.iadd); 
                exprp(); 
            break; 
            case '-':
                match('-');
                term();
                code.emit(OpCode.isub);
                exprp();
            break;
        case ';':
        case Tag.EOF:
        case ')':
        case Tag.RELOP:
        case Tag.WHEN:
        case Tag.ELSE:
        case '}':
            break;
        default:
            error("Erroneous character in exprp. Found " + look);
            break;
            // ... completare ... 
        } 
    } 

    private void term() {
        if (look.tag == '(' || look.tag == Tag.NUM || look.tag == Tag.ID) {
            fact();
            termp();
        } else
            error("Erroneous character in term. Found " + look);
    }

    /**
     *
     */
    private void termp() {
        switch (look.tag) {
        case '*':
            match('*');
            fact();
            code.emit(OpCode.imul);
            termp();
            break;
        case '/':
            match('/');
            fact();
            code.emit(OpCode.idiv)
            termp();
            break;
        case '+':
        case '-':
        case ';':
        case Tag.EOF:
        case ')':
        case Tag.RELOP:
        case Tag.WHEN:
        case Tag.ELSE:
        case '}':
            break;
        default:
            error("Erroneous character in termp. Found " + look);
            break;
        }
    }

    /**
     *
     */
    private void fact() {
        switch (look.tag) {
        case '(':
            match('(');
            expr();
            if (look.tag == ')')
                match(')');
            else
                error("Erroneous character in Fact. Expected ) but missing.");
            break;
        case Tag.NUM:
            int constant = Integer.valueOf(((NumberTok)look).lexeme); //emit(ldc(NUM))
            match(Tag.NUM);
            if (look.tag == '(')
                error("Erroneous character in fact. Found: NUM(");
            break;
        case Tag.ID:
            int id_addr = st.lookupAddress(((Word)look).lexeme);
            code.emit(OpCode.iload, id_addr);
            match(Tag.ID);
            if (look.tag == '(')
                error("Erroneous character in fact. Found: ID(");
            break;
        default:
            error("Erroneous character in Fact. Found " + look);
            break;
        }
    }
    
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        // String path = "testtranslator1.pas";
        // String path = "testExprTranslator.pas";
        // String path = "test1.pas";
        // String path = "test2.pas";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Translator translator = new Translator(lex, br);
            translator.prog();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}