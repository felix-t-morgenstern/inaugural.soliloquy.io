package inaugural.soliloquy.io.test.display.fullsuite;

import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.ui.EventInputs;

import java.util.function.BiConsumer;

import static inaugural.soliloquy.io.test.display.fullsuite.DisplayTest.*;

public class DisplayTestMethods {
    static BiConsumer<String, ProviderAtTime<Float>> PlaySound;

    public static void playMousePressSound(@SuppressWarnings("unused") EventInputs e) {
        PlaySound.accept(PRESS_SOUND_ID, staticProvider(1f));
    }

    public static void playMouseReleaseSound(@SuppressWarnings("unused") EventInputs e) {
        PlaySound.accept(RELEASE_SOUND_ID, staticProvider(1f));
    }

    public static void printKeyPressed(EventInputs e) {
        System.out.println("Key codepoint [" + e.keyCodepoint + "] pressed");
    }

    public static void printKeyReleased(EventInputs e) {
        System.out.println("Key codepoint [" + e.keyCodepoint + "] released");
    }
}
