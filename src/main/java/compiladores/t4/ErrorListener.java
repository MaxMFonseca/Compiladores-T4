// t4 Compiladores
// Max Marcio F Santos 758935

// Error Listener

package compiladores.t4;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;

public class ErrorListener extends BaseErrorListener { 
    // Intancia do error listener
    public static final ErrorListener I = new ErrorListener();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
            int charPositionInLine, String msg, RecognitionException e) 
                throws ParseCancellationException{
        
        Token token = (Token) offendingSymbol;
        String name = COMPLexer.VOCABULARY.getSymbolicName(token.getType());
        
        String buffer = "";

        // Erros lexicos
        if (name != null && name.equals("ERRO"))
            buffer = "Linha " + token.getLine() + ": " + token.getText() + " - simbolo nao identificado";
        else if (name != null && name.equals("CADEIA_NAO_FECHADA"))
            buffer = "Linha " + token.getLine() + ": cadeia literal nao fechada";
        // Erros sintaticos
        else if (token.getType() == Token.EOF)
            buffer = "Linha " + token.getLine() + ": erro sintatico proximo a EOF";
        else
            buffer = "Linha " + token.getLine() + ": erro sintatico proximo a " + token.getText();

        throw new ParseCancellationException(buffer);
    }
}