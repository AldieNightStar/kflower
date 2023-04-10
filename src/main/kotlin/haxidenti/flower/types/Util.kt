package haxidenti.flower.types

fun getReturnType(code: List<Instruction>) =
    code.firstNotNullOfOrNull { it as? ReturnInstruction }?.let { it.type } ?: Primitives.VOID

fun  Instruction.required(type: VarType) {
    if (this.type != type) {
        throw TypedException(info, "Required type is ${type.name} but ${this.type.name} given for")
    }
}