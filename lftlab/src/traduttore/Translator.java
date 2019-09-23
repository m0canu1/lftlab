package traduttore;

import lexer.*;

import java.io.*;

public class Translator {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    private SymbolTable st = new SymbolTable();
    private CodeGenerator code = new CodeGenerator();
    private int count = 0;

    private Translator(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    /**
     * Legge il prossimo token e lo stampa
     */
    private void move() {
        look = lex.lexical_scan(pbr);
        if (look != null) // evito di stampare "token = null"
            System.out.println("token = " + look);
    }

    private void error(String s) {
        throw new Error("near line " + lex.line + ": " + s);
    }

    /**
     * @param t: è il token atteso. Se VERO, legge il prossimo token (se non è EOF).
     */
    private void match(int t) {
        if (look.tag == t) {
            if (look.tag != Tag.EOF)
                move();
        } else
            error("syntax error");
    }

    /**
     * Metodo di partenza.
     * GUI { ID , print , read , case , while , { }
     */
    private void prog() {
        if (look.tag == Tag.ID || look.tag == Tag.PRINT || look.tag ==Tag.READ || look.tag ==Tag.CASE || look.tag ==Tag.WHILE || look.tag == '{') {
            int lnext_prog = code.newLabel(); //S.next = S.newLabeL()
            statlist(lnext_prog);
            code.emitLabel(lnext_prog);
            match(Tag.EOF);
            try {
                code.toJasmin();
            } catch (IOException e) {
                System.out.println("IO error\n");
            }
        }
        else 
            error("Error in prog. Found: " + look);
    }

    /**
     * É il metodo che si occupa di guardare le istruzioni chiave
     * GUI { ID , print , read , case , while , { }
     */
    private void stat(int lnext) {
        switch (look.tag) {
            case Tag.ID:
                if(look.tag == Tag.ID) {
//                    int next_id = code.newLabel();
                    int id_addr = st.lookupAddress(((Word)look).lexeme);
                    if (id_addr == -1) { // controlla se l'ID è già assegnato a un indirizzo
                        id_addr = count; //assegna all'indirizzo puntato da count
                        st.insert((((Word)look).lexeme), count++); //inserisce nella Symbol Table
                    }
                    match(Tag.ID);
                    match(Tag.ASSIGN);
                    expr();
                    code.emit(OpCode.istore, id_addr); //istore nell'indirizzo di id_addr
//                    code.emitLabel(next_id);
                } else
                    error("Error in grammar (stat) after ID, found " + look);
                break;
            case Tag.PRINT:
//                int next_print = code.newLabel();
                match(Tag.PRINT);
                match('(');
                expr();
                code.emit(OpCode.invokestatic, 1); // 1 invoca la funzione print
//                code.emitLabel(next_print);
                match(')');
                break;
            case Tag.READ:
//                int next_read = code.newLabel();
                match(Tag.READ); 
                match('('); 
                if (look.tag==Tag.ID) { 
                    int read_id_addr = st.lookupAddress(((Word)look).lexeme);
                    if (read_id_addr==-1) { 
                        read_id_addr = count;
                        st.insert(((Word)look).lexeme,count++);
                    }
                    match(Tag.ID); 
                    match(')');
                    code.emit(OpCode.invokestatic,0);
                    code.emit(OpCode.istore,read_id_addr);
//                    code.emitLabel(next_read);
                } else
                    error("Error in grammar (stat) after read( with " + look);
                break;
            case Tag.CASE:
                match(Tag.CASE);
                whenlist(lnext);
                if (look.tag == Tag.ELSE) {
                    match(Tag.ELSE);
                    stat(lnext);
                    code.emitLabel(lnext); //S.next
                } else
                    error("Error in grammar (stat) after CASE. Expected ELSE but found: " + look);
                break;
            case Tag.WHILE:
                match(Tag.WHILE);
                match('(');

                int ltrue_stat_while = code.newLabel(); //B.true = newLabel()
                int lnext_stat_while = code.newLabel(); //begin = newLabel(). Dove iniziare se la condizione è vera
                int end_while = code.newLabel(); //Label da cui continuare l'esecuzione se la condizione non è vera

                if (ltrue_stat_while != 0)
                    code.emit(OpCode.GOto, ltrue_stat_while);
                code.emitLabel(ltrue_stat_while);

                bexpr(lnext_stat_while, end_while);
                match(')');

                code.emitLabel(lnext_stat_while);
                stat(ltrue_stat_while);
                code.emit(OpCode.GOto, ltrue_stat_while);

                code.emitLabel(end_while);
                break;
            case '{':
                match('{');
                statlist(lnext);
                match('}');
                break;
            default:
                break;
        }

    }

    /**
     * GUI { ID , print , read , case , while , { }
     */
    private void statlist(int lnext){
        if (look.tag == Tag.ID || look.tag == Tag.PRINT || look.tag == Tag.READ || look.tag == Tag.CASE || look.tag == Tag.WHILE || look.tag == '{') {
            stat(lnext);
            statlistp(lnext);
        } else
            error("Error in statlist. Found " + look);
    }

    /**
     * GUI { ; , EOF , } }
     */
    private void statlistp(int lnext){
        switch(look.tag) {
            case ';':
                match(';');
                stat(lnext);
                statlistp(lnext);
            case Tag.EOF:
            case '}':
                break; //epsilon
            default:
                error("Error in statlistp. Found " + look);
                break;
        }
    }

    /**
     * GUI { when }
     */
    private void whenlist(int lnext) {
        if (look.tag == Tag.WHEN) {
            whenitem(lnext);
            whenlistp(lnext);
        } else
            error("Erroneous character in whenlist. Expected WHEN but found: " + look);
    }

    /**
     * GUI { when , else }
     */
    private void whenlistp(int lnext){
        switch(look.tag) {
            case Tag.WHEN:
                whenitem(lnext);
                whenlistp(lnext);
                break;
            case Tag.ELSE:
                break;
            default:
                error("Erroneous character in whenlistp. Expected ) but found: " + look); //TODO è giusto l'expected??
                break;
        }
    }

    /**
     * GUI { when }
     */
    private void whenitem(int lnext){
        if(look.tag == Tag.WHEN) {
            match(Tag.WHEN);
            int itrue = code.newLabel();
            int next_when = code.newLabel();
            if (look.tag == '('){
                match('(');
                bexpr(itrue, next_when);
                if (look.tag == ')') {
                    match(')');
                    code.emitLabel(itrue);
                    stat(next_when);
                    code.emit(OpCode.GOto, lnext);
                    code.emitLabel(next_when);
                } else
                    error("Erroneous character in (whenitem) after (bexpr). Expected ) but found: " + look);
            }
        } else
            error("Error in whenitem. Expected WHEN but found: " + look);
    }

    /**
     * GUI { ( , NUM , ID }
     */
    private void bexpr(int ltrue, int lfalse) { //label true, label false
        if (look.tag == '(' || look.tag == Tag.NUM || look.tag == Tag.ID) {
            expr(); //TODO non ci vanno parametri?
            /* Non funziona lo switch */
            if (look == Word.eq) {
                match(Tag.RELOP);
                expr();
                code.emit(OpCode.if_icmpeq, ltrue); // emit('if_icmpep', B.true)
                code.emit(OpCode.GOto, lfalse); // emit('goto' B.false)
            } else if (look == Word.ne) {
                match(Tag.RELOP);
                expr();
                code.emit(OpCode.if_icmpne, ltrue); // emit('if_icmpne', B.true)
                code.emit(OpCode.GOto, lfalse); // emit('goto' B.false)
            } else if (look == Word.le) {
                match(Tag.RELOP);
                expr();
                code.emit(OpCode.if_icmple, ltrue); // emit('if_icmple', B.true)
                code.emit(OpCode.GOto, lfalse); // emit('goto' B.false)
            } else if (look == Word.ge) {
                match(Tag.RELOP);
                expr();
                code.emit(OpCode.if_icmpge, ltrue); // emit('if_icmpge', B.true)
                code.emit(OpCode.GOto, lfalse); // emit('goto' B.false)
            } else if (look == Word.lt) {
                match(Tag.RELOP);
                expr();
                code.emit(OpCode.if_icmplt, ltrue); // emit('if_icmplt', B.true)
                code.emit(OpCode.GOto, lfalse); // emit('goto' B.false)
            } else if (look == Word.gt) {
                match(Tag.RELOP);
                expr();
                code.emit(OpCode.if_icmpgt, ltrue); // emit('if_icmpgt', B.true)
                code.emit(OpCode.GOto, lfalse); // emit('goto' B.false)
            }
        } else {
            error("Erroneous character in Bexpr: invalid boolean expression");
        }
    }


    /**
     * GUI { ( , NUM , ID }
     */
    private void expr() {
        if (look.tag == '(' || look.tag == Tag.NUM || look.tag == Tag.ID) {
            term();
            exprp();
        } else
            error("Erroneous character in expr. Found " + look);
    }

    /**
     * GUI { + , - , ) , ; , RELOP. EOF, when , else , } }
     */
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

    /**
     * GUI { ( , NUM , ID }
     */
    private void term() {
        if (look.tag == '(' || look.tag == Tag.NUM || look.tag == Tag.ID) {
            fact();
            termp();
        } else
            error("Erroneous character in term. Found " + look);
    }

    /**
     * GUI { * , / , + , - , ; , EOF , ) , when , else , } }
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
            code.emit(OpCode.idiv);
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
     * GUI { ( , NUM , ID }
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
            int constant = Integer.parseInt(((NumberTok)look).lexeme);
            code.emit(OpCode.ldc, constant); //emit(ldc(NUM))
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
        String path = "src/traduttore/test1.pas";
//        String path = "src/traduttore/A.pas";
        // String path = "B.pas";
        // String path = "C.pas";
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
