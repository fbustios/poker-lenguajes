package org.poker.ui;

import org.poker.model.Card;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CardImageLoader {

    private BufferedImage spriteSheet;
    private final int CARD_WIDTH = 72;
    private final int CARD_HEIGHT = 97;

    private Map<String, BufferedImage> cardImages = new HashMap<>();

    public CardImageLoader() {
        try {
            var resource = getClass().getResource("/textures/cartas.png");

            if (resource == null) {
                System.err.println("¡ERROR CRÍTICO! No se encontró el archivo: /textures/cartas.png");
                System.err.println("Asegúrate de que la carpeta 'textures' esté dentro de 'src/main/resources/'");
                return;
            }

            spriteSheet = ImageIO.read(resource);
            loadAllCards();

        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error cargando el sprite sheet de cartas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadAllCards() {
        if (spriteSheet == null) return;

        String[] suits = {"CLUBS", "HEARTS", "SPADES", "DIAMONDS"};

        for (int suitRow = 0; suitRow < 4; suitRow++) {
            for (int valueCol = 0; valueCol < 13; valueCol++) {

                int x = valueCol * CARD_WIDTH;
                int y = suitRow * CARD_HEIGHT;

                if (x + CARD_WIDTH <= spriteSheet.getWidth() && y + CARD_HEIGHT <= spriteSheet.getHeight()) {
                    BufferedImage cardImg = spriteSheet.getSubimage(x, y, CARD_WIDTH, CARD_HEIGHT);
                    int cardValue = valueCol + 1;
                    String key = suits[suitRow] + "_" + cardValue;
                    cardImages.put(key, cardImg);
                }
            }
        }
    }

    public BufferedImage getCardImage(Card card) {
        if (card == null) return null;
        String key = card.getImageKey();
        return cardImages.get(key);
    }
}