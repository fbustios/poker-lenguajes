package org.poker.ui;

import org.poker.connection.PokerClient;
import org.poker.connection.PokerClientTCP;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class LoginDisplay extends JFrame {

    private JButton btnConnect, btnJoinGame, btnExitGame;
    private Image backgroundImage;
    private Image buttonTexture;
    private int bet = 0;
    private boolean connected = false;

    private PokerClient pokerClient;

    private Runnable joinGameAction;

    private Consumer<PokerClient> onConnectAction;

    public LoginDisplay() {
        setTitle("Poker Login");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        loadResources();

        JPanel mainPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(new Color(50, 50, 50));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        setContentPane(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0);

        JLabel titleLabel = new JLabel("HORSE POKER", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridy = 0;
        mainPanel.add(titleLabel, gbc);

        btnConnect = createTexturedButton("Connect");
        btnJoinGame = createTexturedButton("Join Game");
        btnExitGame = createTexturedButton("Exit Game");

        gbc.gridy = 1;
        mainPanel.add(btnConnect, gbc);
        gbc.gridy = 2;
        mainPanel.add(btnJoinGame, gbc);
        gbc.gridy = 3;
        mainPanel.add(btnExitGame, gbc);


        btnConnect.addActionListener(e -> performConnection());
        btnExitGame.addActionListener(e -> System.exit(0));


        btnJoinGame.addActionListener(e -> performJoinGame());
    }

    private void performJoinGame() {

        if (!connected || pokerClient == null) {
            JOptionPane.showMessageDialog(this,
                    "You must connect first!",
                    "Not connected",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String selectedMode = "HOLDEM";

        try {
            System.out.println("Joining game mode automatically: " + selectedMode);
            pokerClient.joinGame(selectedMode,bet);

            if (joinGameAction != null) {
                joinGameAction.run();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error joining game: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void performConnection() {
        JTextField nameField = new JTextField();
        JTextField betField = new JTextField("0");
        JTextField hostField = new JTextField("localhost");
        JTextField portField = new JTextField("5000");

        JPanel inputPanel = new JPanel(new GridLayout(0, 1));
        inputPanel.add(new JLabel("Player Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Bet:"));
        inputPanel.add(betField);
        inputPanel.add(new JLabel("Host (default: localhost):"));
        inputPanel.add(hostField);
        inputPanel.add(new JLabel("Port (default: 5000):"));
        inputPanel.add(portField);

        int result = JOptionPane.showConfirmDialog(this, inputPanel,
                "Server Connection", JOptionPane.OK_CANCEL_OPTION);
        bet = Integer.parseInt(String.valueOf(betField.getText()));
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String host = hostField.getText().trim();
            String portStr = portField.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name is required!");
                return;
            }
            if (host.isEmpty()) host = "localhost";
            int port = 5000;
            try {
                if (!portStr.isEmpty()) port = Integer.parseInt(portStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid port number.");
                return;
            }

            try {
                PokerClient client = new PokerClientTCP(host, port, name);

                if (client.isConnected()) {
                    this.pokerClient = client;
                    this.connected = true;

                    client.startListening();

                    JOptionPane.showMessageDialog(this, "Connected successfully as " + name);

                    // Notificar al main que tenemos cliente
                    if (onConnectAction != null) {
                        onConnectAction.accept(client);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Could not connect to server.", "Connection Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JButton createTexturedButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                if (buttonTexture != null) {
                    g.drawImage(buttonTexture, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
                super.paintComponent(g);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 16));
                FontMetrics fm = g.getFontMetrics();
                int tx = (getWidth() - fm.stringWidth(getText())) / 2;
                int ty = (getHeight() + fm.getAscent()) / 2 - 4;
                g.drawString(getText(), tx, ty);
            }
        };
        button.setPreferredSize(new Dimension(200, 50));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        return button;
    }

    private void loadResources() {
        try {
            backgroundImage = new ImageIcon("textures/fondo.png").getImage();
            buttonTexture = new ImageIcon("textures/boton.png").getImage();

        } catch (Exception e) {
            System.err.println("No se pudieron cargar texturas: " + e.getMessage());
        }
    }

    public void setJoinGameAction(Runnable action) {
        this.joinGameAction = action;
    }

    public void setOnConnectAction(Consumer<PokerClient> action) {
        this.onConnectAction = action;
    }
}