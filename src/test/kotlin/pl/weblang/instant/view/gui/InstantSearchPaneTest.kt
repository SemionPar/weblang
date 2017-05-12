package pl.weblang.background.gui.paneg

import com.vlsolutions.swing.docking.DockKey
import com.vlsolutions.swing.docking.Dockable
import org.amshove.kluent.`should be`
import org.junit.Test
import pl.weblang.`should be ignoring whitespace`
import pl.weblang.instant.view.gui.InstantSearchPane
import java.awt.Component
import java.awt.Dimension
import javax.swing.JFrame
import javax.swing.JScrollPane


class InstantSearchPaneTest {

    @Test
    fun shouldShowText() {
        // given
        val instantSearchPane = createInstantSearchPane()
        val html = "<html><head></head><body><p>text</p></body></html>"

        // when
        instantSearchPane.displayHtml(html)

        // then
        instantSearchPane.displayedHtml `should be ignoring whitespace` html
    }

    @Test
    fun shouldShowProgressAnimation() {
        // given
        val instantSearchPane = createInstantSearchPane()

        // when
        instantSearchPane.showProgressAnimation()

        // then
        Thread.sleep(2000)
        instantSearchPane.inProgress `should be` true
    }

    @Test
    fun should_Start_ShowProgress_ShowText_ShowProgress() {
        // given
        val instantSearchPane = createInstantSearchPane()
        val html = "<html><head></head><body><p>text</p></body></html>"
        val initialHtml = "<html><head></head><body>Use Ctrl+G to run a search on selected text</body></html>"

        // then
        Thread.sleep(2000)
        instantSearchPane.displayedHtml `should be ignoring whitespace` initialHtml

        instantSearchPane.showProgressAnimation()
        Thread.sleep(2000)
        instantSearchPane.inProgress `should be` true

        instantSearchPane.displayHtml(html)
        Thread.sleep(2000)
        instantSearchPane.displayedHtml `should be ignoring whitespace` html

        instantSearchPane.showProgressAnimation()
        Thread.sleep(2000)
        instantSearchPane.inProgress `should be` true
    }

    private fun createInstantSearchPane(): InstantSearchPane {
        val instantSearchPane = InstantSearchPane(omegatPane = TestOmegatPane())
        JFrame("TestFrame").apply {
            contentPane = instantSearchPane.parentPane
            defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            minimumSize = Dimension(600, 600)
            isVisible = true
            pack()
        }
        return instantSearchPane
    }

}

private class TestOmegatPane : JScrollPane(), Dockable {
    override fun getComponent(): Component {
        TODO("not implemented")
    }

    override fun getDockKey(): DockKey {
        TODO("not implemented")
    }
}
