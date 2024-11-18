from PIL import Image
import numpy as np

def preprocess_image(image, target_size=(224, 224)):
    try:
        img = Image.open(image)
        img = img.resize(target_size)  # 모델 입력 크기에 맞게 리사이즈
        img_array = np.array(img) / 255.0  # 정규화
        img_array = np.expand_dims(img_array, axis=0)  # 배치 차원 추가
        return img_array
    except Exception as e:
        raise ValueError(f"Image preprocessing failed: {str(e)}")