package me.cdh

import java.awt.BorderLayout
import java.io.File
import javax.swing.JButton
import javax.swing.JFileChooser
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel

object Listener {

    var userSelectedFile: File? = null

    // 注册创建文件按钮事件
    fun registerCreateItem() {
        createFile.addActionListener {
            val panel = registerDeleteBtnInTab(defaultTitle)
            tabPane.addTab(null, EditorScrollPane(EditorArea()))
            val index = tabPane.selectedIndex
            if (index != -1) {
                tabPane.setTabComponentAt(bufferList.size, panel)
                bufferList.add(EditorArea())
            }
        }
    }

    // 注册打开文件按钮事件
    fun registerOpenItem() =
        open.addActionListener {
            JFileChooser().apply {
                fileSelectionMode = JFileChooser.FILES_ONLY
                // if user approve open file
                if (showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    userSelectedFile = selectedFile
                    val currentTextArea = EditorArea()
                    // read file and write into buffer
                    tabPane.addTab(null, EditorScrollPane(currentTextArea))
                    bufferList.add(currentTextArea)
                    userSelectedFile?.bufferedReader().use {
                        for (i in 0..<tabPane.tabCount) {
                            if (tabPane.getTitleAt(i) == selectedFile.name) {
                                labelPopup("File is already open!")
                                return@addActionListener
                            }
                        }
                        println(userSelectedFile!!.name) //debug
                        val lastIndex = bufferList.size - 1
                        val panel = registerDeleteBtnInTab(userSelectedFile!!.name)
                        tabPane.setTabComponentAt(lastIndex, panel)
                        // set the current buffer title as same as the file which user selected
                        tabPane.selectedIndex = lastIndex
                        tabPane.setTitleAt(lastIndex, userSelectedFile?.name)
                        // write content to List
                        val curIndex = tabPane.selectedIndex
                        val content = it?.readText()
                        bufferList[curIndex].text = content
                        // write content to gui buffer
                        it?.readLines()?.forEach {
                            currentTextArea.append("$it\n")
                        }
                    }
                } else println("open cancel")
            }
        }

    // 注册保存文件按钮事件
    fun registerSaveItem() {
        save.addActionListener {
            //TODO 处理tab为newfile的情况
            val index = tabPane.selectedIndex
            if (index != -1) {
                val bufferContent = bufferList[index].text
//                println(index)
                userSelectedFile!!.bufferedWriter().use {
                    it.write(bufferContent)
                }
//                println(bufferContent)
                labelPopup("Saved")
            } else println("save cancel")
        }
    }

    fun registerSaveAsItem() {
        saveAs.addActionListener {
            JFileChooser().apply {
                val result = showSaveDialog(displayFrame)
                dialogTitle = "Save File"
                if (result == JFileChooser.APPROVE_OPTION) {
                    val fileToSave = selectedFile
                    val bufferIndex = tabPane.selectedIndex
                    if (bufferIndex != -1 && fileToSave.exists()) {
                        val content = bufferList[bufferIndex].text
                        val overwrite = JOptionPane.showConfirmDialog(
                            null,
                            "File is exists, cover it or not?",
                            "Current file",
                            JOptionPane.YES_NO_OPTION
                        )
                        if (overwrite == JOptionPane.YES_OPTION)
                            fileToSave.bufferedWriter().use {
                                it.write(content)
                            }
                        else println("save cancel")
                    } else {
                        val fileAbsolutePath = fileToSave.absolutePath
                        val file = File(fileAbsolutePath)
                        file.createNewFile()

                        val content = bufferList[bufferIndex].text
                        file.bufferedWriter().use {
                            it.write(content)
                        }
                    }
                }
            }

        }
    }

    fun registerFindAndReplaceItem() {

    }

    // 注册关闭当前页按钮事件
    fun registerCloseCurrentPageItem() {
        closePage.addActionListener {
            val selectedIndex = tabPane.selectedIndex
            if (selectedIndex != -1) {
                tabPane.removeTabAt(selectedIndex)
                bufferList.removeAt(selectedIndex)
            } else error("No tab is selected")
        }
    }

    // 注册删除按钮事件
    private fun registerDeleteBtnInTab(label: String): JPanel {
        val btn = JButton("x").apply {
            isFocusPainted = false
            isContentAreaFilled = false
        }
        val panel = JPanel().apply {
            setSize(100, 20)
            layout = BorderLayout()
            add(JLabel(label).apply {
                font = tabFont
            }, BorderLayout.CENTER)
            add(btn, BorderLayout.EAST)
        }
        with(btn) {
            addActionListener {
                val index = tabPane.indexOfTabComponent(panel)
                println(index)
                if (index >= 0) {
                    val tabIndex = tabPane.indexOfTab(tabPane.getTitleAt(index))
                    tabPane.removeTabAt(tabIndex)
                    bufferList.removeAt(tabIndex)
                }
            }
        }
        return panel
    }
}