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
#define STRAIGHT_FLUSH  9
#define ROYAL_FLUSH     10
#define SUITS 4
#define CARDS 14
#define FULL_HAND 7
#define MAX(a, b) ((a) > (b) ? (a) : (b))




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
int findStraight(const int * suits_freq, const int * values_freq) {
    int flag = 0;
    for (int i = 0; i < SUITS; i++) {
        if (suits_freq[i] == 5) flag = 1;
    }
    int counter = 1;
    int lastElement = 0;
    int max = 0;
    for (int i = 1; i < CARDS; i++) {
        if (values_freq[i] == 1 && (values_freq[i] == values_freq[i-1])) {
            counter += 1;
            lastElement = i;
        } else {
            counter = 1;
        }

        if (values_freq[i] && ((i + 1) > max)) {
            max = i + 1;
        }
        if (counter == 5 && !flag) {
            printf("%s", "STRAIGHT");
            return ((STRAIGHT * 14) + (lastElement + 1));
        }
        if (counter == 5 && (lastElement == 13)) {
            printf("%s", "ROYAL_FLUSH");
            return ROYAL_FLUSH * 14;
        }
        if (counter == 5) {
            printf("%s", "STRAIGHT_FLUSH");
            return (STRAIGHT_FLUSH * 14) + (lastElement + 1);
        }
    }

    if (flag) {
        printf("%s", "FLUSH");
        return (FLUSH * 14) + max;
    }
    return 0;
}

int weighHighHand(char ** hand, int handSize) {
    int * suits_freq = calloc(4, sizeof(int));
    int * values_freq = calloc(14, sizeof(int));
    int * visited_values = calloc(14, sizeof(int));
    int score = 0;
    for (int i = 0; i < handSize; i++) {
        const int number = getCardValue(hand[i][1],0);
        const int suit = getSuitValue(hand[i][0]);
        values_freq[number-1]++;
        suits_freq[suit]++;
    }


    int hasPair = 0;
    int hasTrio = 0;
    for (int i = 0; i < CARDS; i++) {
        if (values_freq[i] == 4) {
            score += (CARDS * FOUR_OF_A_KIND) + (i + 1);
            printf("%s","FOUR_OF_KIND");
            visited_values[i]++;
            break;
        }
        if (values_freq[i] == 3) {
            score += (CARDS * THREE_OF_A_KIND) + (i + 1);
            visited_values[i]++;
            printf("%s","THREE_OF_KIND");
            hasTrio = 1;
        }
        if (values_freq[i] == 2) {

            score += (CARDS * ONE_PAIR) + (i + 1);
            visited_values[i]++;
            printf("%s","PAIR");
            hasPair += 1;
        }
    }
    if (hasPair && hasTrio) {
        score += 14 * 2;
        printf("%s","FULL_HOUSE");
    }
    int highCard = 0;
    for (int i = (CARDS - 1) ; i >= 0; i--) {
        if (!visited_values[i] && values_freq[i]) {
            printf("%d",i + 1);
            printf("%c",'\n');
            highCard = i + 1;
            break;
        }
    }
    int bestScore = score + highCard;
    int hasStraight = findStraight(suits_freq, values_freq);

    printf("%c",'\n');
    free(suits_freq);
    free(values_freq);
    free(visited_values);
    return MAX(bestScore, hasStraight);
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

    //falta corregir este base case y manejar mejor la memoria

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
    return weighHighHand(takingOption, targetSize) > weighHighHand(skippingOption, targetSize) ? takingOption : skippingOption;
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
    //scanf("%d", &players);
    //scanf("%d", &deckSize);
    //scanf("%s", gamemode);
    //char ** communityCards = readHand(5);
    //free(communityCards);
    char ** deck = malloc(5 * sizeof(char *));
    for (int i = 0; i < 5; i++) {
        deck[i] = malloc(3 * sizeof(char));
    }
    deck[0] = "DK";
    deck[1] = "CK";
    deck[2] = "SA";
    deck[3] = "CA";
    deck[4] = "H3";

    char ** deck2 = malloc(5 * sizeof(char *));
    for (int i = 0; i < 5; i++) {
        deck2[i] = malloc(3 * sizeof(char));
    }
    deck2[0] = "DK";
    deck2[1] = "CK";
    deck2[2] = "SA";
    deck2[3] = "CA";
    deck2[4] = "D8";

    int score = weighHighHand(deck, 5);
    int score2 = weighHighHand(deck2, 5);
    printf("%d", score);
    printf("%c", '\n');
    printf("%d", score2);

    char ** fullHand = malloc(5 * sizeof(char *));
    for (int i = 0; i < 5; i++) {
        fullHand[i] = malloc(3 * sizeof(char));
    }
    fullHand[0] = "DK";
    fullHand[1] = "CK";
    fullHand[2] = "D2";
    fullHand[3] = "SA";
    fullHand[4] = "CA";
    fullHand[5] = "D1";
    fullHand[6] = "D8";

    char ** best = getBestHand(fullHand,5);
    printf("%s", "\n");
    for (int i = 0; i < 5; i++) {
        printf("%s", best[i]);
        printf("%c", ' ');
    }
    /*
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
    */
    return 0;
}