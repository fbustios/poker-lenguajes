package org.poker.ui;

import org.poker.connection.PokerClient;
import org.poker.model.Card;
import org.poker.model.GameState;
import org.poker.model.PlayerModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class HoldEmModeDisplay extends JFrame implements ModeDisplay {

    private final PokerClient client;
    private String localPlayerName;
    private final CardImageLoader cardLoader;

    private JButton btnCall, btnCheck, btnRaise, btnFold;
    private JButton btnGameState;
    private JLabel infoLabel;
    private Image backgroundImage, backCardImage, buttonTexture;

    public HoldEmModeDisplay(PokerClient client) {
        this.client = client;
        this.cardLoader = new CardImageLoader();

        setTitle("Texas Hold'Em Poker");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        loadResources();
        initUI();
    }

    @Override
    public void showDisplay() {
        setVisible(true);
    }

    @Override
    public void hideDisplay() {
        setVisible(false);
    }

    @Override
    public void setLocalPlayerName(String name) {
        this.localPlayerName = name;
        if (name != null) {
            setTitle("Texas Hold'Em - Jugando como: " + name);
        }
    }

    @Override
    public void updateState(Map<String, String> message) {
        updateButtonStates();
        repaint();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGame(g);
            }
        };
        setContentPane(mainPanel);

        infoLabel = new JLabel("Esperando inicio de partida...", SwingConstants.CENTER);
        infoLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        infoLabel.setForeground(Color.YELLOW);
        infoLabel.setBounds(0, 20, getWidth(), 30);
        mainPanel.add(infoLabel);

        setupButtons(mainPanel);
    }

    private void setupButtons(JPanel panel) {
        int btnWidth = 120;
        int btnHeight = 50;
        int spacing = 20;

        int startX = getWidth() - (btnWidth * 4) - (spacing * 5);
        int startY = getHeight() - btnHeight - 40;

        btnCall = createGameButton("Call", startX, startY, btnWidth, btnHeight);
        btnCheck = createGameButton("Check", startX + btnWidth + spacing, startY, btnWidth, btnHeight);
        btnRaise = createGameButton("Raise", startX + (btnWidth + spacing) * 2, startY, btnWidth, btnHeight);
        btnFold = createGameButton("Fold", startX + (btnWidth + spacing) * 3, startY, btnWidth, btnHeight);

        btnGameState = createGameButton("GameState", 20, 20, 120, 40);
        btnGameState.setBackground(new Color(0, 102, 204));

        btnCall.addActionListener(e -> {
            if (isMyTurn()) client.placeBet("HOLDEM", 0, "CALL", 0);
        });
        btnCheck.addActionListener(e -> {
            if (isMyTurn()) client.placeBet("HOLDEM", 0, "CHECK", 0);
        });
        btnFold.addActionListener(e -> {
            if (isMyTurn()) client.placeBet("HOLDEM", 0, "FOLD", 0);
        });
        btnRaise.addActionListener(e -> openRaiseDialog());

        btnGameState.addActionListener(e -> showGameStateInfo());

        panel.add(btnCall);
        panel.add(btnCheck);
        panel.add(btnRaise);
        panel.add(btnFold);
        panel.add(btnGameState);

        setButtonsEnabled(false);
    }

    private void showGameStateInfo() {
        if (client == null || client.getGameState() == null) {
            JOptionPane.showMessageDialog(this, "No hay GameState disponible (client is null).");
            return;
        }

        GameState s = client.getGameState();
        StringBuilder sb = new StringBuilder();
        sb.append("=== INFORMACIÓN DEL GAMESTATE (HOLDEM) ===\n\n");
        sb.append("Pot (Bote): $").append(s.getPot()).append("\n");
        sb.append("Ronda: ").append(s.getGameModeRound()).append("\n");
        sb.append("Turno actual: ").append(s.getNextPlayer()).append("\n");
        sb.append("Dealer: ").append(s.getDealer()).append("\n\n");

        sb.append("--- CARTAS COMUNITARIAS ---\n");
        if (s.getCommunityCards() != null && !s.getCommunityCards().isEmpty()) {
            sb.append(s.getCommunityCards()).append("\n");
        } else {
            sb.append("[Ninguna visible]\n");
        }
        sb.append("\n");

        sb.append("--- JUGADORES ---\n");
        for (PlayerModel p : s.getPlayers()) {
            sb.append(p.getName())
                    .append(" | Dinero: $").append(p.getMoney())
                    .append(" | Cartas: ").append(p.getCards().size());

            if (p.getName().equals(localPlayerName)) {
                sb.append(" ").append(p.getCards());
            } else {
                sb.append(" [Ocultas]");
            }
            sb.append("\n");
        }

        JOptionPane.showMessageDialog(this, new JScrollPane(new JTextArea(sb.toString())), "GameState HoldEm", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateButtonStates() {
        if (client == null || client.getGameState() == null) return;

        boolean myTurn = isMyTurn();
        setButtonsEnabled(myTurn);

        if (myTurn) {
            infoLabel.setText("¡ES TU TURNO!");
            infoLabel.setForeground(Color.GREEN);
        } else {
            String next = client.getGameState().getNextPlayer();
            infoLabel.setText(next != null ? "Turno de: " + next : "Esperando...");
            infoLabel.setForeground(Color.WHITE);
        }
    }

    private void setButtonsEnabled(boolean enabled) {
        btnCall.setEnabled(enabled);
        btnCheck.setEnabled(enabled);
        btnRaise.setEnabled(enabled);
        btnFold.setEnabled(enabled);
    }

    private boolean isMyTurn() {
        return client != null && client.isMyTurn();
    }

    private void drawGame(Graphics g) {
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(new Color(34, 100, 34));
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        GameState state = (client != null) ? client.getGameState() : null;
        if (state == null) return;

        drawTableInfo(g, state);

        drawCommunityCards(g, state);

        drawPlayers(g, state.getPlayers());
    }

    private void drawTableInfo(Graphics g, GameState state) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));

        String potText = "Pot: $" + state.getPot();
        FontMetrics fm = g.getFontMetrics();
        int cx = getWidth() / 2 - fm.stringWidth(potText) / 2;
        int cy = getHeight() / 2 + 120;
        g.drawString(potText, cx, cy);

        g.setFont(new Font("Arial", Font.PLAIN, 18));
        String round = state.getGameModeRound();
        if (round != null) {
            g.drawString("Ronda: " + round, 160, 45);
        }
    }

    private void drawCommunityCards(Graphics g, GameState state) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        List<Card> communityCards = state.getCommunityCards();
        if (communityCards == null) communityCards = new ArrayList<>();

        int cardW = 60;
        int cardH = 90;
        int spacing = 10;
        int maxCards = 5;

        int totalWidth = (maxCards * cardW) + ((maxCards - 1) * spacing);
        int startX = centerX - (totalWidth / 2);
        int startY = centerY - (cardH / 2);

        for (int i = 0; i < maxCards; i++) {
            int x = startX + (i * (cardW + spacing));

            if (i < communityCards.size()) {
                Image img = cardLoader.getCardImage(communityCards.get(i));
                if (img != null) {
                    g.drawImage(img, x, startY, cardW, cardH, this);
                } else {
                    g.setColor(Color.WHITE);
                    g.fillRect(x, startY, cardW, cardH);
                    g.setColor(Color.BLACK);
                    g.drawString(communityCards.get(i).toString(), x+5, startY+20);
                }
            } else {
                g.setColor(new Color(0, 0, 0, 50));
                g.drawRect(x, startY, cardW, cardH);
            }
        }
    }

    private void drawPlayers(Graphics g, List<PlayerModel> players) {
        if (players == null || players.isEmpty()) return;

        List<PlayerModel> visualOrder = rotatePlayersToLocal(players);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radiusX = 420;
        int radiusY = 240;

        for (int i = 0; i < visualOrder.size(); i++) {
            PlayerModel p = visualOrder.get(i);
            double angle = Math.PI / 2 + (i * (2 * Math.PI / visualOrder.size()));
            int x = (int) (centerX + radiusX * Math.cos(angle));
            int y = (int) (centerY + radiusY * Math.sin(angle));
            drawOnePlayer(g, p, x, y);
        }
    }

    private void drawOnePlayer(Graphics g, PlayerModel p, int x, int y) {
        boolean isMe = p.getName().equals(localPlayerName);
        boolean isActive = client.getGameState().getNextPlayer() != null
                && client.getGameState().getNextPlayer().equals(p.getName());

        if (isActive) {
            g.setColor(Color.YELLOW);
            g.fillOval(x - 45, y - 45, 90, 90);
        }
        g.setColor(isMe ? new Color(100, 149, 237) : Color.DARK_GRAY);
        g.fillOval(x - 40, y - 40, 80, 80);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        drawCenteredString(g, p.getName(), x, y - 50);

        g.setColor(new Color(144, 238, 144));
        drawCenteredString(g, "$" + p.getMoney(), x, y + 55);

        drawPlayerCards(g, p, x, y, isMe);
    }

    private void drawPlayerCards(Graphics g, PlayerModel p, int x, int y, boolean isMe) {
        if (!isMe) return;

        List<Card> cards = p.getCards();
        if (cards == null || cards.isEmpty()) return;

        int numCards = cards.size();

        int cardW = 50;
        int cardH = 75;
        int spacing = 15;
        int totalW = (numCards * cardW) + ((numCards - 1) * spacing);

        int startX = x - (totalW / 2);
        int startY = y - 10;

        for (int i = 0; i < numCards; i++) {
            Image img = cardLoader.getCardImage(cards.get(i));

            // Fallback si la imagen falla
            if (img == null) img = backCardImage;

            if (img != null) {
                g.drawImage(img, startX + (i * (cardW + spacing)), startY, cardW, cardH, this);
            } else {
                g.setColor(Color.WHITE);
                g.fillRect(startX + (i * (cardW + spacing)), startY, cardW, cardH);
                g.setColor(Color.BLACK);
                g.drawRect(startX + (i * (cardW + spacing)), startY, cardW, cardH);
            }
        }
    }

    private JButton createGameButton(String text, int x, int y, int w, int h) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, w, h);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBackground(new Color(40, 40, 40));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        return btn;
    }

    private void openRaiseDialog() {
        if (!isMyTurn()) return;
        int maxMoney = 0;
        for(PlayerModel p : client.getGameState().getPlayers()) {
            if (p.getName().equals(localPlayerName)) {
                maxMoney = p.getMoney();
                break;
            }
        }
        String input = JOptionPane.showInputDialog(this, "Cantidad a apostar (Máx: " + maxMoney + "):");
        try {
            if (input != null && !input.isEmpty()) {
                int amount = Integer.parseInt(input);
                client.placeBet("HOLDEM", 0, "RAISE", amount);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Número inválido");
        }
    }

    private List<PlayerModel> rotatePlayersToLocal(List<PlayerModel> original) {
        if (original == null) return new ArrayList<>();
        List<PlayerModel> list = new ArrayList<>(original);
        int myIndex = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals(localPlayerName)) {
                myIndex = i;
                break;
            }
        }
        if (myIndex > 0) {
            Collections.rotate(list, -myIndex);
        }
        return list;
    }

    private void drawCenteredString(Graphics g, String text, int x, int y) {
        FontMetrics fm = g.getFontMetrics();
        int w = fm.stringWidth(text);
        g.drawString(text, x - (w / 2), y);
    }

    private void loadResources() {
        try {
            backgroundImage = new ImageIcon("textures/fondo_game.png").getImage();
            backCardImage = new ImageIcon("textures/atras.png").getImage();
            buttonTexture = new ImageIcon("textures/boton.png").getImage();
        } catch (Exception e) {
            System.out.println("No se encontraron texturas, usando colores por defecto.");
        }
    }
}