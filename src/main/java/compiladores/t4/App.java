// t4 Compiladores
// Max Marcio F Santos 758935

package compiladores.t4;

import java.io.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import compiladores.t4.COMPParser.*;

public class App {
    public static void main(String[] args) throws IOException {
        // Abre o arquivo de entrada e cria o lexer
        CharStream cs = CharStreams.fromFileName(args[0]);
        COMPLexer lex = new COMPLexer(cs);

        // Cria o parser e adiciona o errorListener a ele
        COMPParser parser = new COMPParser(new CommonTokenStream(lex));
        parser.removeErrorListeners();
        parser.addErrorListener(ErrorListener.I);

        // abre o arquivo de saida
        PrintWriter writer = new PrintWriter(args[1]); 
        
        try {
            ProgramaContext c = parser.programa();
            // Semantico visita o programa
            Semantico.I.visitPrograma(c); 
            // Verifica erros no semantico
            if (Semantico.I.getErros().isEmpty()) {
                // Gera o codigo
                Gerador gerador = new Gerador();
                gerador.visit(c);
                writer.print(gerador.getSaida().toString());
            } else{
                for (var msg : Semantico.I.getErros())
                     writer.println(msg);
                writer.println("Fim da compilacao");
            }

        } catch (ParseCancellationException exception) {
            // Caso um erro seja capturado ele é colocado no writer
            writer.println(exception.getMessage());     
            writer.println("Fim da compilacao!");   
        }
  
        writer.close();
    }
}
