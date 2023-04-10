package haxidenti.flower.compiler

import haxidenti.flower.lexer.FileInfo

class CompilerException(val info: FileInfo, message: String) : RuntimeException("Compilation error for file: ${info.file} at lin ${info.line}. Message: $message")