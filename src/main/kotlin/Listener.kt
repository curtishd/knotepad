package me.cdh

import java.awt.BorderLayout
import java.io.File
import javax.swing.JButton
import javax.swing.JFileChooser
import javax.swing.JLabel
import javax.swing.JPanel

// register menu item.
object Listener {
    var userSelectedFile: File? = null

    fun registerCreateItem() {
        val panel = deleteBtnInTab(defaultTitle)
        createFile.addActionListener {
            tabPane.addTab(null, EditorScrollPane(EditorArea()))
            val index = tabPane.selectedIndex
            if (index != -1) {
                tabPane.setTabComponentAt(bufferList.size, panel)
                bufferList.add(EditorArea())
            }
        }
    }

    fun registerOpenItem() =
        open.addActionListener {
            JFileChooser().apply {
                fileSelectionMode = JFileChooser.FILES_ONLY
                val result = showOpenDialog(null)
                // if user approve open file
                if (result == JFileChooser.APPROVE_OPTION) {
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
                        val panel = deleteBtnInTab(userSelectedFile!!.name)
                        tabPane.setTabComponentAt(lastIndex, panel)
                        // set the current buffer title as same as the file which user selected
                        tabPane.selectedIndex = lastIndex
                        tabPane.setTitleAt(lastIndex, userSelectedFile?.name)
                        // write content to List
                        val curIndex = tabPane.selectedIndex
                        val content = it?.readText()
                        bufferList[curIndex].text = content
                        it?.readLines()?.forEach {
                            currentTextArea.append("$it\n")
                        }
                    }
                } else println("open cancel")
            }
        }

    //TODO
    fun registerSaveItem() {
        val index = tabPane.selectedIndex
        if (index != -1) {
            val bufferContent = bufferList[index].text
            save.addActionListener {
                userSelectedFile?.bufferedWriter().use {
                    it?.write(bufferContent)
                }
                labelPopup("Saved")
            }
        } else println("save cancel")
    }

    //    fun registerSaveAsItem() {
//        saveAs.addActionListener {
//            JFileChooser().apply {
//                var file: File
//                selectedFile = userSelectedFile
//                val result = showSaveDialog(null)
//                if (result == JFileChooser.APPROVE_OPTION) {
//                    file = this.selectedFile
//                    if (file.exists()) {
//                        val overwrite = JOptionPane.showConfirmDialog(
//                            null,
//                            "File is already exists, Do you want to cover it?",
//                            "File exists",
//                            JOptionPane.YES_NO_OPTION
//                        )
//                        if (overwrite != JOptionPane.YES_OPTION) result
//                    }
//                    file.bufferedWriter().use {
//                        it.append(displayText.text)
//                    }
//                    JOptionPane.showMessageDialog(null, "FIle is saved at ${file.absolutePath}")
//                }
//            }
//        }
//    }
    fun deleteBtnInTab(label: String): JPanel {
        val btn = JButton("x").apply {
            isFocusPainted = false
            isContentAreaFilled = false
        }
        val panel = JPanel().apply {
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