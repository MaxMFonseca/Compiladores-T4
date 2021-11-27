// t3 Compiladores
// Max Marcio F Santos 758935

// Analizador semantico

package compiladores.t4;

import java.util.*;
import org.antlr.v4.runtime.tree.TerminalNode;

import compiladores.t4.COMPParser.Cmd_try_addContext;
import compiladores.t4.COMPParser.ComponentContext;
import compiladores.t4.COMPParser.CoreContext;
import compiladores.t4.COMPParser.Corpo_coreContext;
import compiladores.t4.COMPParser.EaddContext;

public class Semantico extends COMPBaseVisitor<Void> {
    // Intancia do Semantico
    public static final Semantico I = new Semantico();

    private ArrayList<String> erros = new ArrayList<>();

    private ArrayList<String> comp_types = new ArrayList<>();

    @Override
    public Void visitPrograma(COMPParser.ProgramaContext ctx) {
        if(ctx.core().size() > 1)
            erros.add("SEMANTICO: Mais de um Core declarado!");
        return super.visitPrograma(ctx);
    }

    @Override
    public Void visitCore(CoreContext ctx) {
        for(var v : ctx.corpo_core().base_cmd()){
            if(v.cmd_try_add() != null){
                visitCmd_try_add(v.cmd_try_add());
            }
        }
        return null;
    }

    @Override
    public Void visitEadd(EaddContext ctx) {
        if(!comp_types.contains(ctx.IDENT(0).getText()))
            erros.add("SEMANTICO: Componente nao existe! -- " + ctx.IDENT(0).getText());

        return null;
    }

    @Override
    public Void visitComponent(ComponentContext ctx) {
        if(comp_types.contains(ctx.IDENT().getText())){
            erros.add("SEMANTICO: Redeclaracao de componente! -- " + ctx.IDENT().getText());
        }else{
            comp_types.add(ctx.IDENT().getText());
        }

        return null;     
    }

    public ArrayList<String> getErros(){
        return erros;
    }

    @Override
    public Void visitCmd_try_add(Cmd_try_addContext ctx) {
        if(!comp_types.contains(ctx.IDENT().getText())){
            erros.add("SEMANTICO: Componente nao existe! -- " + ctx.IDENT().getText());
        }
        return null;
    }
}
