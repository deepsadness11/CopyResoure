/**
 * Created by Administrator on 2017/3/9 0009.
 */
static start(String srcJavaPath, String targetPath) {
    def srcJavaFile = new File(srcJavaPath)
    def targetFile = new File(targetPath)
    //得到资源文件的路径
    def index = srcJavaPath.indexOf('java')
    def resFilePath = srcJavaPath.substring(0, index) + '\\res'
    def resFile = new File(resFilePath)

    //开始遍历java文件的路劲
    def getUtil = new getFileResourse()
    //将资源文件复制到数组里面
    getUtil.foreachMyFile(srcJavaFile)

//    println getUtil.drawable
//    println getUtil.layout
//    println getUtil.mipmap

    //将数组中的资源进行移动
    getUtil.foreachLayoutResources(resFile, targetFile)
    getUtil.foreachDrawableResources(resFile, targetFile)
    getUtil.foreachMipmapResources(resFile, targetFile)
}

start("D:\\dev\\haibao-android\\app\\src\\main\\java\\com\\haibao\\ui\\mine\\mvp\\view", 'D:\\gn\\')