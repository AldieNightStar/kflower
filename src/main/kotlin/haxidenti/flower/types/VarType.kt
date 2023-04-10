package haxidenti.flower.types

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