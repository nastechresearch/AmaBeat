/*
 * AmaBeat (2026)
 * © nastechresearch — github.com/nastechresearch
 * GPL-3.0 License | Contributors: see git history
 * Do not remove or alter this notice. - Per GPL-3.0 Section 4 & Section 5
 */

package moe.rukamori.amabeat.innertube

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.atomic.AtomicBoolean

object NetworkGatekeeper : Interceptor {
    private val connectionBlocked = AtomicBoolean(true)

    val isConnectionBlocked: Boolean
        get() = connectionBlocked.get()

    fun setConnectionBlocked(blocked: Boolean) {
        connectionBlocked.set(blocked)
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        if (connectionBlocked.get()) {
            throw IOException(
                "Connection blocked by AmaBeat Remote. This app could not be verified as an " +
                    "official AmaBeat build. Install an official build from " +
                    "https://github.com/rukamori/AmaBeat or https://t.me/AmaBeatGC.",
            )
        }
        return chain.proceed(chain.request())
    }
}
