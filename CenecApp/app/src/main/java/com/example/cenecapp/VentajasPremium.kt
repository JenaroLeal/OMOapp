package com.example.cenecapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.stripe.android.PaymentConfiguration

class VentajasPremium : AppCompatActivity() {

    private lateinit var btn:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ventajas_premium)



        btn = findViewById(R.id.elegirPlan)
        btn.setOnClickListener(){
            callPayment()
        }
    }
    private fun callPayment(){
        //sk_test_CGGvfNiIPwLXiDwaOfZ3oX6Y
        var keyStripePayment = "pk_test_Dt4ZBItXSZT1EzmOd8yCxonL"
        PaymentConfiguration.init(applicationContext, keyStripePayment)

        val i:Intent = Intent(this, CheckoutActivity::class.java)
        startActivity(i)
    }
}