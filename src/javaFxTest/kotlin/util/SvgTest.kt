package util

import javafx.embed.swing.SwingNode
import org.apache.batik.anim.dom.SVGDOMImplementation
import org.apache.batik.swing.JSVGCanvas
import org.w3c.dom.svg.SVGDocument
import tornadofx.View
import tornadofx.button
import tornadofx.vbox
import java.io.StringWriter
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import kotlin.test.Test


/**
 * Created by ZengTian on 9/23/2019.
 */
class SvgTest {
    @Test
    fun test() {
        val svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI
        val impl = SVGDOMImplementation.getDOMImplementation()
        val doc = impl.createDocument(svgNS, "svg", null)
        val svgRoot = doc.documentElement

// Set the width and height attributes on the root 'svg' element.
        svgRoot.setAttributeNS(null, "width", "400")
        svgRoot.setAttributeNS(null, "height", "450")

// Create the rectangle.
        val rectangle = doc.createElementNS(svgNS, "rect")
        rectangle.setAttributeNS(null, "x", "10")
        rectangle.setAttributeNS(null, "y", "20")
        rectangle.setAttributeNS(null, "width", "100")
        rectangle.setAttributeNS(null, "height", "50")
        rectangle.setAttributeNS(null, "fill", "red")

// Attach the rectangle to the root 'svg' element.
        svgRoot.appendChild(rectangle)
        val svgCanvas = JSVGCanvas()

    }

    @Test
    fun name() {
//        val shape = FillShape(
//            path = GraphicsPath().apply {
//                moveTo(0, 0)
//                lineTo(100, 100)
//                lineTo(0, 100)
//                close()
//            },
//            clip = null,
//            //paint = Context2d.Color(Colors.GREEN),
//            paint = Context2d.BitmapPaint(Bitmap32(100, 100, Colors.RED, premultiplied = false), Matrix()),
//            transform = Matrix()
//        )
//        assertEquals(
//            """<svg width="100px" height="100px" viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink"><defs><pattern id="def0" patternUnits="userSpaceOnUse" width="100" height="100" patternTransform="scale(1, 1)"><image xlink:href="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGQAAABkCAYAAABw4pVUAAAA/UlEQVR42u3RoQ0AMBDEsNt/6e8YDTAwj5TddnTsdwCGpBkSY0iMITGGxBgSY0iMITGGxBgSY0iMITGGxBgSY0iMITGGxBgSY0iMITGGxBgSY0iMITGGxBgSY0iMITGGxBgSY0iMITGGxBgSY0iMITGGxBgSY0iMITGGxBgSY0iMITGGxBgSY0iMITGGxBgSY0iMITGGxBgSY0iMITGGxBgSY0iMITGGxBgSY0iMITGGxBgSY0iMITGGxBgSY0iMITGGxBgSY0iMITGGxBgSY0iMITGGxBgSY0iMITGGxBgSY0iMITGGxBgSY0iMITGGxBgSY0iMITGGxBgS8wBKb9ZkYYEq8QAAAABJRU5ErkJggg==" width="100" height="100"/></pattern></defs><g transform="scale(1, 1)"><path d="M0 0L100 100L0 100Z" fill="url(#def0)"/></g></svg>""",
//            shape.toSvg().outerXml
//        )
    }
}

class SvgTestView : View() {
    override val root = vbox {
        button("hello")
        add(SwingNode().apply {
            val svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI
            val impl = SVGDOMImplementation.getDOMImplementation()
            val doc = impl.createDocument(svgNS, "svg", null)
            val svgRoot = doc.documentElement

// Set the width and height attributes on the root 'svg' element.
            svgRoot.setAttributeNS(null, "width", "400")
            svgRoot.setAttributeNS(null, "height", "450")

// Create the rectangle.
            val rectangle = doc.createElementNS(svgNS, "rect")
            rectangle.setAttributeNS(null, "x", "10")
            rectangle.setAttributeNS(null, "y", "20")
            rectangle.setAttributeNS(null, "width", "100")
            rectangle.setAttributeNS(null, "height", "50")
            rectangle.setAttributeNS(null, "fill", "red")

// Attach the rectangle to the root 'svg' element.
            svgRoot.appendChild(rectangle)
            val svgCanvas = JSVGCanvas()
            svgCanvas.svgDocument = doc as SVGDocument?
            content = svgCanvas
            val tf = TransformerFactory.newInstance()
            val transformer = tf.newTransformer()
            val writer = StringWriter()
            transformer.transform(DOMSource(doc), StreamResult(writer))
            val xmlString = writer.buffer.toString()
            println(xmlString)                      //Print to console or logs
        })
    }
}


