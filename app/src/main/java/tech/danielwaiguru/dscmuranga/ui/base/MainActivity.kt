package tech.danielwaiguru.dscmuranga.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tech.danielwaiguru.dscmuranga.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}