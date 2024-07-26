package agjava2024;

import java.io.*;
import java.util.*;

public class AlgoritmoGenetico {
    private int tamPopulacao;
    private int tamCromossomo = 0;
    private double capacidadePeso;
    private double larguraMaxima;
    private double alturaMaxima;
    private double profundidadeMaxima;
    private int probMutacao;
    private int qtdeCruzamentos;
    private int numeroGeracoes;
    private ArrayList<Produto> produtos = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> populacao = new ArrayList<>();
    private ArrayList<Integer> roletaVirtual = new ArrayList<>();

    public AlgoritmoGenetico(int tamanhoPopulacao, double capacidadePeso, double larguraMaxima,
                             double alturaMaxima, double profundidadeMaxima, int probabilidadeMutacao, 
                             int qtdeCruzamentos, int numGeracoes) {
        this.tamPopulacao = tamanhoPopulacao;
        this.capacidadePeso = capacidadePeso;
        this.larguraMaxima = larguraMaxima;
        this.alturaMaxima = alturaMaxima;
        this.profundidadeMaxima = profundidadeMaxima;
        this.probMutacao = probabilidadeMutacao;
        this.qtdeCruzamentos = qtdeCruzamentos;
        this.numeroGeracoes = numGeracoes;
    }

    public void executar() {
        this.criarPopulacao();
        for (int i = 0; i < this.numeroGeracoes; i++) {
            System.out.println("Geracao: " + i);
            mostraPopulacao();
            operadoresGeneticos();
            novoPopulacao();
        }
        mostrarCargaAviao(this.populacao.get(obterMelhor()));
    }

    public void mostraPopulacao() {
        for (int i = 0; i < this.tamPopulacao; i++) {
            System.out.println("Cromossomo " + i + ":" + populacao.get(i));
            System.out.println("Avaliacao:" + fitness(populacao.get(i)));
        }
    }

    public void carregaArquivo(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] produto = line.split(",");
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
            System.out.println("Tamanho do cromossomo:" + this.tamCromossomo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Integer> criarCromossomo() {
        ArrayList<Integer> novoCromossomo = new ArrayList<>();
        for (int i = 0; i < this.tamCromossomo; i++) {
            if (Math.random() < 0.6)
                novoCromossomo.add(0);
            else
                novoCromossomo.add(1);
        }
        return novoCromossomo;
    }

    private void criarPopulacao() {
        for (int i = 0; i < this.tamPopulacao; i++)
            this.populacao.add(criarCromossomo());
    }

    private double fitness(ArrayList<Integer> cromossomo) {
        double pesoTotal = 0, volumeTotal = 0;
        boolean excedeDimensoes = false;

        for (int i = 0; i < this.tamCromossomo; i++) {
            if (cromossomo.get(i) == 1) {
                Produto produto = produtos.get(i);
                pesoTotal += produto.getPeso();
                volumeTotal += produto.getLargura() * produto.getAltura() * produto.getProfundidade();

                if (produto.getLargura() > this.larguraMaxima ||
                    produto.getAltura() > this.alturaMaxima ||
                    produto.getProfundidade() > this.profundidadeMaxima) {
                    excedeDimensoes = true;
                }
            }
        }

        if (pesoTotal > this.capacidadePeso || excedeDimensoes) {
            return 0;
        } else {
            return volumeTotal/1000000;
        }
    }

    private void gerarRoleta() {
        ArrayList<Double> fitnessIndividuos = new ArrayList<>();
        double totalFitness = 0;
        for (int i = 0; i < this.tamPopulacao; i++) {
            fitnessIndividuos.add(i, fitness(this.populacao.get(i)));
            totalFitness += fitnessIndividuos.get(i);
        }
        System.out.println("Soma total fitness:" + totalFitness);

        for (int i = 0; i < this.tamPopulacao; i++) {
            double qtdPosicoes = (fitnessIndividuos.get(i) / totalFitness) * 1000;
            for (int j = 0; j <= qtdPosicoes; j++)
                roletaVirtual.add(i);
        }
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
            filho.set(ponto, filho.get(ponto) == 1 ? 0 : 1);

            int ponto2 = r.nextInt(this.tamCromossomo);
            filho.set(ponto2, filho.get(ponto2) == 1 ? 0 : 1);

            System.out.println("Ocorreu mutação!");
        }
    }

    private void operadoresGeneticos() {
        ArrayList<Integer> f1, f2;
        ArrayList<ArrayList<Integer>> filhos;
        gerarRoleta();
        for (int i = 0; i < this.qtdeCruzamentos; i++) {
            filhos = cruzamento();
            f1 = filhos.get(0);
            f2 = filhos.get(1);
            mutacao(f1);
            mutacao(f2);
            populacao.add(f1);
            populacao.add(f2);
        }
    }

    private int obterPior() {
        int indicePior = 0;
        double pior = fitness(this.populacao.get(0));
        for (int i = 1; i < this.tamPopulacao; i++) {
            double nota = fitness(this.populacao.get(i));
            if (nota < pior) {
                pior = nota;
                indicePior = i;
            }
        }
        return indicePior;
    }

    private void novoPopulacao() {
        for (int i = 0; i < this.qtdeCruzamentos; i++) {
            populacao.remove(obterPior());
            populacao.remove(obterPior());
        }
    }

    private int obterMelhor() {
        int indiceMelhor = 0;
        double melhor = fitness(this.populacao.get(0));
        for (int i = 1; i < this.tamPopulacao; i++) {
            double nota = fitness(this.populacao.get(i));
            if (nota > melhor) {
                melhor = nota;
                indiceMelhor = i;
            }
        }
        return indiceMelhor;
    }

    public void mostrarCargaAviao(ArrayList<Integer> resultado) {
        System.out.println("Avaliacao do Melhor:" + this.fitness(resultado));
        System.out.println("Produtos levados no aviao:");
        for (int i = 0; i < resultado.size(); i++) {
            int leva = resultado.get(i);
            if (leva == 1) {
                Produto p = produtos.get(i);
                System.out.println(p.getDescricao() +
                        " Peso:" + p.getPeso() +
                        " Largura:" + p.getLargura() +
                        " Altura:" + p.getAltura() +
                        " Profundidade:" + p.getProfundidade());
            }
        }
    }
}
