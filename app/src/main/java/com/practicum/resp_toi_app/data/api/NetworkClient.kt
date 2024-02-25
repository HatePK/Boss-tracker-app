package com.practicum.resp_toi_app.data.api

import com.practicum.resp_toi_app.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}