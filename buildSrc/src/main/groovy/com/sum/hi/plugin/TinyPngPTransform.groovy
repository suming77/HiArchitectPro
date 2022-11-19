package com.sum.hi.plugin

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import javassist.ClassPool
import javassist.CtClass
import javassist.bytecode.ClassFile
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.gradle.api.Project

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream

class TinyPngPTransform extends Transform {
    private ClassPool classPool = ClassPool.getDefault()

    TinyPngPTransform(Project project) {
        //为了能够查找到Android相关类，需要把Android.jar包的路径添加到classPool类搜索路径
        classPool.appendClassPath(project.android.bootClasspath[0].toString())

        classPool.importPackage("android.os.Bundle")
        classPool.importPackage("android.widget.Toast")
        classPool.importPackage("android.app.Activity")
    }

    @Override
    String getName() {
        return "TinyPngPTransform"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        //接收数据的类型,该Transform接受那些那些内容作为输入参数
        //CLASSES(0x01)
        //RESOURCES(0x02),assets/目录下的资源，而不是res下的资源
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        //该transform工作的作用域
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        //是否增量编译
        //基于Task的上次输出快照和这次输入快照对比，如果相同，则跳过
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        //1.对input-->directory-->class 文件进行遍历
        //2.对input-->jar-->class文件进行遍历
        //3.符合我们的项目包名，并且class文件的路径包含Activity.class结尾，还不能是buildconfig.class,R.class,$.class
        def outputProvider = transformInvocation.outputProvider
        transformInvocation.inputs.each { input ->

            input.directoryInputs.each { dirInput ->
                println("dirinput abs file path:" + dirInput.file.absolutePath)
                handleDirectory(dirInput.file)


                //把input->dir->class->dest目标目录下去
                def dest = outputProvider.getContentLocation(dirInput.name, dirInput.contentTypes, dirInput.scopes, Format.DIRECTORY)
                FileUtils.copyDirectory(dirInput.file, dest)
            }

            input.jarInputs.each { jarInput ->
                println("jarInput abs file path:" + jarInput.file.absolutePath)
                //对jar修改完成之后会返回一个新的jar文件
                def srcFile = handleJar(jarInput.file)

                //防止重名
                def jarName = jarInput.name
                def md5 = DigestUtils.md5Hex(jarInput.file.absolutePath)
                if (jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0, jarName.length() - 4)
                }

                //获取jar包的输出路径
                def dest = outputProvider.getContentLocation(md5 + jarName, jarInput.contentTypes, jarInput.scopes, Format.JAR)
                FileUtils.copyDirectory(srcFile, dest)
            }
        }
        //释放导包的资源
        classPool.clearImportedPackages()
    }

    /**
     * 处理当前目录下的所有class文件
     * @param dir
     */
    void handleDirectory(File dir) {
        classPool.appendClassPath(dir.absolutePath)
        if (dir.isDirectory()) {
            dir.eachFileRecurse { file ->
                def filePath = file.name
                println("handleDirectory file path:" + file.absolutePath)
                if (shouldModifyClass(filePath)) {
                    def inputStream = new FileInputStream(file)
                    def ctClass = modifyClass(inputStream)
                    ctClass.writeFile(filePath)
                    ctClass.detach()
                }
            }
        }
    }

    CtClass modifyClass(InputStream inputStream) {
        def classFile = new ClassFile(new DataInputStream(new BufferedInputStream(inputStream)))
        println("modifyClass name:" + classFile.name)//全类名
        def ctClass = classPool.get(classFile.name)
        if (ctClass.isFrozen()) {
            ctClass.defrost()
        }
        def bundle = classPool.getCtClass("android.os.Bundle")
        CtClass[] params = Arrays.asList(bundle).toArray()
        def method = ctClass.getDeclaredMethod("onCreate", params)

        def message = classFile.name
        method.insertAfter("Toast.makeText(this," + "\"" + message + "\"" + ",Toast.LENGTH_SHORT).show();")
        return ctClass
    }

    File handleJar(File jarFile) {
        classPool.appendClassPath(jarFile.absolutePath)
        def inputJarFile = new JarFile(jarFile)
        def enumeration = inputJarFile.entries()
        def outputJatFile = new File(jarFile.parentFile, "temp_" + jarFile.name)
        if (outputJatFile.exists()) outputJatFile.delete()
        def jarOutputStream = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(outputJatFile)))
        while (enumeration.hasMoreElements()) {
            def inputJarEntry = enumeration.nextElement()
            def inputJarEntryName = inputJarFile.name
            def outputJarEntry = new JarEntry(inputJarEntryName)
                jarOutputStream.putNextEntry(outputJarEntry)
            println("inputJarEntryName:"+inputJarEntryName)
            def inputStream =   inputJarFile.getInputStream(inputJarEntry)
            if (!shouldModifyClass(inputJarEntryName)){
                jarOutputStream.write(IOUtils.toByteArray(inputStream))
                inputStream.close()
                continue
            }

            def ctClass = modifyClass(inputStream)
            def byteCode =ctClass.toBytecode()
            ctClass.detach()
            inputStream.close()

            jarOutputStream.write(byteCode)
            jarOutputStream.flush()
        }
        inputJarFile.close()
        jarOutputStream.closeEntry()
        jarOutputStream.flush()
        jarOutputStream.close()
        return outputJatFile
    }

    boolean shouldModifyClass(String filePath) {
        return (filePath.contains("com/sum/hi")
                && filePath.endsWith("Activity.class")
                && !filePath.contains("R.class")
                && !filePath.contains('$')
                && !filePath.contains('R$')
                && !filePath.contains("BuildConfig.class")
        )
    }
}