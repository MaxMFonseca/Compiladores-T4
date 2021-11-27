# Compiladores-T4

O T4 da disciplina de Compiladores da UFSCar(ENPE 2021/1) consistia em criar uma linguagem e seu compilador.

O tabalho foi feito utilizando JAVA e ANTLR4.

Video de apresentação: https://github.com/MaxMFonseca/Compiladores-T4/blob/main/Video%20explicativo.mp4

### Pré-requisitos
```
Java 8
Maven 3.8.2
```

### Para compilar
```
$ mvn clean compile assembly:single
```

### Para executar
```
$ java -jar <Root>\target\t4-1.0-SNAPSHOT-jar-with-dependencies.jar <entrada> <saída>
```

### Semantica
Componente: 
```
COMPONENT <NomeComponente>
	<NomeVariavel> : <Tipo>
END
```

Inicializa o componente (try_add): 
```
EADD <NomeComponente> (<Verifcar existencia>)
	-- Corpo --
END
```

Adiciona todos os componentes a entidades: 
```
CORE
	try_add(<NomeComponente>)
END
```

Exemplo: in.comp -> out.cpp

YETODO!

---
Trabalho feito por Max Marcio F Santos - 758935
