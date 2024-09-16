package me.cdh

import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JMenuItem
import javax.swing.JPopupMenu
import javax.swing.JTextArea

class EditorArea() : JTextArea() {
    val cut = JMenuItem("Cut").apply { addActionListener { cut() } }
    val copy = JMenuItem("Copy").apply { addActionListener { copy() } }
    val paste = JMenuItem("Paste").apply { addActionListener { paste() } }
    val rightClickMenu = JPopupMenu().apply {
        add(cut)
        addSeparator()
        add(copy)
        add(paste)
    }

    init {
        // 自动换行
//        lineWrap = true
//        wrapStyleWord = true
        tabSize = 4
        font = contentFont
        addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                // 鼠标右键行为
                if (e!!.button == MouseEvent.BUTTON3) {
                    showPopupMenu(e)
                }
                // 鼠标左键行为
                if (e.button == MouseEvent.BUTTON1) {
                    lineAndColumn.text = "Line: $lineCount"
                }
            }

            override fun mousePressed(e: MouseEvent?) {
                showPopupMenu(e!!)
            }

            override fun mouseReleased(e: MouseEvent?) {
                showPopupMenu(e!!)
            }
        })
    }

    fun showPopupMenu(e: MouseEvent) = if (e.isPopupTrigger) rightClickMenu.show(e.component, e.x, e.y) else null
}