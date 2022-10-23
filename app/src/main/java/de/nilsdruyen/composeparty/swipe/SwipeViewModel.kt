package de.nilsdruyen.composeparty.swipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class SwipeViewModel : ViewModel() {

    private val gameEngine: GameEngine = GameEngineImpl()

    private val _gameState = MutableStateFlow(gameEngine.initialGame())
    val gameState: StateFlow<Game>
        get() = _gameState

    init {
        viewModelScope.launch {
            gameState.collect {
                Timber.d("game $it")
            }
        }
    }

    fun performAction(swipeAction: Angle) {
        viewModelScope.launch {
            gameEngine.performAction(swipeAction)
            _gameState.value = gameEngine.randomState()
        }
    }
}