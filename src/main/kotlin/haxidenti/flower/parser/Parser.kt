package haxidenti.flower.parser

import haxidenti.flower.lexer.*

class Parser {

    fun parse(tokens: List<Token>): List<Command> {
        var pos = 0
        val list = mutableListOf<Command>()
        while (pos < tokens.size) {
            var currentTokens = tokens.subList(pos, tokens.size)
            val commandCount = parseCommand(currentTokens)
            if (commandCount != null) {
                pos += commandCount.second
                list.add(commandCount.first)
                continue
            }
            throw ParserException(currentTokens.first().info, "Root code should be enclosed in brackets")
        }
        return list
    }

    fun parseCommand(tokens: List<Token>): Pair<Command, Int>? {
        val first = tokens.firstOrNull() ?: return null
        if (first.isBracket(true)) return null
        val (args, count) = parseArgs(tokens.subList(1, tokens.size))
        if (args.isEmpty()) return null
        val lastToken = tokens.getOrNull(count + 1) ?: return null
        if (!lastToken.isBracket(false)) return null
        return Pair(
            Command(
                first.info,
                tokens[1].asWord?.value ?: throw ParserException(
                    first.info,
                    "Name should be present as word token"
                ),
                args.subList(1, args.size)
            ), count + 1
        )
    }

    /**
     * Will parse args and return list of commands and how much it took
     */
    fun parseArgs(tokens: List<Token>): Pair<List<Command>, Int> {
        var pos = 0
        val list = mutableListOf<Command>()
        while (pos < tokens.size) {
            val curList = tokens.subList(pos, list.size)
            val commandAntCount = curList.toCommand()
            if (commandAntCount != null) {
                val (command, count) = commandAntCount
                pos += count
                list.add(command)
                continue
            }
            throw ParserException(curList.first().info, "Can't parse unknown token")
        }
        return Pair(list, pos)
    }

    private fun Token.isBracket(open: Boolean): Boolean {
        val sym = this as? SymbolToken ?: return false
        return if (open) {
            sym.value == "("
        } else {
            sym.value == ")"
        }
    }

    private fun List<Token>.toCommand(): Pair<Command, Int>? {
        val first = this.getOrNull(0) ?: return null
        (first as? StringToken)
            ?.let { return StaticStringCommand(first.info, it.value).singleTokenPair }
        (first as? NumberToken)?.value?.toFloatOrNull()
            ?.let { return StaticFloatCommand(first.info, it).singleTokenPair }
        (first as? NumberToken)?.value?.toIntOrNull()
            ?.let { return StaticIntCommand(first.info, it).singleTokenPair }
        (first as? WordToken)?.value?.either("true", "false")
            ?.let { return StaticBoolCommand(first.info, first.value == "true").singleTokenPair }
        (first as? WordToken)?.value?.prefixed("b")?.toIntOrNull()
            ?.let { return StaticByteCommand(first.info, it.toByte()).singleTokenPair }
        (first as? WordToken)?.value?.equalsOrNone("null")
            ?.let { return StaticNullCommand(first.info).singleTokenPair }
        (first as? WordToken)
            ?.let { return VariableGetCommand(first.info, it.value).singleTokenPair }
        // Here we are parsing command
        (first as? SymbolToken)?.openedBracket
            ?.let { return parseCommand(this) }
        (first as? SymbolToken)
            ?.let { return OperatorCommand(first.info, it.value).singleTokenPair }
        return null
    }

    private fun String.either(vararg vals: String) = if (vals.contains(this)) this else null
    private fun String.prefixed(pref: String) = if (this.startsWith(pref)) this.substring(pref.length) else null
    private fun String.equalsOrNone(v: String) = if (this == v) this else null

    private val Token.asWord get() = if (this is WordToken) this else null
    private val SymbolToken.openedBracket get() = if (value == "(") this else null
}