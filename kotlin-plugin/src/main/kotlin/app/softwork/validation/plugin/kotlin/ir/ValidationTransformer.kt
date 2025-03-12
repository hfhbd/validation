package app.softwork.validation.plugin.kotlin.ir

import org.jetbrains.kotlin.backend.common.extensions.*
import org.jetbrains.kotlin.backend.common.lower.*
import org.jetbrains.kotlin.fir.extensions.*
import org.jetbrains.kotlin.ir.*
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.symbols.*
import org.jetbrains.kotlin.ir.symbols.impl.*
import org.jetbrains.kotlin.ir.types.*
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.ir.visitors.*
import org.jetbrains.kotlin.name.*

internal class ValidationTransformer(
    private val pluginContext: IrPluginContext,
    private val dump: ((String) -> Unit)?,
) : IrElementTransformerVoid() {

    private val MinLength = AnnotationFqn("app.softwork.validation.MinLength")
    private val MaxLength = AnnotationFqn("app.softwork.validation.MaxLength")
    private val SerialName = AnnotationFqn("kotlinx.serialization.SerialName")

    private val validationExceptionSymbol: IrClassSymbol? =
        pluginContext.referenceClass(ClassId(FqName("app.softwork.validation"), FqName("ValidationException"), false))
    private val unit = pluginContext.symbols.irBuiltIns.unitClass
    private val unitType = pluginContext.symbols.irBuiltIns.unitType
    private val booleanType = pluginContext.symbols.irBuiltIns.booleanType
    private val less =
        pluginContext.symbols.irBuiltIns.lessFunByOperandType[pluginContext.symbols.irBuiltIns.intClass]!!
    private val greater =
        pluginContext.symbols.irBuiltIns.greaterFunByOperandType[pluginContext.symbols.irBuiltIns.intClass]!!
    private val STRINGlength = pluginContext.irBuiltIns.stringClass.getPropertyGetter("length")!!

    private var newInitBlock: IrAnonymousInitializer? = null

    override fun visitClass(declaration: IrClass): IrStatement {

        val newInitBlock: IrAnonymousInitializer = declaration.factory.createAnonymousInitializer(
            startOffset = UNDEFINED_OFFSET,
            endOffset = UNDEFINED_OFFSET,
            origin = IrDeclarationOrigin.DEFINED,
            symbol = IrAnonymousInitializerSymbolImpl(declaration.symbol),
        )
        newInitBlock.parent = declaration
        newInitBlock.body = declaration.factory.createBlockBody(
            startOffset = UNDEFINED_OFFSET,
            endOffset = UNDEFINED_OFFSET,
        )
        this.newInitBlock = newInitBlock

        return declaration.transformPostfix {
            if (newInitBlock.body.statements.isNotEmpty()) {
                val dump = dump
                if (dump != null) {
                    for (stmt in newInitBlock.body.statements) {
                        dump(stmt.dump())
                    }
                }
                // move generated checks before user init code
                val previousInitBlocks = declarations.indexOfFirst {
                    it is IrAnonymousInitializer
                }.takeUnless { it == -1 }
                if (previousInitBlocks == null) {
                    addMember(newInitBlock)
                } else {
                    declarations.add(previousInitBlocks, newInitBlock)
                }
            }
            this@ValidationTransformer.newInitBlock = null
        }
    }

    override fun visitProperty(declaration: IrProperty): IrProperty {
        declaration.getAnnotation(MinLength)?.addInit(
            declaration = declaration,
            declarationName = declaration.getSerialName() ?: declaration.name.asString(),
            comp = less,
            compHuman = ">=",
            originParam = IrStatementOrigin.LT
        )
        declaration.getAnnotation(MaxLength)?.addInit(
            declaration = declaration,
            declarationName = declaration.getSerialName() ?: declaration.name.asString(),
            comp = greater,
            compHuman = "<=",
            originParam = IrStatementOrigin.GT,
        )

        return declaration
    }

    private fun IrProperty.getSerialName(): String? {
        return getAnnotation(SerialName)?.getAnnotationStringValue()
    }

    private fun IrConstructorCall.addInit(
        declaration: IrProperty,
        declarationName: String,
        comp: IrSimpleFunctionSymbol,
        compHuman: String,
        originParam: IrStatementOrigin,
    ) {
        val value = getValueArgument(0)!!
        val klass = declaration.parentClassOrNull ?: return

        with(
            IrBlockBodyBuilder(
                context = pluginContext,
                scope = Scope(unit),
                startOffset = startOffset,
                endOffset = endOffset,
            )
        ) {
            val prop = irCall(
                declaration.getter!!
            ).apply {
                dispatchReceiver = irGet(klass.thisReceiver!!)
                origin = IrStatementOrigin.GET_PROPERTY
            }
            val isNullable = declaration.getter!!.returnType.isNullable()
            val checkLength = irCall(comp).apply {
                putValueArgument(0, irCall(STRINGlength).apply {
                    dispatchReceiver = prop
                    origin = IrStatementOrigin.GET_PROPERTY
                })
                putValueArgument(1, value)
                origin = originParam
            }
            val newInitBlock = newInitBlock ?: return@with
            newInitBlock.body.statements += irIfThen(
                type = unitType,
                condition = if (isNullable) {
                    irIfThen(
                        type = booleanType,
                        condition = irNot(
                            irEqualsNull(
                                prop
                            ),
                        ),
                        thenPart = checkLength
                    ).apply {
                        origin = IrStatementOrigin.ANDAND
                        branches.add(irBranch(irTrue(), irFalse()))
                    }
                } else checkLength,
                thenPart = irThrow(irCallConstructor(
                    validationExceptionSymbol!!.constructors.first {
                        if (it.owner.valueParameters.size == 1) {
                            val singleParameter = it.owner.valueParameters.single()
                            singleParameter.type.isString()
                        } else false
                    },
                    emptyList(),
                ).apply {
                    putValueArgument(0, irConcat().apply {
                        arguments += irString("$declarationName.length $compHuman ")
                        arguments += value
                        arguments += irString(", was ")
                        arguments += prop
                    })
                },
            )).apply {
                origin = IrStatementOrigin.IF
            }
        }
    }
}
