package cc.zengtian.mtt.controller

import cc.zengtian.mtt.config.BaseQuizConfig
import cc.zengtian.mtt.controller.CircleOf5thQuizController.CircleOf5thQuestion

/**
 * Created by ZengTian on 10/14/2019.
 */
class CircleOf5thQuizController : BaseQuizController<CircleOf5thQuestion>(){

    override val config: BaseQuizConfig
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override var curQuestion: CircleOf5thQuestion
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}

    override fun generateQuestion(): CircleOf5thQuestion {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    inner class CircleOf5thQuestion : Question {
        override var answer: Any?
            get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
            set(value) {}
        override val correct: Boolean
            get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        override val answered: Boolean
            get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    }
}
