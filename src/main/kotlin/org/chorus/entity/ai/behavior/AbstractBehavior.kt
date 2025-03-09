package org.chorus.entity.ai.behavior

import lombok.Getter
import lombok.Setter

/**
 * AbstractBehavior包含一个[BehaviorState]属性以及它的Getter/Setter
 *
 *
 * AbstractBehavior contains a [BehaviorState] property and its Getter/Setter
 */
abstract class AbstractBehavior : IBehavior {
    @Getter
    @Setter
    override var behaviorState: BehaviorState = BehaviorState.STOP
}
