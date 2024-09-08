package me.cdh

import java.awt.BorderLayout
import java.awt.Font
import java.util.Timer
import java.util.TimerTask
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.border.EtchedBorder

fun savedLabelPopup() {
    val timer = Timer()
    val frame = JFrame().apply {
        setSize(100, 40)
        isUndecorated = true
        isAlwaysOnTop = true
        isResizable = false
        layout = BorderLayout()
        val panel = JPanel()
        panel.border = EtchedBorder()
        panel.add(
            JLabel("Saved").apply {
                font = Font("Cascadia Mono", Font.PLAIN, 20)
            }, BorderLayout.CENTER
        )
        add(panel)
        isVisible = true
        setLocationRelativeTo(null)
    }
    val task = object : TimerTask() {
        override fun run() {
            frame.dispose()
        }
    }
    timer.schedule(task, 1000L)
}