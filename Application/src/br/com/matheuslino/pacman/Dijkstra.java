package br.com.matheuslino.pacman;

import java.util.ArrayList;
import java.util.List;

// Calculo de caminhos com algoritmo Dijkstra
public class Dijkstra {

	// Atributo privato
	private final static int INF = 10000;

	private static  int[] dijkstra(int g[][], int ordem, int Vi, int Vf) {

		// Variaveis locais utilizadas no algoritmo
		int 	 		vcm = -1;
		boolean[]  visitado = new boolean[ordem];
		int 		custo[] = new int[ordem];
		int 	  caminho[] = new int[ordem];

		for (int i = 0; i < ordem; i++) {
			visitado[i] = false;
			custo[i]    = INF;
			caminho[i]  = -1;
		}

		custo[Vi] = 0;
		caminho[Vi] = Vi;

		while (true) {

			vcm = -1;
			for (int i = 0; i < ordem; i++) {
				if(!visitado[i] && (vcm<0 || custo[i] < custo[vcm])) {
					vcm = i;
				}
			}

			if(vcm<0 || vcm==Vf) break;

			visitado[vcm] = true;

			for (int i = 0; i < ordem; i++) {
				if(g[vcm][i]!=0 && (custo[i] > (custo[vcm] + g[vcm][i]))) {
					custo[i] = custo[vcm] + g[vcm][i];
					caminho[i] = vcm;
				}
			}
		}
		return caminho;			// Retona caminho a ser percorrido
	}

	// Retorna a direcao a ser percorrida pelo fantasma
	public static int shortWay(int grafo[][], int vi, int vf) {

		int valor = 0;
		int aux[] =	dijkstra(grafo,grafo.length,vi,vf);		// Armazena o vetor de menor custo
		List<Integer> lista = new ArrayList<>();			// Lista ordenada das posicoes a serem visitadas

		while(true) {										// Realiza iteracoes enquanto houver elementos (posicoes) disponiveis no vetor

			if(vf!=vi && vf!=-1) {
				lista.add(vf);								// Adiciona elemento
			}else {
				break;
			}
			vf = (vf>=0) ? aux[vf] : -1;
		}

		// Verifica a direcao a ser percorrida caso a lista não esteja vazia
		if(lista.size()>0){

			valor = lista.get((lista.size()-1));		// Recupera a proxima posicao
			lista.remove(lista.size()-1);				// Remove o ultimo elemento da lista

			return valor;
		}else {
			return -1;
		}
	}
}