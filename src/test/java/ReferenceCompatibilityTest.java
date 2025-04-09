import io.quarkus.qute.Qute;
import net.snemeis.configurations.EngineProducer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = { EngineProducer.class })
public class ReferenceCompatibilityTest {

    @Test
    void _1_the_simplest_example() {
        // https://quarkus.io/guides/qute-reference#the_simplest_example
        var output = Qute.fmt("Hello {}!", "Lucy");
        assertEquals("Hello Lucy!", output);

        var defaultOutput = Qute.fmt("Hello {name} {surname ?: 'Default'}!", Map.of("name", "Andy"));
        assertEquals("Hello Andy Default!", defaultOutput);

        var simpleOutput = Qute.fmt("Hello {name}!", Map.of("name", "Lucy"));
        assertEquals("Hello Lucy!", simpleOutput);

        var escapedOutput = Qute
                .fmt("<html>{header}</html>")
                .contentType("text/html")
                .data("header", "<h1>Header</h1>")
                .render();
        assertEquals("<html>&lt;h1&gt;Header&lt;/h1&gt;</html>", escapedOutput);

        var conditionalOutput = Qute.fmt("I am {#if ok}happy{#else}sad{/if}!", Map.of("ok", true));
        assertEquals("I am happy!", conditionalOutput);
    }

    @Test
    void _2_Hello_World_example() {
        var out = Qute.engine().getTemplate("ref_test/2_hello").data("name", "jim").render();
        assertEquals("""
            <html>
              <p>Hello jim!
            </html>
            """.trim(),
            out
        );
    }

    @Test
    void _3_1_Basic_Building_Blocks() {
        var commented = Qute.fmt("{! This is a comment !}").render();
        assertEquals("", commented);

        var unparsed = Qute.fmt("{| <script>if(true){alert('Qute is cute!')};</script> |}").render();
        assertEquals(" <script>if(true){alert('Qute is cute!')};</script> ", unparsed);
    }

    @Test
    void _3_2_Identifiers_and_Tags() {
        // A valid identifier is a sequence of non-whitespace characters.
        // A tag starts and ends with a curly bracket, e.g. {foo}.
        var out = Qute.engine()
                .getTemplate("ref_test/3_2_tags")
                .data("_foo", Map.of("bar", "baz"))
                .render();
        assertEquals("""
                <html>
                  <body>
                    baz
                    {  foo}
                    {{foo}}
                    {"foo":true}
                    {foo}
                  </body>
                </html>""".trim(),
                out
        );
    }

    @Test
    void _3_3_Remove_standalone_lines() {
        var out = Qute.engine()
                .getTemplate("ref_test/3_3_standalone")
                .data("items", List.of(
                        new _3_Standalone("aaa", true, 3.14),
                        new _3_Standalone("bbb", false, 42.1),
                        new _3_Standalone("ccc", true, 42.0)
                ))
                .render();
        assertEquals("""
                <ul>
                  <li>aaa 3.14</li>

                  <li>bbb </li>

                  <li>ccc 42.0</li>

                </ul>""".trim(),
                out
        );

        var engine = Qute.engine().newBuilder().removeStandaloneLines(false).build();
        var standaloneOut = engine.getTemplate("ref_test/3_3_standalone")
                .data("items", List.of(
                        new _3_Standalone("aaa", true, 3.14)
                ))
                .render();
        assertEquals("""
                <ul>

                  <li>aaa 3.14</li>


                </ul>""".trim(),
                standaloneOut
        );
    }

    @Test
    void _3_4_Expressions() {
        var out = Qute.fmt("{name} {item.name} {item['name']}")
                .data("name", "World")
                .data("item", Map.of("name", "World2"))
                .render();
        assertEquals("World World2 World2", out);
    }

    @Test
    void _3_4_3_Current_Context() {
        var template = """
                {#let myParent=order.item.parent myPrice=order.price}
                <h1>{myParent.name}</h1>
                <p>Price: {myPrice}</p>
                {/let}""".trim();
        var data = Map.of(
                "item", Map.of("parent", Map.of("name", "me")),
                "price", 42
        );
        var out = Qute.fmt(template).data("order", data).render();
        assertEquals("""
            <h1>me</h1>
            <p>Price: 42</p>""".trim(),
            out.trim()
        );
    }

    @Test
    void _3_4_4_build_in_resolvers() {
    }

    private record _3_Standalone (String name, boolean active, Double price) {}
}
