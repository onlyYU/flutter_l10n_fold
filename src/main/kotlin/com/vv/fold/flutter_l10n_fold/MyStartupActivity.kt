package com.vv.fold.flutter_l10n_fold

import MyEditorFactoryListener
import com.google.gson.GsonBuilder
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

object FileConstants {
    const val JSON_FILE_NAME = "intl_zh.arb"
    const val JSON_FOLDER_NAME = "lib/l10n"
}

class MyStartupActivity : StartupActivity {
    override fun runActivity(project: Project) {
        readJsonFile(project)

        val connection = ApplicationManager.getApplication().messageBus.connect()
        val editorFactory = EditorFactory.getInstance()
        editorFactory.addEditorFactoryListener(MyEditorFactoryListener(), connection as Disposable)

        val fileWatcher = FileWatcher(project)
        Thread(fileWatcher).start()
    }


    // 读取文件
    public fun readJsonFile(project: Project) {
        val jsonFolderPath = project.basePath?.let { Paths.get(it, FileConstants.JSON_FOLDER_NAME) }
        val jsonFilePath = jsonFolderPath?.let { getJsonFilePath(it) }

        val gson = GsonBuilder()
                .registerTypeAdapter(object : TypeToken<Map<String, String>>() {}.type, MapTypeAdapter())
                .create()

        val reader = jsonFilePath?.let { FileReader(it) }
        if (reader != null) {
            val jsonMapType = object : TypeToken<Map<String, String>>() {}.type
            LanguageData.jsonMap = gson.fromJson(reader, jsonMapType)
        }
    }

    private fun getJsonFilePath(folderPath: Path): String {
        val zhFilePath = folderPath.resolve(FileConstants.JSON_FILE_NAME).toString()
        if (!Files.exists(Paths.get(zhFilePath))) {
            throw RuntimeException("intl_zh.arb file not found")
        }
        return zhFilePath
    }
}