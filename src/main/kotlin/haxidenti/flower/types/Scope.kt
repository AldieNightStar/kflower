package haxidenti.flower.types

class Scope(val parent: Scope?) {
    var variables: List<Variable> = listOf()
    var funcs: List<FuncDecl> = listOf()
    var types: List<VarType> = listOf()

    fun <T> find(getter: Scope.() -> T): T? {
        return getter(this) ?: return parent?.find(getter)
    }

    fun getVariable(name: String): Variable? {
        return find { variables.first { it.name == name } }
    }

    fun getType(name: String): VarType? {
        return find { types.first { it.name == name }}
    }

    fun getFunc(name: String): FuncDecl? {
        return find { funcs.first { it.name == name }}
    }

    fun addVariable(v: Variable) {
        variables += v
    }

    fun addType(t: VarType) {
        types += t
    }

    fun addFunc(f: FuncDecl) {
        funcs += f
    }
}