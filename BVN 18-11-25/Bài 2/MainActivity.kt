package com.example.myapplication

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var lvEmail: ListView
    private lateinit var adapter: EmailAdapter
    private val dsEmail = mutableListOf<Email>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lvEmail = findViewById(R.id.lvEmail)


        dsEmail.add(
            Email(
                sender = "Edurila.com",
                title = "Giảm giá 50% khóa học",
                content = "Only first 10 spots - bestselling course",
                time = "12:34 PM"
            )
        )
        dsEmail.add(
            Email(
                sender = "Chris Abad",
                title = "Make Campaign Monitor better",
                content = "Let us know your thoughts!",
                time = "11:22 AM"
            )
        )
        dsEmail.add(
            Email(
                sender = "Tuto.com",
                title = "8h de formation gratuite",
                content = "Photoshop, SEO, CSS, WordPress,…",
                time = "10:41 AM"
            )
        )
        dsEmail.add(
            Email(
                sender = "Support",
                title = "Société OVH: Suivi des services",
                content = "Notification from OVH",
                time = "10:26 AM"
            )
        )
        dsEmail.add(
            Email(
                sender = "Google",
                title = "Security alert",
                content = "New sign-in from Chrome on Windows",
                time = "9:50 AM"
            )
        )

        dsEmail.add(
            Email(
                sender = "Facebook",
                title = "You have 3 new notifications",
                content = "Someone reacted to your post",
                time = "9:20 AM"
            )
        )

        dsEmail.add(
            Email(
                sender = "LinkedIn",
                title = "5 new jobs for you",
                content = "Based on your profile and search history",
                time = "Yesterday"
            )
        )

        dsEmail.add(
            Email(
                sender = "YouTube",
                title = "New Video Recommendations",
                content = "Top videos you might like today",
                time = "Yesterday"
            )
        )

        dsEmail.add(
            Email(
                sender = "Apple",
                title = "Your receipt from App Store",
                content = "Your subscription has been renewed",
                time = "Yesterday"
            )
        )



        adapter = EmailAdapter(this, dsEmail)
        lvEmail.adapter = adapter
    }
}
