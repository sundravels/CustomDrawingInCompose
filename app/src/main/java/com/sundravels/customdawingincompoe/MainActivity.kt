package com.sundravels.customdawingincompoe

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sundravels.customdawingincompoe.ui.theme.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CustomDawingInCompoeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    SleepCircularProgressBar()
                }
            }
        }
    }
}

@Composable
fun SleepCircularProgressBar() {
    val sweepAngle = remember { Animatable(0f) }
    LaunchedEffect(sweepAngle) {
        sweepAngle.animateTo(
            targetValue = 270F,
            animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
        )
    }
    Canvas(
        modifier = Modifier
            .size(500.dp)
            .padding(horizontal = 15.dp)
    ) {

        val timeArray = arrayOf(3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 1, 2)
        val width = size.width
        val height = size.height

        val circleRadius = (size.minDimension / 2) - 100f
        val thickness = 60f

        val circleCenter = Offset(x = width / 2f, y = height / 2f)



        //background circle
        drawCircle(
            style = Stroke(thickness),
            color = BackgroundCircle,
            radius = (circleRadius + thickness),
            center = circleCenter
        )

        val circleSize = circleRadius + thickness
        val littleLineLength = circleSize * 0.1f
        val largeLineLength = circleSize * 0.2f

        //progress arc
        drawArc(
            color = Purple700,
            startAngle = 0f,
            sweepAngle = sweepAngle.value,
            useCenter = false,
            topLeft = Offset(
                40f,
                (circleCenter.y - (size.minDimension / 2) + 40f)
            ),
            size = Size(size.minDimension - 80f, size.minDimension - 80f),
            style = Stroke(thickness, cap = StrokeCap.Round)
        )

        //center circle
        drawCircle(
            color = Purple700,
            radius = 50f,
            center = circleCenter,
            style = Stroke(join = StrokeJoin.Miter, width = 10f)
        )


        for (i in 0 until 60) {
            val angleInDegrees = (i * 360f) / 60f
            val angleInRad = angleInDegrees * PI / 180f + PI / 2f
            val lineLength = if (i % 5 == 0) largeLineLength else littleLineLength
            val lineThickness = if (i % 5 == 0) 5f else 2f

            val start = Offset(
                x = (circleRadius * cos(angleInRad) + circleCenter.x).toFloat(),
                y = (circleRadius * sin(angleInRad) + circleCenter.y).toFloat()
            )
            val end = Offset(
                x = (circleRadius * cos(angleInRad) + circleCenter.x).toFloat(),
                y = (circleRadius * sin(angleInRad) + lineLength + circleCenter.y).toFloat()
            )

            //clock horns
            rotate(angleInDegrees + 180, pivot = start) {
                drawLine(
                    color = Purple500,
                    start = start,
                    end = end,
                    strokeWidth = lineThickness.dp.toPx()
                )
            }

            //clock numbers
            if (i % 5 == 0) {
                drawContext.canvas.nativeCanvas.apply {
                    drawText("${timeArray[i / 5]}",
                        (circleCenter.x + (cos(angleInDegrees * PI / 180f) * (circleSize - 200f))).toFloat(),
                        (circleCenter.y + 20f + +(sin(angleInDegrees * PI / 180f) * (circleSize - 200f))).toFloat(),
                        Paint().apply {
                            textSize = 50f
                            color = Color.parseColor("#6200EE")
                            textAlign = Paint.Align.CENTER
                        }
                    )
                }

            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CustomDawingInCompoeTheme {
        SleepCircularProgressBar()
    }
}