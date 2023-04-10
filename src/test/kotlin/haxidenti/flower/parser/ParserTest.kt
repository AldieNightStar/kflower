package haxidenti.flower.parser

import haxidenti.flower.lexer.FlowerLexer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ParserTest {

    @Test
    fun testDifferentArguments() {
        parsed<StaticIntCommand>("123 999").dat.eq(123)
        parsed<StaticFloatCommand>("133.44 999").dat.eq(133.44F)
        parsed<StaticStringCommand>("'This is \\\\a string' FFF").dat.eq("This is \\a string")
        parsed<StaticBoolCommand>("true xxx").dat.eq(true)
        parsed<StaticBoolCommand>("false xxx").dat.eq(false)
        parsed<StaticNullCommand>("null xxx").dat.eq(Unit)
        parsed<StaticByteCommand>("b32 999").dat.eq(32.toByte())
        parsed<OperatorCommand>("-->").dat.eq("-->")
        parsed<OperatorCommand>(">>").dat.eq(">>")
        parsed<OperatorCommand>("<<").dat.eq("<<")
    }

    @Test
    fun testParsingCommands() {
        parsed<Command>("(axa 1 2)").let {
            it.name.eq("axa")
            it.args.size.eq(2)
            (it.args.get(0) as StaticIntCommand).dat.eq(1)
            (it.args.get(1) as StaticIntCommand).dat.eq(2)
        }
    }

    val Pair<List<Command>, Int>.command get() = this.first.first()

    fun <T : Command> parsed(str: String) = FlowerParser()
        .parseArgs(FlowerLexer("flower", str).lex()).command as T

    fun <T> Any?.eq(expected: T) = assertEquals(expected, this)
}