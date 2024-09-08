package me.cdh

import javax.swing.JScrollPane
import javax.swing.JTextArea

class EditorArea : JTextArea() {
    init {
        lineWrap = true
        tabSize = 4
        wrapStyleWord = true
        font = contentFont
    }
}

class EditorScrollPane(textArea: EditorArea) : JScrollPane(textArea) {

    init {
        horizontalScrollBarPolicy = HORIZONTAL_SCROLLBAR_NEVER
    }
}