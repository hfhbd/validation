package app.softwork.validation.plugin.kotlin.ir

import org.jetbrains.kotlin.GeneratedDeclarationKey
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.irNot
import org.jetbrains.kotlin.backend.common.lower.irThrow
import org.jetbrains.kotlin.fir.extensions.AnnotationFqn
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.builders.IrBlockBodyBuilder
import org.jetbrains.kotlin.ir.builders.Scope
import org.jetbrains.kotlin.ir.builders.irBranch
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irCallConstructor
import org.jetbrains.kotlin.ir.builders.irConcat
import org.jetbrains.kotlin.ir.builders.irEqualsNull
import org.jetbrains.kotlin.ir.builders.irFalse
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irIfThen
import org.jetbrains.kotlin.ir.builders.irString
import org.jetbrains.kotlin.ir.builders.irTrue
import org.jetbrains.kotlin.ir.declarations.IrAnonymousInitializer
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.declarations.addMember
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.symbols.impl.IrAnonymousInitializerSymbolImpl
import org.jetbrains.kotlin.ir.util.isNullable
import org.jetbrains.kotlin.ir.types.isString
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.getAnnotation
import org.jetbrains.kotlin.ir.util.getAnnotationStringValue
import org.jetbrains.kotlin.ir.util.getPropertyGetter
import org.jetbrains.kotlin.ir.util.parentClassOrNull
import org.jetbrains.kotlin.ir.util.toIrConst
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName

internal class ValidationTransformer(private val pluginContext: IrPluginContext) : IrElementTransformerVoid() {
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
            origin = IrDeclarationOrigin.GeneratedByPlugin(ValidationKey),
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
        )
        declaration.getAnnotation(MaxLength)?.addInit(
            declaration = declaration,
            declarationName = declaration.getSerialName() ?: declaration.name.asString(),
            comp = greater,
            compHuman = "<=",
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
    ) {
        val value = (arguments[0]!! as IrConst).value as Int
        val klass = declaration.parentClassOrNull ?: return

        with(
            IrBlockBodyBuilder(
                context = pluginContext,
                scope = Scope(unit),
                startOffset = startOffset,
                endOffset = endOffset,
            )
        ) {
            val isNullable = declaration.getter!!.returnType.isNullable()
            val checkLength = irCall(comp).apply {
                arguments[0] = irCall(STRINGlength).apply {
                    dispatchReceiver = irCall(
                        declaration.getter!!
                    ).apply {
                        dispatchReceiver = irGet(klass.thisReceiver!!)
                    }
                }
                arguments[1] = value.toIrConst(pluginContext.irBuiltIns.intType)
            }
            val newInitBlock = newInitBlock ?: return@with
            newInitBlock.body.statements += irIfThen(
                type = unitType,
                condition = if (isNullable) {
                    irIfThen(
                        type = booleanType,
                        condition = irNot(
                            irEqualsNull(
                                irCall(
                                    declaration.getter!!
                                ).apply {
                                    dispatchReceiver = irGet(klass.thisReceiver!!)
                                }
                            ),
                        ),
                        thenPart = checkLength
                    ).apply {
                        branches.add(irBranch(irTrue(), irFalse()))
                    }
                } else checkLength,
                thenPart = irThrow(irCallConstructor(
                    validationExceptionSymbol!!.constructors.first {
                        if (it.owner.parameters.size == 1) {
                            val singleParameter = it.owner.parameters.single()
                            singleParameter.type.isString()
                        } else false
                    },
                    emptyList(),
                ).apply {
                    arguments[0] = irConcat().apply {
                        arguments += irString("$declarationName.length $compHuman ")
                        arguments += value.toIrConst(pluginContext.irBuiltIns.intType)
                        arguments += irString(", was ")
                        arguments += irCall(
                            declaration.getter!!
                        ).apply {
                            dispatchReceiver = irGet(klass.thisReceiver!!)
                        }
                    }
                },
            ))
        }
    }
}

private data object ValidationKey : GeneratedDeclarationKey()
