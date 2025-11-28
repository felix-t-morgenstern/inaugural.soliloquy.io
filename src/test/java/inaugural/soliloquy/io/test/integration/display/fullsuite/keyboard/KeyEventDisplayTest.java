package inaugural.soliloquy.io.test.integration.display.fullsuite.keyboard;

import inaugural.soliloquy.io.IOModule;
import inaugural.soliloquy.io.api.dto.AssetDefinitionsDTO;
import inaugural.soliloquy.io.test.integration.display.fullsuite.DisplayTest;
import inaugural.soliloquy.io.test.integration.display.fullsuite.DisplayTestMethods;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.factories.ComponentFactory;

import static inaugural.soliloquy.io.api.Constants.WHOLE_SCREEN;
import static inaugural.soliloquy.tools.collections.Collections.*;
import static inaugural.soliloquy.tools.random.Random.randomString;
import static java.util.UUID.randomUUID;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_B;
import static soliloquy.specs.common.entities.Consumer.consumer;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;
import static soliloquy.specs.io.input.keyboard.KeyBinding.keyBinding;

public class KeyEventDisplayTest extends DisplayTest {
    protected final static AssetDefinitionsDTO ASSET_DTOS = new AssetDefinitionsDTO(
            arrayOf(),
            arrayOf(),
            arrayOf(),
            arrayOf(),
            arrayOf(),
            arrayOf(),
            arrayOf(),
            arrayOf(),
            arrayOf()
    );

    public static void main(String[] args) {
        var displayTest = new DisplayTest();
        displayTest.runTest(
                "Key event display test",
                ASSET_DTOS,
                () -> DisplayTest.runThenClose("Key event", 4000),
                KeyEventDisplayTest::populateTopLevelComponent
        );
    }

    protected static void populateTopLevelComponent(IOModule ioModule,
                                                    Component topLevelComponent) {
        var componentFactory = ioModule.provide(ComponentFactory.class);

        topLevelComponent.add(componentFactory.make(
                randomUUID(),
                0,
                setOf(keyBinding(
                        arrayInts(GLFW_KEY_B),
                        consumer(randomString(), DisplayTestMethods::printKeyPressed),
                        consumer(randomString(), DisplayTestMethods::printKeyReleased)
                )),
                true,
                0,
                staticProvider(floatBoxOf(0f,0f)),
                staticProvider(WHOLE_SCREEN),
                null,
                null,
                topLevelComponent,
                mapOf()
        ));
    }
}
