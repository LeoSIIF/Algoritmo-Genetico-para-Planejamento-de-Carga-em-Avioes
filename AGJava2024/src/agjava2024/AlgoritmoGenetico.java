package agjava2024;

import java.io.*;
import java.util.*;

public class AlgoritmoGenetico {

    private int tamPopulacao;
    private int tamCromossomo = 0;
    private double capacidade;
    private double larguraMaxima;
    private double alturaMaxima;
    private double profundidadeMaxima;
    private int probMutacao;
    private int qtdeCruzamentos;
    private ArrayList<Produto> produtos = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> populacao = new ArrayList<>();
    private ArrayList<Integer> roletaVirtual = new ArrayList<>();

    public AlgoritmoGenetico(int tamanhoPopulacao, double capacidadePeso, double larguraMaxima, double alturaMaxima, double profundidadeMaxima, int probabilidadeMutacao, int qtdeCruzamentos) {
        this.tamPopulacao = tamanhoPopulacao;
        this.capacidade = capacidadePeso;
        this.larguraMaxima = larguraMaxima;
        this.alturaMaxima = alturaMaxima;
        this.profundidadeMaxima = profundidadeMaxima;
        this.probMutacao = probabilidadeMutacao;
        this.qtdeCruzamentos = qtdeCruzamentos;
    }

    public void executar() {
        this.criarPopulacao();
        this.gerarRoleta();
        System.out.println("Selecionado: " + roleta());
        for (int i = 0; i < this.tamPopulacao; i++) {
            System.out.println("Cromossomo " + i + ": " + populacao.get(i));
            System.out.println("Avaliacao: " + fitness(populacao.get(i)));
        }
        System.out.println("Indice do indivíduo de menor avaliação: " + indiceMenorAvaliacao());
    }

    public void carregaArquivo(String fileName) {
        String csvFile = fileName;
        String line = "";
        String[] produto = null;
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                produto = line.split(",");
                Produto novoProduto = new Produto();
                novoProduto.setDescricao(produto[0]);
                novoProduto.setPeso(Double.parseDouble(produto[1]));
                novoProduto.setLargura(Double.parseDouble(produto[2]));
                novoProduto.setAltura(Double.parseDouble(produto[3]));
                novoProduto.setProfundidade(Double.parseDouble(produto[4]));
                produtos.add(novoProduto);
                System.out.println(novoProduto);
                this.tamCromossomo++;
            }
            System.out.println("Tamanho do cromossomo: " + this.tamCromossomo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Integer> criarCromossomo() {
        ArrayList<Integer> novoCromossomo = new ArrayList<>();
        for (int i = 0; i < this.tamCromossomo; i++) {
            if (Math.random() < 0.6) {
                novoCromossomo.add(0);
            } else {
                novoCromossomo.add(1);
            }
        }
        return novoCromossomo;
    }

    private void criarPopulacao() {
        for (int i = 0; i < this.tamPopulacao; i++) {
            this.populacao.add(criarCromossomo());
        }
    }

    private double fitness(ArrayList<Integer> cromossomo) {
        double pesoTotal = 0, volumeTotal = 0;
        boolean excedeDimensoes = false;

        for (int i = 0; i < this.tamCromossomo; i++) {
            if (cromossomo.get(i) == 1) {
                Produto produto = produtos.get(i);
                pesoTotal += produto.getPeso();
                volumeTotal += produto.getLargura() * produto.getAltura() * produto.getProfundidade();

                // Verificação individual das dimensões
                if (produto.getLargura() > larguraMaxima || 
                    produto.getAltura() > alturaMaxima || 
                    produto.getProfundidade() > profundidadeMaxima) {
                    excedeDimensoes = true;
                }
            }
        }

        // Verificação das restrições de peso e dimensões
        if (pesoTotal > this.capacidade || excedeDimensoes) {
            return 0; // Penaliza se exceder qualquer restrição
        } else {
            return volumeTotal; // Retorna o volume total como fitness
        }
    }

    private void gerarRoleta() {
        ArrayList<Double> fitnessIndividuos = new ArrayList<>();
        double totalFitness = 0;
        for (int i = 0; i < this.tamPopulacao; i++) {
            double fitnessValue = fitness(this.populacao.get(i));
            fitnessIndividuos.add(fitnessValue);
            totalFitness += fitnessValue;
        }
        System.out.println("Soma total fitness: " + totalFitness);
        System.out.println("Notas: " + fitnessIndividuos);
        for (int i = 0; i < this.tamPopulacao; i++) {
            double qtdPosicoes = (fitnessIndividuos.get(i) / totalFitness) * 1000;
            for (int j = 0; j <= qtdPosicoes; j++) {
                roletaVirtual.add(i);
            }
        }
        System.out.println("Roleta Virtual: " + roletaVirtual);
    }

    private int roleta() {
        Random r = new Random();
        int selecionado = r.nextInt(roletaVirtual.size());
        return roletaVirtual.get(selecionado);
    }

    private ArrayList<ArrayList<Integer>> cruzamento() {
        ArrayList<Integer> filho1 = new ArrayList<>();
        ArrayList<Integer> filho2 = new ArrayList<>();
        ArrayList<ArrayList<Integer>> filhos = new ArrayList<>();
        ArrayList<Integer> pai1, pai2;
        int indice_pai1, indice_pai2;
        indice_pai1 = roleta();
        indice_pai2 = roleta();
        pai1 = populacao.get(indice_pai1);
        pai2 = populacao.get(indice_pai2);
        Random r = new Random();
        int pos = r.nextInt(this.tamCromossomo);
        for (int i = 0; i <= pos; i++) {
            filho1.add(pai1.get(i));
            filho2.add(pai2.get(i));
        }
        for (int i = pos + 1; i < this.tamCromossomo; i++) {
            filho1.add(pai2.get(i));
            filho2.add(pai1.get(i));
        }
        filhos.add(filho1);
        filhos.add(filho2);
        return filhos;
    }

    private void mutacao(ArrayList<Integer> filho) {
        Random r = new Random();
        int v = r.nextInt(100);
        if (v < this.probMutacao) {
            int ponto = r.nextInt(this.tamCromossomo);
            if (filho.get(ponto) == 1) {
                filho.set(ponto, 0);
            } else {
                filho.set(ponto, 1);
            }

            int ponto2 = r.nextInt(this.tamCromossomo);
            if (filho.get(ponto2) == 1) {
                filho.set(ponto2, 0);
            } else {
                filho.set(ponto2, 1);
            }

            System.out.println("Ocorreu mutação!");
        }
    }

    private int indiceMenorAvaliacao() {
        double menorAvaliacao = Double.MAX_VALUE;
        int indiceMenor = -1;

        for (int i = 0; i < this.tamPopulacao; i++) {
            double avaliacaoAtual = fitness(this.populacao.get(i));
            if (avaliacaoAtual < menorAvaliacao) {
                menorAvaliacao = avaliacaoAtual;
                indiceMenor = i;
            }
        }

        return indiceMenor;
    }
}
