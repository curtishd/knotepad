package me.cdh

import com.formdev.flatlaf.FlatDarkLaf
import java.awt.BorderLayout
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.JPanel
import javax.swing.JTabbedPane
import javax.swing.SwingUtilities
import kotlin.system.exitProcess


val displayText = EditorArea()
val bufferList = mutableListOf(EditorArea())
val displayTextPane = EditorScrollPane(bufferList[0])
val tabPane: JTabbedPane = JTabbedPane(JTabbedPane.TOP).apply {
    addTab(defaultTitle, displayTextPane)
    setTabComponentAt(0, JPanel().apply {
        layout = BorderLayout()
        add(JLabel(defaultTitle).apply {
            font = tabFont
        }, BorderLayout.CENTER)
        add(JButton("x").apply {
            isFocusPainted = false
            isContentAreaFilled = false
        }, BorderLayout.EAST)
    })
}

// ------------------------
val createFile = JMenuItem("Create File").apply {
    val addOne = EditorArea()
    addActionListener {
        tabPane.addTab(defaultTitle, EditorScrollPane(addOne))
        bufferList.add(addOne)
    }
}
val open = JMenuItem("Open")
val save = JMenuItem("Save")
val saveAs = JMenuItem("Save As")
val findAndReplace = JMenuItem("Find And Replace")
val closePage = JMenuItem("Close Current Page")
val settings = JMenuItem("Settings")
val exit = JMenuItem("Exit").apply {
    addActionListener {
        exitProcess(0)
    }
}
val menu = JMenu(">_<").apply {
    add(createFile)
    add(open)
    addSeparator()
    add(save)
    add(saveAs)
    addSeparator()
    add(findAndReplace)
    add(closePage)
    add(settings)
    addSeparator()
    add(exit)
}
val displayMenuBar = JMenuBar().apply {
    add(menu)
}

val displayFrame = JFrame().apply {
    jMenuBar = displayMenuBar
    contentPane = tabPane

    // register event listener
    Listen.registerOpenItem()
    Listen.registerSaveItem()

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
                FlatDarkLaf.setup()
                displayFrame
            }
        }
    }
}