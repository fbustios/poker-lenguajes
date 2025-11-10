#include <stdio.h>
#include <stdlib.h>
#include <string.h>

char ** readDeck(int size) {
    char ** deck = malloc(size * sizeof(char *));
    for (int i = 0; i < size; i++) {
        deck[i] = malloc(sizeof(char *));
        scanf("%s", deck[i]);
    }
    return deck;
}

int weighHighHand(char ** hand, int handsize) {
    int * suits_freq = calloc(4, sizeof(int));
    int * color_freq = calloc(2, sizeof(int));
    int * values_freq = calloc(14, sizeof(int));
    for (int i = 0; i < handsize; i++) {

    }
    free(suits_freq);
    free(color_freq);
    free(values_freq);
    return 0;
}

int isLowHand(char ** hand, int handSize) {

}
int weighLowHand(char ** hand, int handSize) {
    int handWeight = 0;
    for (int i = 0; i < handSize; i++) {
        char cardNumber = hand[i][3];
        int cardValue = cardNumber - '0';
        handWeight -= cardValue;
    }
    return handWeight;
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
    char ** takingOption = getBestHandAux(fullHand, currentHand[currentIndex] = fullHand[currentIndex], targetSize, currentHandSize++, currentIndex++);
    char ** skippingOption = getBestHandAux(fullHand, currentHand, targetSize, currentHandSize,currentIndex++);
    //return weighHighHand() > weighHighHand() ? takingOption : skippingOption;
    return skippingOption;

}

char ** getBestHand(char ** fullHand, int targetSize) {
    char ** currentHand = calloc(targetSize, sizeof(char *));
    for (int i = 0; i < targetSize; i++) {
        currentHand[i] = calloc(1, sizeof(char));
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
    char ** communityCards = readDeck(5);
    free(communityCards);
    for(int i = 0; i < players; i++) {
        char ** playerDeck = readDeck(deckSize);
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
