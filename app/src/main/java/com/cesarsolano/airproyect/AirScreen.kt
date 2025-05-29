package com.cesarsolano.airproyect

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.sin
import kotlin.math.cos

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PredictionScreen() {
    var pm10 by remember { mutableStateOf("") }
    var pm25 by remember { mutableStateOf("") }
    var no2 by remember { mutableStateOf("") }
    var o3 by remember { mutableStateOf("") }
    var resultado by remember { mutableStateOf("") }
    var recomendacion by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    // Función para resetear todos los campos
    fun resetFields() {
        pm10 = ""
        pm25 = ""
        no2 = ""
        o3 = ""
        resultado = ""
        recomendacion = ""
    }

    // Gradiente de fondo dinámico con colores más modernos
    val gradientColors = listOf(
        Color(0xFF667eea), // Azul suave
        Color(0xFF764ba2), // Púrpura elegante
        Color(0xFFf093fb), // Rosa suave
        Color(0xFFf5576c), // Coral vibrante
        Color(0xFF4facfe)  // Azul cielo
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = gradientColors,
                    radius = 1500f
                )
            )
    ) {
        // Fondo animado con partículas flotantes mejoradas
        EnhancedAnimatedBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Título principal con animación mejorada
            EnhancedAnimatedTitle()

            Spacer(modifier = Modifier.height(16.dp))

            // Tarjeta principal con fondo blanco
            WhiteCard {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        text = "🌬️ Datos de Contaminantes",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF1F2937),
                        fontWeight = FontWeight.Bold
                    )

                    // Campos de entrada mejorados
                    EnhancedTextField(
                        value = pm10,
                        onValueChange = { pm10 = it },
                        label = "PM10",
                        unit = "μg/m³",
                        color = Color(0xFF8B5CF6)
                    )

                    EnhancedTextField(
                        value = pm25,
                        onValueChange = { pm25 = it },
                        label = "PM2.5",
                        unit = "μg/m³",
                        color = Color(0xFF06B6D4)
                    )

                    EnhancedTextField(
                        value = no2,
                        onValueChange = { no2 = it },
                        label = "NO2",
                        unit = "μg/m³",
                        color = Color(0xFF10B981)
                    )

                    EnhancedTextField(
                        value = o3,
                        onValueChange = { o3 = it },
                        label = "O3",
                        unit = "μg/m³",
                        color = Color(0xFFF59E0B)
                    )

                    // Botones mejorados
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Botón Reset
                        EnhancedButton(
                            onClick = { resetFields() },
                            modifier = Modifier.weight(1f),
                            colors = listOf(Color(0xFFEF4444), Color(0xFFDC2626)),
                            text = "🔄 Reset"
                        )

                        // Botón Predecir
                        EnhancedButton(
                            onClick = {
                                try {
                                    if (pm10.isBlank() || pm25.isBlank() || no2.isBlank() || o3.isBlank()) {
                                        resultado = "Por favor completa todos los campos."
                                        recomendacion = ""
                                        return@EnhancedButton
                                    }

                                    isLoading = true
                                    val request = PredictionRequest(
                                        PM10 = pm10.toFloat(),
                                        PM2_5 = pm25.toFloat(),
                                        NO2 = no2.toFloat(),
                                        O3 = o3.toFloat()
                                    )

                                    RetrofitClient.apiService.getPrediction(request)
                                        .enqueue(object : Callback<PredictionResponse> {
                                            override fun onResponse(
                                                call: Call<PredictionResponse>,
                                                response: Response<PredictionResponse>
                                            ) {
                                                isLoading = false
                                                if (response.isSuccessful) {
                                                    val calidad = response.body()?.prediccion ?: "Desconocida"
                                                    resultado = "La calidad del aire es $calidad."

                                                    recomendacion = when (calidad.lowercase()) {
                                                        "buena" -> "🌿 Puedes realizar actividades al aire libre sin preocupación."
                                                        "moderada" -> "⚠️ Evita esfuerzos físicos si tienes problemas respiratorios."
                                                        "dañina" -> "🏠 Permanece en casa si es posible y evita actividades al aire libre."
                                                        else -> "❓ No se pudo determinar una recomendación precisa."
                                                    }
                                                } else {
                                                    resultado = "Error: ${response.code()}"
                                                    recomendacion = ""
                                                }
                                            }

                                            override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
                                                isLoading = false
                                                resultado = "Fallo: ${t.message}"
                                                recomendacion = ""
                                            }
                                        })
                                } catch (e: Exception) {
                                    isLoading = false
                                    resultado = "Error al convertir datos"
                                    recomendacion = ""
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = listOf(Color(0xFF10B981), Color(0xFF059669)),
                            text = if (isLoading) "⏳ Analizando..." else "Predecir",
                            isLoading = isLoading
                        )
                    }
                }
            }

            // Tarjeta de resultados mejorada
            AnimatedVisibility(
                visible = resultado.isNotEmpty(),
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ) + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                EnhancedResultCard(resultado = resultado, recomendacion = recomendacion)
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun EnhancedAnimatedTitle() {
    val infiniteTransition = rememberInfiniteTransition(label = "title")

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ), label = "scale"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        ), label = "glow"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.scale(scale)
    ) {
        Box {
            // Efecto de resplandor
            Text(
                text = "🌬️ Calidad del Aire",
                fontSize = 36.sp,
                fontWeight = FontWeight.Black,
                color = Color.White.copy(alpha = glowAlpha),
                textAlign = TextAlign.Center,
                modifier = Modifier.blur(8.dp)
            )
            // Texto principal
            Text(
                text = "🌬️ Calidad del Aire",
                fontSize = 36.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

        Text(
            text = "Predicción inteligente en tiempo real",
            fontSize = 16.sp,
            color = Color.White.copy(alpha = 0.9f),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun WhiteCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    unit: String,
    color: Color
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                "$label ($unit)",
                color = Color(0xFF6B7280)
            )
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = color,
            unfocusedBorderColor = Color(0xFFD1D5DB),
            focusedLabelColor = color,
            unfocusedLabelColor = Color(0xFF6B7280),
            cursorColor = color,
            focusedTextColor = Color(0xFF1F2937),
            unfocusedTextColor = Color(0xFF374151)
        )
    )
}

@Composable
fun EnhancedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: List<Color>,
    text: String,
    isLoading: Boolean = false
) {
    val infiniteTransition = rememberInfiniteTransition(label = "button")
    val shimmer by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Reverse
        ), label = "shimmer"
    )

    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(16.dp),
        enabled = !isLoading
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = colors.map { it.copy(alpha = 0.8f + shimmer * 0.2f) }
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(vertical = 4.dp, horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Text(
                        text,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Text(
                    text,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun EnhancedResultCard(resultado: String, recomendacion: String) {
    WhiteCard {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "📊 Resultado del Análisis",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
            )

            Text(
                text = resultado,
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF374151),
                fontWeight = FontWeight.Medium
            )

            if (recomendacion.isNotEmpty()) {
                HorizontalDivider(
                    color = Color(0xFFE5E7EB),
                    thickness = 1.dp
                )

                Text(
                    text = "💡 Recomendación Personalizada",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF59E0B)
                )

                Text(
                    text = recomendacion,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF374151),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun EnhancedAnimatedBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "background")

    // Partículas flotantes con movimiento más complejo
    val particles = remember {
        List(25) { index ->
            ParticleData(
                initialX = (index * 0.08f) % 1f,
                initialY = (index * 0.12f) % 1f,
                speed = 4000L + (index * 150L),
                size = 4f + (index % 4) * 3f,
                colorIndex = index % 5
            )
        }
    }

    val animatedParticles = particles.mapIndexed { index, particle ->
        val animatedY by infiniteTransition.animateFloat(
            initialValue = 1.2f,
            targetValue = -0.2f,
            animationSpec = infiniteRepeatable(
                animation = tween(particle.speed.toInt(), easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ), label = "particle_y_$index"
        )

        val animatedX by infiniteTransition.animateFloat(
            initialValue = -0.1f,
            targetValue = 0.1f,
            animationSpec = infiniteRepeatable(
                animation = tween(3000 + index * 100, easing = EaseInOutSine),
                repeatMode = RepeatMode.Reverse
            ), label = "particle_x_$index"
        )

        val rotation by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(8000 + index * 200, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ), label = "particle_rotation_$index"
        )

        AnimatedParticle(particle, animatedX, animatedY, rotation)
    }

    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        animatedParticles.forEach { animatedParticle ->
            val finalX = (animatedParticle.particle.initialX + animatedParticle.animatedX) * size.width
            val finalY = animatedParticle.particle.initialY * size.height + (animatedParticle.animatedY * size.height)

            val particleColors = listOf(
                Color(0xFF8B5CF6).copy(alpha = 0.2f),
                Color(0xFF06B6D4).copy(alpha = 0.2f),
                Color(0xFF10B981).copy(alpha = 0.2f),
                Color(0xFFF59E0B).copy(alpha = 0.2f),
                Color(0xFFEF4444).copy(alpha = 0.2f)
            )

            // Partículas circulares con efecto de resplandor
            drawCircle(
                color = particleColors[animatedParticle.particle.colorIndex],
                radius = animatedParticle.particle.size * 2,
                center = Offset(finalX, finalY)
            )

            drawCircle(
                color = particleColors[animatedParticle.particle.colorIndex].copy(alpha = 0.4f),
                radius = animatedParticle.particle.size,
                center = Offset(finalX, finalY)
            )

            // Líneas conectoras ocasionales para crear una red sutil
            if (animatedParticle.particle.colorIndex % 3 == 0) {
                val nextParticle = animatedParticles.getOrNull((animatedParticle.particle.colorIndex + 1) % animatedParticles.size)
                nextParticle?.let { next ->
                    val nextX = (next.particle.initialX + next.animatedX) * size.width
                    val nextY = next.particle.initialY * size.height + (next.animatedY * size.height)

                    val distance = kotlin.math.sqrt(
                        (finalX - nextX) * (finalX - nextX) + (finalY - nextY) * (finalY - nextY)
                    )

                    if (distance < 200) {
                        drawLine(
                            color = Color.White.copy(alpha = 0.1f),
                            start = Offset(finalX, finalY),
                            end = Offset(nextX, nextY),
                            strokeWidth = 1f
                        )
                    }
                }
            }
        }
    }
}

data class ParticleData(
    val initialX: Float,
    val initialY: Float,
    val speed: Long,
    val size: Float,
    val colorIndex: Int
)

data class AnimatedParticle(
    val particle: ParticleData,
    val animatedX: Float,
    val animatedY: Float,
    val rotation: Float
)