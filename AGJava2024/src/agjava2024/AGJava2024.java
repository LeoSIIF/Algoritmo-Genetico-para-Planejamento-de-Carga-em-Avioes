package agjava2024;

public class AGJava2024 {
    public static void main(String[] args) {
        int populacao = 20;
        double capacidadePeso = 400;
        double larguraMaxima = 300;
        double alturaMaxima = 300;
        double profundidadeMaxima = 400;
        int probabilidadeMutacao = 5; // 5%
        int qtdeCruzamentos = 5;
        AlgoritmoGenetico meuAg = new AlgoritmoGenetico(
                populacao, capacidadePeso, larguraMaxima, alturaMaxima, profundidadeMaxima, probabilidadeMutacao, qtdeCruzamentos);
        meuAg.carregaArquivo("dados.csv");
        meuAg.executar();
    }
}
