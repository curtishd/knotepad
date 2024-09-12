package me.cdh

import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JFrame
import javax.swing.JMenuItem
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextArea
import javax.swing.MenuElement

class EditorArea(textArea: String) : JTextArea(textArea) {
    constructor() : this(textArea = "")

    init {
        lineWrap = true
        tabSize = 4
        wrapStyleWord = true
        font = contentFont

        addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                val mousePositionX = e?.x
                val mousePositionY = e?.y
                // 检查鼠标是否右键文本区域
                if (e?.button == MouseEvent.BUTTON3) {
                    val rightClickFrame = JFrame().apply {
                        isUndecorated = true
                        contentPane.add(RightClickPanel)
                        defaultCloseOperation = JFrame.DO_NOTHING_ON_CLOSE
                        // bug
                        setBounds(mousePositionX!! + 280, mousePositionY!! + 100, 100, 300)
                        isAlwaysOnTop = true
                        isVisible = true
                        // 失去焦点则关闭
                        addWindowFocusListener(object : WindowAdapter() {
                            override fun windowLostFocus(e: WindowEvent?) {
                                dispose()
                            }
                        })
                    }
                }
            }
        })
    }

}

class EditorScrollPane(textArea: EditorArea) : JScrollPane(textArea) {

    init {
        horizontalScrollBarPolicy = HORIZONTAL_SCROLLBAR_NEVER
    }
}

// bug
object RightClickPanel : JPanel(), MenuElement by JMenuItem() {
    init {
        add(JMenuItem("Cut"))
        add(JMenuItem("Copy"))
        add(JMenuItem("Paste"))
        add(JMenuItem("Undo"))
        add(JMenuItem("Redo"))
    }
}