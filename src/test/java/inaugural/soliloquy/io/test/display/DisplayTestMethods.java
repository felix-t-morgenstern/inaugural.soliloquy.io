package inaugural.soliloquy.io.test.display;

import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.ui.EventInputs;

import java.util.function.BiConsumer;

import static inaugural.soliloquy.io.test.display.DisplayTest.*;

public class DisplayTestMethods {
    static BiConsumer<String, ProviderAtTime<Float>> PlaySound;

    public final static String DisplayTestMethods_playMousePressSound = "DisplayTestMethods_playMousePressSound";

    public static void DisplayTestMethods_playMousePressSound(@SuppressWarnings("unused") EventInputs e) {
        PlaySound.accept(PRESS_SOUND_ID, staticProvider(1f));
    }

    public final static String DisplayTestMethods_playMouseReleaseSound = "DisplayTestMethods_playMouseReleaseSound";

    public static void DisplayTestMethods_playMouseReleaseSound(@SuppressWarnings("unused") EventInputs e) {
        PlaySound.accept(RELEASE_SOUND_ID, staticProvider(1f));
    }

    public final static String DisplayTestMethods_printWheelOffset = "DisplayTestMethods_printWheelOffset";

    public static void printWheelOffset(EventInputs e) {
        System.out.println("Scroll wheel offset = " + e.scrollWheelOffset);
    }

    public static void printKeyPressed(EventInputs e) {
        System.out.println("Key codepoint [" + e.keyCodepoint + "] pressed");
    }

    public static void printKeyReleased(EventInputs e) {
        System.out.println("Key codepoint [" + e.keyCodepoint + "] released");
    }
}
