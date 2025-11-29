# Proyecto - `Poker Horse`

Proyecto basado en los lenguajes `Java` y `C`. Con el objetivo de implementar el juego `POKER HORSE`

## Objetivo

* Diseñar e implementar un sistema de Poker con la integración de dos paradigmas de programación.
* Comunicación entre C y Java por medio de `Socket TCP`
* Soportar entre 2 y 4 jugadores concurrentes
* Administrar apuestas, pots, turnos y cambio de modo de juego cíclico

## Arquitectura

* `client-java`: implementa la interfaz gráfica y como el cliente interactua con el juego. Además, de comunicarse con el servidor.
* `eval-c`: evaluador de manos de los jugadores actuales.
* `server-java`: implementa la comunicacion entre el servidor y el cliente.

## Variantes

| Letra | Variante                | Descripción breve                                      |
| ----- | ----------------------- | ------------------------------------------------------ |
| H     | Hold'em                 | 2 cartas ocultas + 5 comunitarias.                     |
| O     | Omaha Hi                | 4 cartas ocultas, usar exactamente 2 + 3 comunitarias. |
| R     | Razz                    | Se busca la **peor** mano posible (mano baja).         |
| S     | Seven Card Stud         | 7 cartas por jugador, sin cartas comunitarias.         |
| E     | Eight or Better (Hi-Lo) | Premia mano alta y baja; la baja debe ser 8 o menos.   |

## Rankeo de las manos

<p align="center"><img alt="poker hand ranking" src="https://github.com/fbustios/poker-lenguajes/blob/main/pokerHandRanking.png"></p>
