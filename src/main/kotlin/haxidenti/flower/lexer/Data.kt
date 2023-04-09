package haxidenti.flower.lexer

data class FileInfo(val file: String, val line: Int)

open class Token(val info: FileInfo, val size: Int)

class StringToken(info: FileInfo, val value: String, size: Int) : Token(info, size)
class WordToken(info: FileInfo, val value: String, size: Int) : Token(info, size)
class IgnoredToken(info: FileInfo, size: Int) : Token(info, size)
class NumberToken(info: FileInfo, val value: String, size: Int) : Token(info, size)
class SymbolToken(info: FileInfo, val value: String, size: Int) : Token(info, size)