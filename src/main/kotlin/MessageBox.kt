package me.cdh

import java.awt.BorderLayout
import java.awt.GraphicsEnvironment
import java.util.Timer
import java.util.TimerTask
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.border.EtchedBorder


// popup save label after save event
fun labelPopup(message: String) {
    val messageLabel = JLabel(message)
    messageLabel.font = messageBoxFont
    val windowBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().maximumWindowBounds

    val frame = JFrame().apply {
//        setSize(100, 40)
        isUndecorated = true
        isAlwaysOnTop = true
        isResizable = false
        layout = BorderLayout()
        val panel = JPanel()
        panel.border = EtchedBorder()
        panel.add(messageLabel, BorderLayout.CENTER)
        val x = 2 * windowBounds.width / 5
        val y = 3 * windowBounds.height / 4
        setSize(300, 40)
        add(panel)
        isVisible = true
        setLocation(x, y)
//        setLocationRelativeTo(null)
    }
    val task = object : TimerTask() {
        override fun run() {
            frame.dispose()
        }
    }
    Timer().schedule(task, 2000L)
}