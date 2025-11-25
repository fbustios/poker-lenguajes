#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define SUITS 4
#define CARDS 14
#define FULL_HAND 7
#define HAND_SIZE 5
#define MAX(a, b) ((a) > (b) ? (a) : (b))

enum HandRank {
    NONE = -1,
    HIGH_CARD,
    ONE_PAIR,
    TWO_PAIR,
    THREE_OF_A_KIND,
    STRAIGHT,
    FLUSH,
    FULL_HOUSE,
    FOUR_OF_A_KIND,
    STRAIGHT_FLUSH,
    ROYAL_FLUSH
};

struct Ranking {
    int rank;
    int score;
};

struct Player {
    char name[5];
    char ** bestHand;
    int winner;
};

char ** readHand(const int size) {
    char ** deck = malloc(size * sizeof(char *));

    for (int i = 0; i < size; i++) {
        deck[i] = malloc(3 * sizeof(char));
        scanf("%3s", deck[i]);
    }

    return deck;
}

struct Player* readPlayers(const int players) {
    struct Player* playersList = malloc(sizeof(struct Player) * players);

    for (int i = 0; i < players; i++) {
        char playerName[5];
        scanf("%4s", playerName);
        char ** hand = readHand(FULL_HAND);
        strncpy(playersList[i].name, playerName,4);
        playersList[i].bestHand = hand;
    }
    return playersList;
}

int getCardValue(const char c, const int low) {
    if (c <= '9' && c >= '0') return c - '0';
    switch (c) {
        case 'T' :
            return 10;
        case 'J' :
            return 11;
        case 'Q' :
            return 12;
        case 'K' :
            return 13;
        case 'A' :
            return low ? 1 : 14;
        default:
            return -777;
    }
}

int getSuitValue(const char c) {
    switch (c) {
        case 'H':
            return 0;
        case 'S':
            return 1;
        case 'D':
            return 2;
        case 'C':
            return 3;
        default:
            return 4;
    }
}

struct Ranking findStraightHand(const int * suits_freq, const int * values_freq) {
    int flag = 0;
    for (int i = 0; i < SUITS; i++) {
        if (suits_freq[i] == 5) flag = 1;
    }

    int counter = 1;
    int lastElement = 0;
    int max = 1;
    for (int i = 1; i < CARDS; i++) {
        if (values_freq[i] == 1 && (values_freq[i] == values_freq[i-1])) {
            counter += 1;
            lastElement = i;
        } else {
            counter = 1;
        }
        if (values_freq[i]) {
            max = MAX(max, i + 1);
        }
        if (counter == 5 && !flag) {
            //printf("%s", "STRAIGHT");
            const struct Ranking r = {STRAIGHT, lastElement + 1};
            return r;
        }
        if (counter == 5 && (lastElement == 13)) {
            //printf("%s", "ROYAL_FLUSH");
            const struct Ranking r = {ROYAL_FLUSH, lastElement + 1};
            return r;
        }
        if (counter == 5) {
            //printf("%s", "STRAIGHT_FLUSH");
            const struct Ranking r = {STRAIGHT_FLUSH, lastElement + 1};
            return r;
        }
    }

    if (flag) {
        //printf("%s", "FLUSH");
        const struct Ranking r = {FLUSH, max};
        return r;
    }
    const struct Ranking r = {HIGH_CARD, max};
    return r;
}

struct Ranking findRanking(char ** hand) {
    int * suits_freq = calloc(4, sizeof(int));
    int * values_freq = calloc(14, sizeof(int));
    for (int i = 0; i < HAND_SIZE; i++) {
        const int number = getCardValue(hand[i][1],0);
        const int suit = getSuitValue(hand[i][0]);
        values_freq[number-1]++;
        suits_freq[suit]++;
    }
    int pairCount = 0;
    int hasPair = 0;
    int hasTrio = 0;
    for (int i = 0; i < CARDS; i++) {
        if (values_freq[i] == 4) {
            //printf("%s","FOUR_OF_KIND");
            free(suits_freq);
            free(values_freq);
            const struct Ranking r = {FOUR_OF_A_KIND, i + 1};
            return r;
        }
        if (values_freq[i] == 3) {
            //printf("%s","THREE_OF_KIND");
            hasTrio = i + 1;
        }
        if (values_freq[i] == 2) {
            //printf("%s","PAIR");
            hasPair += i + 1;
            pairCount += 1;
        }
    }
    if (hasPair && hasTrio) {
        //printf("%s","FULL_HOUSE");
        free(suits_freq);
        free(values_freq);
        const struct Ranking r = {FULL_HOUSE, (hasTrio * 50) + hasPair};
        return r;

    }
    if (hasPair) {
        //printf("%s","PAIRS");
        free(suits_freq);
        free(values_freq);
        if (pairCount == 2) {
            const struct Ranking r = {TWO_PAIR, 0};
            //printf("%c",'X');
            return r;
        }
        const struct Ranking r = {ONE_PAIR, hasPair};
        return r;
    }
    if (hasTrio) {
        //printf("%s","TRIO");
        free(suits_freq);
        free(values_freq);
        const struct Ranking r = {THREE_OF_A_KIND, hasTrio};
        return r;
    }
    const struct Ranking fullHand = findStraightHand(suits_freq, values_freq);
    //printf("%c",'\n');
    free(suits_freq);
    free(values_freq);
    return fullHand;
}

char ** decideTieByHighCard(char ** firstHand, char ** secondHand) {
    int * hand1_freq = calloc(CARDS,sizeof(int));
    int * hand2_freq = calloc(CARDS,sizeof(int));
    for (int i = 0; i < HAND_SIZE; i++) {
        int value = getCardValue(firstHand[i][1],0);
        int value2 = getCardValue(secondHand[i][1],0);
        hand1_freq[value-1]++;
        hand2_freq[value2-1]++;
    }

    int winner = -1;
    int max = 0;
    for (int i = 0; i < CARDS; i++) {
        int cardValue = i + 1;
        if (!hand1_freq[i] && hand2_freq[i]) {
            if (cardValue >= max) {
                max = cardValue;
                winner = 2;
            }
        }

        if (hand1_freq[i] && !hand2_freq[i]) {
            if (cardValue >= max) {
                max = cardValue;
                winner = 1;
            }
        }
    }
    free(hand1_freq);
    free(hand2_freq);
    return winner == 1 ? firstHand : secondHand;
}

char ** decideTie(char ** firstHand, char ** secondHand) {
    struct Ranking firstHandRanking = findRanking(firstHand);
    struct Ranking secondHandRanking = findRanking(secondHand);
    int rank1 = firstHandRanking.rank;
    int rank2 = secondHandRanking.rank;
    int score1 = firstHandRanking.score;
    int score2 = secondHandRanking.score;
    if (rank1 == rank2) {
        if (rank1 == 2) { return decideTieByHighCard(firstHand, secondHand);}
        if (score1 == score2) {
            return decideTieByHighCard(firstHand,secondHand);
        }
        return score1 > score2 ? firstHand : secondHand;
    };

    return rank1 > rank2 ? firstHand : secondHand;
}

int weighLowHand(char ** hand, int handSize) {
    int handWeight = 0;
    for (int i = 0; i < handSize; i++) {
        char cardNumber = hand[i][1];
        int cardValue = cardNumber - '0';
        handWeight -= cardValue;
    }
    return handWeight;
}

char ** copyHand(char ** originalHand, int size) {
    char ** copy = malloc(size * sizeof(char *));
    for (int i = 0; i < size; i++) {
        copy[i] = malloc(3 * sizeof(char));
        copy[i] = originalHand[i];
    }
    return copy;
}

char ** getBestHandAux(char ** fullHand, char ** currentHand, int targetSize, int currentHandIndex, int currentIndex) {
    if (currentHandIndex == targetSize) return currentHand;

    if (targetSize - currentHandIndex == 7 - currentIndex) {
        char ** newHand = copyHand(currentHand,5);
        for (int i = currentIndex; i < FULL_HAND ; i++) {
            newHand[currentHandIndex++] = fullHand[i];
        }
        return newHand;
    }
    char ** copyCurrentHand = copyHand(currentHand, currentHandIndex);
    char ** copyCurrentHand2 = copyHand(currentHand, currentHandIndex);
    copyCurrentHand[currentHandIndex] = fullHand[currentIndex];
    char ** takingOption = getBestHandAux(fullHand, copyCurrentHand, targetSize, currentHandIndex + 1, currentIndex + 1);
    char ** skippingOption = getBestHandAux(fullHand, copyCurrentHand2, targetSize, currentHandIndex, currentIndex + 1);
    return decideTie(takingOption, skippingOption);
}


char ** getBestHand(char ** fullHand, const int targetSize) {
    char ** currentHand = calloc(targetSize, sizeof(char *));
    for (int i = 0; i < targetSize; i++) {
        currentHand[i] = calloc(2, sizeof(char));
    }
    return getBestHandAux(fullHand, currentHand, targetSize, 0, 0);
}


int same(char ** firstHand, char ** secondHand) {
    int * hand1_freq = calloc(CARDS,sizeof(int));
    int * hand2_freq = calloc(CARDS,sizeof(int));
    for (int i = 0; i < HAND_SIZE; i++) {
        int value = getCardValue(firstHand[i][1],0);
        int value2 = getCardValue(secondHand[i][1],0);
        hand1_freq[value-1]++;
        hand2_freq[value2-1]++;
    }

    for (int i = 0; i < CARDS; i++) {
        if (!hand1_freq[i] && hand2_freq[i]) {
            free(hand1_freq);
            free(hand2_freq);
            return 0;
        }

        if (hand1_freq[i] && !hand2_freq[i]) {
            free(hand1_freq);
            free(hand2_freq);
            return 0;
        }
    }
    free(hand1_freq);
    free(hand2_freq);
    return 1;
}

int totalTie(char ** firstHand, char ** secondHand) {
    struct Ranking firstHandRanking = findRanking(firstHand);
    struct Ranking secondHandRanking = findRanking(secondHand);
    int rank1 = firstHandRanking.rank;
    int rank2 = secondHandRanking.rank;
    int score1 = firstHandRanking.score;
    int score2 = secondHandRanking.score;
    if ((rank1 == rank2) && (score1 == score2)) {
        return same(firstHand,secondHand);
    };
    return 0;
}


void decideWinner(struct Player * players, const int numPlayers) {
    struct Player best = players[0];
    for (int i = 0; i < numPlayers; i++) {
        if (decideTie(best.bestHand, players[i].bestHand) != best.bestHand) {
            best = players[i];
        }
    }


    for (int i = 0; i < numPlayers; i++) {
        if (totalTie(best.bestHand, players[i].bestHand)) {
            players[i].winner = 1;
        } else {
            players[i].winner = 0;
        }
    }
}


void decideBestHands(struct Player * players, int sizePlayers) {
    printf("%s", "llegue6");
    for (int i = 0; i < sizePlayers; i ++) {
        players[i].bestHand = getBestHand(players[i].bestHand, 5);
        printf("%s", "\n");
    }

}


int main(void) {
    int numPlayers;
    char gamemode[10];
    int deckSize;
    scanf("%d", &numPlayers);
    scanf("%d", &deckSize);
    scanf("%s", gamemode);

    struct Player * players = readPlayers(numPlayers);
    decideBestHands(players, numPlayers);
    decideWinner(players, numPlayers);
    for (int i = 0; i < numPlayers; i++) {
       if (players[i].winner) {
           for (int j = 0; j < 5; j++) {
               printf("%s", players[i].bestHand[j]);
               printf("%s"," ");
           }
           printf("%s", "\n");
       }
    }
    free(players);

    return 0;
}