<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>

<h1>Algoritmo Genético para Planejamento de Carga em Aviões</h1>

<p>Este projeto implementa um algoritmo genético para otimizar a distribuição de carga em um avião, maximizando a utilização do espaço disponível e balanceando o peso.</p>

<h2>Objetivo</h2>
<p>Aplicar os conhecimentos adquiridos sobre algoritmos genéticos no contexto de um problema de otimização real, adaptando o algoritmo genético para resolver o problema de planejamento de carga em aviões.</p>

<h2>Parâmetros</h2>
<ul>
    <li><strong>Capacidade de Peso:</strong> Capacidade máxima de peso que o avião pode transportar (em kg).</li>
    <li><strong>Dimensões Máximas:</strong> Dimensões máximas da área de carga do avião (em cm), representada por largura, altura e profundidade.</li>
</ul>

<h2>Modelagem do Problema</h2>

<h3>Definição dos Parâmetros do Problema</h3>
<ul>
    <li><strong>capacidadePeso:</strong> Capacidade máxima de peso do avião (em kg).</li>
    <li><strong>larguraMaxima, alturaMaxima, profundidadeMaxima:</strong> Dimensões máximas da área de carga do avião (em cm).</li>
</ul>

<h3>Representação dos Cromossomos</h3>
<p>Os cromossomos são representados como vetores de bits, onde cada bit indica a presença (1) ou ausência (0) de um item de carga no avião.</p>

<h3>Função de Avaliação (Fitness)</h3>
<p>A função de avaliação calcula o volume total dos itens selecionados, penalizando soluções que excedem o peso máximo permitido ou as dimensões máximas da área de carga do avião.</p>

<h2>Implementação</h2>

<h3>Adaptação do Código</h3>
<p>O código foi adaptado do problema clássico da mochila para considerar as restrições adicionais de dimensões. A função de avaliação foi modificada para penalizar soluções inválidas e calcular o benefício com base no volume de carga.</p>

<h3>Estrutura do Projeto</h3>
<p>O projeto consiste em três classes principais:</p>
<ul>
    <li><code>AGJava2024</code>: Classe principal que inicializa e executa o algoritmo genético.</li>
    <li><code>AlgoritmoGenetico</code>: Classe que implementa o algoritmo genético, incluindo a criação da população, a função de avaliação, cruzamento e mutação.</li>
    <li><code>Produto</code>: Classe que representa os itens de carga, com atributos de descrição, peso e dimensões.</li>
</ul>

<h3>Dados de Entrada</h3>
<p>Os dados de entrada são fornecidos em um arquivo CSV (<code>dados.csv</code>) com a seguinte estrutura:</p>
<pre>
Descricao,Peso,Largura,Altura,Profundidade
Item1,50,100,100,100
Item2,30,150,100,200
...
</pre>

<h3>Como Executar</h3>
<ol>
    <li>Clone este repositório.</li>
    <li>Certifique-se de que o arquivo <code>dados.csv</code> está no diretório raiz do projeto.</li>
    <li>Compile e execute o projeto utilizando um ambiente de desenvolvimento Java ou via linha de comando:</li>
</ol>
<pre>
javac -d bin src/agjava2024/*.java
java -cp bin agjava2024.AGJava2024
</pre>

</body>
</html>
