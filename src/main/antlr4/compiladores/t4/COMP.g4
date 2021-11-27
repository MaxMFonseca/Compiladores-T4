// t4 Compiladores
// Max Marcio F Santos 758935

grammar COMP;

// Lex
PALAVRAS_CHAVE: 'CORE' | 'COMPONENT' | 'EADD' | 'END';

IDENT : ('a'..'z'|'A'..'Z') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*;

NUM_INT : ('0'..'9')+;
NUM_REAL : ('0'..'9')+ ('.' ('0'..'9')+)?;

SIMBS : ':' | ',' | '.' | '(' | ')' | '[' | ']' | '<-' | '..' | '^' | '&';

WS : ( ' ' | '\t' | '\r' | '\n' ) -> skip;

CADEIA_NAO_FECHADA : '"' (~('\n' | '\r' | '"'))* ('\n' | '\r');
CADEIA : '"' (~('\n' | '\r' | '"'))* '"';

ERRO: .;

// regras sintaticas
programa: (core | eadd | component)* EOF;

core: 'CORE' corpo_core 'END';
corpo_core: decl_variavel* base_cmd*;

component: 'COMPONENT' IDENT component_corpo 'END';
component_corpo: decl_variavel*;

eadd: 'EADD' IDENT '(' IDENT ')' eadd_corpo 'END';
eadd_corpo: decl_variavel* eadd_cmd*;
eadd_cmd: cmd_lua_atr | cmd_comp_atribuicao | base_cmd;
cmd_comp_atribuicao: 'comp' '.' IDENT '=' atr_in;
cmd_lua_atr: 'lua_get('identificador ',' IDENT')'; 

base_cmd: cmd_try_add | cmd_chamada | cmd_atribuicao;
cmd_atribuicao:  identificador '=' atr_in;
cmd_try_add: 'try_add('IDENT')';
cmd_chamada: identificador '('params')';

decl_variavel: IDENT ':' tipo;
atr_in: (cmd_chamada | valor_constante | identificador);
params: IDENT (',' IDENT)*;
identificador: IDENT ('.' IDENT)*;
valor_constante: CADEIA | NUM_INT | NUM_REAL | 'true' | 'false';
tipo: 'uint' | 'string' | 'table' | 'bool' | IDENT;