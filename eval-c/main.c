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

char ** getBetterHighHand(char ** firstHand, char ** secondHand) {
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

char ** copyHand(char ** originalHand, int size) {
    char ** copy = malloc(size * sizeof(char *));
    for (int i = 0; i < size; i++) {
        copy[i] = malloc(3 * sizeof(char));
        copy[i] = originalHand[i];
    }
    return copy;
}

char ** findBestHighCombination_aux(char ** fullHand, char ** currentHand, int targetSize, int currentHandIndex, int currentIndex) {
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
    char ** takingOption = findBestHighCombination_aux(fullHand, copyCurrentHand, targetSize, currentHandIndex + 1, currentIndex + 1);
    char ** skippingOption = findBestHighCombination_aux(fullHand, copyCurrentHand2, targetSize, currentHandIndex, currentIndex + 1);
    return getBetterHighHand(takingOption, skippingOption);
}

int isLowHand(struct Ranking handRaking, char ** hand) {
    if (handRaking.rank == ONE_PAIR || handRaking.rank == TWO_PAIR || handRaking.rank == THREE_OF_A_KIND || handRaking.rank == FOUR_OF_A_KIND) return 0;
    for (int i = 0; i < HAND_SIZE; i++) {
        int cardNumber = getCardValue(hand[i][1],1);
        if (cardNumber > 8) return 0;
    }
    return 1;
}

char ** getBetterLowHand(char ** firstHand, char ** secondHand) {
    struct Ranking firstHandRanking = findRanking(firstHand);
    struct Ranking secondHandRanking = findRanking(secondHand);
    //resumible en dos variables xd
    if (!isLowHand(firstHandRanking, firstHand) && isLowHand(secondHandRanking, secondHand)) return secondHand;
    if (!isLowHand(secondHandRanking, secondHand) && isLowHand(firstHandRanking, firstHand)) return firstHand;

    return decideTieByHighCard(firstHand, secondHand) == firstHand ? secondHand : firstHand;
}


char ** findBestLowCombination_aux(char ** fullHand, char ** currentHand, int targetSize, int currentHandIndex, int currentIndex) {
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
    char ** takingOption = findBestLowCombination_aux(fullHand, copyCurrentHand, targetSize, currentHandIndex + 1, currentIndex + 1);
    char ** skippingOption = findBestLowCombination_aux(fullHand, copyCurrentHand2, targetSize, currentHandIndex, currentIndex + 1);
    return getBetterLowHand(takingOption, skippingOption);
}


char ** findBestHighCombination(char ** fullHand, const int targetSize) {
    char ** currentHand = calloc(targetSize, sizeof(char *));
    for (int i = 0; i < targetSize; i++) {
        currentHand[i] = calloc(2, sizeof(char));
    }
    return findBestHighCombination_aux(fullHand, currentHand, targetSize, 0, 0);
}

char ** findBestLowCombination(char ** fullHand, const int targetSize) {
    char ** currentHand = calloc(targetSize, sizeof(char *));
    for (int i = 0; i < targetSize; i++) {
        currentHand[i] = calloc(2, sizeof(char));
    }
    return findBestLowCombination_aux(fullHand, currentHand, targetSize, 0, 0);
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

void findBestHighHandsForPlayers(struct Player * players, int sizePlayers) {
    printf("%s", "llegue6");
    for (int i = 0; i < sizePlayers; i ++) {
        players[i].bestHand = findBestHighCombination(players[i].bestHand, 5);
        printf("%s", "\n");
    }

}

void findBestLowHandsForPlayers(struct Player * players, int sizePlayers) {
    printf("%s", "llegue6");
    for (int i = 0; i < sizePlayers; i ++) {
        players[i].bestHand = findBestLowCombination(players[i].bestHand, 5);
        printf("%s", "\n");
    }

}

void decideWinnerHigh(struct Player * players, const int numPlayers) {
    struct Player best = players[0];
    for (int i = 0; i < numPlayers; i++) {
        if (getBetterHighHand(best.bestHand, players[i].bestHand) != best.bestHand) {
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

void decideWinnerLow(struct Player * players, const int numPlayers) { //falta revisar
    struct Player best = players[0];
    for (int i = 0; i < numPlayers; i++) {
        if (getBetterLowHand(best.bestHand, players[i].bestHand) != best.bestHand) {
            best = players[i];
        }
    }
    if (!isLowHand(findRanking(best.bestHand),best.bestHand)) {
        return;
    }
    for (int i = 0; i < numPlayers; i++) {
        if (totalTie(best.bestHand, players[i].bestHand)) {
            players[i].winner = 1;
        } else {
            players[i].winner = 0;
        }
    }
}



int main(void) {
    int numPlayers;
    char gamemode;
    int deckSize;
    scanf("%d", &numPlayers);
    scanf("%d", &deckSize);
    getchar();
    scanf("%c", &gamemode);

    struct Player * players = readPlayers(numPlayers);
    if (gamemode == 'h' || gamemode == 'o' || gamemode == 's') {
        findBestHighHandsForPlayers(players, numPlayers);
        decideWinnerHigh(players, numPlayers);
    } else if (gamemode == 'r') {
        findBestLowHandsForPlayers(players, numPlayers);
        decideWinnerLow(players, numPlayers);
    } else if (gamemode == 'e') {  //NOT WORKING
        findBestHighHandsForPlayers(players, numPlayers);
        decideWinnerHigh(players, numPlayers);
        findBestLowHandsForPlayers(players, numPlayers);
        decideWinnerLow(players, numPlayers);

    } else {
        /*
        char ** deck = malloc(7 * sizeof(char *));

        for (int i = 0; i < 7; i++) {
            deck[i] = malloc(3 * sizeof(char));
        }

        char ** deck2 = malloc(5 * sizeof(char *));

        for (int i = 0; i < 5; i++) {
            deck2[i] = malloc(3 * sizeof(char));
        }
        deck[0] = "C2";
        deck[1] = "DK";
        deck[2] = "S2";
        deck[3] = "H2";
        deck[4] = "D5";
        deck[5] = "SA";
        deck[6] = "HA";

        deck2[0] = "C2";
        deck2[1] = "D2";
        deck2[2] = "H2";
        deck2[3] = "S2";
        deck2[4] = "D6";
        //char ** best = decideTieByHighCard(deck2, deck);
        char ** best = findBestHighCombination(players[0].bestHand,5);
        //findBestHighHandsForPlayers(players,numPlayers);
        for (int j = 0; j < 5; j++) {
            printf("%s", best[j]);
            printf("%s"," ");
        }
        //printf("%d", isLowHand(findRanking(best),best));
        */
    }

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