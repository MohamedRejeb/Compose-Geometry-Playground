package com.mocoding.geometryapp.common.drawing.geo

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.dp
import com.mocoding.geometryapp.common.drawText
import com.mocoding.geometryapp.common.utils.calcDistance
import com.mocoding.geometryapp.common.utils.lineTo
import com.mocoding.geometryapp.common.utils.moveTo
import com.mocoding.geometryapp.common.utils.toRadian
import kotlin.math.abs
import kotlin.math.sin

data class ProtractorDrawing(
    override val position: Offset = Offset.Zero,
    override val rotation: Float = 0f
): GeoDrawing {

    override val color: Color = Color.Black
    override fun drawOn(drawScope: DrawScope) {
        val size = getSize(canvasSize = drawScope.size)

        val topLeft = getTopLeft(
            canvasSize = drawScope.size,
            toolSize = size
        )

        val radius = size.width/2f
        val center = Offset(topLeft.x + size.width / 2f, topLeft.y + size.height)

        drawScope.drawCircle(
            color = Color.Black,
            radius = with(drawScope) { 4.dp.toPx() },
            center = center,
            style = Fill
        )

        drawScope.drawArc(
            color = Color.Blue.copy(0.4f),
            -180f,
            180f,
            useCenter = false,
            topLeft = topLeft,
            size = Size(size.width, size.height * 2f),
            style = Fill
        )

        val paddingHorizontal = size.width / 11f
        val paddingTop = 6f
        val unitsSize = size.width - paddingHorizontal * 2f
        val lineHeight = size.height * 1f/10f

        val numberOfUnits = 18

        val unitSize = unitsSize / numberOfUnits
        val unitLineWidth = unitSize / 12f
        val unitFontSize = unitSize * 2f/4f

        drawScope.drawIntoCanvas { canvas ->
            canvas.rotate(rotation, topLeft.x, topLeft.y)
        }

        repeat(numberOfUnits + 1) { unitNumber ->
            val angle = unitNumber * 10f

            val x = abs(radius - center.x)
            val y = abs(center.y)

            if (unitNumber > 0)
                drawScope.drawIntoCanvas { canvas ->
                    canvas.rotate(10f, center.x, center.y)
                }

            val startOffset = Offset(x, y)
            val endOffset = Offset(x + lineHeight, y)

            val unitPath = Path()

            unitPath.moveTo(
                startOffset
            )
            unitPath.lineTo(
                endOffset
            )

            drawScope.drawPath(
                path = unitPath,
                color = Color.Black,
                style = Stroke(
                    width = unitLineWidth,
                    cap = StrokeCap.Round
                )
            )

            drawScope.drawIntoCanvas { canvas ->
                canvas.rotate(-90f, endOffset.x, endOffset.y)
            }

            val unitAngle = unitNumber * 10

            drawScope.drawText(
                text = unitAngle.toString(),
                topLeft = Offset(
                    (endOffset + Offset(-unitFontSize/4f*unitAngle.toString().length, 0f)).x,
                    (endOffset + Offset(-unitFontSize/4f/unitAngle.toString().length, unitLineWidth + unitFontSize)).y
                ),
                color = Color.Black,
                fontSize = unitFontSize
            )

            drawScope.drawIntoCanvas { canvas ->
                canvas.rotate(90f, endOffset.x, endOffset.y)
            }
        }

        drawScope.drawIntoCanvas { canvas ->
            canvas.rotate(180f, center.x, center.y)
            canvas.rotate(-rotation, topLeft.x, topLeft.y)
        }
    }

    override fun moveBy(offset: Offset): ProtractorDrawing {
        return copy(position = position + offset)
    }

    override fun rotateBy(angle: Float): ProtractorDrawing {
        return copy(rotation = this.rotation + angle)
    }

    override fun isHovered(canvasSize: Size, hoverOffset: Offset): Boolean {
        val size = getSize(canvasSize)

        val topLeft = getTopLeft(canvasSize, size)
        val center = Offset(topLeft.x + size.width / 2f, topLeft.y + size.height)

        val distanceFromCenter = calcDistance(hoverOffset, center)

        if (distanceFromCenter > size.height) return false

        if (hoverOffset.x < topLeft.x
            || hoverOffset.y < topLeft.y) return false

        if (hoverOffset.x > topLeft.x + size.width
            || hoverOffset.y > topLeft.y + size.height) return false

        return true
    }

    private fun getSize(canvasSize: Size): Size {
        return Size(canvasSize.width / 2f, canvasSize.width / 4f)
    }

    private fun getTopLeft(canvasSize: Size, toolSize: Size): Offset {
        val x = (canvasSize.width - toolSize.width) / 2f
        val y = (canvasSize.height - toolSize.height) / 2f

        return Offset(x, y) + position
    }

}