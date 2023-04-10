package haxidenti.flower.parser

import haxidenti.flower.lexer.FlowerLexer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FlowerParserTest {
    @Test
    fun testCommandParse() {
        val parsed = FlowerParser().parse(FlowerLexer("file", "(a b 'kill \\'me')(d e b32)").lex()).let {
            it[0].let {
                it.name eq "a"
                it.args.size eq 2
                (it.args[0] as VariableGetCommand).varName eq "b"
                (it.args[1] as StaticStringCommand).dat eq "kill 'me"
            }
            it[1].let {
                it.name eq "d"
                it.args.size eq 2
                (it.args[0] as VariableGetCommand).varName eq "e"
                (it.args[1] as StaticByteCommand).dat eq 32.toByte()
            }
        }
    }

    @Test
    fun testCommandParseWithNested() {
        val parsed = FlowerParser().parse(FlowerLexer("file", "(cmd :: (pos 1 2) 3)").lex()).first().let {
            it.name eq "cmd"
            it.args.size eq 3
            (it.args[0] as OperatorCommand).dat eq "::"
            it.args[1].let {
                it.name eq "pos"
                it.args.size eq 2
                (it.args[0] as StaticIntCommand).dat eq 1
                (it.args[1] as StaticIntCommand).dat eq 2
            }
            (it.args[2] as StaticIntCommand).dat eq 3
        }
    }

    private infix fun <T> T.eq(ex: T) = assertEquals(ex, this)
}