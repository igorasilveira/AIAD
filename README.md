# AIAD1819

## Running
* Add JADE, Repast and SaJaS dependencias to classpath. *
### JADE
* Eclipse running arguments:

`-gui -agents Board:agents.BoardAgent;Player1:agents.PlayerAgent(aggressive);Player2:agents.PlayerAgent(defensive);Player3:agents.PlayerAgent(smart);Player4:agents.PlayerAgent`

### SaJaS+Repast3

Run class `MyLauncher.java`

## Comunicação entre agentes
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

## Estratégias
* Decidir se atacar, fortificar ou nada
	* Critérios: 
		1. Aggressive ataca 75% das vezes que tem ataques
		2. Defensive fortifica 75% das vezes que tem fortificações
		3. Aleatório decide aleatoriamente
		4. Ataca sempre que pode e fortifica quando pode e não tem ataques
* Decidir melhor ataque (Mindset Random é sempre aleatório)
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
* Decidir melhor fortificação
	* Melhor território a fortificar e de origem das peças
		* Critérios:
			1. Calcular para cada território a desvantagem em que se encontra (quanto maior o valor maior a desvantagem):
				* (maximo entre total de unidades de cada adversario vizinho)) - (unidades do território)
			2. Escolher par 'de' e 'para' com base na divisão da desvantagem da origem pela desvantagem do destino, sendo que valores mais afastados de 0 são mais vantajosos (particularmente valores negativos que implicam que um dos territórios tenha mais unidades que o vizinho adversário com mais unidades).
			3. Escolher as unidades com base na desvantagem de cada território (origem e destino) de forma a equilibrar os valores
* Decidir melhor defesa
	* Critérios:
		1. ver [excel](https://github.com/rmcarvalho/AIAD1819/blob/master/docs/dice_roll_odds.xlsx) com probabilidades de lançamentos calculadas simulando 1000 ataques para cada combinação de quantidades de dados lançados
* Decidir melhor setup (Igor)
	* Melhor posicionamento quando há territórios vazios
		* Critérios:
			1. ?
	* Melhor posicionamento quando não há territórios vazios
		* Critérios:
			1. ?
* Decidir quando trocar cartas e que cartas trocar (iguais ou diferentes)
	* Critérios:
		1. Agentes agressivos e defensivos entregam cartas mal puderem.
		2. Agentes smart tentam nao entregar cartas o maximo tempo possível, de modo a receberem mais unidades quando entregarem o seu set.
		3. Agentes aleatórios decidem à sorte. 

## Decisions
 * Attack, Fortify or Nothing
 * How to attack
 	* difference of attacking pieces vs defending pieces (attacker’s advantage)
	* number of dice chosen
		* number of pieces on the attacking territory
		* number of pieces on the defending territory
 * How to fortify
 	* difference between the maximum amount of pieces of the same player around the origin territory and the amount of pieces of the current player’s territory (territory’s disadvantage)
	* destination territory’s disadvantage
 * How to defend
 	* number of dice chosen by the attacker and the defender
	* number of units of the territory under attack
 * How to setup
 	* ?
 * How to Trade cards
 	* ?

## Links  
  * [Game Description (wikipedia)](https://en.wikipedia.org/wiki/Risk_(game))
  * [Game Rules](http://www.ultraboardgames.com/risk/game-rules.php)
  * [1st part description](https://github.com/rmcarvalho/AIAD1819/blob/master/docs/proj1_definicao.pdf)
  * [Cards](https://drive.google.com/drive/folders/0BwJ1gMT0fLRPSWFISFNQNkVCZVk)
