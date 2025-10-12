package inaugural.soliloquy.io.test.integration.display.fullsuite.textline;

import inaugural.soliloquy.io.IOModule;
import inaugural.soliloquy.io.test.integration.display.fullsuite.DisplayTest;
import soliloquy.specs.io.graphics.renderables.Component;

import static inaugural.soliloquy.tools.collections.Collections.listOf;

public class TextLineBoldItalicDisplayTest extends TextLineSimpleDisplayTest {
    public static void main(String[] args) {
        var displayTest = new DisplayTest();
        displayTest.runTest(
                "Bold italic text line display test",
                ASSET_DTOS,
                () -> DisplayTest.runThenClose("Bold italic text line", 4000),
                TextLineBoldItalicDisplayTest::populateTopLevelComponent
        );
    }

    protected static void populateTopLevelComponent(IOModule ioModule,
                                                    Component topLevelComponent) {
        populateTopLevelComponent(
                ioModule,
                topLevelComponent,
                listOf(0),
                listOf(0)
        );
    }
}
