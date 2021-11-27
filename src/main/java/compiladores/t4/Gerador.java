// t4 Compiladores
// Max Marcio F Santos 758935

// Classe reponsavel gerar o codigo

package compiladores.t4;

import java.lang.invoke.StringConcatFactory;
import java.util.*;

import compiladores.t4.COMPParser.Atr_inContext;
import compiladores.t4.COMPParser.Base_cmdContext;
import compiladores.t4.COMPParser.Cmd_atribuicaoContext;
import compiladores.t4.COMPParser.Cmd_chamadaContext;
import compiladores.t4.COMPParser.Cmd_try_addContext;
import compiladores.t4.COMPParser.Decl_variavelContext;
import compiladores.t4.COMPParser.EaddContext;
import compiladores.t4.COMPParser.IdentificadorContext;
import compiladores.t4.COMPParser.Valor_constanteContext;

public class Gerador extends COMPBaseVisitor<Void> {    
    private StringBuilder saida = new StringBuilder();

    public StringBuilder getSaida(){
        return saida;
    }

    @Override
    public Void visitPrograma(COMPParser.ProgramaContext ctx) {
        return super.visitPrograma(ctx);
    }

    @Override
    public Void visitCore(COMPParser.CoreContext ctx) {
        saida.append("static void core_add(Entity& entity, const sol::table& lua_static_entity, const sol::table& spawn_info)\n{");
        endl();

        var corpo = ctx.corpo_core();
        
        for(var v : corpo.decl_variavel()){
            tab();
            saida.append(v.tipo().IDENT().getText() + " " + v.IDENT().getText() + ";");
            endl();
        }

        for(var v : corpo.base_cmd()){
            tab();
            visitBase_cmd(v);
            endl();
        }
    
        saida.append("}\n\n");
        return null;
    }

    @Override
    public Void visitComponent(COMPParser.ComponentContext ctx) {
        saida.append("struct " + ctx.IDENT() + "Component\n{\n");

        for(var v : ctx.component_corpo().decl_variavel()){
            tab();
            visitDecl_variavel(v);
            saida.append(";\n");
        }

        saida.append("}\n\n");

        return null;
    }    

    @Override
    public Void visitEadd(COMPParser.EaddContext ctx) {
        saida.append("static bool try_add_" + ctx.IDENT(0).toString().toLowerCase() + "_component(Entity& entity, const sol::table& lua_static_entity, const sol::table& spawn_info)\n{\n");

        String comp_name = ctx.IDENT(0).toString();
        String c_var = comp_name.toLowerCase() + "_c";
        String part = ctx.IDENT(1).toString();

        tab();
        saida.append("const sol::object " + part + " = lua_static_entity[" + part + "];");
        endl();
        tab();
        saida.append("if ( " + part + ".valid())");
        endl();
        tab();
        saida.append("{");
        endl();

        tab(2);
        saida.append("auto& " + c_var + " = entity.add< " + comp_name + " >();");
        endl(2);

        var corpo = ctx.eadd_corpo();
        for(var v : corpo.eadd_cmd()){
            tab(2);
            if(v.cmd_lua_atr() != null){
                saida.append("LuaManager::get_from_table(lua_static_entity, " + v.cmd_lua_atr().IDENT() + ", " + c_var + "." + v.cmd_lua_atr().identificador().getText() + ");");
            }else if(v.cmd_comp_atribuicao() != null)
            {
                saida.append(c_var + "." + v.cmd_comp_atribuicao().IDENT() + " = ");
                visitAtr_in(v.cmd_comp_atribuicao().atr_in());
            }else{
                visitBase_cmd(v.base_cmd());
            }
            endl(2);
        }

        endl();
        tab(2);
        saida.append("return true;");
        endl();
        tab();
        saida.append("}");
        endl();
        tab();
        saida.append("return false;");
        endl();

        saida.append("}\n");

        return null;
    }

    @Override
    public Void visitDecl_variavel(COMPParser.Decl_variavelContext ctx) {
        if(ctx.tipo().getText().equals("string"))
            saida.append("std::string");
        else if(ctx.tipo().getText().equals("table"))
            saida.append("sol::table");
        else if(ctx.tipo().getText().equals("uint"))
            saida.append("uint32_t");
        else if(ctx.tipo().IDENT() != null)
            saida.append(ctx.tipo().IDENT().getText());
        else
            saida.append(ctx.tipo().getText());

        saida.append(" " + ctx.IDENT().getText());

        return null;
    }

    @Override
    public Void visitCmd_chamada(COMPParser.Cmd_chamadaContext ctx) {
        
        visitIdentificador(ctx.identificador());

        saida.append("(");
        
        if(ctx.params().IDENT().size() > 0){
            saida.append(ctx.params().IDENT(0));
            if(ctx.params().IDENT().size() > 1)
                for(int i = 1; i < ctx.params().IDENT().size() - 2; i++)
                    saida.append("," + ctx.params().IDENT(i));
        }

        saida.append(")");
        return null;
    }

    @Override
    public Void visitCmd_atribuicao(Cmd_atribuicaoContext ctx) {
        visitIdentificador(ctx.identificador());
        visitAtr_in(ctx.atr_in());    
        saida.append(";");
        return null;
    }

    @Override
    public Void visitAtr_in(Atr_inContext ctx) {
        if(ctx.cmd_chamada() != null)
            visitCmd_chamada(ctx.cmd_chamada());
        else if(ctx.valor_constante() != null)
            visitValor_constante(ctx.valor_constante());
        else if(ctx.identificador() != null)
            visitIdentificador(ctx.identificador());

        return null;
    }

    @Override
    public Void visitValor_constante(Valor_constanteContext ctx) {
        saida.append(ctx.getText());
        return null;
    }

    @Override
    public Void visitIdentificador(IdentificadorContext ctx) {
        saida.append(ctx.IDENT(0));
        if(ctx.IDENT().size() > 1)
            for(int i = 1; i < ctx.IDENT().size() - 2; i++)
                saida.append("." + ctx.IDENT(i));

        return null;
    }

    @Override
    public Void visitCmd_try_add(Cmd_try_addContext ctx) {
        saida.append("try_add_" + ctx.IDENT().toString().toLowerCase() + "_coponent(entity, lua_static_entity, spawn_info)");
        return null;
    }

    private void tab() {
        saida.append("    ");
    }

    private void tab(int count) {
        for(int i = 0; i < count; i++){
            saida.append("    ");
        }
    }

    private void endl(){
        saida.append("\n");
    }

    private void endl(int count) {
        for(int i = 0; i < count; i++){
            saida.append("\n");
        }
    }
    
    @Override
    public Void visitBase_cmd(Base_cmdContext ctx) {
        if(ctx.cmd_try_add() != null){
            visitCmd_try_add(ctx.cmd_try_add());
        }else if(ctx.cmd_chamada() != null){
            visitCmd_chamada(ctx.cmd_chamada());
            saida.append(";\n");
        }else{
            visitCmd_atribuicao(ctx.cmd_atribuicao());
        }
        return null;
    }
}
