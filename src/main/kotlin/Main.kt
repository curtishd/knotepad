package me.cdh

import com.formdev.flatlaf.FlatLightLaf
import javax.swing.JFrame
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.SwingUtilities


val createFile = JMenuItem("Create File")
val createWin = JMenuItem("Create Window")
val open = JMenuItem("Open")
val save = JMenuItem("Save")
val saveAs = JMenuItem("Save As")
val findAndReplace = JMenuItem("Find And Replace")
val settings = JMenuItem("Settings")
val exit = JMenuItem("Exit")
val menu = JMenu("=").apply {
    add(createFile)
    add(createWin)
    add(open)
    addSeparator()
    add(save)
    add(saveAs)
    addSeparator()
    add(findAndReplace)
    add(settings)
    addSeparator()
    add(exit)
}
val displayMenuBar = JMenuBar().apply {
    add(menu)
}
val displayFrame = JFrame().apply {
    jMenuBar = displayMenuBar

    setSize(1000, 800)
    setLocationRelativeTo(null)
    defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    isVisible = true
}

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SwingUtilities.invokeLater {
                FlatLightLaf.setup()
                displayFrame
            }
        }
    }
}