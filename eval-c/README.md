# 🃏 Poker Hand Evaluator (Servidor C)

Este módulo es el **componente del servidor** encargado de **determinar la mejor mano de cada jugador con 7 cartas al finalizar una ronda HORSE, evaluar y comparar dichas manos**.  
Determina la jerarquía entre dos manos, calcula su ranking (par, trío, full house, etc.)  
y aplica reglas de desempate mediante **High Card** en caso de igualdad.

Incluye las funciones principales:
- `findRanking(char **hand)` → Evalúa el tipo de jugada de una mano.
- `decideTieByHighCard(char **firstHand, char **secondHand)` → Resuelve empates por carta alta.
- `decideTie(char **firstHand, char **secondHand)` → Determina la mano ganadora.

Este componente se integra en el **servidor de juego**, recibiendo las manos de los jugadores  
y devolviendo la mano ganadora según las reglas estándar de póker HORSE.


