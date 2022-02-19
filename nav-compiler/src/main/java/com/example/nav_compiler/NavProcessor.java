package com.example.nav_compiler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.nav_annotation.Destination;
import com.google.auto.service.AutoService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

/**
 * @Author: smy
 * @Date: 2022/2/15 13:47
 * @Desc:
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"com.example.nav_annotation.Destination"})
public class NavProcessor extends AbstractProcessor {

    private static final String PAGE_TYPE_ACTIVITY = "Activity";
    private static final String PAGE_TYPE_FRAGMENT = "Fragment";
    private static final String PAGE_TYPE_DIALOG = "Dialog";
    private static final String OUTPUT_FILER_NAME = "destination.json";
    private Messager mMessager;
    private Filer mFiler;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        //Android lib中无法打印日志，使用Messager打印
        mMessager = processingEnv.getMessager();
        mMessager.printMessage(Diagnostic.Kind.NOTE, "enter init ……");

        mFiler = processingEnv.getFiler(); // 文件
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //使用Destination注解的所有页面集合
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Destination.class);
        if (!elements.isEmpty()) {
            HashMap<String, JSONObject> destMap = new HashMap<>();
            handleDestination(elements, Destination.class, destMap);
            try {
                //生成文件
                FileObject resource = mFiler.createResource(StandardLocation.CLASS_OUTPUT, "", OUTPUT_FILER_NAME);
                // /app/build/intermediates/javac/debug/classes/目录下
                // app/main/assets

                String resourcePath = resource.toUri().getPath();
                String appPath = resourcePath.substring(0, resourcePath.indexOf("app") + 4);
                String assetsPath = appPath + "src/main/assets";
                File file = new File(assetsPath);
                if (file.exists()) {
                    file.mkdirs();//创建目录
                }

//                file.createNewFile();
                String content = JSON.toJSONString(destMap);
                File outFile = new File(assetsPath, OUTPUT_FILER_NAME);
                if (outFile.exists()) {
                    outFile.delete();
                }
//                outFile.mkdirs();
                outFile.createNewFile();
                FileOutputStream outputStream = new FileOutputStream(outFile);
                OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);
                streamWriter.write(content);
                streamWriter.flush();

                outputStream.close();
                streamWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void handleDestination(Set<? extends Element> elements, Class<Destination> destinationClass, HashMap<String, JSONObject> destMap) {
        for (Element element : elements) {
            TypeElement typeElement = (TypeElement) element;

            //全类名
            String clazName = typeElement.getQualifiedName().toString();

            Destination annotation = typeElement.getAnnotation(destinationClass);
            String pageUrl = annotation.pageUrl();
            boolean asStarter = annotation.asStarter();

            int id = Math.abs(clazName.hashCode());

            //Activity Fragment Dialog
            String destType = getDestinationType(typeElement);
            if (destMap.containsKey(pageUrl)) {
                mMessager.printMessage(Diagnostic.Kind.ERROR, "不同页面不能使用相同的pageUrl:" + pageUrl);
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("clazName", clazName);
                jsonObject.put("pageUrl", pageUrl);
                jsonObject.put("id", id);
                jsonObject.put("asStarter", asStarter);
                jsonObject.put("destType", destType);

                destMap.put(pageUrl, jsonObject);
            }

        }
    }

    private String getDestinationType(TypeElement typeElement) {
        //父类的列类型
        TypeMirror typeMirror = typeElement.getSuperclass();
        //androidx.fragment.app.Fragment
        String superClazName = typeMirror.toString();//全类名

        if (superClazName.contains(PAGE_TYPE_ACTIVITY.toLowerCase())) {
            return PAGE_TYPE_ACTIVITY.toLowerCase();
        } else if (superClazName.contains(PAGE_TYPE_FRAGMENT.toLowerCase())) {
            return PAGE_TYPE_FRAGMENT.toLowerCase();
        } else if (superClazName.contains(PAGE_TYPE_DIALOG.toLowerCase())) {
            return PAGE_TYPE_DIALOG.toLowerCase();
        }

        //这个父类的类型是类的类型或者接口的类型
        if (typeMirror instanceof DeclaredType) {
            Element element = ((DeclaredType) typeMirror).asElement();
            if (element instanceof TypeElement) {
                return getDestinationType((TypeElement) element);
            }
        }
        return null;
    }
}
