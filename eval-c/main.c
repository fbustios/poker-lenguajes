#include <stdio.h>
#include <stdlib.h>
#include <string.h>

char ** readCommunityCards(int communityCards) {
    char ** cards = calloc(5, sizeof(char *));

    for (int i = 0; i < communityCards; i++) {
        cards[i] = calloc(1, sizeof(char));
        scanf("%s", cards[i]);
    }
    for (int i = 0; i < communityCards; i++) {
        printf("%s", cards[i]);
        printf("%s", "\n");
    }
    return cards;
}


int weighHighHand(int * hand, int handsize) {
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

int weighLowHand(char ** hand, int handsize) {
    for (int i = 0; i < handsize; i++) {
    }
    return 0;
}

char ** mergeCards() {
    return ;
}

int getBestHand(char ** fullHand, int fullHandSize) {

}

int main(void) {
    int players;
    char gamemode[10];
    scanf("%d", &players);
    scanf("%s", gamemode);
    if (strcmp(gamemode, "omaha") || strcmp(gamemode,"holdem")) {
        char ** communityCards = readCommunityCards(5);
        mergeCards();
        return weighHighHand(,7);
    }
    if (strcmp(gamemode, "razz") || strcmp(gamemode,"omaha-hilo")) {
        int lowHand = weighLowHand(,7);
        return lowHand;
    }
    for(int i = 0; i < players; i++) {

    }


    return 0;
}
