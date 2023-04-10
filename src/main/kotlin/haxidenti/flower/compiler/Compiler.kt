package haxidenti.flower.compiler

import haxidenti.flower.parser.Command
import haxidenti.flower.parser.OperatorCommand
import haxidenti.flower.parser.VariableGetCommand
import haxidenti.flower.types.BlockInstruction
import haxidenti.flower.types.Instruction
import haxidenti.flower.types.Scope
import haxidenti.flower.types.VariableGetInstruction

class FlowerCompiler {
    val globalScope = Scope(null)
    var currentScope = globalScope

    fun compile(commands: List<Command>): BlockInstruction? {
        return null
    }

    fun compileOne(cmd: Command): Instruction? {
        return null
    }

    fun compileVariableGet(cmd: Command): Instruction? {
        return if (cmd is VariableGetCommand) {
            val variable = currentScope.getVariable(cmd.varName) ?: throw CompilerException(
                cmd.info, "No such variable: ${cmd.varName}"
            )
            VariableGetInstruction(cmd.info, variable, currentScope)
        } else null
    }

    private val Command.splitArgs get() = this.args.splitBy { it is OperatorCommand && it.dat == "::" }
    private val Command.instruction get() = compileOne(this)

    fun <T> List<T>.splitBy(checker: (T) -> Boolean): List<List<T>> {
        val result = mutableListOf<MutableList<T>>()
        var sublist = mutableListOf<T>()
        for (value in this) {
            if (checker(value)) {
                result.add(sublist)
                sublist = mutableListOf()
            } else {
                sublist.add(value)
            }
        }
        result.add(sublist)
        return result
    }

    private fun Command.hasOperator(op: String): Boolean {
        return this.args.filterIsInstance<OperatorCommand>().any { it.dat == op }
    }


}