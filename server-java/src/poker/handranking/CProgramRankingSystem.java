package poker.handranking;

import network.control.ConnectionPlayerMapping;
import poker.items.Card;
import poker.items.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CProgramRankingSystem implements RankingSystem {
    private final ConnectionPlayerMapping playerMapping;
    public CProgramRankingSystem(final ConnectionPlayerMapping playerMapping) {
        this.playerMapping = playerMapping;
    }

    @Override
    public List<Player> rank(List<Player> players, char gameMode) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("C:/Users/faken/Desktop/code/java/lenguajes/proyecto/pokerlenguajes/server-java/src/poker/handranking/main.exe");
            Process process = processBuilder.start();

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

            writer.write(players.size() + "\n");
            writer.write("7\n");
            writer.write(gameMode + "\n");
            for (int i = 0; i < players.size(); i++) {
                writer.write(players.get(i).getName() + "\n");
                StringBuilder sb = new StringBuilder();
                List<Card> cards = players.get(i).getCards();
                for (int j = 0; j < cards.size(); j++) {
                    if (j < (cards.size() - 1)) {
                        sb.append(cards.get(j).toString() + " ");
                    } else {
                        sb.append(cards.get(j).toString());
                    }

                }
                writer.write(sb + "\n");
            }
            System.out.println("udhfhnwiej");
            writer.flush();
            writer.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            List<Player> roundWinners = new ArrayList<>();
            String line;

            while ((line = reader.readLine()) != null) {
                String[] l = line.split(" ");
                if (l.length == 6) {
                    Optional<Player> player = playerMapping.getPlayerFromName(l[5]);
                    if(player.isEmpty()) throw new IllegalStateException("NO EXISTO");
                    roundWinners.add(player.get());
                    System.out.println(playerMapping.getPlayerFromName(l[5]));
                }

            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("se cae el C program");
            }
            return roundWinners;

        } catch (Exception e) {
            System.out.println("daerawd");
            throw new RuntimeException(e);
        }
    }
}

