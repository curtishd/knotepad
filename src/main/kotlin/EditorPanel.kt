package me.cdh

import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JMenuItem
import javax.swing.JPopupMenu
import javax.swing.JTextArea

class EditorArea() : JTextArea() {
    val cut = JMenuItem("Cut").apply {
        addActionListener {
            cut()
        }
    }
    val copy = JMenuItem("Copy").apply {
        addActionListener {
            copy()
        }
    }
    val paste = JMenuItem("Paste").apply {
        addActionListener {
            paste()
        }
    }
    val rightClickMenu = JPopupMenu().apply {
        add(cut)
        addSeparator()
        add(copy)
        add(paste)
    }

    init {
//        lineWrap = true
        tabSize = 4
//        wrapStyleWord = true
        font = contentFont
        addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                if (e!!.button == MouseEvent.BUTTON3) {
                    showPopupMenu(e)
                }
                if (e.button == MouseEvent.BUTTON1) {
                    val line = lineCount.toString()
                    val column = columns.toString()
                    lineAndColumn.text = "$line:$column"
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