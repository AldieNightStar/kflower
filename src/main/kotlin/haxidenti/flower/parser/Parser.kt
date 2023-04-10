package haxidenti.flower.parser

import haxidenti.flower.lexer.*

class FlowerParser {

    fun parseOne(tokens: List<Token>): Command? {
        val first = tokens.firstOrNull() ?: return null
        parseSingleToken(first)?.let { return it }
        return null
    }

    fun parseCommand(tokens: List<Token>): Command? {
        if (tokens.size < 3) return null
        if (!tokens.first().isOpenBracket) return null
        val commandName = tokens[1].named
        var pos = 2
        val args = mutableListOf<Command>()
        while (pos < tokens.size) {
            val slice = tokens.subList(pos, tokens.size)
            val arg = parseOne(slice)
            if (arg != null) {
                pos += arg.size
                args.add(arg)
                continue
            } else {
                break
            }
        }
        if (args.isEmpty())  throw ParserException(tokens.first().info, "Empty command found")
        return Command(tokens.first().info, commandName, args, pos)
    }

    fun parseSingleToken(tok: Token): Command? {
        parseInt(tok)?.let { return it }
        parseFloat(tok)?.let { return it }
        parseString(tok)?.let { return it }
        parseBool(tok)?.let { return it }
        parseNull(tok)?.let { return it }
        parseByte(tok)?.let { return it }
        parseVariable(tok)?.let { return it }
        parseOperator(tok)?.let { return it }

        return null
    }

    fun parseInt(token: Token): StaticIntCommand? {
        val n = token as? NumberToken ?: return null
        return n.value.toIntOrNull()?.let { StaticIntCommand(token.info, it) }
    }

    fun parseFloat(token: Token): StaticFloatCommand? {
        val n = token as? NumberToken ?: return null
        return n.value.toFloatOrNull()?.let { StaticFloatCommand(token.info, it) }
    }

    fun parseString(token: Token): StaticStringCommand? {
        val s = token as? StringToken ?: return null
        return StaticStringCommand(s.info, s.value)
    }

    fun parseBool(token: Token): StaticBoolCommand? {
        val b = token as? WordToken ?: return null
        if (b.value == "true") return StaticBoolCommand(b.info, true)
        else if (b.value == "false") return StaticBoolCommand(b.info, false)
        return null
    }

    fun parseByte(token: Token): StaticByteCommand? {
        val b = token as? WordToken ?: return null
        if (!b.value.startsWith("b")) return null
        val bvalue = b.value.substring(1).toByteOrNull() ?: return null
        return StaticByteCommand(b.info, bvalue)
    }

    fun parseVariable(token: Token): VariableGetCommand? {
        val w = token as? WordToken ?: return null
        return VariableGetCommand(w.info, w.value)
    }

    fun parseNull(token: Token): StaticNullCommand? {
        val w = token as? WordToken ?: return null
        return if (w.value == "null") StaticNullCommand(w.info) else null
    }

    fun parseOperator(token: Token): OperatorCommand? {
        val s = token as? SymbolToken ?: return null
        if (s.value == "(" || s.value == ")") return null
        return OperatorCommand(s.info, s.value)
    }

    private val Token.named get() = if (this is WordToken) this.value else throw ParserException(this.info, "Token word with name required")
    private val Token.isOpenBracket get() = this is SymbolToken && this.value == "("
    private val Token.isClosedBracket get() = this is SymbolToken && this.value == ")"
}