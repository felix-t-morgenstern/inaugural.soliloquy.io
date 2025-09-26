package inaugural.soliloquy.io.test.integration.display.fullsuite;

import soliloquy.specs.ui.EventInputs;

import java.util.function.Consumer;

public class DisplayTestMethods {
    static Consumer<String> PlaySound;

    public static void playMousePressSound(EventInputs e) {
        PlaySound.accept("pressSoundId");
    }

    public static void playMouseReleaseSound(EventInputs e) {
        PlaySound.accept("releaseSoundId");
    }
}
