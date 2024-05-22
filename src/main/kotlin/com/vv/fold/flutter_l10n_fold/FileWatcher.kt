package com.vv.fold.flutter_l10n_fold

import com.intellij.openapi.project.Project
import java.nio.file.*

class FileWatcher(private val project: Project) : Runnable {
    private val watchService: WatchService = FileSystems.getDefault().newWatchService()
    private var isFileExists = false

    init {
        val path = project.basePath?.let { Paths.get(it, FileConstants.JSON_FOLDER_NAME) }
        if (path != null && Files.exists(path)) {
            val filePath = path.resolve(FileConstants.JSON_FILE_NAME)
            if (Files.exists(filePath)) {
                path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY)
                isFileExists = true
            }
        }
    }

    override fun run() {
        if (!isFileExists) {
            return
        }

        try {
            while (true) {
                val key = watchService?.take()
                for (event in key?.pollEvents() ?: emptyList()) {
                    val context = event.context() as Path
                    if (context.fileName.toString() == FileConstants.JSON_FILE_NAME) {
                        MyStartupActivity().readJsonFile(project)
                    }
                }
                if (key != null && !key.reset()) {
                    break
                }
            }
        } catch (ex: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }
}