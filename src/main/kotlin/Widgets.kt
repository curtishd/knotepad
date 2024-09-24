package me.cdh

import java.awt.BorderLayout
import java.awt.Container
import java.awt.Dimension
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.ButtonGroup
import javax.swing.ImageIcon
import javax.swing.JButton
import javax.swing.JCheckBoxMenuItem
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.JPanel
import javax.swing.JPopupMenu
import javax.swing.JScrollPane
import javax.swing.JTabbedPane

val bufferList = mutableListOf(EditorArea())

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
        add(JButton(ImageIcon(Main.javaClass.classLoader.getResource("white_close_button.png"))).apply {
            isFocusPainted = false
            isContentAreaFilled = false
        }, BorderLayout.EAST)
    })
}

// settings 子菜单
//----------------------theme-------------------------
private val macDarkTheme = JCheckBoxMenuItem("Dark").apply {
    repaintTheme(this, MAC_DARK)
}
private val macLightTheme = JCheckBoxMenuItem("Light White").apply {
    repaintTheme(this, MAC_LIGHT)
}
private val darkTheme = JCheckBoxMenuItem("Light Gray").apply {
    repaintTheme(this, DARK)
}
private val lightTheme = JCheckBoxMenuItem("Light").apply {
    repaintTheme(this, LIGHT)
}
private val darculaTheme = JCheckBoxMenuItem("Darcula").apply {
    repaintTheme(this, DARCULA)
}
private val group = ButtonGroup().apply {
    add(macDarkTheme)
    add(macLightTheme)
    add(lightTheme)
    add(darkTheme)
    add(darculaTheme)
}
val changeTheme = JMenu("Change Theme").apply {
    add(darkTheme)
    add(lightTheme)
    add(darculaTheme)
    add(macDarkTheme)
    add(macLightTheme)
}

//----------------------------------------------
// 菜单栏选项
val createFile = JMenuItem("Create File")
val open = JMenuItem("Open")
val save = JMenuItem("Save")
val saveAs = JMenuItem("Save As")
val findAndReplace = JMenuItem("Find And Replace")
val closePage = JMenuItem("Close Current Page")
val settings = JMenu("Settings").apply {
    add(changeTheme)
}
val exit = JMenuItem("Exit").apply {
    addActionListener {
        exitOrNot()
    }
}

// 菜单栏按钮
val menu = JMenu().apply {
    icon = ImageIcon(Main.javaClass.classLoader.getResource("white_menu.png"))
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

val lineDisplay = JLabel("Line: 1")

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

    add(lineDisplay, BorderLayout.WEST)
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