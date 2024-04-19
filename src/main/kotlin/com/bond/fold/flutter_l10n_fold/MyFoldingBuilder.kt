package com.bond.fold.flutter_l10n_fold

import MyPsiElementVisitor
import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilder
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import org.jetbrains.annotations.NotNull
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.jvm.isAccessible


class MyFoldingBuilder : FoldingBuilder, DumbAware {
    override fun buildFoldRegions(@NotNull node: ASTNode, @NotNull document: Document): Array<FoldingDescriptor> {
        val descriptors = mutableListOf<FoldingDescriptor>()
        val visitor = MyPsiElementVisitor(descriptors)
        node.psi.accept(visitor)
        return descriptors.toTypedArray()
    }

    override fun getPlaceholderText(@NotNull node: ASTNode): String {
        val key = node.text.substringAfter("getLanguage<S>().").substringBefore("{")
        return LanguageData.jsonMap[key] ?: key
//        return  invokeDartMethod(node.text)
    }

    override fun isCollapsedByDefault(@NotNull node: ASTNode): Boolean {
        return true
    }

    private fun invokeDartMethod(methodString: String): String {
        val className = "l10n" // Dart class name
        val methodName = methodString // Dart method name

        val kClass = Class.forName(className).kotlin
        val kFunction = kClass.memberFunctions.find { it.name == methodName }

        kFunction?.isAccessible = true
        val result = kFunction?.call()

        return result?.toString() ?: methodString
    }
}