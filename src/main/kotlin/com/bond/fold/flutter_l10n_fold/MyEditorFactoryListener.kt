import com.intellij.openapi.editor.event.EditorFactoryEvent
import com.intellij.openapi.editor.event.EditorFactoryListener
import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiManager

class MyEditorFactoryListener : EditorFactoryListener {
    override fun editorCreated(event: EditorFactoryEvent) {
        val editor = event.editor as? EditorEx ?: return
        val project = editor.project ?: return
        val document = editor.document
        val file = FileDocumentManager.getInstance().getFile(document) ?: return
        val psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document) ?: return
        val psiManager = PsiManager.getInstance(project)

        if (file.extension == "dart") {
            val visitor = MyPsiElementVisitor(mutableListOf())
            psiFile.accept(visitor)
        }
    }
}