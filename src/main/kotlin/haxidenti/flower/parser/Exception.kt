package haxidenti.flower.parser

import haxidenti.flower.lexer.FileInfo

class ParserException(val info: FileInfo, message: String) : RuntimeException("Parsing of '${info.file}' on Line: ${info.line} cause error: $message")