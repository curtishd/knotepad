package me.cdh

import com.formdev.flatlaf.themes.FlatMacDarkLaf
import com.formdev.flatlaf.themes.FlatMacLightLaf
import java.awt.BorderLayout
import java.awt.Container
import java.awt.Dimension
import java.awt.Image
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.ImageIcon
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.JPanel
import javax.swing.JPopupMenu
import javax.swing.JScrollPane
import javax.swing.JTabbedPane
import javax.swing.SwingUtilities

val bufferList = mutableListOf(EditorArea()) // 维护tabpane内部editorarea

//默认展示空白文件
val displayTextPane = JScrollPane(bufferList[0])
val tabPane = JTabbedPane(JTabbedPane.TOP).apply {
    addTab(defaultTitle, displayTextPane)
    setTabComponentAt(0, Container().apply {
        layout = BorderLayout()
        add(JLabel(defaultTitle).apply {
            setSize(150, 20)
            font = tabFont
        }, BorderLayout.CENTER)
        add(JButton("x").apply {
            isFocusPainted = false
            isContentAreaFilled = false
        }, BorderLayout.EAST)
    })
}

// 菜单栏选项
val createFile = JMenuItem("Create File")
val open = JMenuItem("Open")
val save = JMenuItem("Save")
val saveAs = JMenuItem("Save As")
val findAndReplace = JMenuItem("Find And Replace")
val closePage = JMenuItem("Close Current Page")
val settings = JMenuItem("Settings")
val exit = JMenuItem("Exit").apply {
    addActionListener {
        exitOrNot()
    }
}

// 菜单栏按钮
val menu = JMenu().apply {
    icon = ImageIcon(Main.javaClass.getResource("/menu.png"))
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


val lineAndColumn = JLabel("1:1")

val indentBtn = JButton("4 spaces").apply {
    isContentAreaFilled = false
    isBorderPainted = false
    val two = JMenuItem("2 spaces")
    val four = JMenuItem("4 spaces")
    two.addActionListener {
        val index = tabPane.selectedIndex
        if (index != -1) {
            text = two.text
            bufferList[index].tabSize = 2
        }
    }
    four.addActionListener {
        val index = tabPane.selectedIndex
        if (index != -1) {
            text = four.text
            bufferList[index].tabSize = 4
        }
    }

    val pop = JPopupMenu().apply {
        add(two)
        add(four)
    }
    addActionListener {
        pop.show(this, 0, height)
    }
}

// 状态栏
val statusBar = JPanel().apply {
    layout = BorderLayout()
    val dimension = Dimension(preferredSize)
    dimension.height = 18
    preferredSize = dimension

    add(lineAndColumn, BorderLayout.WEST)
    add(indentBtn, BorderLayout.EAST)
}


// 基础框架
val displayFrame = JFrame().apply {
    jMenuBar = displayMenuBar
    layout = BorderLayout()
    add(tabPane, BorderLayout.CENTER)
    add(statusBar, BorderLayout.SOUTH)

    // register event listener
    registerCreateItem()
    registerOpenItem()
    registerSaveItem()
    registerSaveAsItem()
    registerCloseCurrentPageItem()

    setSize(1000, 800)
    setLocationRelativeTo(null)
    defaultCloseOperation = JFrame.DO_NOTHING_ON_CLOSE
    isVisible = true
    addWindowListener(object : WindowAdapter() {
        override fun windowClosing(e: WindowEvent?) {
            exitOrNot()
        }
    })
}

object Main {
    @JvmStatic
    fun main(args: Array<String>) =
        SwingUtilities.invokeLater {
            FlatMacLightLaf.setup()
            displayFrame
        }
}