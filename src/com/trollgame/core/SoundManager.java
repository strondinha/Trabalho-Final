package com.trollgame.core;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.*;

public final class SoundManager {
    private static final Map<String, Clip> CLIPS = new HashMap<>();
    private static Clip musicClip;

    private SoundManager() {}

    public static void playSfx(String resourcePath) {
        Clip clip = getClip(resourcePath);
        if (clip == null) return;
        clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }

    public static void loopMusic(String resourcePath) {
        stopMusic();
        musicClip = getClip(resourcePath);
        if (musicClip == null) return;
        musicClip.stop();
        musicClip.setFramePosition(0);
        musicClip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public static void loopMusicWithFallback(String primary, String fallback) {
        stopMusic();
        musicClip = getClip(primary);
        if (musicClip == null && fallback != null) {
            musicClip = getClip(fallback);
        }
        if (musicClip == null) return;
        musicClip.stop();
        musicClip.setFramePosition(0);
        musicClip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public static void stopMusic() {
        if (musicClip != null) {
            musicClip.stop();
            musicClip = null;
        }
    }

    public static void stopAll() {
        stopMusic();
        for (Clip clip : CLIPS.values()) {
            if (clip != null) {
                clip.stop();
            }
        }
    }

    private static Clip getClip(String resourcePath) {
        if (CLIPS.containsKey(resourcePath)) return CLIPS.get(resourcePath);

        try {
            AudioInputStream ais = openAudioStream(resourcePath);
            if (ais == null) {
                CLIPS.put(resourcePath, null);
                return null;
            }
            AudioFormat baseFormat = ais.getFormat();
            AudioFormat decodedFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                baseFormat.getSampleRate(),
                16,
                baseFormat.getChannels(),
                baseFormat.getChannels() * 2,
                baseFormat.getSampleRate(),
                false
            );
            AudioInputStream decodedAis = AudioSystem.getAudioInputStream(decodedFormat, ais);
            Clip clip = AudioSystem.getClip();
            clip.open(decodedAis);
            CLIPS.put(resourcePath, clip);
            return clip;
        } catch (Exception e) {
            CLIPS.put(resourcePath, null);
            return null;
        }
    }

    private static AudioInputStream openAudioStream(String resourcePath) {
        try {
            InputStream is = SoundManager.class.getResourceAsStream(resourcePath);
            if (is != null) {
                return AudioSystem.getAudioInputStream(new BufferedInputStream(is));
            }
        } catch (Exception ignored) {
        }

        try {
            String relative = resourcePath.startsWith("/") ? resourcePath.substring(1) : resourcePath;
            java.io.File f1 = new java.io.File("game/src/" + relative);
            if (f1.exists()) {
                return AudioSystem.getAudioInputStream(f1);
            }
            java.io.File f2 = new java.io.File("src/" + relative);
            if (f2.exists()) {
                return AudioSystem.getAudioInputStream(f2);
            }
        } catch (Exception ignored) {
        }

        return null;
    }
}
