package haxidenti.flower.types

import haxidenti.flower.lexer.FileInfo

open class Instruction(val info: FileInfo, val type: VarType, val scope: Scope)

open class VarType(
    val name: String,
    val fields: List<VarType>,
    val funcs: List<FuncDecl>,
) {
    override fun equals(other: Any?): Boolean = (other as? VarType)?.let { return it.name == name } ?: false
    override fun hashCode() = name.hashCode()
    val ref get() = TypeRef(this)
}

object Primitives {
    val INT = Primitive("int")
    val BOOL = Primitive("boolean")
    val BYTE = Primitive("byte")
    val FLOAT = Primitive("float")
    val STRING = Primitive("string")
    val VOID = Primitive("void")
}

class Primitive(name: String) : VarType(name, listOf(), listOf())
class TypeRef(val refType: VarType) : VarType("type[${refType.name}]", listOf(), listOf())

class Variable(val name: String, val type: VarType)

class TypeDecl(info: FileInfo, val declType: VarType, scope: Scope) : Instruction(info, declType.ref, scope)
class FuncCall(info: FileInfo, val func: FuncDecl, val args: List<Instruction>, scope: Scope) :
    Instruction(info, func.type, scope)

class FuncDecl(info: FileInfo, val name: String, val args: List<VarType>, val body: BlockInstruction, scope: Scope) :
    Instruction(info, body.type, scope)

class ReturnInstruction(info: FileInfo, val data: Instruction, scope: Scope) : Instruction(info, data.type, scope)

class BlockInstruction(info: FileInfo, val code: List<Instruction>, scope: Scope) :
    Instruction(info, getReturnType(code), scope)

open class StaticInstruction<T>(info: FileInfo, val data: T, type: VarType, scope: Scope) :
    Instruction(info, type, scope)

class StaticStringInstruction(info: FileInfo, data: String, scope: Scope) :
    StaticInstruction<String>(info, data, Primitives.STRING, scope)

class StaticIntInstruction(info: FileInfo, data: Int, scope: Scope) :
    StaticInstruction<Int>(info, data, Primitives.INT, scope)

class StaticFloatInstruction(info: FileInfo, data: Float, scope: Scope) :
    StaticInstruction<Float>(info, data, Primitives.FLOAT, scope)

class StaticByteInstruction(info: FileInfo, data: Byte, scope: Scope) :
    StaticInstruction<Byte>(info, data, Primitives.BYTE, scope)

class StaticBoolInstruction(info: FileInfo, data: Boolean, scope: Scope) :
    StaticInstruction<Boolean>(info, data, Primitives.BOOL, scope)

class VariableGetInstruction(info: FileInfo, val variable: Variable, scope: Scope) :
    Instruction(info, variable.type, scope)