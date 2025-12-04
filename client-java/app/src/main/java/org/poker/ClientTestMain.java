package org.poker;

import org.poker.model.GameState;
import org.poker.model.PlayerModel;
import org.poker.connection.PokerClient;
import org.poker.connection.PokerClientTCP;

import java.util.Scanner;

public final class ClientTestMain {
    private static final String PLAYER_NAME_MSG = "Nombre del jugador (4 letras exactas): ";

    private ClientTestMain() {

    }

    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        System.out.println("=== CLIENTE DE POKER HORSE - MODO PRUEBA ===\n");

        System.out.print("Host del servidor (default: localhost): ");
        String host = scanner.nextLine();
        if (host.isEmpty()) {
            host = "localhost";
        }

        System.out.print("Puerto (default: 5000): ");
        String portStr = scanner.nextLine();
        int port = portStr.isEmpty() ? 5000 : Integer.parseInt(portStr);

        PokerClient client = new PokerClientTCP(host, port);

        if (!client.isConnected()) {
            System.err.println("No se pudo conectar al servidor.");
            scanner.close();
            return;
        }

        client.setMessageListener((event, message) -> {
            System.out.println("\n[EVENTO] " + event);

            if (event.equals("game_started")) {
                System.out.println("¡El juego ha comenzado!");
            } else if (event.equals("player_action")) {
                String player = message.get("player");
                String action = message.get("pokerAction");
                System.out.println(player + " realizó: " + action);
            }
        });

        client.startListening();

        boolean running = true;
        while (running) {
            System.out.println("\n╔════════════════════════════════════╗");
            System.out.println("║        MENÚ DE POKER HORSE         ║");
            System.out.println("╠════════════════════════════════════╣");
            System.out.println("║ 1. Unirse al juego                 ║");
            System.out.println("║ 2. Apostar (CALL)                  ║");
            System.out.println("║ 3. Apostar (RAISE)                 ║");
            System.out.println("║ 4. Retirarse (FOLD)                ║");
            System.out.println("║ 5. Pasar (CHECK)                   ║");
            System.out.println("║ 6. Ver estado del juego            ║");
            System.out.println("║ 7. Prueba de conexión (echo)       ║");
            System.out.println("║ 8. Salir                           ║");
            System.out.println("╚════════════════════════════════════╝");
            System.out.print("Selecciona una opción: ");

            String option = scanner.nextLine().trim();

            String playerName;
            try {
                switch (option) {
                    case "1":
                        System.out.println("\nModos disponibles:");
                        System.out.println("  1. Holdem");
                        System.out.println("  2. Omaha");
                        System.out.println("  3. Razz");
                        System.out.println("  4. Stud");
                        System.out.println("  5. Eight");
                        System.out.print("Selecciona modo de juego: ");
                        String modeChoice = scanner.nextLine();

                        String mode = switch (modeChoice) {
                            case "1" -> "Holdem";
                            case "2" -> "Omaha";
                            case "3" -> "Razz";
                            case "4" -> "Stud";
                            case "5" -> "Eight";
                            default -> modeChoice;
                        };

                        System.out.print(PLAYER_NAME_MSG);
                        playerName = scanner.nextLine();
                        playerName = playerName.toLowerCase();

                        System.out.print("Dinero: ");
                        final int money = scanner.nextInt();

                        client.joinGame(mode, playerName, money);
                        System.out.println("✓ Solicitud enviada para unirse a " + mode);
                        break;

                    case "2":
                        System.out.print(PLAYER_NAME_MSG);
                        playerName = scanner.nextLine();
                        playerName = playerName.toLowerCase();

                        if (checkMyTurn(client, playerName)) {
                            client.placeBet(client.getGameState().getGameMode(), playerName, "call", 0);
                            System.out.println("✓ CALL enviado");
                        }
                        break;

                    case "3":
                        System.out.print(PLAYER_NAME_MSG);
                        playerName = scanner.nextLine();
                        playerName = playerName.toLowerCase();

                        if (checkMyTurn(client, playerName)) {
                            System.out.print("Cantidad a apostar: ");
                            int amount = Integer.parseInt(scanner.nextLine());
                            client.placeBet(client.getGameState().getGameMode(), playerName, "raise", amount);
                            System.out.println("✓ RAISE de " + amount + " enviado");
                        }
                        break;

                    case "4":
                        System.out.print(PLAYER_NAME_MSG);
                        playerName = scanner.nextLine();
                        playerName = playerName.toLowerCase();

                        if (checkMyTurn(client, playerName)) {
                            client.placeBet(client.getGameState().getGameMode(), playerName, "fold", 0);
                            System.out.println("✓ FOLD enviado");
                        }
                        break;

                    case "5":
                        System.out.print(PLAYER_NAME_MSG);
                        playerName = scanner.nextLine();
                        playerName = playerName.toLowerCase();

                        if (checkMyTurn(client, playerName)) {
                            client.placeBet(client.getGameState().getGameMode(), playerName, "check", 0);
                            System.out.println("✓ CHECK enviado");
                        }
                        break;

                    case "6":
                        System.out.print(PLAYER_NAME_MSG);
                        playerName = scanner.nextLine();
                        playerName = playerName.toLowerCase();

                        showGameState(client, playerName);
                        break;

                    case "7":
                        System.out.println("Enviando mensaje de prueba...");
                        System.out.println("Estado de conexión: " + (client.isConnected() ? "Conectado" : "Desconectado"));
                        break;

                    case "8":
                        System.out.println("\n¡Hasta luego!");
                        client.disconnect();
                        running = false;
                        break;

                    default:
                        System.out.println("⚠ Opción inválida");
                }
            } catch (Exception e) {
                System.err.println("❌ Error: " + e.getMessage());
                e.printStackTrace();
            }

            if (running) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        scanner.close();
    }

    private static boolean checkMyTurn(PokerClient client, String playerName) {
        if (!client.isMyTurn(playerName)) {
            System.out.println("⚠ No es tu turno. Espera a que te toque jugar.");
            return false;
        }
        return true;
    }

    private static void showGameState(PokerClient client, String playerName) {
        GameState state = client.getGameState();

        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║      ESTADO ACTUAL DEL JUEGO       ║");
        System.out.println("╠════════════════════════════════════╣");
        System.out.println("║ Modo: " + padRight(state.getGameMode(), 28) + " ║");
        System.out.println("║ Ronda: " + padRight(state.getGameModeRound(), 27) + " ║");
        System.out.println("║ Pot: $" + padRight(String.valueOf(state.getPot()), 27) + " ║");
        System.out.println("║ Dealer: " + padRight(state.getDealer(), 26) + " ║");
        System.out.println("║ Siguiente: " + padRight(state.getNextPlayer(), 23) + " ║");
        System.out.println("║ ¿Es mi turno?: " + padRight(client.isMyTurn(playerName) ? "SÍ" : "NO", 19) + " ║");
        System.out.println("║ Jugadores: " + padRight(String.valueOf(state.getPlayers().size()), 23) + " ║");
        System.out.println("╚════════════════════════════════════╝");

        if (!state.getPlayers().isEmpty()) {
            System.out.println("\nJugadores en la partida:");
            for (PlayerModel player : state.getPlayers()) {
                System.out.println("  • " + player.getName() + " (índice: " + player.getIndex() + ")");
            }
        }
    }

    private static String padRight(String s, int length) {
        if (s == null) s = "N/A";
        if (s.length() >= length) return s.substring(0, length);
        return String.format("%-" + length + "s", s);
    }
}
