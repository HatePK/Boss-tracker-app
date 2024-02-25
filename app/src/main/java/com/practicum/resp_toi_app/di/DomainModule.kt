package com.practicum.resp_toi_app.di

import com.practicum.resp_toi_app.domain.api.BossesInteractor
import com.practicum.resp_toi_app.domain.api.BossesRepository
import com.practicum.resp_toi_app.domain.impl.BossesInteractorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideBossesInteractor(bossesRepository: BossesRepository): BossesInteractor {
        return BossesInteractorImpl(bossesRepository)
    }
}