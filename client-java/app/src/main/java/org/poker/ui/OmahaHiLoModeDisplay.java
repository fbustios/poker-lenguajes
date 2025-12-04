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

public class OmahaHiLoModeDisplay extends JFrame implements ModeDisplay {

    private final PokerClient client;
    private String localPlayerName;
    private final CardImageLoader cardLoader;

    private JButton btnCall, btnCheck, btnRaise, btnFold;
    private JButton btnGameState;
    private JLabel infoLabel;
    private Image backgroundImage, backCardImage;

    public OmahaHiLoModeDisplay(PokerClient client) {
        this.client = client;
        this.cardLoader = new CardImageLoader();

        setTitle("Omaha Hi-Lo (8 or Better)");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        loadResources();
        initUI();
    }

    @Override
    public void showDisplay() { setVisible(true); }

    @Override
    public void hideDisplay() { setVisible(false); }

    @Override
    public void setLocalPlayerName(String name) {
        this.localPlayerName = name;
        if (name != null) setTitle("Omaha Hi-Lo - Jugando como: " + name);
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

        infoLabel = new JLabel("Esperando inicio...", SwingConstants.CENTER);
        infoLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        infoLabel.setForeground(Color.YELLOW);
        infoLabel.setBounds(0, 20, getWidth(), 30);
        mainPanel.add(infoLabel);

        setupButtons(mainPanel);
    }

    private void setupButtons(JPanel panel) {
        int btnWidth = 120; int btnHeight = 50; int spacing = 20;
        int startX = getWidth() - (btnWidth * 4) - (spacing * 5);
        int startY = getHeight() - btnHeight - 40;

        btnCall = createGameButton("Call", startX, startY, btnWidth, btnHeight);
        btnCheck = createGameButton("Check", startX + btnWidth + spacing, startY, btnWidth, btnHeight);
        btnRaise = createGameButton("Raise", startX + (btnWidth + spacing) * 2, startY, btnWidth, btnHeight);
        btnFold = createGameButton("Fold", startX + (btnWidth + spacing) * 3, startY, btnWidth, btnHeight);

        btnGameState = createGameButton("GameState", 20, 20, 120, 40);
        btnGameState.setBackground(new Color(0, 102, 204));

        btnCall.addActionListener(e -> { if (isMyTurn()) client.placeBet("EIGHT", 0, "CALL", 0); });
        btnCheck.addActionListener(e -> { if (isMyTurn()) client.placeBet("EIGHT", 0, "CHECK", 0); });
        btnFold.addActionListener(e -> { if (isMyTurn()) client.placeBet("EIGHT", 0, "FOLD", 0); });
        btnRaise.addActionListener(e -> openRaiseDialog());

        btnGameState.addActionListener(e -> showGameStateInfo());

        panel.add(btnCall); panel.add(btnCheck); panel.add(btnRaise); panel.add(btnFold); panel.add(btnGameState);
        setButtonsEnabled(false);
    }

    private void showGameStateInfo() {
        if (client == null || client.getGameState() == null) return;
        GameState s = client.getGameState();
        StringBuilder sb = new StringBuilder();
        sb.append("=== OMAHA HI-LO (EIGHT) ===\n");
        sb.append("Pot: $").append(s.getPot()).append("\n");
        sb.append("Turno: ").append(s.getNextPlayer()).append("\n\n");

        sb.append("--- CARTAS COMUNITARIAS (MESA) ---\n");
        if (s.getCommunityCards() != null && !s.getCommunityCards().isEmpty()) {
            sb.append(s.getCommunityCards()).append("\n");
        } else {
            sb.append("[Ninguna visible]\n");
        }
        sb.append("\n");

        sb.append("--- JUGADORES ---\n");
        for (PlayerModel p : s.getPlayers()) {
            sb.append(p.getName()).append(" ($").append(p.getMoney()).append(")");
            if (p.getName().equals(localPlayerName)) {
                sb.append(" | TUS CARTAS: ").append(p.getCards());
            } else {
                sb.append(" | [4 Cartas Ocultas]");
            }
            sb.append("\n");
        }
        JOptionPane.showMessageDialog(this, new JScrollPane(new JTextArea(sb.toString())), "GameState EIGHT", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateButtonStates() {
        if (client == null || client.getGameState() == null) return;
        boolean myTurn = isMyTurn();
        setButtonsEnabled(myTurn);
        if (myTurn) {
            infoLabel.setText("¡ES TU TURNO!"); infoLabel.setForeground(Color.GREEN);
        } else {
            String next = client.getGameState().getNextPlayer();
            infoLabel.setText(next != null ? "Turno de: " + next : "Esperando...");
            infoLabel.setForeground(Color.WHITE);
        }
    }

    private void setButtonsEnabled(boolean enabled) {
        btnCall.setEnabled(enabled); btnCheck.setEnabled(enabled);
        btnRaise.setEnabled(enabled); btnFold.setEnabled(enabled);
    }

    private boolean isMyTurn() { return client != null && client.isMyTurn(); }

    private void drawGame(Graphics g) {
        if (backgroundImage != null) g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        else { g.setColor(new Color(34, 100, 34)); g.fillRect(0, 0, getWidth(), getHeight()); }

        GameState state = (client != null) ? client.getGameState() : null;
        if (state == null) return;

        drawTableInfo(g, state);
        drawCommunityCards(g, state);
        drawPlayers(g, state.getPlayers());
    }

    private void drawTableInfo(Graphics g, GameState state) {
        g.setColor(Color.WHITE); g.setFont(new Font("Arial", Font.BOLD, 24));
        String potText = "Pot: $" + state.getPot();
        g.drawString(potText, getWidth()/2 - g.getFontMetrics().stringWidth(potText)/2, getHeight()/2 + 130);

        g.setFont(new Font("Arial", Font.PLAIN, 18));
        String round = state.getGameModeRound();
        if (round != null) g.drawString("Ronda: " + round, 160, 45);
    }

    private void drawCommunityCards(Graphics g, GameState state) {
        int cx = getWidth()/2; int cy = getHeight()/2;

        List<Card> cc = state.getCommunityCards();
        if (cc == null) cc = new ArrayList<>();

        int cw=60, ch=90, sp=10;
        int sx = cx - ((5*cw + 4*sp)/2);
        int sy = cy - ch/2;

        for(int i=0; i<5; i++) {
            int x = sx + i*(cw+sp);

            if(i < cc.size()) {
                Image img = cardLoader.getCardImage(cc.get(i));
                if(img!=null) g.drawImage(img,x,sy,cw,ch,this);
                else {
                    g.setColor(Color.WHITE); g.fillRect(x,sy,cw,ch);
                    g.setColor(Color.BLACK); g.drawString(cc.get(i).toString(), x+5, sy+20);
                }
            }
            else {
                g.setColor(new Color(0,0,0,50));
                g.drawRect(x,sy,cw,ch);
            }
        }
    }

    private void drawPlayers(Graphics g, List<PlayerModel> players) {
        if (players == null) return;
        List<PlayerModel> sorted = rotatePlayersToLocal(players);
        int cx=getWidth()/2, cy=getHeight()/2, rx=420, ry=240;

        for(int i=0; i<sorted.size(); i++) {
            double ang = Math.PI/2 + (i*(2*Math.PI/sorted.size()));
            drawOnePlayer(g, sorted.get(i), (int)(cx+rx*Math.cos(ang)), (int)(cy+ry*Math.sin(ang)));
        }
    }

    private void drawOnePlayer(Graphics g, PlayerModel p, int x, int y) {
        boolean me = p.getName().equals(localPlayerName);
        boolean active = client.getGameState().getNextPlayer()!=null && client.getGameState().getNextPlayer().equals(p.getName());

        if(active) { g.setColor(Color.YELLOW); g.fillOval(x-45, y-45, 90, 90); }

        g.setColor(me ? new Color(100,149,237) : Color.DARK_GRAY);
        g.fillOval(x-40, y-40, 80, 80);

        g.setColor(Color.WHITE); g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString(p.getName(), x-g.getFontMetrics().stringWidth(p.getName())/2, y-50);

        g.setColor(new Color(144,238,144));
        g.drawString("$"+p.getMoney(), x-g.getFontMetrics().stringWidth("$"+p.getMoney())/2, y+55);

        drawPlayerCards(g, p, x, y, me);
    }

    private void drawPlayerCards(Graphics g, PlayerModel p, int x, int y, boolean me) {
        if (!me) return;
        List<Card> cards = p.getCards();
        int n = (cards!=null && !cards.isEmpty()) ? cards.size() : 4;

        int cw=45, ch=65, sp=5;
        int sx = x - ((n*cw + (n-1)*sp)/2);
        int sy = y-10;

        for(int i=0; i<n; i++) {
            Image img = null;
            if (me && cards != null && i < cards.size()) {
                img = cardLoader.getCardImage(cards.get(i));
            }

            if(img==null) img = backCardImage;

            if(img!=null) g.drawImage(img, sx + i*(cw+sp), sy, cw, ch, this);
            else { g.setColor(Color.RED); g.fillRect(sx + i*(cw+sp), sy, cw, ch); }
        }
    }

    private List<PlayerModel> rotatePlayersToLocal(List<PlayerModel> l) {
        if(l==null) return new ArrayList<>();
        List<PlayerModel> list = new ArrayList<>(l);
        int idx = -1;
        for(int i=0; i<list.size(); i++) if(list.get(i).getName().equals(localPlayerName)) idx=i;
        if(idx>0) Collections.rotate(list, -idx);
        return list;
    }

    private JButton createGameButton(String t, int x, int y, int w, int h) {
        JButton b=new JButton(t); b.setBounds(x,y,w,h);
        b.setFont(new Font("Arial", Font.BOLD, 14)); b.setBackground(new Color(40,40,40)); b.setForeground(Color.WHITE);
        b.setFocusPainted(false); return b;
    }

    private void openRaiseDialog() {
        if(!isMyTurn()) return;
        int max = 0; for(PlayerModel p:client.getGameState().getPlayers()) if(p.getName().equals(localPlayerName)) max=p.getMoney();
        String s = JOptionPane.showInputDialog(this, "Apostar (Max "+max+"):");
        try { if(s!=null) client.placeBet("EIGHT", 0, "RAISE", Integer.parseInt(s)); } catch(Exception e){}
    }

    private void loadResources() {
        try { backgroundImage = new ImageIcon("textures/fondo_game.png").getImage(); backCardImage = new ImageIcon("textures/atras.png").getImage(); } catch(Exception e){}
    }
}