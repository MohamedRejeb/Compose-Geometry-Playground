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
import com.mocoding.geometryapp.common.helper.LineEquation
import com.mocoding.geometryapp.common.utils.lineTo
import com.mocoding.geometryapp.common.utils.moveTo
import com.mocoding.geometryapp.common.utils.rotateBy
import com.mocoding.geometryapp.common.utils.shrinkAngle
import org.jetbrains.skia.Font
import kotlin.math.abs

data class RulerDrawing(
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

        val cornerRadius = with(drawScope) { 6.dp.toPx() }

        val path = Path()
        path.moveTo(topLeft)
        path.lineTo(topLeft.copy(x = topLeft.x + size.width).rotateBy(rotation, topLeft))
        path.lineTo(topLeft.copy(x = topLeft.x + size.width, y = topLeft.y + size.height).rotateBy(rotation, topLeft))
        path.lineTo(topLeft.copy(y = topLeft.y + size.height).rotateBy(rotation, topLeft))
        path.close()

        drawScope.drawPath(
            path = path,
            color = Color.Blue.copy(alpha = 0.4f),
            style = Fill
        )

        val paddingHorizontal = size.width / 11f
        val paddingTop = 6f
        val unitsSize = size.width - paddingHorizontal * 2f
        val lineHeight = size.height * 1f/6f

        val numberOfUnits = 10

        val unitSize = unitsSize / numberOfUnits
        val unitLineWidth = unitSize / 12f
        val unitFontSize = unitSize / 3f

        val unitsPath = Path()

        drawScope.drawIntoCanvas { canvas ->
            canvas.rotate(rotation, topLeft.x, topLeft.y)
        }

        repeat(numberOfUnits + 1) { unitNumber ->
            val currentUnitX =
                if (unitNumber == 0) 0f
                else unitSize * unitNumber

            val startOffset = (Offset(paddingHorizontal + currentUnitX, paddingTop) + topLeft)
            val endOffset = (Offset(paddingHorizontal + currentUnitX, paddingTop + lineHeight) + topLeft)
            unitsPath.moveTo(
                startOffset
            )
            unitsPath.lineTo(
                endOffset
            )

            val topLeft = Offset(
                (endOffset + Offset(-unitFontSize/4f*unitNumber.toString().length, 0f)).x,
                (endOffset + Offset(-unitFontSize/4f*unitNumber.toString().length, unitLineWidth + unitFontSize)).y
            )

            drawScope.drawText(
                text = unitNumber.toString(),
                topLeft = topLeft,
                color = Color.Black,
                fontSize = unitFontSize
            )
        }

        drawScope.drawPath(
            path = unitsPath,
            color = Color.Black,
            style = Stroke(
                width = unitLineWidth,
                cap = StrokeCap.Round
            )
        )

        drawScope.drawIntoCanvas { canvas ->
            canvas.rotate(-rotation, topLeft.x, topLeft.y)
        }

//        drawScope.drawRoundRect(
//            color = Color.Blue.copy(alpha = 0.4f),
//            size = size,
//            topLeft = topLeft,
//            cornerRadius = CornerRadius(cornerRadius, cornerRadius),
//            style = Fill
//        )
    }

    override fun moveBy(offset: Offset): RulerDrawing {
        return copy(position = position + offset)
    }

    override fun rotateBy(angle: Float): GeoDrawing {
        return copy(rotation = (rotation + angle).shrinkAngle())
    }

    override fun isHovered(canvasSize: Size, hoverOffset: Offset): Boolean {
        val rulerSize = getSize(canvasSize = canvasSize)

        val topLeft = getTopLeft(
            canvasSize = canvasSize,
            toolSize = rulerSize
        )

        val rotatedHoverOffset = hoverOffset.rotateBy(-rotation, topLeft)

        if (rotatedHoverOffset.x < topLeft.x
            || rotatedHoverOffset.y < topLeft.y) return false

        if (rotatedHoverOffset.x > topLeft.x + rulerSize.width
            || rotatedHoverOffset.y > topLeft.y + rulerSize.height) return false

        return true
    }

    fun isRotateArea(canvasSize: Size, hoverOffset: Offset): Boolean {
        val size = getSize(canvasSize = canvasSize)

        val topLeft = getTopLeft(
            canvasSize = canvasSize,
            toolSize = size
        )

        val rotatedHoverOffset = hoverOffset.rotateBy(-rotation, topLeft)

        if (rotatedHoverOffset.x < (topLeft.x + size.width * 4f/5f)) return false

        return true
    }

    fun isMoveArea(canvasSize: Size, hoverOffset: Offset): Boolean {
        return !isRotateArea(canvasSize, hoverOffset)
    }

    fun getSize(canvasSize: Size): Size {
        val width = canvasSize.width * 2f/3f
        val height = width / 6f

        return Size(width, height)
    }

    fun getTopLeft(canvasSize: Size, toolSize: Size): Offset {
        val x = (canvasSize.width - toolSize.width) / 2f
        val y = (canvasSize.height - toolSize.height) / 2f

        return Offset(x, y) + position
    }

    fun isInRulerArea(
        canvasSize: Size,
        offset: Offset
    ): Boolean {
        val size = getSize(canvasSize)
        val topLeft = getTopLeft(canvasSize, size)

        val rotatedOffset = offset.rotateBy(-rotation, topLeft)

        if (topLeft.x - rotatedOffset.x > 40
            || rotatedOffset.x - (topLeft.x + size.width) > 40) return false

        if (topLeft.y - rotatedOffset.y > 40
            || rotatedOffset.y - (topLeft.y + size.height) > 40) return false

        return true
    }

    fun getLineEquation(
        canvasSize: Size,
        offset: Offset
    ): LineEquation {
        val size = getSize(canvasSize)
        val topLeft = getTopLeft(canvasSize, size)

        val rotatedOffset = offset.rotateBy(-rotation, topLeft)

        val botLeft = topLeft.copy(y = topLeft.y + size.height)

        return if (abs(topLeft.y - rotatedOffset.y) < abs(rotatedOffset.y - (topLeft.y + size.height)))
            LineEquation.from(
                point1 = topLeft,
                point2 = (topLeft + Offset(size.width, 0f)).rotateBy(rotation, topLeft)
            )
        else
            LineEquation.from(
                point1 = botLeft.rotateBy(rotation, topLeft),
                point2 = (botLeft + Offset(size.width, 0f)).rotateBy(rotation, topLeft)
            )
    }

}