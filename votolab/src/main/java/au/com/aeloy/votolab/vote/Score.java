package au.com.aeloy.votolab.vote;

public enum Score {
    UP(1),
    DOWN(-1);

    private final int score;

    Score(int score) {
        this.score = score;
    }
}
