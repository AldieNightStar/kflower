package haxidenti.flower.types

import haxidenti.flower.lexer.FileInfo

open class Instruction(val info: FileInfo, val type: VarType)

open class VarType(
    val name: String,
    val fields: List<VarType>,
    val funcs: List<FuncDecl>,
) {
    override fun equals(other: Any?): Boolean = (other as? VarType)?.let { return it.name == name } ?: false
    override fun hashCode() = name.hashCode()
    val ref get() = TypeRef(this)
}