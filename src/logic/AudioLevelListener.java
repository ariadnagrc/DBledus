package logic;

@FunctionalInterface
public interface AudioLevelListener {
    void onAudioLevel(double nivelDB);
}