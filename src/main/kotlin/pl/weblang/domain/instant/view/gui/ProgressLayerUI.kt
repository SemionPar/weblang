package pl.weblang.domain.instant.view.gui

import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.beans.PropertyChangeEvent
import javax.swing.JComponent
import javax.swing.JLayer
import javax.swing.JTextPane
import javax.swing.Timer
import javax.swing.plaf.LayerUI

class ProgressLayerUI : LayerUI<JTextPane>(), ActionListener {
    private var mIsRunning: Boolean = false
    private var mIsFadingOut: Boolean = false
    private var mTimer: Timer? = null

    private var mAngle: Int = 0
    private var mFadeCount: Int = 0
    private val mFadeLimit = 15

    val isVisible: Boolean get() = !mIsFadingOut

    override fun paint(g: Graphics?, c: JComponent) {
        val w = c.width
        val h = c.height

        // Paint the view.
        super.paint(g, c)

        if (!mIsRunning) {
            return
        }

        val g2 = g?.create() as Graphics2D

        val fade = mFadeCount.toFloat() / mFadeLimit.toFloat()
        // Gray it out.
        val urComposite = g2.composite
        g2.composite = AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, .5f * fade)
        g2.fillRect(0, 0, w, h)
        g2.composite = urComposite

        // Paint the wait indicator.
        val s = Math.min(w, h) / 5
        val cx = w / 2
        val cy = h / 2
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON)
        g2.stroke = BasicStroke((s / 4).toFloat(), BasicStroke.CAP_ROUND,
                                BasicStroke.JOIN_ROUND)
        g2.paint = Color.white
        g2.rotate(Math.PI * mAngle / 180, cx.toDouble(), cy.toDouble())
        for (i in 0..11) {
            val scale = (11.0f - i.toFloat()) / 11.0f
            g2.drawLine(cx + s, cy, cx + s * 2, cy)
            g2.rotate(-Math.PI / 6, cx.toDouble(), cy.toDouble())
            g2.composite = AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, scale * fade)
        }

        g2.dispose()
    }

    override fun actionPerformed(e: ActionEvent) {
        if (mIsRunning) {
            firePropertyChange("tick", 0, 1)
            mAngle += 3
            if (mAngle >= 360) {
                mAngle = 0
            }
            if (mIsFadingOut) {
                if (--mFadeCount == 0) {
                    mIsRunning = false
                    mTimer!!.stop()
                }
            } else if (mFadeCount < mFadeLimit) {
                mFadeCount++
            }
        }
    }

    fun start() {
        if (mIsRunning) {
            return
        }

        // Run a thread for animation.
        mIsRunning = true
        mIsFadingOut = false
        mFadeCount = 0
        val fps = 24
        val tick = 1000 / fps
        mTimer = Timer(tick, this)
        mTimer!!.start()
    }

    fun stop() {
        mIsFadingOut = true
    }

    override fun applyPropertyChange(propertyChangeEvent: PropertyChangeEvent, jLayer: JLayer<out JTextPane>) {
        if ("tick" == propertyChangeEvent.propertyName) {
            jLayer.repaint()
        }
    }
}
