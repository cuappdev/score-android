package com.cornellappdev.score.nav

import com.cornellappdev.score.util.UIEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
abstract class BaseNavigationRepository<RouteType> @Inject constructor() {

    private val _navigationFlow : MutableStateFlow<UIEvent<RouteType>?> = MutableStateFlow(null)
    val navigationFlow = _navigationFlow.asStateFlow()

    fun navigateTo(route: RouteType) {
        _navigationFlow.value = UIEvent(route)
    }
}
