import groovy.swing.SwingBuilder

import javax.swing.JFrame
import javax.swing.WindowConstants
import javax.swing.event.DocumentListener
import java.awt.BorderLayout
import java.awt.event.ActionEvent
import java.awt.event.FocusListener

/**
 * Created by Administrator on 2017/3/9 0009.
 */
void show() {
    SwingBuilder.build() {
        frame(title: '拷贝资源工具类', size: [500, 700],
                visible: true, defaultCloseOperation: WindowConstants.EXIT_ON_CLOSE) {
            //需要进行选择，或者进行输入。输入的话。需要一行
            //输入需要包名。和文件路径

            gridLayout(cols: 4, rows: 5)
            outLabel = label ''
            label ''
            label 'java文件的路劲: '
            srcInput = textField(columns: 2, actionPerformed: { echo.text = input.text.toUpperCase() })
            label '目标文件夹路径: '
            targetInput = textField(columns: 2, actionPerformed: { echo.text = input.text.toUpperCase() })

            button(text: '点击开始生成',
                    actionPerformed: {
                        ActionEvent event ->
                            def btn = event.source
                            btn.enabled = false
//                        btn.setText('正在生成.....')
                            try {
                                outLabel.text = '正在生成.....'
                                println srcInput.text
                                println targetInput.text
                                Process.start(srcInput.text, targetInput.text)
//                            main.start(list.selectedItem.toString())
                                outLabel.text = "<html>生成成功！！</html>"
                            } catch (Exception e) {
                                e.printStackTrace()
                                outLabel.text = "<html>发生错误<br> $e.message</html>"
                            }
//                        btn.setText('点击开始生成')
                            btn.enabled = true
                    })
//}
        }
    }
}