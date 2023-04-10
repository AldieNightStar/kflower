package haxidenti.flower.types

import haxidenti.flower.lexer.FileInfo

class TypeDecl(info: FileInfo, type: VarType) : Instruction(info, type.ref)
class FuncCall(info: FileInfo, val func: FuncDecl, val args: List<Instruction>) : Instruction(info, func.type)
class FuncDecl(info: FileInfo, val name: String, val args: List<VarType>, val body: BlockInstruction) :
    Instruction(info, body.type)

class ReturnInstruction(info: FileInfo, val data: Instruction) : Instruction(info, data.type)

class BlockInstruction(info: FileInfo, val code: List<Instruction>) : Instruction(info, getReturnType(code))

open class StaticInstruction<T>(info: FileInfo, val data: T, type: VarType) : Instruction(info, type)
class StaticStringInstruction(info: FileInfo, data: String) : StaticInstruction<String>(info, data, Primitives.STRING)
class StaticIntInstruction(info: FileInfo, data: Int) : StaticInstruction<Int>(info, data, Primitives.INT)
class StaticFloatInstruction(info: FileInfo, data: Float) : StaticInstruction<Float>(info, data, Primitives.FLOAT)
class StaticByteInstruction(info: FileInfo, data: Byte) : StaticInstruction<Byte>(info, data, Primitives.BYTE)
class StaticBoolInstruction(info: FileInfo, data: Boolean) : StaticInstruction<Boolean>(info, data, Primitives.BOOL)

class VariableGetInstruction(info: FileInfo, val variable: Variable) : Instruction(info, variable.type)