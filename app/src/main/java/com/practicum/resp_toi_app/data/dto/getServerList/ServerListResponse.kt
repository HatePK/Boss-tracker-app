package com.practicum.resp_toi_app.data.dto.getServerList

import com.practicum.resp_toi_app.data.dto.Response

data class ServerListResponse(val serverList: List<ServerDto>): Response()

