# AIAD1819

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


## Links  
  * [Game Description (wikipedia)](https://en.wikipedia.org/wiki/Risk_(game))
  * [Game Rules](http://www.ultraboardgames.com/risk/game-rules.php)
  * [1st part description](https://github.com/rmcarvalho/AIAD1819/blob/master/docs/proj1_definicao.pdf)
  * [Cards](https://drive.google.com/drive/folders/0BwJ1gMT0fLRPSWFISFNQNkVCZVk)
