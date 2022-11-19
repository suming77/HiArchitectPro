package com.sum.hi.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.ProjectConfigurationException

//alt+enter -> fix package name
class TinyPngPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        if (!project.plugins.hasPlugin("com.android.application")){
            throw new ProjectConfigurationException("plugin:com.android.application must be apply", null)
        }
//方式一
//        def android  = project.extensions.findByType(AppExtension.class)
//        android.registerTransform(new TinyPngPTransform())

        //方式二 没有语法提示
        project.android.registerTransform(new TinyPngPTransform(project))
    }
}