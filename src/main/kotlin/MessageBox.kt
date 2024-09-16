package me.cdh

import java.awt.BorderLayout
import java.awt.GraphicsEnvironment
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.border.EtchedBorder
import kotlin.concurrent.timer
import kotlin.system.exitProcess

// 弹出保存标签当保存成功的时候
fun labelPopup(message: String) {
    val messageLabel = JLabel(message)
    messageLabel.font = messageBoxFont
    val windowBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().maximumWindowBounds

    val frame = JFrame().apply {
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
    }
    timer(initialDelay = 2000L, period = 1L, action = { frame.dispose() })
}

// 确认退出
fun exitOrNot() {
    for (i in 0..<tabPane.tabCount) {
        if (bufferList[i].text.isNotBlank()) {
            val result = JOptionPane.showConfirmDialog(
                null,
                "Are you sure to exit?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION
            )
            if (result == JOptionPane.YES_OPTION) {
                exitProcess(0)
            }
        } else exitProcess(0)
    }
}