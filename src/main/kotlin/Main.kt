package me.cdh

import com.formdev.flatlaf.themes.FlatMacDarkLaf
import javax.swing.SwingUtilities

object Main {
    @JvmStatic
    fun main(args: Array<String>) = SwingUtilities.invokeLater {
        FlatMacDarkLaf.setup()
        displayFrame
    }
}