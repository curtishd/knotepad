package me.cdh

import java.awt.BorderLayout
import java.awt.Color
import java.awt.Container
import java.awt.Dimension
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.Box
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
import javax.swing.JTextField
import javax.swing.border.LineBorder

val bufferList = mutableListOf(EditorArea())

// show empty file default
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

// settings
//----------------------theme-------------------------
private val macDarkTheme = JCheckBoxMenuItem("Dark").apply {
    repaintTheme(this, MAC_DARK)
}
private val macLightTheme = JCheckBoxMenuItem("Light White").apply {
    repaintTheme(this, MAC_LIGHT)
}

private val darculaTheme = JCheckBoxMenuItem("Darcula").apply {
    repaintTheme(this, DARCULA)
}

@Suppress("Unused")
private val group = ButtonGroup().apply {
    add(macDarkTheme)
    add(macLightTheme)
    add(darculaTheme)
}
val changeTheme = JMenu("Change Theme").apply {
    add(darculaTheme)
    add(macDarkTheme)
    add(macLightTheme)
}

val createFile: JMenuItem = JMenuItem("Create File")
val open: JMenuItem = JMenuItem("Open")
val save: JMenuItem = JMenuItem("Save")
val saveAs: JMenuItem = JMenuItem("Save As")
val findAndReplace: JMenuItem = JMenuItem("Find And Replace")
val closePage: JMenuItem = JMenuItem("Close Current Page")
val settings: JMenu = JMenu("Settings").apply {
    add(changeTheme)
}
val exit: JMenuItem = JMenuItem("Exit").apply {
    addActionListener {
        exitOrNot()
    }
}

val menu: JMenu = JMenu().apply {
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

val displayMenuBar: JMenuBar = JMenuBar().apply {
    add(menu)
}

val lineDisplay: JLabel = JLabel("Line: 1")

val indentBtn: JButton = JButton("4 spaces").apply {
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

// status bar
val statusBar: JPanel = JPanel().apply {
    layout = BorderLayout()
    val dimension = Dimension(preferredSize)
    dimension.height = 18
    preferredSize = dimension

    add(lineDisplay, BorderLayout.WEST)
    add(indentBtn, BorderLayout.EAST)
}

// framework
val displayFrame: JFrame = JFrame().apply {
    jMenuBar = displayMenuBar
    layout = BorderLayout()
    add(tabPane, BorderLayout.CENTER)
    add(statusBar, BorderLayout.SOUTH)

    // register event listener
    registerCreateItem()
    registerOpenItem()
    registerSaveItem()
    registerSaveAsItem()
    registerFindAndReplaceItem()
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
    addComponentListener(object : ComponentAdapter() {
        override fun componentMoved(e: ComponentEvent?) {
            val x = displayFrame.locationOnScreen.x
            val y = displayFrame.locationOnScreen.y
            val disX = displayFrame.width
            dialog.setLocation(x + disX - 320, y + 80)
            dialog.setSize(300, 100)
        }
    })
}

// Search and replace widgets

internal val dialogBtn = JButton("X").apply {
    addActionListener {
        dialog.dispose()
    }
}

internal val search: JTextField = JTextField().apply {
    val newSize = Dimension(displayFrame.width - 800, 30)
    preferredSize = newSize
    text = "Search"
    foreground = Color.GRAY
    addComponentListener(object : ComponentAdapter() {
        override fun componentResized(e: ComponentEvent?) {
            displayFrame.repaint()
        }
    })
    addFocusListener(object : FocusAdapter() {
        override fun focusLost(e: FocusEvent?) {
            text = "Search"
            foreground = Color.GRAY
        }

        override fun focusGained(e: FocusEvent?) {
            text = ""
            foreground = Color.WHITE
        }
    })
}

internal val replace: JTextField = JTextField().apply {
    isVisible = false
    val newSize = Dimension(displayFrame.width - 800, 30)
    preferredSize = newSize
    text = "Replace"
    foreground = Color.GRAY
    addComponentListener(object : ComponentAdapter() {
        override fun componentResized(e: ComponentEvent?) {
            displayFrame.repaint()
        }
    })
    addFocusListener(object : FocusAdapter() {
        override fun focusLost(e: FocusEvent?) {
            text = "Replace"
            foreground = Color.GRAY
        }

        override fun focusGained(e: FocusEvent?) {
            text = ""
            foreground = Color.WHITE
        }
    })
}
private val textFieldBox = Box.createVerticalBox()
private val jp = JPanel().apply {
    add(replace)
}
internal val displayReplace = JButton(">").apply {
    isBorderPainted = false
    var toggle = false
    addActionListener {
        if (toggle) {
            text = ">"
            replace.isVisible = false
            toggle = !toggle
        } else {
            text = "v"
            replace.isVisible = true
            toggle = !toggle
        }
    }
}
internal val dialog: JFrame = JFrame().apply {
    val pt = JPanel()
    pt.add(search)
    textFieldBox.add(pt)
    textFieldBox.add(jp)
    val hBox = Box.createHorizontalBox()
    val leftP = JPanel()
    leftP.add(displayReplace)
    hBox.add(leftP)
    hBox.add(Box.createHorizontalStrut(5))
    hBox.add(textFieldBox)
    val p = JPanel()
    p.add(dialogBtn)
    hBox.add(Box.createHorizontalStrut(5))
    hBox.add(p)
    val container = JPanel()
    container.border = LineBorder(Color.WHITE, 1)
    container.add(hBox)
    add(container)

    isResizable = false
    isUndecorated = true
    isAlwaysOnTop = true
    defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
}