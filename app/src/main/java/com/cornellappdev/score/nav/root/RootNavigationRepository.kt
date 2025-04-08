package com.cornellappdev.score.nav.root

import com.cornellappdev.score.nav.BaseNavigationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RootNavigationRepository @Inject constructor() : BaseNavigationRepository<ScoreScreens>()
