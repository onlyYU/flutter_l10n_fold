import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiRecursiveElementVisitor

object Constants {
    // 包含字符串"getLanguage<S>()."
    val REGEX = Regex("getLanguage<S>\\(\\)\\.LMID_\\d{8}(?=\\d{8}|$)")

}
open class MyPsiElementVisitor(private val descriptors: MutableList<FoldingDescriptor>) : PsiRecursiveElementVisitor() {

    override fun visitElement(element: PsiElement) {
        val matchResult = Constants.REGEX.find(element.text)
        if (matchResult != null) {
            val start = matchResult.range.first
            val end = matchResult.range.last + 1
            val range = TextRange(element.textRange.startOffset + start, element.textRange.startOffset + end)
            val descriptor = FoldingDescriptor(element.node, range)
            descriptors.add(descriptor)
        }

        super.visitElement(element)
    }

    private fun isSpecificCodeBlock(element: PsiElement): Boolean {
        return element.text.contains(Constants.REGEX)
    }
}