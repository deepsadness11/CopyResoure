import groovy.transform.Field

import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by Administrator on 2017/3/9 0009.
 */

//def file = new File("D:\\dev\\haibao-android\\app\\src\\main\\java\\com\\haibao\\ui\\mine\\mvp\\view");
//def resourcePath = new File("D:\\dev\\haibao-android\\app\\src\\main\\res");
//def outPutPath = 'D:\\gn\\'

//缓存的数组
@Field
List drawable = new ArrayList();
@Field
List layout = new ArrayList();
@Field
List mipmap = new ArrayList();

//遍历文件夹。将对应的资源放入数组中
def foreachMyFile(File file) {
    file.eachFile {
        if (it.isFile()) {
            //遍历到文件
            //将文件存入数组中
            addToListFromJava(it);
        } else {
            foreachMyFile(it)
        }
    }
}

//遍历资源数组，将文件拷贝对对应文件夹
def foreachDrawableResources(File resFile, File targetFile) {
    //drawable
    def tempDrawable = new ArrayList();
    tempDrawable.addAll(drawable)
    tempDrawable.each { String it ->
        //复制
        findNameOnResourceFile(it, resFile, targetFile)
    }
}

//遍历资源数组，将文件拷贝对对应文件夹
def foreachLayoutResources(File resFile, File targetFile) {
//    layout内可能有layout
    def tempLayout = new ArrayList();
    tempLayout.addAll(layout)
    //layout里面有所不同，还需要将layout内涉及的资源保存到drawable和mipmap内
    tempLayout.each { String it ->

        println resFile
        println it

        findNameOnResourceFile(it, resFile, targetFile)
    }
}

//遍历资源数组，将文件拷贝对对应文件夹
def foreachMipmapResources(File resFile, File targetFile) {
    mipmap.each { String it ->
        findNameOnResourceFile(it, resFile, targetFile)
    }
}


def addToListFromJava(File it) {
    // 按指定模式在字符串查找
    String pattern2 = "R\\.(mipmap|layout|drawable)\\.(\\w+)";
// 创建 Pattern 对象
    Pattern r = Pattern.compile(pattern2);
    String name = it.name
    Matcher m2 = r.matcher(it.text)
    if (m2.find()) {
        m2.each {
            System.out.println("Found value: " + it);
            switch (it[1]) {
                case 'drawable':
                    drawable.add(it[2])
                    break
                case 'layout':
                    layout.add(it[2])
                    break
                case 'mipmap':
                    mipmap.add(it[2])
                    break
            }
        }
    } else {
        System.out.println("NO MATCH");
    }

}

def findNameOnResourceFile(String itemName, File resFile, File targetFile) {
    resFile.eachFile {
        if (it.isDirectory()) {
            //如果是目录，则继续进行执行
            findNameOnResourceFile(itemName, it, targetFile)
        } else {
            //如果是文件，则进行匹配
            def nameSpilt = it.name.split('\\.')
            String realFileName = nameSpilt[0]
            if (realFileName == itemName) {
                //得到它的parent. 需要保存这个parent
                def parentName = it.parentFile.name
                println 'parentName=='+parentName
                //得到它完整的路径
                println it.absolutePath
                //开始复制文件
                //如果是layout，还需要分析layout
                if (parentName.contains('drawable')) {
                    if (addToListFromXMl('drawable', it)) {
                        foreachDrawableResources(resFile, targetFile)
                        return
                    }
                }

                if (parentName == 'layout') {
                    //如果返回true,则说明layout内部有layout 就是本身的集合发生了改变。故需要重新遍历
                    if (addToListFromXMl('layout', it)) {
                        foreachLayoutResources(resFile, targetFile)
                        return
                    }
                }
                copyFile(it.parentFile.name, it.absolutePath, it.name, targetFile)
            }
        }
    }
}
/**
 *
 * @param parentFileName 根文件夹的文件夹名称
 * @param scrFilePath 原文件目录
 * @param fileName 文件名称
 *
 * @return
 */
def copyFile(String parentFileName, String scrFilePath, String fileName, File targetFile) {
    def outPutPath = targetFile.getAbsolutePath()
    //输出目录的根目录是否存在
    def outFile = new File(outPutPath + "\\$parentFileName")
    String targetFilePath = outPutPath + "\\$parentFileName" + "\\$fileName"
    def finalTargetFile = new File(targetFilePath)

    if (finalTargetFile.exists()) {  //如果文件已存在，则直接结束
        return
    }

    if (!outFile.exists()) {    //不存在，则创建文件夹
        outFile.mkdirs()
    } else {
        Path outFilePath = FileSystems.getDefault().getPath(targetFilePath);
        Path srcFilePath = FileSystems.getDefault().getPath(scrFilePath);
        Files.copy(srcFilePath, outFilePath)
    }
}

boolean addToListFromXMl(String from, File it) {
    // 按指定模式在字符串查找 @drawable/selector_main_store
    String pattern2 = "\\@(mipmap|layout|drawable)/(\\w+)"
// 创建 Pattern 对象
    Pattern r = Pattern.compile(pattern2);
    String name = it.name
    Matcher m2 = r.matcher(it.text)
    if (m2.find()) {
        m2.each {
            System.out.println("Found value: " + it);
            switch (it[1]) {
                case 'drawable':
                    drawable.add(it[2])
                    return from == 'drawable'
                    break
                case 'layout':
                    layout.add(it[2])
                    return from == 'drawable'
                    break
                case 'mipmap':
                    mipmap.add(it[2])
                    break
            }
        }
    } else {
        System.out.println("NO MATCH");
    }
}