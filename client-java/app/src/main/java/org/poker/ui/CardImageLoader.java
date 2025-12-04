package org.poker.ui;

import org.poker.model.Card;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CardImageLoader {
    private String path="textures/textures/cartas/";

    private Map<String, Image> cardImages = new HashMap<>();

    public CardImageLoader() {
        loadAllCards();
    }

    private void loadAllCards() {

        String[] suits = {"CLUBS", "HEARTS", "SPADES", "DIAMONDS"};
        String[] entrada = {"C","H","S","D"};

        for (int suitRow = 0; suitRow < 4; suitRow++) {
            for (int valueCol = 1; valueCol < 14; valueCol++) {
                Image cardImg = new ImageIcon(path + entrada[suitRow] + valueCol + ".png").getImage();
                String key = suits[suitRow] + "_" + valueCol;
                cardImages.put(key, cardImg);

            }
        }
    }

    public Image getCardImage(Card card) {
        if (card == null) return null;
        return cardImages.get(card.getImageKey());
    }
}