"""
作者: Dr.Awe
日期：2023年05月29日9:12
"""
import time
import pika
import requests
import io
import json
import logging

from PIL import Image
from modelConfig import YOLOModel

logging.basicConfig(level=logging.INFO,
                    format='%(asctime)s - %(filename)s[line:%(lineno)d] - %(levelname)s: %(message)s')


class Consumer:
    def __init__(self, model_path, queue_name='my_queue'):
        self.url = "http://localhost:8080"  # 实际后端地址
        self.secret = "xf10pOX1%PEM*N3NhYf^Rr7AXI2slZg$jSXbSLS3Yza@SJSv"  # 验证密钥 不要泄露
        self.model = YOLOModel(model_path)
        self.queue_name = queue_name
        self.connection = pika.BlockingConnection(pika.ConnectionParameters('localhost',
                                                                            5672,
                                                                            'testhost',
                                                                            pika.PlainCredentials('admin', 'admin'),
                                                                            ))
        self.channel = self.connection.channel()
        self.channel.queue_declare(queue=self.queue_name, durable=True)

    def fetch_image(self, url):

        try:
            s_time = time.time()
            response = requests.get(self.url + url)
            response.raise_for_status()
            image = Image.open(io.BytesIO(response.content)).convert('RGB')
            # image_array = self.model.process_image(image)
            logging.info(f"fetch image using {time.time()-s_time}sec")
            return image
        except Exception as e:
            logging.error(e)
            return None

    def inference(self, image_array):
        outputs = self.model.inference(image_array)
        return outputs

    def callback(self, ch, method, properties, body):
        logging.info("a new message is received")
        s_time = time.time()
        message: dict = json.loads(body)
        headers = {
            'Connection': 'keep-alive',
            'Content-Type': 'application/json'
        }
        if {'image_url', 'task_id'}.issubset(set(message.keys())):
            params = {
                'task_id': message.get('task_id'),
                'secret': self.secret
            }
            with requests.post(self.url + "/task/start", data=json.dumps(params), headers=headers) as res:
                response = res.json()
                if response.get('code'):
                    logging.error(f"Request Failed: {res.json()}")
            image_array = self.fetch_image(message.get("image_url"))
            # outputs = self.inference(image_array)
            outputs = [[0, 0.1, 0.1, 0.2, 0.2, 0.9], [0, 0.3, 0.3, 0.4, 0.4, 0.8], [0, 0.5, 0.5, 0.6, 0.6, 0.9]]
            logging.info("the current task is completed")
            params = {
                'outputs': str(outputs),
                'task_id': message.get('task_id'),
                'secret': self.secret
            }
            with requests.post(self.url + "/task/complete", data=json.dumps(params), headers=headers) as res:
                response = res.json()
                if response.get('code'):
                    logging.error(f"Request Failed: {res.json()}")
            ch.basic_ack(delivery_tag=method.delivery_tag)
            e_time = time.time()
            logging.info(f"task takes time for {e_time - s_time}sec")
            logging.info('Waiting for next messages...')
            return
        raise RuntimeError("Invalid Message Format")

    def start(self):
        try:
            logging.info('application is starting...')
            self.channel.basic_consume(queue=self.queue_name, on_message_callback=self.callback)
            logging.info('Waiting for messages...')
            self.channel.start_consuming()
        except RuntimeError as e:
            logging.error(e)


if __name__ == "__main__":
    model_path = 'path/to/your/yolo_model.pth'
    consumer = Consumer(model_path)
    consumer.start()
