import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Analisador {

   
    private static int charClass;
    private static char[] lexeme = new char[100];
    private static char nextChar;
    private static int lexLen;
    private static int token;
    private static int nextToken;
    private static BufferedReader in_fp;

   
    private static final int LETTER = 0;
    private static final int DIGIT = 1;
    private static final int UNKNOWN = 99;

   
    private static final int INT_LIT = 10;
    private static final int IDENT = 11;
    private static final int ASSIGN_OP = 20;
    private static final int ADD_OP = 21;
    private static final int SUB_OP = 22;
    private static final int MULT_OP = 23;
    private static final int DIV_OP = 24;
    private static final int LEFT_PAREN = 25;
    private static final int RIGHT_PAREN = 26;
    private static final int FOR_PAL = 20;
    private static final int IF_PAL = 21;
    private static final int ELSE_PAL = 22;
    private static final int WHILE_PAL = 23;
    private static final int DO_PAL = 24;
    private static final int INT_PAL = 25;
    private static final int FLOAT_PAL = 26;
    private static final int SWITCH_PAL = 27;

    private static int lookup(char ch) {
        switch (ch) {
            case '(':
                addChar();
                nextToken = LEFT_PAREN;
                break;
            case ')':
                addChar();
                nextToken = RIGHT_PAREN;
                break;
            case '+':
                addChar();
                nextToken = ADD_OP;
                break;
            case '-':
                addChar();
                nextToken = SUB_OP;
                break;
            case '*':
                addChar();
                nextToken = MULT_OP;
                break;
            case '/':
                addChar();
                nextToken = DIV_OP;
                break;
            default:
                addChar();
                nextToken = -1; // EOF ou desconhecido
                break;
        }
        return nextToken;
    }

    private static void addChar() {
        if (lexLen <= 98) {
            lexeme[lexLen++] = nextChar;
            lexeme[lexLen] = 0;
        } else {
            System.out.println("Error - lexeme is too long");
        }
    }

    private static void getChar() {
        try {
            int readChar = in_fp.read(); // Le o proximo caractere do arquivo
            if (readChar != -1) { // Verifica se n é o fim do arquivo
                nextChar = (char) readChar;
                if (Character.isLetter(nextChar))
                    charClass = LETTER;
                else if (Character.isDigit(nextChar))
                    charClass = DIGIT;
                else
                    charClass = UNKNOWN;
            } else {
                charClass = -1; // EOF
            }
        } catch (IOException e) {
            charClass = -1; // EOF em caso de erro de leitura
        }
    }

    private static void getNonBlank() {
        while (Character.isWhitespace(nextChar))
            getChar();
    }

    // isEndOfFile()
    // determina se o arquivo que esta sendo lido chegou ao fim
    // ready() eh um metodo do bufferedreader que retorna true se a stream esta
    // pronta
    // para ser lida.
    // Se o arquivo chegou ao fim, o metodo ready() retornara false.
    public static boolean isEndOfFile() {
        try {
            return !in_fp.ready();
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
    }

    private static int lex() {
        lexLen = 0;
        getNonBlank();
        switch (charClass) {
          
            case LETTER:
                addChar();
                getChar();
                while (charClass == LETTER || charClass == DIGIT) {
                    addChar();
                    getChar();
                }
                String lexemeStr = new String(lexeme).trim();
                switch (lexemeStr) {
                    case "for":
                        nextToken = FOR_PAL;
                        break;
                    case "if":
                        nextToken = IF_PAL;
                        break;
                    case "else":
                        nextToken = ELSE_PAL;
                        break;
                    case "while":
                        nextToken = WHILE_PAL;
                        break;
                    case "do":
                        nextToken = DO_PAL;
                        break;
                    case "int":
                        nextToken = INT_PAL;
                        break;
                    case "float":
                        nextToken = FLOAT_PAL;
                        break;
                    case "switch":
                        nextToken = SWITCH_PAL;
                        break;
                    default:
                        if (isEndOfFile()) {
                            nextToken = -1;
                        } else {
                            nextToken = IDENT;
                        }
                        break;
                }
                break;

           
            case DIGIT:
                addChar();
                getChar();
                while (charClass == DIGIT) {
                    addChar();
                    getChar();
                }
                nextToken = INT_LIT;
                break;

            
            case UNKNOWN:
                lookup(nextChar);
                getChar();
                break;

        // fim do arq
            case -1:
                nextToken = -1;
                lexeme = new char[] { 'E', 'O', 'F' };
                break;
        } // fim do switch
        System.out.printf("Next token is: %d, Next lexeme is %s\n", nextToken, new String(lexeme).trim());
        return nextToken;
    }

    public static void main(String[] args) {
        try {
            in_fp = new BufferedReader(new FileReader("front.in"));
            getChar();
            do {
                lex();
            } while (nextToken != -1); // EOF 
        } catch (IOException e) {
            System.out.println("ERROR - cannot open front.in");
        }
    }

}
