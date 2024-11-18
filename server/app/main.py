import io
import os
import numpy as np
from PIL import Image
from fastapi import FastAPI, File, UploadFile, HTTPException

import tensorflow as tf
from server.app.preprocess import preprocess_image
from server.app.database import get_db_connection

app = FastAPI()
eye_model_path = "resources/model_resnet50_172.keras"

eye_model = tf.keras.models.load_model(eye_model_path)

@app.get("/")
async def root():
    return {"message": "hello"}


@app.post("/chat")
async def chat():
    return "hi"


@app.get("/database")
async def test_database():
    try:
        conn = get_db_connection()
        rows = conn.execute("SELECT name FROM sqlite_master WHERE type='table'").fetchall()
        return {"tables": [dict(row) for row in rows]}
    except Exception as e:
        return {"error": str(e)}


@app.post("/predict")
async def predict(file: UploadFile = File()):
    # 파일을 바이너리 데이터로 읽기
    file_bytes = await file.read()

    image = io.BytesIO(file_bytes)
    img_array = preprocess_image(image)

    # 2. 모델 예측 수행
    prediction = eye_model.predict(img_array)
    predicted_label = np.argmax(prediction, axis=1)[0]

    # 3. 데이터베이스 조회
    conn = get_db_connection()
    disease_info = conn.execute(
        'SELECT * FROM disease_info WHERE id = ?', (predicted_label,)
    ).fetchone()
    conn.close()

    if disease_info is None:
        raise HTTPException(status_code=404, detail="Disease information not found")

    return {
        '질환': disease_info['질환'],
        '정의': disease_info['정의'],
        '원인': disease_info['원인'],
        '병원 검사': disease_info['병원_검사'],
        '집에서의 관리법': disease_info['집에서의_관리법']
    }


if __name__ == '__main__':
    import uvicorn

    uvicorn.run(app, host='0.0.0.0', port=8000)

