package inaugural.soliloquy.io.test.integration.display.fullsuite.textline;

import inaugural.soliloquy.io.IOModule;
import inaugural.soliloquy.io.api.dto.AssetDefinitionsDTO;
import inaugural.soliloquy.io.test.integration.display.fullsuite.DisplayTest;
import soliloquy.specs.io.graphics.Graphics;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.TextJustification;
import soliloquy.specs.io.graphics.renderables.factories.TextLineRenderableFactory;

import java.awt.*;
import java.util.List;

import static inaugural.soliloquy.tools.collections.Collections.*;
import static java.util.UUID.randomUUID;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

public class TextLineSimpleDisplayTest extends DisplayTest {
    protected final static AssetDefinitionsDTO ASSET_DTOS = new AssetDefinitionsDTO(
            arrayOf(),
            arrayOf(
                    CINZEL_DEF
            ),
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
                "Simple text line display test",
                ASSET_DTOS,
                () -> DisplayTest.runThenClose("Simple text line", 4000),
                TextLineSimpleDisplayTest::populateTopLevelComponent
        );
    }

    protected static void populateTopLevelComponent(IOModule ioModule,
                                                    Component topLevelComponent) {
        populateTopLevelComponent(ioModule, topLevelComponent, listOf(), listOf());
    }

    protected static void populateTopLevelComponent(IOModule ioModule,
                                                    Component topLevelComponent,
                                                    List<Integer> italicIndices,
                                                    List<Integer> boldIndices) {
        var graphics = ioModule.provide(Graphics.class);
        var font = graphics.getFont(CINZEL_ID);

        var lineHeight = staticProvider(0.1f);
        var glyphPadding = 0.05f;

        var factory = ioModule.provide(TextLineRenderableFactory.class);
        var strings = arrayOf(
                "ABCDEFGHIJKLM",
                "NOPQRSTUVWXYZ",
                "abcdefghijklm",
                "nopqrstuvwxyz",
                "0123456789-=",
                "!@#$%^&*()_+",
                "[]\\;',./{}|:\"<>?"
        );
        var row = 0;
        for (var string : strings) {
            factory.make(
                    font,
                    staticProvider(string),
                    staticProvider(vertexOf(0f, (0f + (0.1f * row++)))),
                    lineHeight,
                    TextJustification.LEFT,
                    glyphPadding,
                    mapOf(0, staticProvider(Color.RED)),
                    italicIndices,
                    boldIndices,
                    staticProvider(0.00075f),
                    staticProvider(Color.WHITE),
                    nullProvider(),
                    nullProvider(),
                    nullProvider(),
                    0,
                    randomUUID(),
                    topLevelComponent
            );
        }
    }
}
