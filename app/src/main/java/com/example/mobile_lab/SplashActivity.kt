package com.example.mobile_lab

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.BounceInterpolator
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.mobile_lab.ui.theme.Mobile_labTheme
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Mobile_labTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SplashScreen(onAnimationFinish = {
                        // Po zakończeniu animacji, przejdź do MainActivity
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        finish() // Zamknij SplashActivity
                    })
                }
            }
        }
    }
}

@Composable
fun SplashScreen(onAnimationFinish: () -> Unit) {
    val context = LocalContext.current

    // Tło ekranu powitalnego
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        // Struktura z dwoma elementami - tekst i animacja
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Tekst wyśrodkowany idealnie w poziomie i przesunięty do góry
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 200.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Książka Barmańska",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center
                )
            }

            // Animowane kieliszki koktajlowe
            AndroidView(
                factory = { context ->
                    // Tworzenie widoków dla animacji
                    val cocktailGlass1 = ImageView(context).apply {
                        setImageResource(R.drawable.cocktail_glass)
                        visibility = View.VISIBLE
                        alpha = 0f
                        id = View.generateViewId()
                    }

                    val cocktailGlass2 = ImageView(context).apply {
                        setImageResource(R.drawable.cocktail_glass_2)
                        visibility = View.VISIBLE
                        alpha = 0f
                        id = View.generateViewId()
                    }

                    val container = android.widget.FrameLayout(context).apply {
                        addView(cocktailGlass1)
                        addView(cocktailGlass2)
                    }

                    // Wycentrowanie kontenera na ekranie
                    container.layoutParams = android.widget.FrameLayout.LayoutParams(
                        android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
                        android.widget.FrameLayout.LayoutParams.MATCH_PARENT
                    ).apply {
                        gravity = android.view.Gravity.CENTER
                    }

                    // Ustawienie rozmiarów i pozycji kieliszków
                    cocktailGlass1.layoutParams = android.widget.FrameLayout.LayoutParams(400, 400).apply {
                        gravity = android.view.Gravity.CENTER
                        leftMargin = -200 // Mniejszy margines w lewo
                    }

                    cocktailGlass2.layoutParams = android.widget.FrameLayout.LayoutParams(400, 400).apply {
                        gravity = android.view.Gravity.CENTER
                        rightMargin = -200 // Mniejszy margines w prawo
                    }

                    // Animacje dla pierwszego kieliszka
                    val fadeIn1 = ObjectAnimator.ofFloat(cocktailGlass1, View.ALPHA, 0f, 1f).apply {
                        duration = 800
                    }

                    val moveX1 = ObjectAnimator.ofFloat(cocktailGlass1, View.TRANSLATION_X, -200f, 0f).apply {
                        duration = 1000
                        interpolator = AccelerateDecelerateInterpolator()
                    }

                    val rotate1 = ObjectAnimator.ofFloat(cocktailGlass1, View.ROTATION, 0f, 360f).apply {
                        duration = 1500
                    }

                    // Animacje dla drugiego kieliszka
                    val fadeIn2 = ObjectAnimator.ofFloat(cocktailGlass2, View.ALPHA, 0f, 1f).apply {
                        duration = 800
                    }

                    val moveX2 = ObjectAnimator.ofFloat(cocktailGlass2, View.TRANSLATION_X, 200f, 0f).apply {
                        duration = 1000
                        interpolator = AccelerateDecelerateInterpolator()
                    }

                    val rotate2 = ObjectAnimator.ofFloat(cocktailGlass2, View.ROTATION, 0f, -360f).apply {
                        duration = 1500
                    }

                    // Animacja zderzenia kieliszków (ruch w górę i w dół) - zgodnie z prawami fizyki
                    val bounce1 = ObjectAnimator.ofFloat(cocktailGlass1, View.TRANSLATION_Y, 0f, -50f, 0f).apply {
                        duration = 800
                        startDelay = 1500
                        interpolator = BounceInterpolator() // Nadaje efekt odbicia po zderzeniu
                    }

                    val bounce2 = ObjectAnimator.ofFloat(cocktailGlass2, View.TRANSLATION_Y, 0f, -50f, 0f).apply {
                        duration = 800
                        startDelay = 1500
                        interpolator = BounceInterpolator()
                    }

                    // Animacja skalowania na zakończenie
                    val scaleDown1 = ObjectAnimator.ofPropertyValuesHolder(
                        cocktailGlass1,
                        PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 0.5f),
                        PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 0.5f)
                    ).apply {
                        duration = 500
                        startDelay = 2300
                    }

                    val scaleDown2 = ObjectAnimator.ofPropertyValuesHolder(
                        cocktailGlass2,
                        PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 0.5f),
                        PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 0.5f)
                    ).apply {
                        duration = 500
                        startDelay = 2300
                    }

                    // Uruchomienie wszystkich animacji w odpowiedniej kolejności
                    val animatorSet = AnimatorSet()
                    animatorSet.playTogether(fadeIn1, fadeIn2, moveX1, moveX2, rotate1, rotate2)

                    val bounceSet = AnimatorSet()
                    bounceSet.playTogether(bounce1, bounce2)

                    val scaleSet = AnimatorSet()
                    scaleSet.playTogether(scaleDown1, scaleDown2)

                    val finalSet = AnimatorSet()
                    finalSet.playSequentially(animatorSet, bounceSet, scaleSet)
                    finalSet.start()

                    // Po zakończeniu animacji, wywołujemy callback
                    finalSet.doOnEnd {
                        onAnimationFinish()
                    }

                    container
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

// Rozszerzenie dla AnimatorSet, aby łatwiej obsługiwać zdarzenie zakończenia
fun AnimatorSet.doOnEnd(action: () -> Unit) {
    this.addListener(object : android.animation.AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: android.animation.Animator) {
            action()
        }
    })
}
