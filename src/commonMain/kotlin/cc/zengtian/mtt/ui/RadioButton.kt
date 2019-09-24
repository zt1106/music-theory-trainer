package cc.zengtian.mtt.ui

/**
 * Created by ZengTian on 2019/9/22.
 */
class RadioButtonModel<T>(val data: List<T>,
                          var selected: T,
                          val textGetter: (T) -> String = { it.toString() })