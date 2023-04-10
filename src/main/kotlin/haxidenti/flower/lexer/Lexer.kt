package haxidenti.flower.lexer

private object Regexes {
    val SYMBOLS = Regex("^[^\\w\\s]+")
    val NUMBER_INT = Regex("^-?\\d+")
    val NUMBER_FLOAT = Regex("^-?\\d+\\.\\d+")
    val WORD = Regex("^[a-zA-Z0-9_\$]+")
}

fun CharSequence.subSequenceToEnd(pos: Int): CharSequence {
    return subSequence(pos, this.length)
}

class FlowerLexer(val fileName: String, val text: String) {
    var line: Int = 1
    var pos = 0

    val info: FileInfo get() = FileInfo(fileName, line)
    val isEnded: Boolean get() = pos >= text.length

    fun reset() {
        line = 1
        pos = 0
    }

    fun lex(): List<Token> {
        val list = mutableListOf<Token>()
        while (pos < text.length) {
            val curText = text.subSequenceToEnd(pos)
            // Next lines
            if (curText.startsWith("\n")) {
                line += 1
                pos += 1
                continue
            }
            // Parsing
            val tok = lexOne(curText) ?: throw LexerException(info, "Unknown token: ${curText.subSequence(0, 100)}")
            pos += tok.size
            if (tok !is IgnoredToken) list.add(tok)
        }
        return list
    }

    fun lexOne(text: CharSequence): Token? {
        if (text.startsWith(" ") || text.startsWith("\t") || text.startsWith("\r")) return IgnoredToken(info, 1)
        if (text.startsWith("(") || text.startsWith(")"))
            return SymbolToken(info, text[0].toString(), 1)
        lexString(text)?.let { return it }
        lexNumberFloat(text)?.let { return it }
        lexNumberInt(text)?.let { return it }
        lexWord(text)?.let { return it }
        lexComments(text)?.let { return it }
        lexSymbols(text)?.let { return it }

        return null
    }

    fun lexWord(text: CharSequence): WordToken? = Regexes.WORD.find(text)?.let {
        return WordToken(info, it.value, it.value.length)
    }

    fun lexSymbols(text: CharSequence): SymbolToken? = Regexes.SYMBOLS.find(text)?.let {
        return SymbolToken(info, it.value, it.value.length)
    }

    fun lexNumberInt(text: CharSequence): NumberToken? = Regexes.NUMBER_INT.find(text)?.let {
        return NumberToken(info, it.value, it.value.length)
    }

    fun lexNumberFloat(text: CharSequence): NumberToken? = Regexes.NUMBER_FLOAT.find(text)?.let {
        return NumberToken(info, it.value, it.value.length)
    }

    fun lexComments(text: CharSequence): IgnoredToken? {
        if (!text.startsWith("#")) return null
        var count = 1
        for (c in text.subSequenceToEnd(1)) {
            if (c == '\n') break
            count += 1
        }
        return IgnoredToken(info, count)
    }

    fun lexString(text: CharSequence): StringToken? {
        val qt = text[0]
        if (qt !in "'\"") return null
        val sb = StringBuilder()
        var size = 1
        var esc = false
        var ended = false
        for (c in text.subSequenceToEnd(1)) {
            if (esc) {
                when (c) {
                    'n' -> sb.append('\n')
                    't' -> sb.append('\t')
                    '0' -> sb.append('\u0000')
                    'r' -> sb.append('\r')
                    else -> sb.append(c)
                }
                size++
                esc = false
                continue
            }
            if (c == '\\') {
                esc = true
                size++
                continue
            }
            if (c == qt) {
                size++
                ended = true
                break
            }
            if (c == '\n') {
                throw LexerException(info, "Next line symbols not allowed until string is ended")
            }
            sb.append(c)
            size++
        }
        if (ended) {
            return StringToken(info, sb.toString(), size)
        }
        return null
    }

}