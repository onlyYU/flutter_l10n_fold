package com.bond.fold.flutter_i10n_fold

import MyPsiElementVisitor
import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilder
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import org.jetbrains.annotations.NotNull


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
    }

    override fun isCollapsedByDefault(@NotNull node: ASTNode): Boolean {
        return true
    }
}