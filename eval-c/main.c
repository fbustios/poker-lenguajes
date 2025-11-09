#include <stdio.h>
#include <stdlib.h>

int weigh_hand(int * hand, int handsize) {
    int * suits_freq = (int *)calloc(4, sizeof(int));
    int * color_freq = (int *)calloc(2, sizeof(int));
    int * values_freq = (int *)calloc(14, sizeof(int));
    for (int i = 0; i < handsize; i++) {

    }
    free(suits_freq);
    free(color_freq);
    free(values_freq);
    return 0;
}

int main(void) {
    printf("Hello, World!\n");


    return 0;
}
