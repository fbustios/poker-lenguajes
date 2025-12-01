package poker.handranking;

import poker.items.Player;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class CProgramRankingSystem implements RankingSystem {
    private final File executable;
    public CProgramRankingSystem(final File executable) {
        this.executable = executable;
    }

    @Override
    public List<Player> rank(List<Player> players) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(executable.getAbsolutePath());
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);  // Imprime lo que el programa C imprime
            }
            int exitCode = process.waitFor();
            return List.of();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
