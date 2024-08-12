import logging
import numpy as np

"""
模型配置类
在这个文件中配置模型的各种行为和属性
"""

class YOLOModel:
    def __init__(self, path):
        self.image_size = (480, 480)  # todo 用于输入模型的图像矩阵大小，自己调节(防止用户上传过大图片导致识别任务耗时严重)

        self.load_model(path)


    def load_model(self, model_path):
        """
        模型在这里加载
        :param model_path: 模型路径
        :return: 加载后的模型对象
        """
        model = None
        return model


    def process_image(self, image):
        """
        图像预处理  自己增加自己的处理
        :param image: PIL Image 对象
        :return: np矩阵
        """
        try:
            image = image.resize(self.image_size)
            image_array = np.array(image) / 255.0
            return image_array
        except Exception as e:
            logging.error(e)
            return None

    def inference(self, image_array):
        """
        模型推理接口
        :param image_array: 传入的图像np矩阵
        :return: 输出结果，对结果格式化 具体格式为 [Data],Data => [class,x1,y1,x2,y2,confidence]
        exp: [[0,0.1,0.1,0.2,0.2,0.9],[0,0.3,0.3,0.4,0.4,0.8],[0,0.5,0.5,0.6,0.6,0.9]]
        """
        outputs = None
        # exp: outputs = self.model.predict(image_array)
        return outputs