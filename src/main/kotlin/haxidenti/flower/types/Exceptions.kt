package haxidenti.flower.types

import haxidenti.flower.lexer.FileInfo

class TypedException(val info: FileInfo, message: String) : RuntimeException("${info.file}:${info.line} :: $message")