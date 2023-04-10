package haxidenti.flower.parser

import haxidenti.flower.lexer.FlowerLexer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FlowerParserTest {
    @Test
    fun testSimpleCommand() {
        val parsed = FlowerParser().parseCommand(FlowerLexer("file", "(a b c)").lex())!!
        parsed.args.size eq 2
        (parsed.args[0] as VariableGetCommand).varName eq "b"
        (parsed.args[1] as VariableGetCommand).varName eq "c"
    }

    private infix fun <T> T.eq(ex: T) = assertEquals(ex, this)
}