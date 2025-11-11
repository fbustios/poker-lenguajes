#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define HIGH_CARD       0
#define ONE_PAIR        1
#define TWO_PAIR        2
#define THREE_OF_A_KIND 3
#define STRAIGHT        4
#define FLUSH           5
#define FULL_HOUSE      6
#define FOUR_OF_A_KIND  7
#define STRAIGHT_FLUSH  8
#define ROYAL_FLUSH     9
#define SUITS 4
#define CARDS 14



char ** readHand(const int size) {
    char ** deck = malloc(size * sizeof(char *));
    for (int i = 0; i < size; i++) {
        deck[i] = malloc(3 * sizeof(char));
        scanf("%3s", deck[i]);
    }
    return deck;
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
            return 1;
        case 'S':
            return 2;
        case 'D':
            return 3;
        case 'C':
            return 4;
        default:
            return 0;
    }
}
int findStraight(const int * suits_freq, const int * values_freq) {
    int flag = 0;
    for (int i = 0; i < SUITS; i++) {
        if (suits_freq[i] == 5) flag = 1;
    }
    int counter = 0;
    int firstElement = 0;
    int lastElement = 0;
    for (int i = 0; i < CARDS; i++) {
        if (counter == 5 && !flag) return ((STRAIGHT * 14) + (firstElement + 1));
        if (counter == 5 && (lastElement == 13)) return ROYAL_FLUSH * 14;
        if (counter == 5 && (lastElement == 9)) return STRAIGHT_FLUSH * 14;
        counter += 1;
        lastElement = i;

        if (values_freq[i] != 1) {
            counter = 0;
            firstElement = i;
            lastElement = i;
        }

    }
    if (flag) return FLUSH * 14;
    return 0;
}

int weighHighHand(char ** hand, int handSize) {
    int * suits_freq = calloc(4, sizeof(int));
    int * values_freq = calloc(14, sizeof(int));
    int * visited_values = calloc(14, sizeof(int));
    int * visited_suits = calloc(14, sizeof(int));
    int score = 0;
    for (int i = 0; i < handSize; i++) {
        const int number = getCardValue(hand[i][1],0);
        const int suit = getSuitValue(hand[i][0]);
        values_freq[number]++;
        suits_freq[suit]++;
    }
    int hasStraight = findStraight(suits_freq, values_freq);
    if (hasStraight) {
        free(suits_freq);
        free(values_freq);
        free(visited_suits);
        free(visited_values);
        return hasStraight;
    }

    for (int i = 0; i < 14; i++) {
        if (values_freq[i] == 4) score += (14 * FOUR_OF_A_KIND) + (i + 1);
        if (values_freq[i] == 3) score += (14 * THREE_OF_A_KIND) + (i + 1);
        if (values_freq[i] == 2) score += (14 * ONE_PAIR) + (i + 1);
    }
    free(suits_freq);
    free(values_freq);
    free(visited_values);
    free(visited_suits);
    return score;
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

char ** mergeCards() {
    return 0;
}


char ** getBestHandAux(char ** fullHand, char ** currentHand, int targetSize, int currentHandSize, int currentIndex) {
    if (currentHandSize == targetSize) return currentHand;

    if (targetSize - currentHandSize == targetSize - currentIndex) {
        for (int i = currentIndex; i < targetSize ; i++) {
            currentHand[i] = fullHand[i];
        }
        return currentHand;
    }
    char ** copyCurrentHand = copyHand(currentHand, currentHandSize);
    char ** copyCurrentHand2 = copyHand(currentHand, currentHandSize);
    copyCurrentHand[currentIndex] = fullHand[currentIndex];
    char ** takingOption = getBestHandAux(fullHand, copyCurrentHand, targetSize, currentHandSize + 1, currentIndex + 1);
    char ** skippingOption = getBestHandAux(fullHand, copyCurrentHand2, targetSize, currentHandSize, currentIndex + 1);
    return weighHighHand(takingOption, targetSize + 1) > weighHighHand(skippingOption, targetSize) ? takingOption : skippingOption;
}

char ** getBestHand(char ** fullHand, int targetSize) {
    char ** currentHand = calloc(targetSize, sizeof(char *));
    for (int i = 0; i < targetSize; i++) {
        currentHand[i] = calloc(2, sizeof(char));
    }
    return getBestHandAux(fullHand, currentHand, targetSize, 0, 0);
}

int main(void) {
    int players;
    char gamemode[10];
    int deckSize;
    scanf("%d", &players);
    scanf("%d", &deckSize);
    scanf("%s", gamemode);
    char ** communityCards = readHand(5);
    free(communityCards);
    for(int i = 0; i < players; i++) {
        char ** playerDeck = readHand(deckSize);
        if (strcmp(gamemode, "omaha") || strcmp(gamemode,"holdem")) {
            //char ** communityCards = readCommunityCards(5);
            mergeCards();

            //return weighHighHand(,7);
        }
        if (strcmp(gamemode, "razz") || strcmp(gamemode,"omaha-hilo")) {
            //int lowHand = weighLowHand(,7);
            //return lowHand;
        }
    }


    return 0;
}
