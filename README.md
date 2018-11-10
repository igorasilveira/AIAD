# AIAD1819

## Running
* Eclipse running arguments:

`-gui -agents Board:agents.BoardAgent;Player1:agents.PlayerAgent(aggressive);Player2:agents.PlayerAgent(defensive);Player3:agents.PlayerAgent(smart);Player4:agents.PlayerAgent`

## TODO
* implementar regras do jogo
	  - [ ] Cards Functionality (Earning, trading) (if there's time)

* implementar tabuleiro como agente que:
  * controla a vez do jogador (tabuleiro toma iniciativa de mandar estado do jogo ao agente jogador)
  * a cada reposta do jogador atualiza o estado do jogo e efetua qualquer que seja o passo seguinte na lógica de jogo
* implementar jogadores como agentes
  1. começar com jogadas aleatorias
  2. passar para agentes puramente competitivos
  3. implementar negociação de cooperação

### Comunicação entre agentes
* Board->Player 
	* send: game state (class game) 
		* REQUEST 
	* answer: Player->Board
* Player->Board 
	* send: move (attack class or fortify class) or end turn 
		* PROPOSE 
	* answer: worked or not 
		* AGREE REFUSE
* Board->Player 
	* send: game state + attack class 
		* ??
	* answer: number of dice
* Player->Player 
	* send: treaty proposition
		* PROPOSE
	* answer: yes or no
		* ACCEPT-PROPOSAL
		* REJECT-PROPOSAL

### Estratégias
- [ ] Decidir se atacar, fortificar ou nada (Ricardo)
	* Critérios: 
		1. ?
- [X] Decidir melhor ataque (Mindset Random é sempre aleatório)
	* Melhor território a atacar
		* Critérios:
			1. Calcular para cada alvo a vantagem do atacante:
				* (total de peças do atacante em territórios vizinhos) - (peças do atacante no território alvo)
			2. Escolher os alvos com vantagem máxima.
	* Melhor território de origem das peças
		* Critérios:
			1. Escolhidos potenciais alvos, escolher o território de origem com mais peças.
	* Melhor quantidade de dados
		* Critérios:
			* Smart escolhe baseado nas probabilidades [aqui](https://github.com/rmcarvalho/AIAD1819/blob/master/docs/dice_roll_odds.xlsx) registadas
			* Aggressive lança o maximo de dados possivel (3)
			* Defensive lança o minimo de dados possivel (1)
			* Random é aleatório
- [X] Decidir melhor fortificação
	* Melhor território a fortificar e de origem das peças
		* Critérios:
			1. Calcular para cada território a desvantagem em que se encontra (quanto maior o valor maior a desvantagem):
				* (maximo entre total de unidades de cada adversario vizinho)) - (unidades do território)
			2. Escolher par 'de' e 'para' com base na divisão da desvantagem da origem pela desvantagem do destino, sendo que valores mais afastados de 0 são mais vantajosos (particularmente valores negativos que implicam que um dos territórios tenha mais unidades que o vizinho adversário com mais unidades).
			3. Escolher as unidades com base na desvantagem de cada território (origem e destino) de forma a equilibrar os valores
- [X] Decidir melhor defesa
	* Critérios:
		1. ver [excel](https://github.com/rmcarvalho/AIAD1819/blob/master/docs/dice_roll_odds.xlsx) com probabilidades de lançamentos calculadas simulando 1000 ataques para cada combinação de quantidades de dados lançados
- [ ] Decidir melhor setup (Igor)
	* Melhor posicionamento quando há territórios vazios
		* Critérios:
			1. ?
	* Melhor posicionamento quando não há territórios vazios
		* Critérios:
			1. ?
- [X] Decidir quando trocar cartas e que cartas trocar (iguais ou diferentes)
	* Critérios:
		1. Agentes agressivos e defensivos entregam cartas mal puderem.
		2. Agentes smart tentam nao entregar cartas o maximo tempo possível, de modo a receberem mais unidades quando entregarem o seu set.
		3. Agentes aleatórios decidem à sorte. 

## Links  
  * [Game Description (wikipedia)](https://en.wikipedia.org/wiki/Risk_(game))
  * [Game Rules](http://www.ultraboardgames.com/risk/game-rules.php)
  * [1st part description](https://github.com/rmcarvalho/AIAD1819/blob/master/docs/proj1_definicao.pdf)
  * [Cards](https://drive.google.com/drive/folders/0BwJ1gMT0fLRPSWFISFNQNkVCZVk)
