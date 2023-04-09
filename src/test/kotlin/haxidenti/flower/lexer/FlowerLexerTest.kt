package haxidenti.flower.lexer

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FlowerLexerTest {

    @Test
    fun testString() = FlowerLexer("file", "").lexString("'An \\'string'")
        .let { it!!.value }
        .let { assertEquals("An 'string", it) }

    @Test
    fun testNumberInt() = FlowerLexer("file", "").lexNumberInt("123.1")
        .let { it!!.value }
        .let { assertEquals("123", it) }

    @Test
    fun testNumberFloat() = FlowerLexer("file", "").lexNumberFloat("123.1")
        .let { it!!.value }
        .let { assertEquals("123.1", it) }

    @Test
    fun testComment() = FlowerLexer("file", "").lexComments("# dsadsadsa\n")
        .let { it!!.size }
        .let { assertEquals(11, it) }

    @Test
    fun testLexOne() = FlowerLexer("file", "").lexOne("???\n")!!
        .let { it as SymbolToken; it.value }
        .let { assertEquals("???", it) }

    @Test
    fun testLex() = FlowerLexer("file", "abc 123 \n'xyz'").lex()!!
        .apply { assertEquals("abc", (get(0) as WordToken).value) }
        .apply { assertEquals("123", (get(1) as NumberToken).value) }
        .apply { assertEquals("xyz", (get(3) as StringToken).value) }

}
