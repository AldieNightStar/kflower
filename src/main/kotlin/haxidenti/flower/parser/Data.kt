package haxidenti.flower.parser

import haxidenti.flower.lexer.FileInfo

open class Command(val info: FileInfo, val name: String, val args: List<Command>)

class VariableGetCommand(info: FileInfo, val varName: String) :
    Command(info, "get", listOf(StaticStringCommand(info, varName)))

open class StaticCommand<T>(info: FileInfo, val dat: T) : Command(info, "data", listOf())

class StaticIntCommand(info: FileInfo, dat: Int) : StaticCommand<Int>(info, dat)
class StaticFloatCommand(info: FileInfo, dat: Float) : StaticCommand<Float>(info, dat)
class StaticStringCommand(info: FileInfo, dat: String) : StaticCommand<String>(info, dat)
class StaticBoolCommand(info: FileInfo, dat: Boolean) : StaticCommand<Boolean>(info, dat)
class StaticByteCommand(info: FileInfo, dat: Byte) : StaticCommand<Byte>(info, dat)
class OperatorCommand(info: FileInfo, dat: String) : StaticCommand<String>(info, dat)
class StaticNullCommand(info: FileInfo) : StaticCommand<Unit>(info, Unit)

val Command.singleTokenPair get() = Pair(this, 1)