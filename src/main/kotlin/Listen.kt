package me.cdh

import java.io.File
import javax.swing.JFileChooser
import javax.swing.JOptionPane

object Listen {
    var userSelectedFile: File? = null
    fun registerOpenItem() =
        open.addActionListener {
            JFileChooser().apply {
                fileSelectionMode = JFileChooser.FILES_ONLY
                showOpenDialog(null)
                userSelectedFile = selectedFile
                val currentTextArea = EditorArea()

                tabPane.addTab(userSelectedFile?.name, EditorScrollPane(currentTextArea))
                bufferList.add(currentTextArea)

                userSelectedFile?.bufferedReader().use {
                    val currentTabIndex = tabPane.selectedIndex//-1
                    it?.readLines()?.forEach {
                        currentTextArea.append("$it\n")
                        // 设置当前buffer标题为用户打开的文件名
                        tabPane.selectedIndex = currentTabIndex + 1
                        tabPane.setTitleAt(currentTabIndex + 1, userSelectedFile?.name)
                    }
                }
            }
        }

    /**
     * 写出文件
     */
    fun registerSaveItem() {
//        val scrollPane = tabPane.selectedComponent as EditorScrollPane
//        val textArea = scrollPane.getComponent(0) as EditorArea
        val index = tabPane.selectedIndex
        val bufferContent = bufferList[index].text
        save.addActionListener {
            userSelectedFile?.bufferedWriter().use {
                it?.write(bufferContent)
            }
            savedLabelPopup()
        }
    }

    fun registerSaveAsItem() {
        saveAs.addActionListener {
            JFileChooser().apply {
                var file: File
                selectedFile = userSelectedFile
                val result = showSaveDialog(null)
                if (result == JFileChooser.APPROVE_OPTION) {
                    file = this.selectedFile
                    if (file.exists()) {
                        val overwrite = JOptionPane.showConfirmDialog(
                            null,
                            "File is already exists, Do you want to cover it?",
                            "File exists",
                            JOptionPane.YES_NO_OPTION
                        )
                        if (overwrite != JOptionPane.YES_OPTION) result
                    }
                    file.bufferedWriter().use {
                        it.append(displayText.text)
                    }
                    JOptionPane.showMessageDialog(null, "FIle is saved at ${file.absolutePath}")
                }
            }
        }
    }
}