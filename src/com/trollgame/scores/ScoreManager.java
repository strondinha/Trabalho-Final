package com.trollgame.scores;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ScoreManager {
    private static final String FILE_NAME = "ranking.txt";

    public static class Score {
        public final String name;
        public final double time;
        public final String skin;

        public Score(String name, double time, String skin) {
            this.name = name;
            this.time = time;
            this.skin = skin;
        }
    }

    private static Path resolveFile() {
        Path inGameFolder = Path.of("game", FILE_NAME);
        if (Files.exists(inGameFolder)) {
            return inGameFolder;
        }
        return Path.of(FILE_NAME);
    }

    public static void saveScore(String name, double time, String skin) {
        List<Score> scores = getScores();
        scores.add(new Score(name, time, skin));
        scores.sort(Comparator.comparingDouble(s -> s.time));

        List<String> lines = new ArrayList<>();
        for (Score s : scores) {
            lines.add(s.name + "|" + s.time + "|" + s.skin);
        }

        try {
            Files.write(resolveFile(), lines, StandardCharsets.UTF_8);
        } catch (IOException ignored) {
        }
    }

    public static List<Score> getScores() {
        Path file = resolveFile();
        List<Score> scores = new ArrayList<>();
        if (!Files.exists(file)) return scores;

        try {
            for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
                String[] parts = line.split("\\|");
                if (parts.length < 3) continue;
                String name = parts[0].trim();
                double time = Double.parseDouble(parts[1]);
                String skin = parts[2].trim();
                scores.add(new Score(name, time, skin));
            }
        } catch (Exception ignored) {
        }

        scores.sort(Comparator.comparingDouble(s -> s.time));
        return scores;
    }
}
