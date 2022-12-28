package com.sum.hi.hilibrary.taskflow

/**
 * @创建者 mingyan.su
 * @创建时间 2022/12/05 23:15
 * @类描述 ${TODO}任务组，管理一组任务的依赖关系先后执行顺序
 */
class Project private constructor(id: String) : Task(id) {
    lateinit var endTask: Task //任务组中，所有任务的接收节点
    lateinit var startTask: Task //任务组中的开始节点

    //复写开始方法，
    override fun start() {
        startTask.start()
    }

    override fun run(id: String) {
        //不需要处理的
    }

    override fun behind(behindTask: Task) {
        //当咱们给一个任务组添加后置任务的时候，那么这个任务应该添加到 组当中谁的后面？？？
        endTask.behind(behindTask)//把新来的后置任务添加到任务组的结束节点上面去
        //这样的话任务组里面的所有任务都结束了，这个后置任务才会执行
    }

    override fun dependOn(dependTask: Task) {
        startTask.dependOn(dependTask)
    }

    override fun removeDependence(dependTask: Task) {
        startTask.removeDependence(dependTask)
    }

    override fun removeBehind(behindTask: Task) {
        endTask.removeBehind(behindTask)
    }

    class Builder(projectName: String, iTaskCreator: ITaskCreator) {
        private val mFactory = TaskFactory(iTaskCreator)
        private var mEndTask: Task = CriticalTask(projectName + "_end")
        private var mStartTask: Task = CriticalTask(projectName + "_start")
        private var mProject: Project = Project(projectName)
        private var mPriority = 0 //默认为该任务组中 所有任务优先级的 最高的
        private var mCurrentTaskShouldDependOnStartTask = true//本次添加进来的task，是否把start节点当作依赖
        //那如果这个task 它存在于其他task的依赖关系，那么就不能直接添加到start节点后面了，而需要通过dependOn来指定任务的依赖关系

        private var mCurrentAddTask: Task? = null

        fun add(id: String): Builder {
            val task = mFactory.getTask(id)
            if (task.priority > mPriority) {
                mPriority = task.priority
            }
            return add(task)
        }

        private fun add(task: Task): Builder {
            if (mCurrentTaskShouldDependOnStartTask && mCurrentAddTask != null) {
                mStartTask.behind(mCurrentAddTask!!)
            }
            mCurrentAddTask = task
            mCurrentTaskShouldDependOnStartTask = true
            mCurrentAddTask!!.behind(mEndTask)
            return this
        }

        fun dependOn(id: String): Builder {
            return dependOn(mFactory.getTask(id))
        }

        private fun dependOn(task: Task): Builder {
            // 确定 刚才我们添加进来的mCurrentAddTask 和 task 的依赖关系----mCurrentTask 依赖于task
            task.behind(mCurrentAddTask!!)

            //start - task10 -mCurrentAddTask(task11) -- end
            mEndTask.removeDependence(task)
            mCurrentTaskShouldDependOnStartTask = false
            return this
        }

        fun build(): Project {
            if (mCurrentAddTask == null) {
                mStartTask.behind(mEndTask)
            } else {
                if (mCurrentTaskShouldDependOnStartTask) {
                    mStartTask.behind(mCurrentAddTask!!)
                }
            }
            mStartTask.priority = mPriority
            mEndTask.priority = mPriority
            mProject.startTask = mStartTask
            mProject.endTask = mEndTask
            return mProject
        }

    }

    private class TaskFactory(private val iTaskCreator: ITaskCreator) {
        //利用iTaskCreator创建task实例，并管理
        private val mCacheTask: MutableMap<String, Task> = HashMap()
        fun getTask(id: String): Task {
            var task = mCacheTask[id]
            if (task != null) {
                return task
            }

            task = iTaskCreator.creatorTask(id)
            requireNotNull(task) { "create task fail, make sure iTaskCreator can create a Task with only taskId" }
            mCacheTask[id] = task
            return task
        }
    }

    //开始和结束的标记任务节点
    class CriticalTask internal constructor(id: String) : Task(id) {
        override fun run(id: String) {
            //nothing to do
        }

    }

}