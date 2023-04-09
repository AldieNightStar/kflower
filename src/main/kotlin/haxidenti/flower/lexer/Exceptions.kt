package haxidenti.flower.lexer

class LexerException(fileInfo: FileInfo, message: String) :
    RuntimeException("${fileInfo.file} line:${fileInfo.line} :: $message")