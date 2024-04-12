package com.formate.formate.data.network

import com.formate.formate.BuildConfig
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue

object DbClient {
    val client = createSupabaseClient(
        supabaseUrl = BuildConfig.BACKEND_URL,
        supabaseKey = BuildConfig.BACKEND_KEY
    ) {
        install(GoTrue)
    }
}