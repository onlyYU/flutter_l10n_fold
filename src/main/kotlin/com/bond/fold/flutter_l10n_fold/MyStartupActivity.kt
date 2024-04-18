package com.bond.fold.flutter_i10n_fold

import MyEditorFactoryListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import java.io.FileReader
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

object LanguageData {
    var jsonMap: Map<String, String> = mapOf()
}

class MyStartupActivity : StartupActivity {
    override fun runActivity(project: Project) {
        readJsonFile(project)

        val connection = ApplicationManager.getApplication().messageBus.connect()
        val editorFactory = EditorFactory.getInstance()
        editorFactory.addEditorFactoryListener(MyEditorFactoryListener(), connection as Disposable)
    }


    // 读取文件
    private fun readJsonFile(project: Project) {
        val jsonFolderPath = project.basePath?.let { Paths.get(it, "lib/l10n") }
        val jsonFilePath = jsonFolderPath?.let { getJsonFilePath(it) }
        val jsonMapType = object : TypeToken<Map<String, String>>() {}.type
        LanguageData.jsonMap = Gson().fromJson(jsonFilePath?.let { FileReader(it) }, jsonMapType)
    }

    private fun getJsonFilePath(folderPath: Path): String {
        val zhFilePath = folderPath.resolve("intl_zh.arb").toString()
        if (!Files.exists(Paths.get(zhFilePath))) {
            throw RuntimeException("intl_zh.arb file not found")
        }
        return zhFilePath
    }
}