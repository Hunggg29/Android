package com.example.myapplication

import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var listViewSuggested: ListView
    private lateinit var horizontalScrollViewRecommended: HorizontalScrollView
    private lateinit var linearLayoutRecommended: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ánh xạ các view từ layout
        listViewSuggested = findViewById(R.id.listViewSuggested)
        horizontalScrollViewRecommended = findViewById(R.id.horizontalScrollViewRecommended)
        linearLayoutRecommended = findViewById(R.id.linearLayoutRecommended)

        //DATA: Suggested for you (ListView)
        val suggestedApps = listOf(
            App(
                name = "Galaxy Shooter 2077",
                category = "Arcade • Space • Offline",
                rating = "4.7",
                size = "145 MB",
                iconColor = "#4CAF50",
                iconText = "G"
            ),
            App(
                name = "Legend of Samurai",
                category = "Action • RPG",
                rating = "4.6",
                size = "512 MB",
                iconColor = "#9C27B0",
                iconText = "S"
            ),
            App(
                name = "Cyber Drift Racing",
                category = "Racing • Drift",
                rating = "4.8",
                size = "268 MB",
                iconColor = "#03A9F4",
                iconText = "C"
            )
        )

        // gán adapter danh sách "Suggested"
        val suggestedAdapter = AppAdapter(this, suggestedApps)
        listViewSuggested.adapter = suggestedAdapter

        //DATA: Recommended for you (Horizontal)
        val recommendedApps = listOf(
            App(
                name = "MemoAI - Smart Notes",
                category = "",
                rating = "",
                size = "",
                iconColor = "#FF9800",
                iconText = "M"
            ),
            App(
                name = "Pixel Painter",
                category = "",
                rating = "",
                size = "",
                iconColor = "#3F51B5",
                iconText = "P"
            ),
            App(
                name = "Focus Timer",
                category = "",
                rating = "",
                size = "",
                iconColor = "#009688",
                iconText = "F"
            ),
            App(
                name = "Story Camera",
                category = "",
                rating = "",
                size = "",
                iconColor = "#E91E63",
                iconText = "S"
            )
        )

        // Render từng item theo chiều ngang
        recommendedApps.forEach { app ->
            val appView = layoutInflater.inflate(R.layout.item_app_horizontal, null)

            val textViewName = appView.findViewById<TextView>(R.id.textViewAppName)
            val imageViewIcon = appView.findViewById<TextView>(R.id.imageViewAppIcon)

            // gán dữ liệu hiển thị
            textViewName.text = app.name
            imageViewIcon.text = app.iconText
            imageViewIcon.setBackgroundColor(Color.parseColor(app.iconColor))

            // set kích thước + margin cho item
            val layoutParams = LinearLayout.LayoutParams(
                resources.getDimensionPixelSize(R.dimen.app_horizontal_width),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val marginEnd = (16 * resources.displayMetrics.density).toInt()
            layoutParams.setMargins(0, 0, marginEnd, 0)
            appView.layoutParams = layoutParams

            // thêm vào layout nằm ngang
            linearLayoutRecommended.addView(appView)
        }
    }
}

// Model dữ liệu app
data class App(
    val name: String,
    val category: String,
    val rating: String,
    val size: String,
    val iconColor: String,
    val iconText: String
)
