package com.example.ketchexample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.ketchexample.databinding.ContentMainBinding
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.ketch.android.Ketch
import com.ketch.android.KetchSdk
import com.ketch.android.data.Consent
import com.ketch.android.data.HideExperienceStatus
import com.ketch.android.data.KetchConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var ketch: Ketch
    private lateinit var binding: ContentMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ContentMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Instantiate a KetchSDK object
        ketch = KetchSdk.create(
            context = this,
            fragmentManager = supportFragmentManager,
            organization = "dunkin",
            property = "dunkin_android",
            logLevel = Ketch.LogLevel.DEBUG,
            listener = getKetchListener(),
        )

        // Set language, jurisdiction, and region
        ketch.setLanguage("en")
        ketch.setRegion("US")


        lifecycleScope.launch {
            val advertisingId = getGaId()
            Log.d("###","advertisingId - $advertisingId")
            if (!advertisingId.isNullOrEmpty()) {
                ketch.setIdentities(mapOf("aaid" to advertisingId))
            }
        }

        ketch.load()
        //ketch.showConsent(true) // Show consent dialog appears only after calling this.
    }

    private fun getKetchListener(): Ketch.Listener {
        return object : Ketch.Listener {
            override fun onConfigUpdated(config: KetchConfig?) {}

            override fun onConsentUpdated(consent: Consent) {
                Log.d("###", "onConsentUpdated")
            }

            override fun onDismiss(status: HideExperienceStatus) {
                Log.d("###", "onDismiss")
            }

            override fun onEnvironmentUpdated(environment: String?) {}

            override fun onError(errMsg: String?) {

            }

            override fun onGPPUpdated(values: Map<String, Any?>) {}

            override fun onIdentitiesUpdated(identities: String?) {}

            override fun onJurisdictionUpdated(jurisdiction: String?) {}

            override fun onRegionInfoUpdated(regionInfo: String?) {}

            override fun onShow() {
                Log.d("###", "onShow")
            }

            override fun onTCFUpdated(values: Map<String, Any?>) {}

            override fun onUSPrivacyUpdated(values: Map<String, Any?>) {}

        }
    }
    fun fetchGaId() {

    }

    private suspend fun getGaId(): String? = withContext(Dispatchers.IO) {
        val idInfo: AdvertisingIdClient.Info = AdvertisingIdClient.getAdvertisingIdInfo(applicationContext)
        val gaId: String? = idInfo.id
        return@withContext gaId
    }

}
