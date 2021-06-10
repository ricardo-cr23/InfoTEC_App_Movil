package com.example.myapplication

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var writeAccess = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar2)

        checkDownloadPermission()

        val toggle = ActionBarDrawerToggle(this,drawerLayout,toolbar2,R.string.open,R.string.close)
        toggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        nav_menu.setNavigationItemSelectedListener(this)

        setToolbarTitle("Inicio")
        changeFragment(Home())
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawer(GravityCompat.START)

        when(item.itemId){
            R.id.home -> {
                setToolbarTitle("Inicio")
                changeFragment(Home())
            }

            R.id.news -> {
                setToolbarTitle("Noticias")
                changeFragment(News())
            }

            R.id.faq -> {
                setToolbarTitle("Preguntas Frecuentes")
                changeFragment(FAQ())
            }

            R.id.contact -> {
                setToolbarTitle("Contacto")
                changeFragment(Contact())
            }
        }
        return true
    }

    fun setToolbarTitle(title:String){
        supportActionBar?.title = title
    }

    fun changeFragment(frag: Fragment){
        val fragment = supportFragmentManager.beginTransaction()
        fragment.replace(R.id.fragment_container,frag).commit()
    }

    private fun checkDownloadPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(this@MainActivity,
                "El permiso para acceder al almacenamiento interno es necesario para descarga de archivos",
                Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 100);
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    writeAccess=true
                } else {
                    // Permission denied
                    writeAccess=false
                    Toast.makeText(this@MainActivity,"Permiso denegado. La descarga de archivos no funcionará sin el permiso respectivo",Toast.LENGTH_LONG).show()
                }
            }
        }
    }




}