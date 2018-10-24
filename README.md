# AIAD1819

## TODO
* implementar regras do jogo
  - [X] Game Board
  - [X] Piece Value
  - [ ] Game Start Setup
  	  - [X] Starting units Per player
	  - [ ] Initial die roll to set who starts (in this case, since agents wont have relative physical position, instead of 'to the left', die rolls will set the order)
	  - [ ] When territories are all claimed, continue until all armies are placed
	  - [X] Shuflle cards
	  - [ ] Game starts with highest rolling player
  - [X] Cards Existence
  - [ ] Game Play
	  - [ ] Place New Armies
	  - [ ] Cards Functionality (Earning, trading)
	  - [ ] Atacking
		  - [ ] To attack
		  - [X] Decide Battle
	  - [ ] Fortify Position
  - [X] Victory Check (End of the game)

* implementar tabuleiro como agente que:
  * controla a vez do jogador (tabuleiro toma iniciativa de mandar estado do jogo ao agente jogador)
  * responde a jogador depois de cada jogada se esta é válida ou não
* implementar jogadores como agentes
  1. começar com jogadas aleatorias
  2. passar para agentes puramente competitivos
  3. implementar negociação de cooperação

## Links  
  * [Game Description (wikipedia)](https://en.wikipedia.org/wiki/Risk_(game))
  * [Game Rules](http://www.ultraboardgames.com/risk/game-rules.php)
  * [1st part description](https://github.com/rmcarvalho/AIAD1819/blob/master/docs/proj1_definicao.pdf)
  * [Cards](https://drive.google.com/drive/folders/0BwJ1gMT0fLRPSWFISFNQNkVCZVk)
