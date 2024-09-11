package me.cdh

import java.awt.Color
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import javax.swing.JButton
import javax.swing.JTextField


object AffiliateComponent {
    val searchArea = JTextField().apply {
        addFocusListener(object : FocusAdapter() {
            override fun focusGained(e: FocusEvent?) {
                font = searchFont
                background = Color.GRAY
                text = "Search"
            }
        })
    }
    val replaceArea = JTextField().apply {
        addFocusListener(object : FocusAdapter() {
            override fun focusGained(e: FocusEvent?) {
                font = replaceFont
                background = Color.GRAY
                text = "Replace"
            }
        })
    }
    val arrow = JButton(">")
    val previousBtn = JButton("previous")
    val nextBtn = JButton("next")
    val replaceBtn = JButton("Replace")
    val replaceAllBtn = JButton("Replace All")

}